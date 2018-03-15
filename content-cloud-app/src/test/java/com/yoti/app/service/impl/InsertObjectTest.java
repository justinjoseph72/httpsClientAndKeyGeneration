package com.yoti.app.service.impl;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.yoti.app.content_cloud.adpaters.InsertProtoAdapter;
import com.yoti.app.content_cloud.model.InsertMessageRequest;
import com.yoti.app.content_cloud.model.InsertMessageResponse;
import com.yoti.app.domain.Person;
import com.yoti.app.content_cloud.service.InsertObject;
import com.yoti.app.content_cloud.service.impl.InsertObjectImpl;
import com.yoti.app.content_cloud.service.impl.JsonPayloadConversionImpl;
import com.yoti.app.exception.CloudInteractionException;
import com.yoti.app.guice_binding.InsertObjectModule;
import com.yoti.app.guice_binding.InsertProtoAdapterModule;
import com.yoti.app.httpClient.impl.RequestClientImpl;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

@Slf4j
public class InsertObjectTest {

    private InsertObject insertObject;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void init() {
        Injector injector = Guice.createInjector(new Module[]{new InsertObjectModule(), new InsertProtoAdapterModule()});
        insertObject = injector.getInstance(InsertObject.class);
    }

    @Test
    public void testObjectCreated() {
        Assert.assertNotNull(insertObject);
        InsertObjectImpl imp = (InsertObjectImpl) insertObject;


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
                .build();
    }

}
