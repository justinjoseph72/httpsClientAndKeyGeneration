package com.yoti.app.content_cloud.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yoti.app.UrlConstants.ErrorCodes;
import com.yoti.app.UrlConstants.ErrorMessages;
import com.yoti.app.content_cloud.service.PayloadConversion;
import com.yoti.app.exception.CloudInteractionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JsonPayloadConversionImpl implements PayloadConversion {

    private final ObjectMapper mapper;

    @Override
    public <T> String getEncryptedPayload(final T obj, final String encryptionKeyId) throws CloudInteractionException {
        try {
            validateInputObject(obj);
            String jsonStr = mapper.writeValueAsString(obj);
            return encryptDataUsingKey(jsonStr, encryptionKeyId);
        } catch (JsonProcessingException e) {
            log.warn(" Exception {} {}", e.getClass().getName(), e.getMessage());
            throw new CloudInteractionException(ErrorCodes.BODY_CONVERSION_ERROR, e.getMessage());
        }
    }

    private <T> void validateInputObject(final T obj) {
        if (obj == null) {
            throw new CloudInteractionException(ErrorCodes.NULL_INPUT, ErrorMessages.NULL_OBJ_MSG);
        }
    }

    private String encryptDataUsingKey(final String dataToEncrypt, final String encryptionKeyId) {
        // TODO fetch the AES key using the encryptionKeyId and encrypt the JSON and return the encryptedData
        return dataToEncrypt;
    }
}
