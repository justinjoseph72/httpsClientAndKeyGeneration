package com.yoti.app.httpClient;

import org.apache.http.impl.client.CloseableHttpClient;

public interface RequestClient {

    CloseableHttpClient getCloudContentInvoker();
}
