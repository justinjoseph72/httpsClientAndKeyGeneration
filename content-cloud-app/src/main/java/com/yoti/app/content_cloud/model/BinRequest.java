package com.yoti.app.content_cloud.model;

import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.NotBlank;

@Builder
@Getter
public class BinRequest {
    @NotBlank
    private String recordId;
    @NotBlank
    private String requesterPublicKey;
    @NotBlank
    private String cloudId;
    @NotBlank
    private String dataGroup;
}
