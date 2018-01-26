package com.yoti.app.guice_binding;

import com.google.inject.AbstractModule;
import com.yoti.app.content_cloud.service.RetrieveObject;
import com.yoti.app.content_cloud.service.impl.RetrieveObjectImpl;
import com.yoti.app.httpClient.RequestClient;
import com.yoti.app.httpClient.impl.RequestClientImpl;

public class RetrieveObjectModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(RetrieveObject.class).to(RetrieveObjectImpl.class);
        bind(RequestClient.class).to(RequestClientImpl.class);
    }
}
