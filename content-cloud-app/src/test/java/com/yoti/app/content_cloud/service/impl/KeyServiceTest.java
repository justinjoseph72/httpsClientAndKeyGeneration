package com.yoti.app.content_cloud.service.impl;

import com.yoti.app.UrlConstants.ServerConstants;
import com.yoti.app.content_cloud.service.KeyService;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class KeyServiceTest {

    @Autowired
    private KeyService keyService;

    private KeyPair keyPair;

    @Before
    public void init() throws NoSuchProviderException, NoSuchAlgorithmException {
        Security.addProvider(new BouncyCastleProvider());
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ServerConstants.CIPHER_ALGORITHM, ServerConstants.BC_PROVIDER);
        keyPairGenerator.initialize(2018);
        keyPair = keyPairGenerator.generateKeyPair();
    }

    @Test
    public void loadContext() {
        Assert.assertNotNull(keyService);
        Assert.assertNotNull(keyPair);
    }

    @Test
    public void shouldGetPrivateKeyFromValidPrivateKeyByte() {
        byte[] privateKyeByte = keyPair.getPrivate().getEncoded();
        Assert.assertNotNull(privateKyeByte);
        PrivateKey privateKeyFormService = keyService.getPrivateKey(privateKyeByte);
        Assert.assertNotNull(privateKeyFormService);
        Assert.assertFalse(privateKeyFormService.isDestroyed());
    }

    @Test
    public void shouldGetPublicKeyFromValidPublicKeyByte() {
        byte[] publicKeyByte = keyPair.getPublic().getEncoded();
        Assert.assertNotNull(publicKeyByte);
        PublicKey publicKeyFromService = keyService.getPublicKey(publicKeyByte);
        Assert.assertNotNull(publicKeyFromService);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionForNullPrivateKey() {
        keyService.getPrivateKey(null);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionForNullPublicKey() {
        keyService.getPublicKey(null);
    }

    @Test
    public void shouldEncrytAndDecryptProperly() throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        String testStr = "This is a test String";
        PrivateKey privateKey = keyPair.getPrivate();
        Cipher cipher = Cipher.getInstance(ServerConstants.CIPHER_ALGORITHM, ServerConstants.BC_PROVIDER);
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] encryptedData = cipher.doFinal(testStr.getBytes());
        Assert.assertNotNull(encryptedData);
        byte[] publicKeyByte = keyPair.getPublic().getEncoded();
        PublicKey publicKeyFormService = keyService.getPublicKey(publicKeyByte);
        cipher.init(Cipher.DECRYPT_MODE, publicKeyFormService);
        String decryptedText = new String(cipher.doFinal(encryptedData));
        Assert.assertNotNull(decryptedText);
        Assert.assertThat(decryptedText, Matchers.is(testStr));

    }

}
