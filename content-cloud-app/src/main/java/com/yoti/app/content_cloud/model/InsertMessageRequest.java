package com.yoti.app.content_cloud.model;

import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
@Getter
public class InsertMessageRequest<T> {
    @NotBlank
    private String cloudId;
    @NotBlank
    private String requesterPublicKey;
    @NotBlank
    private String dataGroup;
    @NotNull
    private T dataObj;
    @NotBlank
    private String encryptionKeyId;
    @NotNull
    private List<String> tag;
}
