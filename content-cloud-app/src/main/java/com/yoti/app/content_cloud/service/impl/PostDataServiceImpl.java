package com.yoti.app.content_cloud.service.impl;

import com.yoti.app.UrlConstants.ServerConstants;
import com.yoti.app.content_cloud.model.PostDataModel;
import com.yoti.app.content_cloud.service.PostDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostDataServiceImpl implements PostDataService {

    private final RestTemplate restTemplate;

    private HttpHeaders getHeaders() {

        //TODO update the headers to use the correct values
        HttpHeaders headers = new HttpHeaders();
        headers.add(ServerConstants.CONTENT_TYPE_HEADER, "application/json");
        headers.add(ServerConstants.AUTH_DIGEST, "sdfssfsdf");
        headers.add(ServerConstants.AUTH_KEY, "edsfsdf");
        return headers;
    }

    public ResponseEntity<?> postData(final PostDataModel postDataModel) {
        log.debug("posting to url {}", postDataModel.getPostUrl());
        log.debug("payload : {}", postDataModel.getPayload());
        HttpEntity<String> strEntity = new HttpEntity<>(postDataModel.getPayload(), getHeaders());
        ResponseEntity<String> response = restTemplate.exchange(postDataModel.getPostUrl(), HttpMethod.POST, strEntity, String.class);
        return response;
    }
}
