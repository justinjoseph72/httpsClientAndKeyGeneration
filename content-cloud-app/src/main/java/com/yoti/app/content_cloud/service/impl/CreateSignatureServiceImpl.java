package com.yoti.app.content_cloud.service.impl;

import com.yoti.app.UrlConstants.ErrorCodes;
import com.yoti.app.content_cloud.service.CreateSignatureService;
import com.yoti.app.exception.KeyGenerationException;
import com.yoti.app.exception.SignMessageException;
import com.yoti.signedrequests.utils.key.PrivateKeyProvider;
import com.yoti.signedrequests.utils.key.StaticRsaPrivateKeyProvider;
import com.yoti.signedrequests.utils.service.SignedRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreateSignatureServiceImpl implements CreateSignatureService {

    private final SignedRequestService signedRequestService;

    @Override
    public String signMessage(final String message, final String privateKeyStr) {
        try {
            //String privateKeyString = new String(Base64.getEncoder().encode(privateKeyByte));
            PrivateKeyProvider privateKeyProvider = new StaticRsaPrivateKeyProvider(privateKeyStr);
            return signRequestMessage(message, privateKeyProvider);
        } catch (KeyGenerationException e) {
            log.warn("the provided private key is not valid {}", e.getMessage());
            throw new SignMessageException(ErrorCodes.PRIVATE_KEY_ERROR.concat(" ").concat(e.getMessage()));
        } catch (Exception e) {
            log.warn("The signing of message failed due to {} {}", e, e.getMessage());
            throw new SignMessageException(String.format("%s %s ", e.getClass().getName(), e.getMessage()));
        }
    }

    private String signRequestMessage(final String message, final PrivateKeyProvider privateKeyProvider){
        return signedRequestService.sign(message,privateKeyProvider);
    }
}
