package com.yoti.app.content_cloud.service;

import com.yoti.app.content_cloud.model.InsertMessageRequest;
import com.yoti.app.content_cloud.model.InsertMessageResponse;
import com.yoti.app.controllers.model.ContentCloudModel;
import com.yoti.app.exception.CloudInteractionException;

public interface InsertObject {

    <T> InsertMessageResponse insertObjectToCloud(final ContentCloudModel<InsertMessageRequest<T>> contentCloudModel) throws CloudInteractionException;
}
