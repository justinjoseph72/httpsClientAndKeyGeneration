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
            @JsonProperty("privateKey") byte[] privateKey,
            @JsonProperty("payload") T data
    ) {
        this.privateKey = privateKey;
        this.data = data;
    }

    @JsonProperty("privateKey")
    @NotNull(message = "private jey should not be null")
    private byte[] privateKey;

    @JsonProperty("payload")
    @NotNull(message = "payload should not be null")
    private T data;
}
