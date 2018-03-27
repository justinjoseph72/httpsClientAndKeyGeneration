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
    @NotNull
    private byte[] keyId;

    @JsonProperty("wrapper")
    @NotNull
    private byte[] wrapper;

    @JsonProperty("wrappedKey")
    @NotNull
    private byte[] wrappedKey;

    @JsonProperty("validTo")
    @NotNull
    private Date validTo;
}
