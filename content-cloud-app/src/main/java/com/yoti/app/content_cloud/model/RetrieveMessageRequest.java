package com.yoti.app.content_cloud.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import lombok.Builder;
import lombok.Value;
import org.hibernate.validator.constraints.NotBlank;

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
    @NotBlank
    private String cloudId;

    @JsonProperty("requesterPublicKey")
    @NotBlank
    private String requesterPublicKey;

    @JsonProperty("dataGroup")
    @NotBlank
    private String dataGroup;

    @JsonProperty("queryTags")
    @NotNull
    private ImmutableList<String> queryTags;

    @JsonProperty("startDate")
    @NotNull
    private Date startDate;

    @JsonProperty("endDate")
    @NotNull
    private Date endDate;

    @JsonProperty("searchType")
    private int searchType;

    @JsonProperty("retrieveBin")
    @NotNull
    private Boolean retrieveBin;

}
