package com.yoti.app.content_cloud.service;

import com.yoti.app.UrlConstants.ServerConstants;
import com.yoti.app.httpClient.RequestClient;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

public interface ContentCloudInteraction {


    default void populateHeaders(final HttpPost httpPost) {
        httpPost.setHeader(ServerConstants.CONTENT_TYPE_HEADER, "application/json");
        httpPost.setHeader(ServerConstants.AUTH_DIGEST, "sdfssfsdf");
        httpPost.setHeader(ServerConstants.AUTH_KEY, "edsfsdf");
    }

    default HttpResponse postDataAndGetResponse(RequestClient requestClient, HttpEntity entity, String url) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(entity);
        populateHeaders(httpPost);
        HttpResponse httpResponse = requestClient.getCloudContentInvoker().execute(httpPost);
        return httpResponse;
    }




}
