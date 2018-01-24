package com.yoti.app.content_cloud.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yoti.app.UrlConstants.ErrorCodes;
import com.yoti.app.UrlConstants.ErrorMessages;
import com.yoti.app.content_cloud.annotations.CloudBody;
import com.yoti.app.content_cloud.service.PayloadConversion;
import com.yoti.app.exception.CloudInteractionException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class JsonPayloadConversionImpl implements PayloadConversion {

    @Inject
    @Getter
    private ObjectMapper mapper;

    @Override
    public <T> String getEncryptedPayload(final T obj, final String encryptionKeyId) throws CloudInteractionException {
        try {
            validateInputObject(obj);
            String jsonStr = mapper.writeValueAsString(obj);
            return encryptDataUsingKey(jsonStr, encryptionKeyId);
        } catch (JsonProcessingException e) {
            log.info(e.getMessage());
            throw new CloudInteractionException(ErrorCodes.BODY_CONVERSION_ERROR, e.getMessage());
        }
    }

    private <T> void validateInputObject(final T obj) {
        if (obj == null) {
            throw new CloudInteractionException(ErrorCodes.NULL_INPUT, ErrorMessages.NULL_OBJ_MSG);
        }
        if (!obj.getClass().isAnnotationPresent(CloudBody.class)) {
            throw new CloudInteractionException(ErrorCodes.NOT_CLOUD_BODY, ErrorMessages.NOT_CLOUD_BODY);
        }
    }

    private String encryptDataUsingKey(final String dataToEncrypt, final String encryptionKeyId) {
        // TODO fetch the AES key using the encryptionKeyId and encrypt the JSON and return the encryptedData
        return dataToEncrypt;
    }
}
