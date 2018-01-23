package com.yoti.app.keygen;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.security.Key;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

@Slf4j
public class GenerateKeyPairTest {

    private GenerateKeyPair generateKeyPair;

    @Before
    public void init(){
        generateKeyPair = new GenerateKeyPair();
    }

    @Test
    public void testGenerateKeyPair(){
        KeyPair keyPair = generateKeyPair.generateKeyValuePair();
        Assert.assertNotNull(keyPair);
        PrivateKey privateKey = keyPair.getPrivate();
        Assert.assertNotNull(privateKey);
        PublicKey publicKey = keyPair.getPublic();
        Assert.assertNotNull(publicKey);
    }

    @Test
    public void testGenrateKey(){
        Key key = generateKeyPair.generateAESKeyForEncryption();
        Assert.assertNotNull(key);
        log.info(String.valueOf(key.getEncoded()));
    }

}
