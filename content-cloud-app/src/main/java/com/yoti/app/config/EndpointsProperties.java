package com.yoti.app.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "content_cloud.endpoint")
@Data
@Component
public class EndpointsProperties {

    private String insertData;
    private String retrieveData;
    private String moveDataToBin;
    private String restoreDataFromBin;
    private String removeBinnedObject;
    private String emptyBin;

}
