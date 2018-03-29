package com.yoti.app.content_cloud.service.impl;

import com.yoti.app.UrlConstants.ServerConstants;
import com.yoti.app.content_cloud.service.KeyService;
import com.yoti.app.exception.KeyGenerationException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.*;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Slf4j
@Service
public class KeyServiceImpl implements KeyService {


    private KeyFactory keyFactory;

    {
        try {
            keyFactory = KeyFactory.getInstance(ServerConstants.CIPHER_ALGORITHM, ServerConstants.PROVIDER);
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            log.warn("Key Factory initialization exception {} {}", e.getClass().getName(), e.getMessage());
            throw new KeyGenerationException(String.format("Key factory initialization exception {} {} ", e.getClass().getName(), e.getMessage()));
        }
    }


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
