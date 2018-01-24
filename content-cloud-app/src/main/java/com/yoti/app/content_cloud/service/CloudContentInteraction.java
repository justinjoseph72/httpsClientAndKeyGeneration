package com.yoti.app.content_cloud.service;

import com.yoti.app.UrlConstants.ServerConstants;
import org.apache.http.client.methods.HttpPost;

public interface CloudContentInteraction {

    default void populateHeaders(final HttpPost httpPost) {
        httpPost.setHeader(ServerConstants.CONTENT_TYPE_HEADER,"application/x-protobuf");
        httpPost.setHeader(ServerConstants.AUTH_DIGEST,"sdfssfsdf");
        httpPost.setHeader(ServerConstants.AUTH_KEY,"edsfsdf");
    }
}
