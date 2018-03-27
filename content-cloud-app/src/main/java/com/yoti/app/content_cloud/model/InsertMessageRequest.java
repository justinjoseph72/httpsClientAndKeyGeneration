package com.yoti.app.content_cloud.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import lombok.Builder;
import lombok.Value;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.List;

@Value
@Builder
public class InsertMessageRequest<T> {

    @JsonCreator
    InsertMessageRequest(
            @JsonProperty("cloudId") String cloudId,
            @JsonProperty("requesterPublicKey") String requesterPublicKey,
            @JsonProperty("dataGroup") String dataGroup,
            @JsonProperty("dataObj") T dataObj,
            @JsonProperty("encryptionKeyId") String encryptionKeyId,
            @JsonProperty("tag") List<String> tag

    ) {
        this.cloudId = cloudId;
        this.requesterPublicKey = requesterPublicKey;
        this.dataGroup = dataGroup;
        this.dataObj = dataObj;
        this.encryptionKeyId = encryptionKeyId;
        this.tag = ImmutableList.copyOf(tag);
    }

    @JsonProperty("cloudId")
    @NotBlank(message = "cloudId cannot be null")
    private String cloudId;

    @JsonProperty("requesterPublicKey")
    @NotBlank(message = "requesterPublicKey cannot be null")
    private String requesterPublicKey;

    @JsonProperty("dataGroup")
    @NotBlank(message = "dataGroup cannot be null")
    private String dataGroup;

    @JsonProperty("dataObj")
    @NotNull(message = "dataObj cannot be null")
    private T dataObj;

    @JsonProperty("encryptionKeyId")
    @NotBlank(message = "encryptionKeyId cannot be null")
    private String encryptionKeyId;

    @JsonProperty("tag")
    @NotNull(message = "tag cannot be null")
    private ImmutableList<String> tag;
}
