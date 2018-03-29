package com.yoti.app.content_cloud.service;

import com.yoti.app.exception.KeyGenerationException;

import java.security.PrivateKey;
import java.security.PublicKey;

public interface KeyService {

    PrivateKey getPrivateKey(final byte[] privateKeyByte) throws KeyGenerationException;

    PublicKey getPublicKey(final byte[] publicKeyByte) throws KeyGenerationException;
}
