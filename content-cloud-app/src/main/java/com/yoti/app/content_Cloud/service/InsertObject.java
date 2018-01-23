package com.yoti.app.content_Cloud.service;

import com.yoti.app.exception.CloudInteractionException;

public interface InsertObject {

    <T> boolean insertObjectToCloud(T obj) throws CloudInteractionException;
}
