package com.yoti.app.content_cloud.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BinRequest {
    private String recordId;
    private String requesterPublicKey;
    private String cloudId;
    private String dataGroup;
}
