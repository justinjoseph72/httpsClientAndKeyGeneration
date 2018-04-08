package com.yoti.app.content_cloud.service.impl;

import com.google.common.collect.ImmutableList;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import com.yoti.app.UrlConstants.ErrorCodes;
import com.yoti.app.UrlConstants.ErrorMessages;
import com.yoti.app.config.EndpointsProperties;
import com.yoti.app.content_cloud.adpaters.RetrieveProtoAdapter;
import com.yoti.app.content_cloud.model.PostDataModel;
import com.yoti.app.content_cloud.model.ResponseRecord;
import com.yoti.app.content_cloud.model.RetrieveMessageRequest;
import com.yoti.app.content_cloud.model.RetrieveMessageResponse;
import com.yoti.app.content_cloud.service.PostDataService;
import com.yoti.app.content_cloud.service.RetrieveObject;
import com.yoti.app.controllers.model.ContentCloudModel;
import com.yoti.app.exception.CloudDataAdapterException;
import com.yoti.app.exception.CloudDataConversionException;
import com.yoti.app.exception.CloudInteractionException;
import com.yoti.ccloudpubapi_v1.RetrieveProto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RetrieveObjectImpl implements RetrieveObject {

    private final RetrieveProtoAdapter retrieveProtoAdapter;
    private final EndpointsProperties endpointsProperties;
    private final PostDataService postDataService;
    private final Validator validator;
    private final JsonFormat.Printer jsonPrinter = JsonFormat.printer().preservingProtoFieldNames();
    private final JsonFormat.Parser jsonParser = JsonFormat.parser();

    @Override
    public RetrieveMessageResponse fetchRecordsFromCloud(final ContentCloudModel<RetrieveMessageRequest> retrieveMessageRequest) throws CloudInteractionException, CloudDataConversionException, CloudDataAdapterException {
        validateInputObject(retrieveMessageRequest);
        validateInputObject(retrieveMessageRequest.getData());
        try {
            PostDataModel postDataModel = getPostDataModel(retrieveMessageRequest);
            ResponseEntity<?> responseEntity = postDataService.postData(postDataModel);
            return handleResponse(responseEntity);
        } catch (CloudDataConversionException | CloudDataAdapterException | CloudInteractionException e) {
            log.warn(" Exception {} {}", e.getClass().getName(), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.warn(" Exception {} {}", e.getClass().getName(), e.getMessage());
            throw new CloudInteractionException(ErrorCodes.CLOUD_RETRIEVE_ERROR, e.getMessage());
        }

    }

    private PostDataModel getPostDataModel(final ContentCloudModel<RetrieveMessageRequest> retrieveMessageRequest) throws InvalidProtocolBufferException {
        RetrieveProto.RetrieveRequest retrieveRequest = retrieveProtoAdapter
                .getRetrieveRequestProtoFromRetrieveRequest(retrieveMessageRequest.getData());
        String payload =  jsonPrinter.print(retrieveRequest);
        return PostDataModel.builder()
                .payload(payload)
                .postUrl(endpointsProperties.getRetrieveData())
                .keyData(retrieveMessageRequest.getKeyData())
                .build();
    }

    private <T> void validateInputObject(final T obj) {
        if (obj == null) {
            throw new CloudInteractionException(ErrorCodes.NULL_INPUT, ErrorMessages.NULL_OBJ_MSG);
        }
        Set<ConstraintViolation<T>> violations = validator.validate(obj);
        if (!CollectionUtils.isEmpty(violations)) {
            String message = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining("::"));
            log.debug("Invalid InputObject with error {}", message);
            throw new CloudInteractionException(ErrorCodes.INVALID_CLOUD_BODY, message);
        }
    }

    private RetrieveMessageResponse handleResponse(final ResponseEntity<?> httpResponse) throws CloudInteractionException, CloudDataConversionException {
        //TODO have to check the response from the server and convert accordingly after decrypting the data in the responseRecord
        //TODO check the response from the mock service its not returning records for the input i use in tests
        if (httpResponse.getStatusCodeValue() != 200) {
            throw new CloudInteractionException(ErrorCodes.CLOUD_INSERT_ERROR, String.format("Status %d returned from the Content Cloud Service", httpResponse.getStatusCodeValue()));
        }
        try {
            String jsonResponseBody = (String) httpResponse.getBody();
            log.debug("the response jsonResponseBody {}", jsonResponseBody);
            RetrieveProto.RetrieveResponse.Builder builder = RetrieveProto.RetrieveResponse.newBuilder();
            jsonParser.merge(jsonResponseBody, builder);
            RetrieveProto.RetrieveResponse retrieveResponse = builder.build();
            log.debug("Retrieve Response body parsed successfully");
            List<RetrieveProto.Record> recordProtoList = retrieveResponse.getRecordsList();
            if (recordProtoList != null && !recordProtoList.isEmpty()) {
                List<ResponseRecord> records = new ArrayList<>();
                recordProtoList.forEach((RetrieveProto.Record recordProto) -> {
                    try {
                        ResponseRecord responseRecord = retrieveProtoAdapter.getResponseRecordFromRecordProto(recordProto);
                        records.add(responseRecord);
                    } catch (ParseException e) {
                        log.warn("record parsing error {}", e.getMessage());
                        throw new CloudInteractionException(ErrorCodes.CLOUD_RETRIEVE_ERROR, e.getMessage());
                    }
                });
                return RetrieveMessageResponse.builder()
                        .records(ImmutableList.copyOf(records))
                        .hasMoreRecords(retrieveResponse.getMoreRecords())
                        .build();
            }
        } catch (IOException e) {
            log.warn(" Exception {} {}", e.getClass().getName(), e.getMessage());
            throw new CloudDataConversionException(e.getMessage());
        } catch (Exception e) {
            log.warn(" Exception {} {}", e.getClass().getName(), e.getMessage());
            throw new CloudInteractionException(ErrorCodes.CLOUD_RETRIEVE_ERROR, e.getMessage());
        }
        return null;
    }
}
