package com.yoti.app.content_cloud.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

import java.util.Date;

@Builder
@Value
public class ResponseRecord {
    @JsonCreator
    ResponseRecord(
            @JsonProperty("recordId") String recordId,
            @JsonProperty("encryptedRecordData") String encryptedRecordData,
            @JsonProperty("encryptionKeyId") String encryptionKeyId,
            @JsonProperty("tag") String tag,
            @JsonProperty("recordTimestamp") @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ssZ") Date recordTimestamp,
            @JsonProperty("binned") boolean binned

    ) {
        this.recordId = recordId;
        this.encryptedRecordData = encryptedRecordData;
        this.encryptionKeyId = encryptionKeyId;
        this.tag = tag;
        this.recordTimestamp = recordTimestamp;
        this.binned = binned;
    }

    @JsonProperty("recordId")
    private String recordId;

    @JsonProperty("encryptedRecordData")
    private String encryptedRecordData;

    @JsonProperty("encryptionKeyId")
    private String encryptionKeyId;

    @JsonProperty("tag")
    private String tag;

    @JsonProperty("recordTimestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ssZ")
    private Date recordTimestamp;

    @JsonProperty("binned")
    private boolean binned;
}
