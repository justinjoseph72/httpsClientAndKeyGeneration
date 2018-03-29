package com.yoti.app.content_cloud.service;

public interface SignMessageService {

    <T> byte[] signMessage(final T message, final byte[] privateKey);

}
