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
            @JsonProperty("privateKey") String privateKeyStr,
            @JsonProperty("publicKey") String publicKeyStr
    ) {
        this.privateKeyStr = privateKeyStr;
        this.publicKeyStr = publicKeyStr;
    }

    //base64  encoded string
    @JsonProperty("privateKey")
    @NotBlank(message = "private key cannot be blank or null")
    private String privateKeyStr;

    //base64 encoded str
    @JsonProperty("publicKey")
    @NotBlank(message = "public key cannot be blank or null")
    private String publicKeyStr;
}
