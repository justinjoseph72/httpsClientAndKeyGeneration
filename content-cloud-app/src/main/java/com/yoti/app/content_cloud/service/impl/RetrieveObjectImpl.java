package com.yoti.app.content_cloud.service.impl;

import com.google.protobuf.util.JsonFormat;
import com.yoti.app.UrlConstants.ErrorCodes;
import com.yoti.app.UrlConstants.ErrorMessages;
import com.yoti.app.UrlConstants.ServerConstants;
import com.yoti.app.content_cloud.adpaters.RetrieveProtoAdapter;
import com.yoti.app.content_cloud.model.ResponseRecord;
import com.yoti.app.content_cloud.model.RetrieveMessageRequest;
import com.yoti.app.content_cloud.model.RetrieveMessageResponse;
import com.yoti.app.content_cloud.service.PostDataService;
import com.yoti.app.content_cloud.service.RetrieveObject;
import com.yoti.app.exception.CloudDataAdapterException;
import com.yoti.app.exception.CloudDataConversionException;
import com.yoti.app.exception.CloudInteractionException;
import com.yoti.ccloudpubapi_v1.RetrieveProto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RetrieveObjectImpl implements RetrieveObject {

    private final RetrieveProtoAdapter retrieveProtoAdapter;

    private final PostDataService postDataService;

    private final JsonFormat.Printer jsonPrinter = JsonFormat.printer().preservingProtoFieldNames();
    private final JsonFormat.Parser jsonParser = JsonFormat.parser();

    @Override
    public RetrieveMessageResponse fetchRecordsFromCloud(final RetrieveMessageRequest retrieveMessageRequest) throws CloudInteractionException, CloudDataConversionException, CloudDataAdapterException {
        validateRequest(retrieveMessageRequest);
        try {
            RetrieveProto.RetrieveRequest retrieveRequest = retrieveProtoAdapter
                    .getRetrieveRequestProtoFromRetrieveRequest(retrieveMessageRequest);
            String jsonStr = jsonPrinter.print(retrieveRequest);
            log.info("the json string is {}", jsonStr);
            ResponseEntity<?> responseEntity = postDataService.postData(ServerConstants.RETRIEVE_DATA_URL, jsonStr);
            return handleResponse(responseEntity);
        } catch (CloudDataConversionException | CloudDataAdapterException | CloudInteractionException e) {
            log.info("Exception {} ", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.info("Exception {}", e.getMessage());
            throw new CloudInteractionException(ErrorCodes.CLOUD_RETRIEVE_ERROR, e.getMessage());
        }

    }

    private void validateRequest(final RetrieveMessageRequest obj) {
        if (obj == null) {
            log.info("retrieve message request object is null");
            throw new CloudInteractionException(ErrorCodes.NULL_INPUT, ErrorMessages.NULL_OBJ_MSG);
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
            log.info("the response jsonResponseBody");
            log.info(jsonResponseBody);
            RetrieveProto.RetrieveResponse.Builder builder = RetrieveProto.RetrieveResponse.newBuilder();
            jsonParser.merge(jsonResponseBody, builder);
            RetrieveProto.RetrieveResponse retrieveResponse = builder.build();
            List<RetrieveProto.Record> recordProtoList = retrieveResponse.getRecordsList();
            if (recordProtoList != null && !recordProtoList.isEmpty()) {
                List<ResponseRecord> records = new ArrayList<>();
                recordProtoList.forEach((RetrieveProto.Record recordProto) -> {
                    try {
                        ResponseRecord responseRecord = retrieveProtoAdapter.getResponseRecordFromRecordProto(recordProto);
                        records.add(responseRecord);
                    } catch (ParseException e) {
                        throw new CloudInteractionException(ErrorCodes.CLOUD_RETRIEVE_ERROR, e.getMessage());
                    }
                });
                return RetrieveMessageResponse.builder()
                        .records(records)
                        .hasMoreRecords(retrieveResponse.getMoreRecords())
                        .build();
            }
        } catch (IOException e) {
            log.warn("Exception {} ", e.getMessage());
            throw new CloudDataConversionException(e.getMessage());
        } catch (Exception e) {
            log.warn("Exception {}", e.getMessage());
            throw new CloudInteractionException(ErrorCodes.CLOUD_RETRIEVE_ERROR, e.getMessage());
        }
        return null;
    }
}
