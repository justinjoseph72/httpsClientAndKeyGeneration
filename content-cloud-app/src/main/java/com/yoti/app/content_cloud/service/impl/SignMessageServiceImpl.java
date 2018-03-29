package com.yoti.app.content_cloud.service.impl;

import com.yoti.app.content_cloud.service.KeyService;
import com.yoti.app.content_cloud.service.SignMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SignMessageServiceImpl implements SignMessageService {

    private final KeyService keyService;

    @Override
    public <T> byte[] signMessage(final T message, final byte[] privateKey) {
        return new byte[0];
    }
}
