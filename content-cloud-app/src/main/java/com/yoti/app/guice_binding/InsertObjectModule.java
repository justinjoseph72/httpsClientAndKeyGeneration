package com.yoti.app.guice_binding;

import com.google.inject.AbstractModule;
import com.yoti.app.content_Cloud.service.InsertObject;
import com.yoti.app.content_Cloud.service.PayloadConversion;
import com.yoti.app.content_Cloud.service.impl.InsertObjectImpl;
import com.yoti.app.content_Cloud.service.impl.JsonPayloadConversionImpl;
import com.yoti.app.httpClient.RequestClient;
import com.yoti.app.httpClient.impl.RequestClientImpl;

public class InsertObjectModule  extends AbstractModule{
    @Override
    protected void configure() {
        bind(InsertObject.class).to(InsertObjectImpl.class);
        bind(RequestClient.class).to(RequestClientImpl.class);
        bind(PayloadConversion.class).to(JsonPayloadConversionImpl.class);
    }
}
