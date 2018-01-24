package com.yoti.app.content_cloud.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class InsertMessageRequest<T> {
    private String cloudId;
    private String requesterPublicKey;
    private String dataGroup;
    private T dataObj;
    private String encryptionKeyId;
    private List<String> tag;
}
