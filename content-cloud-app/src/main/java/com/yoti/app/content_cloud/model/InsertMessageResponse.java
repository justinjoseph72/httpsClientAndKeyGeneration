package com.yoti.app.content_cloud.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class InsertMessageResponse {

    @JsonCreator
    InsertMessageResponse(
            @JsonProperty("recordId") String recordId
    ) {
        this.recordId = recordId;
    }


    @JsonProperty("recordId")
    private String recordId;
}
