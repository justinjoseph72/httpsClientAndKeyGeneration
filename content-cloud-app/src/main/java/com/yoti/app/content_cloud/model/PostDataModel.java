package com.yoti.app.content_cloud.model;

import com.yoti.app.controllers.model.KeyData;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PostDataModel {

    private String postUrl;
    private String payload;
    private KeyData keyData;
}
