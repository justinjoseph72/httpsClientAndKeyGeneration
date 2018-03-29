package com.yoti.app.content_cloud.service.impl;

import com.yoti.app.content_cloud.RequestHelper;
import com.yoti.app.content_cloud.model.ResponseRecord;
import com.yoti.app.content_cloud.model.RetrieveMessageRequest;
import com.yoti.app.content_cloud.model.RetrieveMessageResponse;
import com.yoti.app.content_cloud.service.RetrieveObject;
import com.yoti.app.controllers.model.ContentCloudModel;
import com.yoti.app.exception.CloudInteractionException;
import com.yoti.ccloudpubapi_v1.RetrieveProto;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Clock;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("mock")
public class RetrieveObjectTest {

    @Autowired
    private RetrieveObject retrieveObject;

    private Clock clock;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void init() {
        clock = Clock.systemUTC();
    }

    @Test
    public void validateObjectCreation() {
        Assert.assertNotNull(retrieveObject);
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
        RetrieveMessageResponse response = retrieveObject.fetchRecordsFromCloud(getContentCloudModel(getInsertMessageRequest()));
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getRecords());
        Assert.assertThat(response.getRecords(), Matchers.not(Matchers.emptyIterableOf(ResponseRecord.class)));
        Assert.assertThat(response.getRecords(), Matchers.not(Matchers.empty()));

    }

    private RetrieveMessageRequest getInsertMessageRequest() {
        return RequestHelper.getRetrieveMessageRequest("cloudId", "public key", Arrays.asList("query1", "query2"),
                Date.from(clock.instant()), Date.from(clock.instant().plus(2, ChronoUnit.HOURS)), RetrieveProto.RetrieveRequest.SearchType.AND_TAGS_VALUE, true);

    }

    private <T> ContentCloudModel<T> getContentCloudModel(T retrieveMessageRequest) {
        ContentCloudModel contentCloudModel = ContentCloudModel.builder()
                .data(retrieveMessageRequest)
                .privateKey("ssss".getBytes())
                .build();
        return contentCloudModel;
    }
}
