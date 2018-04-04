package com.yoti.app.content_cloud.service.impl;

import com.yoti.app.content_cloud.RequestHelper;
import com.yoti.app.content_cloud.model.BinRequest;
import com.yoti.app.content_cloud.service.BinInteractions;
import com.yoti.app.controllers.model.ContentCloudModel;
import com.yoti.app.exception.CloudInteractionException;
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

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("mock")
public class BinInteractionTest {

    @Autowired
    private BinInteractions binInteractions;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();


    @Test
    public void validateAllObjects() {
        Assert.assertNotNull(binInteractions);

    }

    @Test
    public void validateInputObject() {
        expectedException.expect(CloudInteractionException.class);
        binInteractions.moveObjectToBin(null);
    }

    @Test
    public void validResponseForValidMoveToBinInput() {
        Boolean moved = binInteractions.moveObjectToBin(getContentCloudModel(getBinRequest()));
        Assert.assertNotNull(moved);
        Assert.assertTrue(moved.booleanValue());
    }

    @Test
    public void validResponseForValidRestoreBinInput() {
        Boolean moved = binInteractions.restoreObjectFromBin(getContentCloudModel(getBinRequest()));
        Assert.assertNotNull(moved);
        Assert.assertTrue(moved.booleanValue());
    }

    @Test
    public void validResponseForValidRemoveBinInput() {
        Boolean moved = binInteractions.removeBinnedObjectFromBin(getContentCloudModel(getBinRequest()));
        Assert.assertNotNull(moved);
        Assert.assertTrue(moved.booleanValue());
    }


    private BinRequest getBinRequest() {
        return BinRequest.builder()
                .cloudId("sdf")
                .dataGroup("CONNECTIOND")
                .recordId("eee")
                .requesterPublicKey("sdfasdfasdf")
                .build();
    }

    private <T> ContentCloudModel<T> getContentCloudModel(final T binRequest) {
        ContentCloudModel contentCloudModel = ContentCloudModel.builder()
                .data(binRequest)
                .keyData(RequestHelper.getKeyData())
                .build();
        return contentCloudModel;
    }
}
