package com.yoti.app.service.impl;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.yoti.app.content_cloud.model.ResponseRecord;
import com.yoti.app.content_cloud.model.RetrieveMessageRequest;
import com.yoti.app.content_cloud.model.RetrieveMessageResponse;
import com.yoti.app.content_cloud.service.RetrieveObject;
import com.yoti.app.content_cloud.service.impl.RetrieveObjectImpl;
import com.yoti.app.domain.Person;
import com.yoti.app.exception.CloudInteractionException;
import com.yoti.app.guice_binding.RetrieveObjectModule;
import com.yoti.ccloudpubapi_v1.RetrieveProto;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.Clock;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class RetrieveObjectTest {

    private RetrieveObject retrieveObject;

    private Clock clock;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void init() {
        Injector injector = Guice.createInjector(new RetrieveObjectModule());
        retrieveObject = injector.getInstance(RetrieveObject.class);
        clock = Clock.systemUTC();
    }

    @Test
    public void validateObjectCreation() {
        Assert.assertNotNull(retrieveObject);
        RetrieveObjectImpl impl = (RetrieveObjectImpl) retrieveObject;
        Assert.assertNotNull(impl.getRequestClient());
        Assert.assertNotNull(impl.getRetrieveProtoAdapter());
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
                .endDate(Date.from(clock.instant().plus(2, ChronoUnit.HOURS)))
                .startDate(Date.from(clock.instant()))
                .searchType(0)
                .build();
    }
}
