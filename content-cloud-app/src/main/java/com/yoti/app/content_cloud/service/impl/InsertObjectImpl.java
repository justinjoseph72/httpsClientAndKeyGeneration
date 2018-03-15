package com.yoti.app.content_cloud.service.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yoti.app.UrlConstants.ErrorCodes;
import com.yoti.app.UrlConstants.ErrorMessages;
import com.yoti.app.UrlConstants.ServerConstants;
import com.yoti.app.content_cloud.adpaters.InsertProtoAdapter;
import com.yoti.app.content_cloud.model.InsertMessageRequest;
import com.yoti.app.content_cloud.model.InsertMessageResponse;
import com.yoti.app.content_cloud.service.InsertObject;
import com.yoti.app.exception.CloudDataAdapterException;
import com.yoti.app.exception.CloudDataConversionException;
import com.yoti.app.exception.CloudInteractionException;
import com.yoti.app.httpClient.RequestClient;
import com.yoti.ccloudpubapi_v1.InsertProto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ByteArrayEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Slf4j
@Service
@RequiredArgsConstructor
public class InsertObjectImpl implements InsertObject {

    @Inject
    @Getter
    private RequestClient requestClient;

    @Inject
    @Getter
    private InsertProtoAdapter insertProtoAdapter;

    @Override
    public <T> InsertMessageResponse insertObjectToCloud(final InsertMessageRequest<T> insertMessageRequest) throws CloudInteractionException, CloudDataAdapterException {
        validateInputObject(insertMessageRequest);
        try {
            InsertProto.InsertRequest request = insertProtoAdapter.getInsertProtoFromInsertMessageRequest(insertMessageRequest);
            ByteArrayEntity protoEntity = new ByteArrayEntity(request.toByteArray());
            protoEntity.setContentType("application/x-protobuf");
            HttpResponse httpResponse = postDataAndGetResponse(requestClient, protoEntity, ServerConstants.INSERT_DATA_URL);
            return handleResponse(httpResponse);
        } catch (CloudDataConversionException | CloudDataAdapterException | CloudInteractionException e) {
            log.info("Exception {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.info(e.getMessage());
            throw new CloudInteractionException(ErrorCodes.CLOUD_INSERT_ERROR, e.getMessage());
        }
    }


    private InsertMessageResponse handleResponse(final HttpResponse httpResponse) {
        if (httpResponse.getStatusLine().getStatusCode() != 200) {
            throw new CloudInteractionException(ErrorCodes.CLOUD_INSERT_ERROR, String.format("Status %d returned from the Content Cloud Service", httpResponse.getStatusLine().getStatusCode()));
        }
        try {
            byte[] responseByte = IOUtils.toByteArray(httpResponse.getEntity().getContent());
            InsertProto.InsertResponse insertResponseProto = InsertProto.InsertResponse.parseFrom(responseByte);
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
