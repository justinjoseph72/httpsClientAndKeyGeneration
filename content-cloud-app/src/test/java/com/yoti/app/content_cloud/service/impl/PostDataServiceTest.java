package com.yoti.app.content_cloud.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yoti.app.content_cloud.RequestHelper;
import com.yoti.app.content_cloud.model.InsertMessageRequest;
import com.yoti.app.content_cloud.service.PostDataService;
import com.yoti.app.controllers.model.ContentCloudModel;
import com.yoti.app.controllers.model.KeyData;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Arrays;

@SpringBootTest
@Slf4j
@RunWith(SpringRunner.class)
public class PostDataServiceTest {

    @Autowired
    private PostDataService postDataService;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void loadContexts(){
        Assert.assertNotNull(postDataService);
    }

    @Test
    public void shouldPostInsertRequestWithCorrectSignature() {
        final ContentCloudModel<InsertMessageRequest> message = getContentCloudModelForInsertMessageRequest();



    }


    private ContentCloudModel<InsertMessageRequest> getContentCloudModelForInsertMessageRequest() {
        InsertMessageRequest messageRequest =  RequestHelper.getInsertMessagRequest("TEstStr", "sfsdf", "ccc",
                Arrays.asList("key1", "key2"), "eee");
        KeyData keyData = RequestHelper.getKeyData();
        ContentCloudModel contentCloudModel = ContentCloudModel.builder().keyData(keyData).data(messageRequest).build();
        return contentCloudModel;
    }


    private <T> String getJsonPayload(T obj)  {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
