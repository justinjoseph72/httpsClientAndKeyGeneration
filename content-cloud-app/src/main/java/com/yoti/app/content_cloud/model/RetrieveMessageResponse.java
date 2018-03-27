package com.yoti.app.content_cloud.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class RetrieveMessageResponse {

    @JsonCreator
    RetrieveMessageResponse(
            @JsonProperty("records") List<ResponseRecord> records,
            @JsonProperty("hasMoreRecords") boolean hasMoreRecords
    ) {
        this.records = ImmutableList.copyOf(records);
        this.hasMoreRecords = hasMoreRecords;
    }

    @JsonProperty("records")
    private ImmutableList<ResponseRecord> records;

    @JsonProperty("hasMoreRecords")
    private boolean hasMoreRecords;
}
