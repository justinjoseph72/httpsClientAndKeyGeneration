package com.yoti.app.content_cloud.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yoti.app.UrlConstants.ServerConstants;
import com.yoti.app.content_cloud.RequestHelper;
import com.yoti.app.content_cloud.model.InsertMessageRequest;
import com.yoti.app.content_cloud.service.SignMessageService;
import com.yoti.app.exception.SignMessageException;
import com.yoti.signedrequests.utils.exceptions.SignedRequestValidationException;
import com.yoti.signedrequests.utils.service.SignedRequestService;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.*;
import java.util.Arrays;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class SignMessageTest {

    @Autowired
    private SignMessageService signMessageService;

    @Autowired
    private SignedRequestService signedRequestService;

    @Autowired
    private ObjectMapper mapper;

    private KeyPair keyPair;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void init() throws NoSuchProviderException, NoSuchAlgorithmException {
        Security.addProvider(new BouncyCastleProvider());
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ServerConstants.CIPHER_ALGORITHM, ServerConstants.BC_PROVIDER);
        keyPairGenerator.initialize(2018);
        keyPair = keyPairGenerator.generateKeyPair();
    }

    @Test
    public void loadContext() {
        Assert.assertNotNull(signMessageService);
    }

    @Test
    public void shouldThrowExceptionOnUsingSignMessageExceptionInvalidPrivateKeyToSignMessages() {
        expectedException.expect(SignMessageException.class);
        byte[] privateKeyByte = "sdfsf".getBytes();
        Assert.assertNotNull(privateKeyByte);
        InsertMessageRequest insertMessageRequest = RequestHelper.getInsertMessagRequest("TEstStr", "sfsdf", "ccc",
                Arrays.asList("key1", "key2"), "eee");
        String signedData = signMessageService.signMessage(getJsonPayload(insertMessageRequest),privateKeyByte);

    }

    @Test
    public void shouldSignTheInsertMessageRequestWithValidPrivateKeyWithoutAnyErrors() {
        byte[] privateKeyByte = keyPair.getPrivate().getEncoded();
        Assert.assertNotNull(privateKeyByte);
        InsertMessageRequest insertMessageRequest = RequestHelper.getInsertMessagRequest("TEstStr", "sfsdf", "ccc",
                Arrays.asList("key1", "key2"), "eee");
        String signedData = signMessageService.signMessage(getJsonPayload(insertMessageRequest),privateKeyByte);
        Assert.assertNotNull(signedData);
    }

    @Test
    public void shouldVerifyTheSignForInputDataForValidKeyPair() {
        byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();
        PublicKey publicKey = keyPair.getPublic();
        InsertMessageRequest insertMessageRequest = RequestHelper.getInsertMessagRequest("TEstStr", "sfsdf", "ccc",
                Arrays.asList("key1", "key2"), "eee");
        String message = getJsonPayload(insertMessageRequest);
        String signedData = signMessageService.signMessage(message,privateKeyBytes);
        signedRequestService.verify(publicKey,message,signedData);
    }

    @Test
    public void shouldThrowExceptionOnVerifyingDifferentData(){
        expectedException.expect(SignedRequestValidationException.class);
        expectedException.expectMessage("The signature is not valid");
        String message ="This is a test string";
        String signedData = signMessageService.signMessage(message,keyPair.getPrivate().getEncoded());
        signedRequestService.verify(keyPair.getPublic(),"Another text",signedData);
    }

    private <T> String getJsonPayload(T obj)  {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return null;
        }
    }


}
