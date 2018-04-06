package com.yoti.app.controllers.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import org.hibernate.validator.constraints.NotBlank;

@Value
@Builder
public class KeyData {

    @JsonCreator
    KeyData(
            @JsonProperty("privateKey") byte[] privateKeyStr,
            @JsonProperty("publicKey") String publicKeyStr
    ) {
        this.privateKeyStr = privateKeyStr;
        this.publicKeyStr = publicKeyStr;
    }

    //base64  encoded byte
    @JsonProperty("privateKey")
    @NotBlank(message = "private key cannot be blank or null")
    private byte[] privateKeyStr;

    //base64 encoded byte
    @JsonProperty("publicKey")
    @NotBlank(message = "public key cannot be blank or null")
    private String publicKeyStr;
}
