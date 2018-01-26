package com.yoti.app.guice_binding;

import com.google.inject.AbstractModule;
import com.yoti.app.content_cloud.service.BinInteractions;
import com.yoti.app.content_cloud.service.impl.BinInteractionsImpl;
import com.yoti.app.httpClient.RequestClient;
import com.yoti.app.httpClient.impl.RequestClientImpl;

public class BinObjectModule extends AbstractModule{
    @Override
    protected void configure() {
        bind(BinInteractions.class).to(BinInteractionsImpl.class);
        bind(RequestClient.class).to(RequestClientImpl.class);
    }
}
