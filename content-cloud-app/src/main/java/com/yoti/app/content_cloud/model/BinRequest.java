package com.yoti.app.content_cloud.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import org.hibernate.validator.constraints.NotBlank;

@Builder
@Value
public class BinRequest {

    @JsonCreator
    BinRequest(
            @JsonProperty("recordId") String recordId,
            @JsonProperty("requesterPublicKey") String requesterPublicKey,
            @JsonProperty("cloudId") String cloudId,
            @JsonProperty("dataGroup") String dataGroup
    ) {
        this.recordId = recordId;
        this.requesterPublicKey = requesterPublicKey;
        this.cloudId = cloudId;
        this.dataGroup = dataGroup;
    }

    @JsonProperty("recordId")
    @NotBlank
    private String recordId;

    @JsonProperty("requesterPublicKey")
    @NotBlank
    private String requesterPublicKey;

    @JsonProperty("cloudId")
    @NotBlank
    private String cloudId;

    @JsonProperty("dataGroup")
    @NotBlank
    private String dataGroup;
}
