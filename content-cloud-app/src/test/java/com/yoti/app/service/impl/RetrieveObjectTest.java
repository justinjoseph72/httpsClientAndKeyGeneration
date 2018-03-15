package com.yoti.app.service.impl;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.yoti.app.content_cloud.model.ResponseRecord;
import com.yoti.app.content_cloud.model.RetrieveMessageRequest;
import com.yoti.app.content_cloud.model.RetrieveMessageResponse;
import com.yoti.app.content_cloud.service.RetrieveObject;
import com.yoti.app.content_cloud.service.impl.JsonPayloadConversionImpl;
import com.yoti.app.content_cloud.service.impl.RetrieveObjectImpl;
import com.yoti.app.domain.Person;
import com.yoti.app.exception.CloudInteractionException;
import com.yoti.app.guice_binding.PayloadConverterModule;
import com.yoti.app.guice_binding.RetrieveObjectModule;
import com.yoti.ccloudpubapi_v1.RetrieveProto;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Clock;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RetrieveObjectTest {

    @Autowired
    private RetrieveObject retrieveObject;

    private Clock clock;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void init() {
      //  Injector injector = Guice.createInjector(new RetrieveObjectModule(), new PayloadConverterModule());
       // retrieveObject = injector.getInstance(RetrieveObject.class);
        clock = Clock.systemUTC();
    }

    @Test
    public void validateObjectCreation() {
        Assert.assertNotNull(retrieveObject);
        RetrieveObjectImpl impl = (RetrieveObjectImpl) retrieveObject;
    }

    @Test
    public void validateInputObject() {
        expectedException.expect(CloudInteractionException.class);
        retrieveObject.fetchRecordsFromCloud(null);
    }

    /**
     * This method is failing have to sort out Tags for fix this
     * the response should have Tags for the byte to parse
     */
    @Test
    public void validObjectReturnedForValidInput() {
        RetrieveMessageResponse response = retrieveObject.fetchRecordsFromCloud(getInsertMessageRequest());
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getRecords());
        Assert.assertThat(response.getRecords(), Matchers.not(Matchers.emptyIterableOf(ResponseRecord.class)));
        Assert.assertThat(response.getRecords(), Matchers.not(Matchers.empty()));

    }

    private RetrieveMessageRequest getInsertMessageRequest() {
        return RetrieveMessageRequest.builder()
                .cloudId("cloudId")
                .dataGroup("Connection")
                .requesterPublicKey("public key")
                .queryTags(Arrays.asList("query1", "query2"))
                .endDate(Date.from(clock.instant().plus(2, ChronoUnit.HOURS)))
                .startDate(Date.from(clock.instant()))
                .searchType(0)
                .build();
    }
}
