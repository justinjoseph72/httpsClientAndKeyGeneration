package com.yoti.app.content_Cloud.service.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yoti.app.UrlConstants.ErrorCodes;
import com.yoti.app.UrlConstants.ErrorMessages;
import com.yoti.app.UrlConstants.ServerConstants;
import com.yoti.app.content_Cloud.annotations.CloudBody;
import com.yoti.app.content_Cloud.service.InsertObject;
import com.yoti.app.content_Cloud.service.PayloadConversion;
import com.yoti.app.exception.CloudInteractionException;
import com.yoti.app.httpClient.RequestClient;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;


@Slf4j
@Singleton
public class InsertObjectImpl implements InsertObject {

    @Inject
    @Getter
    private RequestClient requestClient;

    @Inject
    @Getter
    private PayloadConversion payloadConversion;

    @Override
    public <T> boolean insertObjectToCloud(final T obj) throws CloudInteractionException {
        validateInputObject(obj);
        String postStr = payloadConversion.getPayloadAsString(obj);
        try {
            StringEntity stringEntity = new StringEntity(postStr);
            HttpPost httpPost = new HttpPost(ServerConstants.TEST_URL);
            httpPost.setEntity(stringEntity);
            requestClient.getCloudContentInvoker().execute(httpPost);
        } catch (Exception e) {
            log.info(e.getMessage());
            throw new CloudInteractionException(ErrorCodes.CLOUD_INSERT_ERROR,e.getMessage());
        }
        return true;

    }

    private <T> void validateInputObject(final T obj) {
        if (obj == null) {
            throw new CloudInteractionException(ErrorCodes.NULL_INPUT, ErrorMessages.NULL_OBJ_MSG);
        }
        if (!obj.getClass().isAnnotationPresent(CloudBody.class)) {
            throw new CloudInteractionException(ErrorCodes.NOT_CLOUD_BODY, ErrorMessages.NOT_CLOUD_BODY);
        }
    }


}
