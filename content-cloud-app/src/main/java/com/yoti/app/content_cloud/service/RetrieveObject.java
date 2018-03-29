package com.yoti.app.content_cloud.service;

import com.yoti.app.content_cloud.model.RetrieveMessageRequest;
import com.yoti.app.content_cloud.model.RetrieveMessageResponse;
import com.yoti.app.controllers.model.ContentCloudModel;
import com.yoti.app.exception.CloudDataAdapterException;
import com.yoti.app.exception.CloudDataConversionException;
import com.yoti.app.exception.CloudInteractionException;

public interface RetrieveObject {

    RetrieveMessageResponse fetchRecordsFromCloud(ContentCloudModel<RetrieveMessageRequest> retrieveMessageRequest) throws CloudInteractionException, CloudDataConversionException,CloudDataAdapterException;
}
