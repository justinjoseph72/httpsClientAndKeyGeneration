package com.yoti.app.content_cloud.service.impl;

import com.yoti.app.content_cloud.service.KeyService;
import com.yoti.app.exception.KeyGenerationException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeyServiceImpl implements KeyService {

    private final KeyFactory keyFactory;

    @Override
    public PrivateKey getPrivateKey(final @NonNull byte[] privateKeyByte) throws KeyGenerationException {
        try {
            KeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyByte);
            return keyFactory.generatePrivate(privateKeySpec);
        } catch (Exception e) {
            log.warn("Error while creating the private key from byte {} {}", e.getClass().getName(), e.getMessage());
            throw new KeyGenerationException(String.format("Error while creating the private key from byte %s %s", e.getClass().getName(), e.getMessage()));
        }
    }

    @Override
    public PublicKey getPublicKey(final @NonNull byte[] publicKeyByte) throws KeyGenerationException {
        try {
            KeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyByte);
            return keyFactory.generatePublic(publicKeySpec);
        } catch (Exception e) {
            log.warn("Error while creating the private key from byte {} {}", e.getClass().getName(), e.getMessage());
            throw new KeyGenerationException(String.format("Error while creating the private key from byte %s %s", e.getClass().getName(), e.getMessage()));
        }
    }
}
