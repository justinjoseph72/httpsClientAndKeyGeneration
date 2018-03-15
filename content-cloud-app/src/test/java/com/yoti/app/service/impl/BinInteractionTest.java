package com.yoti.app.service.impl;

import com.yoti.app.content_cloud.model.BinRequest;
import com.yoti.app.content_cloud.service.BinInteractions;
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
        BinRequest binRequest = getBinRequest();
        Boolean moved = binInteractions.moveObjectToBin(binRequest);
        Assert.assertNotNull(moved);
        Assert.assertTrue(moved.booleanValue());
    }

    @Test
    public void validResponseForValidRestoreBinInput() {
        BinRequest binRequest = getBinRequest();
        Boolean moved = binInteractions.restoreObjectFromBin(binRequest);
        Assert.assertNotNull(moved);
        Assert.assertTrue(moved.booleanValue());
    }

    @Test
    public void validResponseForValidRemoveBinInput() {
        BinRequest binRequest = getBinRequest();
        Boolean moved = binInteractions.removeBinnedObjectFromBin(binRequest);
        Assert.assertNotNull(moved);
        Assert.assertTrue(moved.booleanValue());
    }

    @Test
    public void validResponseForValidEmptyBinInput() {
        BinRequest binRequest = getBinRequest();
        Boolean moved = binInteractions.emptyBin(binRequest);
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
}
