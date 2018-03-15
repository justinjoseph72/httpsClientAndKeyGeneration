package com.yoti.app.content_cloud.model;

import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Builder
@Getter
public class RetrieveMessageRequest {
    @NotBlank
    private String cloudId;
    @NotBlank
    private String requesterPublicKey;
    @NotBlank
    private String dataGroup;
    @NotNull
    private List<String> queryTags;
    @NotNull
    private Date startDate;
    @NotNull
    private Date endDate;
    private int searchType;
    @NotNull
    private Boolean retrieveBin;

}
