package com.yoti.app.httpClient.impl;

import com.yoti.app.httpClient.RequestClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.SSLContext;

@Slf4j
public class RequestClientImpl implements RequestClient {

    @Override
    public CloseableHttpClient getCloudContentInvoker() {
        try {
            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, (cert, authType) -> true).build();
            CloseableHttpClient httpClient = HttpClients.custom()
                    .setSSLContext(sslContext)
                    .setSSLHostnameVerifier(new NoopHostnameVerifier()).build();
            return httpClient;

        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return null;
    }
}
