package com.yoti.app.guice_binding;

import com.google.inject.AbstractModule;
import com.yoti.app.content_Cloud.service.PayloadConversion;
import com.yoti.app.content_Cloud.service.impl.JsonPayloadConversionImpl;

public class PayloadConverterModule  extends AbstractModule{
    @Override
    protected void configure() {
        bind(PayloadConversion.class).to(JsonPayloadConversionImpl.class);

    }
}
