package com.yoti.app.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.yoti.app.domain.Person;
import com.yoti.app.content_cloud.service.PayloadConversion;
import com.yoti.app.content_cloud.service.impl.JsonPayloadConversionImpl;
import com.yoti.app.guice_binding.PayloadConverterModule;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class JsonPayloadConverterTest {

    @Autowired
    private PayloadConversion payloadConversion;


    @Test
    public void testInjectionOfClasses() {
        Assert.assertNotNull(payloadConversion);
    }

    @Test
    public void testConversion() throws IOException {
        Person person = new Person(1, "jj", "dd", "sf@sdf.com");
        String str = payloadConversion.getEncryptedPayload(person, "sd");
        Assert.assertNotNull(str);
        log.info(str);
        ObjectMapper mapper = new ObjectMapper();
        Person person1 = mapper.readValue(str, Person.class);
        Assert.assertNotNull(person1);
        Assert.assertTrue(person.equals(person1));
    }
}
