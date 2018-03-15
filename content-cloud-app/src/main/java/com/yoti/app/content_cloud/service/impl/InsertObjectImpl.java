package com.yoti.app.content_cloud.service.impl;

import com.google.protobuf.util.JsonFormat;
import com.yoti.app.UrlConstants.ErrorCodes;
import com.yoti.app.UrlConstants.ErrorMessages;
import com.yoti.app.UrlConstants.ServerConstants;
import com.yoti.app.content_cloud.adpaters.InsertProtoAdapter;
import com.yoti.app.content_cloud.model.InsertMessageRequest;
import com.yoti.app.content_cloud.model.InsertMessageResponse;
import com.yoti.app.content_cloud.service.InsertObject;
import com.yoti.app.content_cloud.service.PostDataService;
import com.yoti.app.exception.CloudDataAdapterException;
import com.yoti.app.exception.CloudDataConversionException;
import com.yoti.app.exception.CloudInteractionException;
import com.yoti.app.httpClient.RequestClient;
import com.yoti.ccloudpubapi_v1.InsertProto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ByteArrayEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Slf4j
@Service
@RequiredArgsConstructor
public class InsertObjectImpl implements InsertObject {


    private final PostDataService postDataService;

    private final InsertProtoAdapter insertProtoAdapter;

    private final JsonFormat.Printer jsonPrinter = JsonFormat.printer().preservingProtoFieldNames();
    private final JsonFormat.Parser jsonParser = JsonFormat.parser();

    @Override
    public <T> InsertMessageResponse insertObjectToCloud(final InsertMessageRequest<T> insertMessageRequest) throws CloudInteractionException, CloudDataAdapterException {
        validateInputObject(insertMessageRequest);
        try {
            InsertProto.InsertRequest request = insertProtoAdapter.getInsertProtoFromInsertMessageRequest(insertMessageRequest);
            String jsonPayload = jsonPrinter.print(request);
            log.info("the insert request is /n {}", jsonPayload);
            ResponseEntity<?> responseEntity = postDataService.postData(ServerConstants.INSERT_DATA_URL, jsonPayload);
            return handleResponse(responseEntity);
        } catch (CloudDataConversionException | CloudDataAdapterException | CloudInteractionException e) {
            log.info("Exception {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.info(e.getMessage());
            throw new CloudInteractionException(ErrorCodes.CLOUD_INSERT_ERROR, e.getMessage());
        }
    }


    private InsertMessageResponse handleResponse(final ResponseEntity<?> httpResponse) {
        if (httpResponse.getStatusCodeValue() != 200) {
            throw new CloudInteractionException(ErrorCodes.CLOUD_INSERT_ERROR, String.format("Status %d returned from the Content Cloud Service", httpResponse.getStatusCodeValue()));
        }
        try {
            String jsonResponseBody = (String) httpResponse.getBody();
            log.info("the response json body is /n {}", jsonResponseBody);
            InsertProto.InsertResponse.Builder responseBuilder = InsertProto.InsertResponse.newBuilder();
            jsonParser.merge(jsonResponseBody, responseBuilder);
            InsertProto.InsertResponse insertResponseProto = responseBuilder.build();
            return insertProtoAdapter.getInsertMessageResponse(insertResponseProto);
        } catch (IOException e) {
            log.info("Exception {}", e.getMessage());
            throw new CloudDataConversionException(e.getMessage());
        } catch (Exception e) {
            log.info("Exception {}", e.getMessage());
            throw new CloudInteractionException(ErrorCodes.CLOUD_INSERT_ERROR, e.getMessage());
        }

    }


    private <T> void validateInputObject(final T obj) {
        if (obj == null) {
            throw new CloudInteractionException(ErrorCodes.NULL_INPUT, ErrorMessages.NULL_OBJ_MSG);
        }

    }


}
