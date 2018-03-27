package com.yoti.app.content_cloud.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Value
@Builder
public class RegisterPublicKeyModel {

    @JsonCreator
    RegisterPublicKeyModel(
            @JsonProperty("keyId") byte[] keyId,
            @JsonProperty("wrapper") byte[] wrapper,
            @JsonProperty("wrappedKey") byte[] wrappedKey,
            @JsonProperty("validTo") Date validTo
    ) {
        this.keyId = keyId;
        this.wrapper = wrapper;
        this.wrappedKey = wrappedKey;
        this.validTo = validTo;
    }

    @JsonProperty("keyId")
    @NotNull(message = "keyId should not be null")
    private byte[] keyId;

    @JsonProperty("wrapper")
    @NotNull(message = "wrapper should not be null")
    private byte[] wrapper;

    @JsonProperty("wrappedKey")
    @NotNull(message = "wrappedKey should not be null")
    private byte[] wrappedKey;

    @JsonProperty("validTo")
    @NotNull(message = "validTo should not be null")
    private Date validTo;
}
