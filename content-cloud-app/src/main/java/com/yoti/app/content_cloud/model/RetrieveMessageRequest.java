package com.yoti.app.content_cloud.model;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Builder
@Getter
public class RetrieveMessageRequest {
    private String cloudId;
    private String requesterPublicKey;
    private String dataGroup;
    private List<String> queryTags;
    private Date startDate;
    private Date endDate;
    private String searchType;
    private Boolean retrieveBin;

}
