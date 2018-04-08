package com.yoti.app.config;

import com.yoti.app.UrlConstants.ServerConstants;
import com.yoti.app.exception.KeyGenerationException;
import com.yoti.signedrequests.utils.key.PrivateKeyProvider;
import com.yoti.signedrequests.utils.key.StaticRsaPrivateKeyProvider;
import com.yoti.signedrequests.utils.service.DefaultSignedReqService;
import com.yoti.signedrequests.utils.service.SignedRequestService;
import com.yoti.signedrequests.utils.signature.Sha256WithRsaSignatureProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;
import java.security.*;
import java.time.Clock;

@Configuration
@Slf4j
public class AppConfig {

    @PostConstruct
    public void init() {
        Security.addProvider(new BouncyCastleProvider());
    }

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
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            log.warn("Exception in in initalising rest template {} {}", e.getClass().getName(), e.getMessage());
        }
        return requestFactory;
    }


    @Bean
    public RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate(getHttpComponentsClientHttpRequestFactory());
        return restTemplate;
    }

    @Bean
    public KeyFactory getKeyFactory(){
        try {
            return KeyFactory.getInstance(ServerConstants.CIPHER_ALGORITHM, ServerConstants.BC_PROVIDER);
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            log.warn("Key Factory initialization exception {} {}", e.getClass().getName(), e.getMessage());
            throw new KeyGenerationException(String.format("Key factory initialization exception {} {} ", e.getClass().getName(), e.getMessage()));
        }
    }

    @Bean
    public SignedRequestService getDefaultSignedReqService(){
        return new DefaultSignedReqService(new Sha256WithRsaSignatureProvider(ServerConstants.BC_PROVIDER));
    }

    @Bean
    public Clock getClock(){
        return Clock.systemUTC();
    }

}
