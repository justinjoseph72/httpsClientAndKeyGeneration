package com.yoti.app.content_Cloud.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yoti.app.UrlConstants.ErrorCodes;
import com.yoti.app.content_Cloud.service.PayloadConversion;
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
    public <T> String getPayloadAsString(final T obj) throws CloudInteractionException {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.info(e.getMessage());
            throw new CloudInteractionException(ErrorCodes.BODY_CONVERSION_ERROR,e.getMessage());
        }
    }
}
