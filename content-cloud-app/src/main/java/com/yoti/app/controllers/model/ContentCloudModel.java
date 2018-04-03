package com.yoti.app.controllers.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;

@Value
@Builder
public class ContentCloudModel<T> {

    @JsonCreator
    ContentCloudModel(
            @JsonProperty("keyData") KeyData keyData,
            @JsonProperty("payload") T data
    ) {
        this.keyData = keyData;
        this.data = data;
    }

    @JsonProperty("keyData")
    @NotNull(message = "key data should not be null")
    private KeyData keyData;

    @JsonProperty("payload")
    @NotNull(message = "payload should not be null")
    private T data;
}
