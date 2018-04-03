package com.yoti.app.content_cloud.service.impl;

import com.yoti.app.content_cloud.RequestHelper;
import com.yoti.app.content_cloud.model.InsertMessageRequest;
import com.yoti.app.content_cloud.model.InsertMessageResponse;
import com.yoti.app.content_cloud.service.InsertObject;
import com.yoti.app.controllers.model.ContentCloudModel;
import com.yoti.app.domain.Person;
import com.yoti.app.exception.CloudInteractionException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Arrays;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("mock")
public class InsertObjectTest {

    @Autowired
    private InsertObject insertObject;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testObjectCreated() {
        Assert.assertNotNull(insertObject);
    }

    @Test
    public void testValidateInputObject() throws NoSuchProviderException, NoSuchAlgorithmException {

        ContentCloudModel contentCloudModel = getContentCloudModel();
        final InsertMessageRequest insertMessageRequest;
        InsertMessageResponse response = insertObject.insertObjectToCloud(contentCloudModel);
        Assert.assertNotNull(response);
        insertMessageRequest = RequestHelper.getInsertMessagRequest(null, "public-key", "cloudId", Arrays.asList("key1", "key2"), "encryptionId");
        contentCloudModel = ContentCloudModel.builder().data(insertMessageRequest).keyData(RequestHelper.getKeyData()).build();
        expectedException.expect(CloudInteractionException.class);
        insertObject.insertObjectToCloud(contentCloudModel);
    }

    private ContentCloudModel getContentCloudModel() throws NoSuchProviderException, NoSuchAlgorithmException {
        InsertMessageRequest insertMessageRequest = getInsertMessageRequest();
        return ContentCloudModel.builder().data(insertMessageRequest).keyData(RequestHelper.getKeyData()).build();
    }


    @Test
    public void testWriteSuccess() throws NoSuchProviderException, NoSuchAlgorithmException {
        InsertMessageResponse response = insertObject.insertObjectToCloud(getContentCloudModel());
        Assert.assertNotNull(response);
        log.info(response.getRecordId());
    }

    private InsertMessageRequest getInsertMessageRequest() {
        Person person = new Person(1, "jj", "dd", "sf@sdf.com");
        return RequestHelper.getInsertMessagRequest(person, "public-key", "cloudId", Arrays.asList("key1", "key2"), "encryptionId");
    }

}
