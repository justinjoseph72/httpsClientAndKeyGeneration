package com.yoti.app.service.impl;

import com.yoti.app.content_cloud.model.InsertMessageRequest;
import com.yoti.app.content_cloud.model.InsertMessageResponse;
import com.yoti.app.content_cloud.service.InsertObject;
import com.yoti.app.domain.Person;
import com.yoti.app.exception.CloudInteractionException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class InsertObjectTest {

    @Autowired
    private InsertObject insertObject;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void init() {

    }

    @Test
    public void testObjectCreated() {
        Assert.assertNotNull(insertObject);
    }

    @Test
    public void testValidateInputObject() {
        InsertMessageRequest insertMessageRequest = getInsertMessageRequest();
        InsertMessageResponse response = insertObject.insertObjectToCloud(insertMessageRequest);
        Assert.assertNotNull(response);
        insertMessageRequest = InsertMessageRequest.builder()
                .cloudId("cloudId")
                .dataGroup("Connection")
                .dataObj(null)
                .requesterPublicKey("public key")
                .encryptionKeyId("keyId")
                .build();
        expectedException.expect(CloudInteractionException.class);
        insertObject.insertObjectToCloud(insertMessageRequest);
    }


    @Test
    public void testWriteSuccess() {
        InsertMessageRequest insertMessageRequest = getInsertMessageRequest();
        InsertMessageResponse response = insertObject.insertObjectToCloud(insertMessageRequest);
        Assert.assertNotNull(response);
        log.info(response.getRecordId());
    }

    private InsertMessageRequest getInsertMessageRequest() {
        Person person = new Person(1, "jj", "dd", "sf@sdf.com");
        return InsertMessageRequest.builder()
                .cloudId("cloudId")
                .dataGroup("Connection")
                .dataObj(person)
                .requesterPublicKey("public key")
                .encryptionKeyId("keyId")
                .tag(Arrays.asList("key1", "key2"))
                .build();
    }

}
