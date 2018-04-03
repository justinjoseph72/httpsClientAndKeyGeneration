package com.yoti.app.content_cloud.service.impl;

import com.yoti.app.UrlConstants.ErrorCodes;
import com.yoti.app.UrlConstants.ServerConstants;
import com.yoti.app.content_cloud.service.KeyService;
import com.yoti.app.content_cloud.service.SignMessageService;
import com.yoti.app.exception.KeyGenerationException;
import com.yoti.app.exception.SignMessageException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class SignMessageServiceImpl implements SignMessageService {

    private final KeyService keyService;

    @Override
    public byte[] signMessage(final String message, final byte[] privateKeyByte) {
        try {
            PrivateKey privateKey = keyService.getPrivateKey(privateKeyByte);
            if (privateKey == null) {
                throw new SignMessageException(ErrorCodes.PRIVATE_KEY_ERROR);
            }
            return signMessage(message, privateKey);
        } catch (KeyGenerationException e) {
            log.warn("the provided private key is not valid {}", e.getMessage());
            throw new SignMessageException(ErrorCodes.PRIVATE_KEY_ERROR.concat(" ").concat(e.getMessage()));
        } catch (Exception e) {
            log.warn("The signing of message failed due to {} {}", e, e.getMessage());
            throw new SignMessageException(String.format("%s %s ", e.getClass().getName(), e.getMessage()));
        }
    }

    private byte[] signMessage(final String message, final PrivateKey privateKey) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        final Signature signature = Signature.getInstance(ServerConstants.SIGNATURE_ALGORITHM, ServerConstants.BC_PROVIDER);
        signature.initSign(privateKey);
        signature.update(message.getBytes(StandardCharsets.UTF_8));
        return signature.sign();
    }
}
