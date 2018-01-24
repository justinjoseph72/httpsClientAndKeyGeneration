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
import com.yoti.app.exception.CloudInteractionException;
import com.yoti.app.httpClient.RequestClient;
import com.yoti.ccloudpubapi_v1.InsertProto;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;

import java.io.IOException;


@Slf4j
@Singleton
public class InsertObjectImpl implements InsertObject {

    @Inject
    @Getter
    private RequestClient requestClient;

    @Inject
    @Getter
    private InsertProtoAdapter insertProtoAdapter;


    @Override
    public <T> InsertMessageResponse insertObjectToCloud(final InsertMessageRequest<T> insertMessageRequest) throws CloudInteractionException {
        validateInputObject(insertMessageRequest);
        InsertProto.InsertRequest request = insertProtoAdapter.getInsertProtoFromInsertMessageRequest(insertMessageRequest);
        try {
            ByteArrayEntity protoEntity = new ByteArrayEntity(request.toByteArray());
            protoEntity.setContentType("application/x-protobuf");
            HttpPost httpPost = new HttpPost(ServerConstants.TEST_URL);
            httpPost.setEntity(protoEntity);
            populateHeaders(httpPost);
            HttpResponse httpResponse = requestClient.getCloudContentInvoker().execute(httpPost);
            return handleResponse(httpResponse);
        } catch (Exception e) {
            log.info(e.getMessage());
            throw new CloudInteractionException(ErrorCodes.CLOUD_INSERT_ERROR, e.getMessage());
        }
    }


    private InsertMessageResponse handleResponse(final HttpResponse httpResponse) throws IOException {
        if(httpResponse.getStatusLine().getStatusCode()!=200){
            throw new CloudInteractionException(ErrorCodes.CLOUD_INSERT_ERROR,String.format("Status %d returned from the Content Cloud Service",httpResponse.getStatusLine().getStatusCode()));
        }
        String responseContent = IOUtils.toString(httpResponse.getEntity().getContent());
        return InsertMessageResponse.builder().recordId(responseContent).build();
    }


    private <T> void validateInputObject(final T obj) {
        if (obj == null) {
            throw new CloudInteractionException(ErrorCodes.NULL_INPUT, ErrorMessages.NULL_OBJ_MSG);
        }

    }


}
