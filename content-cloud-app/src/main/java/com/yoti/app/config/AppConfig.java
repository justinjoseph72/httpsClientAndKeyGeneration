package com.yoti.app.config;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

@Configuration
public class AppConfig {

    @Bean
    public HttpComponentsClientHttpRequestFactory getHttpComponentsClientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory requestFactory = null;
        try {
            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, (cert, authType) -> true).build();
            CloseableHttpClient httpClient = HttpClients.custom()
                    .setSSLContext(sslContext)
                    .setSSLHostnameVerifier(new NoopHostnameVerifier()).build();
            requestFactory = new HttpComponentsClientHttpRequestFactory();
            requestFactory.setHttpClient(httpClient);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return requestFactory;
    }


    @Bean
    public RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate(getHttpComponentsClientHttpRequestFactory());
        return restTemplate;
    }
}
