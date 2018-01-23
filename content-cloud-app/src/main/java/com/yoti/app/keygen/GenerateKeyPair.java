package com.yoti.app.keygen;

import com.yoti.app.exception.KeyPairGenerareException;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.KeyGenerator;
import java.io.UnsupportedEncodingException;
import java.security.*;

@Slf4j
public class GenerateKeyPair {

    private static final String KET_PAIR_ALGORITHM = "DSA";
    private static final String KEY_ALGORITHM = "AES";
    private static final String RANDOM_ALGORITHM = "SHA1PRNG";
    private static final String DIGEST_ALGORITHM = "SHA-256";
    private static final String PROVIDER = "BC";
    private static final int KEY_SIZE = 1024;


    public KeyPair generateKeyValuePair() throws KeyPairGenerareException{
        KeyPair keyPair = null;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KET_PAIR_ALGORITHM,PROVIDER);
            SecureRandom random = SecureRandom.getInstance(RANDOM_ALGORITHM,PROVIDER);
            keyPairGenerator.initialize(KEY_SIZE,random);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            log.info(e.getMessage());
            throw new KeyPairGenerareException(e.getMessage());
        } catch (NoSuchProviderException e) {
            log.info(e.getMessage());
            throw new KeyPairGenerareException(e.getMessage());
        }
        return keyPair;
    }

    public Key generateAESKeyForEncryption() throws KeyPairGenerareException {
        KeyGenerator keyGenerator = null;
        try {
            keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            log.info(e.getMessage());
            throw new KeyPairGenerareException(e.getMessage());
        }
        return keyGenerator.generateKey();
    }

    public byte[] getMessageDigest(String messasge) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest messageDigest = MessageDigest.getInstance(DIGEST_ALGORITHM);
        return messageDigest.digest(messasge.getBytes("UTF-8"));
    }
}
