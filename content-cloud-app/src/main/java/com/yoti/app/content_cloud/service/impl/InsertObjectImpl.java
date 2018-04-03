package com.yoti.app.content_cloud.service.impl;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import com.yoti.app.UrlConstants.ErrorCodes;
import com.yoti.app.UrlConstants.ErrorMessages;
import com.yoti.app.config.EndpointsProperties;
import com.yoti.app.content_cloud.adpaters.InsertProtoAdapter;
import com.yoti.app.content_cloud.model.InsertMessageRequest;
import com.yoti.app.content_cloud.model.InsertMessageResponse;
import com.yoti.app.content_cloud.model.PostDataModel;
import com.yoti.app.content_cloud.service.InsertObject;
import com.yoti.app.content_cloud.service.PostDataService;
import com.yoti.app.controllers.model.ContentCloudModel;
import com.yoti.app.exception.CloudDataAdapterException;
import com.yoti.app.exception.CloudDataConversionException;
import com.yoti.app.exception.CloudInteractionException;
import com.yoti.ccloudpubapi_v1.InsertProto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class InsertObjectImpl implements InsertObject {


    private final PostDataService postDataService;
    private final InsertProtoAdapter insertProtoAdapter;
    private final EndpointsProperties endpointsProperties;
    private final Validator validator;
    private final JsonFormat.Printer jsonPrinter = JsonFormat.printer().preservingProtoFieldNames();
    private final JsonFormat.Parser jsonParser = JsonFormat.parser();

    @Override
    public <T> InsertMessageResponse insertObjectToCloud(final ContentCloudModel<InsertMessageRequest<T>> contentCloudModel) throws CloudInteractionException, CloudDataAdapterException {
        validateInputObject(contentCloudModel);
        validateInputObject(contentCloudModel.getData());
        try {
            PostDataModel postData = getPostDataModel(contentCloudModel);
            ResponseEntity<?> responseEntity = postDataService.postData(postData);
            return handleResponse(responseEntity);
        } catch (CloudDataConversionException | CloudDataAdapterException | CloudInteractionException e) {
            log.warn(" Exception {} {}", e.getClass().getName(), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.warn(" Exception {} {}", e.getClass().getName(), e.getMessage());
            throw new CloudInteractionException(ErrorCodes.CLOUD_INSERT_ERROR, e.getMessage());
        }
    }

    private <T> PostDataModel getPostDataModel(final ContentCloudModel<InsertMessageRequest<T>> contentCloudModel) throws InvalidProtocolBufferException {

        InsertProto.InsertRequest request = insertProtoAdapter.getInsertProtoFromInsertMessageRequest(contentCloudModel.getData());
        String payLoad =  jsonPrinter.print(request);
        return  PostDataModel.builder()
                .keyData(contentCloudModel.getKeyData())
                .payload(payLoad)
                .postUrl(endpointsProperties.getInsertData())
                .build();
    }


    private InsertMessageResponse handleResponse(final ResponseEntity<?> httpResponse) {
        if (httpResponse.getStatusCodeValue() != 200) {
            log.debug("The status from content cloud is {}", httpResponse.getStatusCodeValue());
            throw new CloudInteractionException(ErrorCodes.CLOUD_INSERT_ERROR, String.format("Status %d returned from the Content Cloud Service", httpResponse.getStatusCodeValue()));
        }
        try {
            String jsonResponseBody = (String) httpResponse.getBody();
            log.debug("the response json body is {}", jsonResponseBody);
            InsertProto.InsertResponse.Builder responseBuilder = InsertProto.InsertResponse.newBuilder();
            jsonParser.merge(jsonResponseBody, responseBuilder);
            InsertProto.InsertResponse insertResponseProto = responseBuilder.build();
            log.debug("response from content cloud parsed successfully");
            return insertProtoAdapter.getInsertMessageResponse(insertResponseProto);
        } catch (IOException e) {
            log.warn(" Exception {} {}", e.getClass().getName(), e.getMessage());
            throw new CloudDataConversionException(e.getMessage());
        } catch (Exception e) {
            log.warn(" Exception {} {}", e.getClass().getName(), e.getMessage());
            throw new CloudInteractionException(ErrorCodes.CLOUD_INSERT_ERROR, e.getMessage());
        }

    }

    private <T> void validateInputObject(final T obj) {
        if (obj == null) {
            throw new CloudInteractionException(ErrorCodes.NULL_INPUT, ErrorMessages.NULL_OBJ_MSG);
        }
        Set<ConstraintViolation<T>> violations = validator.validate(obj);
        if (!CollectionUtils.isEmpty(violations)) {
            String message = violations.stream().map(s -> s.getMessage()).collect(Collectors.joining("::"));
            log.debug("Invalid InputObject with error {}", message);
            throw new CloudInteractionException(ErrorCodes.INVALID_CLOUD_BODY, message);
        }
    }


}
