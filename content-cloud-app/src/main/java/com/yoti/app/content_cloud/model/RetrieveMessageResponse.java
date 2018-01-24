package com.yoti.app.content_cloud.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class RetrieveMessageResponse {
    private List<ResponseRecord> records;
    private boolean hasMoreRecords;
}
