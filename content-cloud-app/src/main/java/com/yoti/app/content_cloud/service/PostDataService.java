package com.yoti.app.content_cloud.service;

import com.yoti.app.UrlConstants.ServerConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class PostDataService {

    private final RestTemplate restTemplate;

    private HttpHeaders getHeaders() {

        //TODO update the headers to use the correct values
        HttpHeaders headers = new HttpHeaders();
        headers.add(ServerConstants.CONTENT_TYPE_HEADER, "application/json");
        headers.add(ServerConstants.AUTH_DIGEST, "sdfssfsdf");
        headers.add(ServerConstants.AUTH_KEY, "edsfsdf");
        return headers;
    }

    public ResponseEntity<?> postData(final String url, final String payLoad) {
        log.debug("posting to url {}", url);
        log.debug("payload : {}", payLoad);
        HttpEntity<String> strEntity = new HttpEntity<>(payLoad, getHeaders());
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, strEntity, String.class);
        return response;
    }

}
