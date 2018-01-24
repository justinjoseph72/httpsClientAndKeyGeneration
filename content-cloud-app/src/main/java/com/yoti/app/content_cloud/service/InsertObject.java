package com.yoti.app.content_cloud.service;

import com.yoti.app.content_cloud.model.InsertMessageRequest;
import com.yoti.app.content_cloud.model.InsertMessageResponse;
import com.yoti.app.exception.CloudInteractionException;

public interface InsertObject extends CloudContentInteraction {

    <T> InsertMessageResponse insertObjectToCloud(InsertMessageRequest<T> insertMessageRequest) throws CloudInteractionException;
}
