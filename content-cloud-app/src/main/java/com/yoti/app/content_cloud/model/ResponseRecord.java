package com.yoti.app.content_cloud.model;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Builder
@Getter
public class ResponseRecord {
    private String recordId;
    private String encryptedRecordData;
    private String encryptionKeyId;
    private String tag;
    private Date recordTimestamp;
    private boolean binned;
}
