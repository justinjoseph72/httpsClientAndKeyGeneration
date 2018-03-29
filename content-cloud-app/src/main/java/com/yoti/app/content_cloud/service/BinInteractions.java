package com.yoti.app.content_cloud.service;

import com.yoti.app.content_cloud.model.BinRequest;
import com.yoti.app.controllers.model.ContentCloudModel;
import com.yoti.app.exception.CloudDataAdapterException;
import com.yoti.app.exception.CloudInteractionException;

public interface BinInteractions {

    Boolean moveObjectToBin(final ContentCloudModel<BinRequest> binRequest) throws CloudDataAdapterException, CloudInteractionException;

    Boolean restoreObjectFromBin(final ContentCloudModel<BinRequest> binRequest) throws CloudDataAdapterException, CloudInteractionException;

    Boolean emptyBin(final ContentCloudModel<BinRequest> binRequest) throws CloudDataAdapterException, CloudInteractionException;

    Boolean removeBinnedObjectFromBin(final ContentCloudModel<BinRequest> binRequest) throws CloudDataAdapterException, CloudInteractionException;
}
