package com.yoti.app.content_cloud.service;

public interface CreateSignatureService {

     String signMessage(final String message, final String privateKeyStr);

}
