package com.yoti.app.content_cloud.service;

import com.yoti.app.content_cloud.model.RetrieveMessageRequest;
import com.yoti.app.content_cloud.model.RetrieveMessageResponse;
import com.yoti.app.exception.CloudInteractionException;
import com.yoti.ccloudpubapi_v1.RetrieveProto;

import java.io.IOException;

public interface RetrieveObject extends CloudContentInteraction {

    RetrieveMessageResponse fetchRecordsFromCloud(RetrieveMessageRequest retrieveMessageRequest) throws CloudInteractionException;
}
