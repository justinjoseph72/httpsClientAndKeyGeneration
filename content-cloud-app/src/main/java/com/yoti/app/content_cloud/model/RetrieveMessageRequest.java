package com.yoti.app.content_cloud.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import com.yoti.ccloudpubapi_v1.RetrieveProto;
import lombok.Builder;
import lombok.Value;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Builder
@Value
public class RetrieveMessageRequest {

    @JsonCreator
    RetrieveMessageRequest(
            @JsonProperty("cloudId") String cloudId,
            @JsonProperty("requesterPublicKey") String requesterPublicKey,
            @JsonProperty("dataGroup") String dataGroup,
            @JsonProperty("queryTags") List<String> queryTags,
            @JsonProperty("startDate") Date startDate,
            @JsonProperty("endDate") Date endDate,
            @JsonProperty("searchType") int searchType,
            @JsonProperty("retrieveBin") Boolean retrieveBin
    ) {
        this.cloudId = cloudId;
        this.requesterPublicKey = requesterPublicKey;
        this.dataGroup = dataGroup;
        this.queryTags = ImmutableList.copyOf(queryTags);
        this.startDate = startDate;
        this.endDate = endDate;
        this.searchType = searchType;
        this.retrieveBin = retrieveBin;
    }

    @JsonProperty("cloudId")
    @NotBlank(message = "cloudId may not be null")
    private String cloudId;

    @JsonProperty("requesterPublicKey")
    @NotBlank(message = "cloudId may not be null")
    private String requesterPublicKey;

    @JsonProperty("dataGroup")
    @NotBlank(message = "cloudId may not be null")
    private String dataGroup;

    @JsonProperty("queryTags")
    @NotNull(message = "querytags cannot be null")
    private ImmutableList<String> queryTags;

    @JsonProperty("startDate")
    @NotNull(message = "startDate may not be null")
    private Date startDate;

    @JsonProperty("endDate")
    @NotNull(message = "endDate may not be null")
    private Date endDate;

    @JsonProperty("searchType")
    @Max(RetrieveProto.RetrieveRequest.SearchType.OR_TAGS_VALUE)
    @Min(RetrieveProto.RetrieveRequest.SearchType.AND_TAGS_VALUE)
    private int searchType;

    @JsonProperty("retrieveBin")
    @NotBlank(message = "retrieveBin may not be null")
    private Boolean retrieveBin;

}
