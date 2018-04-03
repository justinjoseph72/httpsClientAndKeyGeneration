package com.yoti.app.content_cloud.service;

public interface SignMessageService {

     String signMessage(final String message, final byte[] privateKey);

}
