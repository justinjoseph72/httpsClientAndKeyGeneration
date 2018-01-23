package com.yoti.app.content_Cloud.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yoti.app.exception.CloudInteractionException;

public interface PayloadConversion {

    <T> String getPayloadAsString(T obj) throws CloudInteractionException;
}
