package com.yoti.app.content_cloud.service;

import com.yoti.app.exception.CloudInteractionException;

public interface PayloadConversion {

    <T> String getEncryptedPayload(final T obj, final String encryptionKeyId) throws CloudInteractionException;
}
