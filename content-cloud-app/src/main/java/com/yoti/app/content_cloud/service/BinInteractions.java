package com.yoti.app.content_cloud.service;

import com.yoti.app.content_cloud.model.BinRequest;
import com.yoti.app.exception.CloudDataAdapterException;
import com.yoti.app.exception.CloudInteractionException;

public interface BinInteractions {

    Boolean moveObjectToBin(final BinRequest binRequest) throws CloudDataAdapterException, CloudInteractionException;

    Boolean restoreObjectFromBin(final BinRequest binRequest) throws CloudDataAdapterException, CloudInteractionException;

    Boolean emptyBin(final BinRequest binRequest) throws CloudDataAdapterException, CloudInteractionException;

    Boolean removeBinnedObjectFromBin(final BinRequest binRequest) throws CloudDataAdapterException, CloudInteractionException;
}
