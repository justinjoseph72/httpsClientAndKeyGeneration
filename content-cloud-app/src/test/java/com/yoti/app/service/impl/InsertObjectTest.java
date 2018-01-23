package com.yoti.app.service.impl;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.yoti.app.content_Cloud.model.Person;
import com.yoti.app.content_Cloud.service.InsertObject;
import com.yoti.app.content_Cloud.service.impl.InsertObjectImpl;
import com.yoti.app.content_Cloud.service.impl.JsonPayloadConversionImpl;
import com.yoti.app.exception.CloudInteractionException;
import com.yoti.app.guice_binding.InsertObjectModule;
import com.yoti.app.httpClient.impl.RequestClientImpl;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class InsertObjectTest {

    private InsertObject insertObject;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void init(){
        Injector injector = Guice.createInjector(new InsertObjectModule());
        insertObject = injector.getInstance(InsertObject.class);
    }

    @Test
    public void testObjectCreated(){
        Assert.assertNotNull(insertObject);
        InsertObjectImpl imp = (InsertObjectImpl)insertObject;
        Assert.assertNotNull(imp.getRequestClient());
        Assert.assertThat(imp.getRequestClient(), CoreMatchers.instanceOf(RequestClientImpl.class));
        Assert.assertThat(imp.getPayloadConversion(), CoreMatchers.instanceOf(JsonPayloadConversionImpl.class));

    }

    @Test
    public void testValidateInputObject(){
        Person person = new Person(1,"jj","dd","sf@sdf.com");
        boolean flag = insertObject.insertObjectToCloud(person);
        Assert.assertTrue(flag);
        person = null;
        expectedException.expect(CloudInteractionException.class);
        flag = insertObject.insertObjectToCloud(person);
    }


}
