package com.yoti.app.guice_binding;

import com.google.inject.AbstractModule;
import com.yoti.app.content_cloud.service.PayloadConversion;
import com.yoti.app.content_cloud.service.impl.JsonPayloadConversionImpl;

public class InsertProtoAdapterModule extends AbstractModule{

    @Override
    protected void configure() {
        bind(PayloadConversion.class).to(JsonPayloadConversionImpl.class);
    }
}
