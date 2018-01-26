package com.yoti.app.service.impl;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.yoti.app.content_cloud.model.BinRequest;
import com.yoti.app.content_cloud.service.BinInteractions;
import com.yoti.app.content_cloud.service.impl.BinInteractionsImpl;
import com.yoti.app.exception.CloudDataAdapterException;
import com.yoti.app.exception.CloudInteractionException;
import com.yoti.app.guice_binding.BinObjectModule;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class BinInteractionTest {

    private BinInteractions binInteractions;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void init() {
        Injector injector = Guice.createInjector(new BinObjectModule());
        binInteractions = injector.getInstance(BinInteractions.class);
    }


    @Test
    public void validateAllObjects() {
        Assert.assertNotNull(binInteractions);
        BinInteractionsImpl impl = (BinInteractionsImpl) binInteractions;
        Assert.assertNotNull(impl.getBinAdapter());
        Assert.assertNotNull(impl.getRequestClient());
    }

    @Test
    public void validateInputObject() {
        expectedException.expect(CloudInteractionException.class);
        binInteractions.moveObjectToBin(null);
    }

    @Test
    public void validResponseForValidInput() {
        BinRequest binRequest = getBinRequest();
        Boolean moved = binInteractions.moveObjectToBin(binRequest);
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
