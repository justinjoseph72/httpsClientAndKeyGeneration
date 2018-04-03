package com.yoti.app.content_cloud.service;

public interface SignMessageService {

     byte[] signMessage(final String message, final byte[] privateKey);

}
