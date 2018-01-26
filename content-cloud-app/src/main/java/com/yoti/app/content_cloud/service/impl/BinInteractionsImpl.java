package com.yoti.app.content_cloud.service.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yoti.app.UrlConstants.ErrorCodes;
import com.yoti.app.UrlConstants.ErrorMessages;
import com.yoti.app.UrlConstants.ServerConstants;
import com.yoti.app.content_cloud.adpaters.BinOpsProtoAdapter;
import com.yoti.app.content_cloud.model.BinRequest;
import com.yoti.app.content_cloud.service.BinInteractions;
import com.yoti.app.exception.CloudDataAdapterException;
import com.yoti.app.exception.CloudDataConversionException;
import com.yoti.app.exception.CloudInteractionException;
import com.yoti.app.httpClient.RequestClient;
import com.yoti.ccloudpubapi_v1.BinOpsProto;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ByteArrayEntity;

import java.io.IOException;

@Slf4j
@Singleton
public class BinInteractionsImpl implements BinInteractions {

    @Inject
    @Getter
    private RequestClient requestClient;

    @Inject
    @Getter
    private BinOpsProtoAdapter binAdapter;


    @Override
    public Boolean moveObjectToBin(final BinRequest binRequest) throws CloudDataAdapterException, CloudInteractionException {
        validateRequest(binRequest);
        try {
            BinOpsProto.MoveToBinRequest moveToBinRequestProto = binAdapter.getMoveToBinRequestProto(binRequest);
            HttpResponse httpResponse = getHttpResponse(moveToBinRequestProto.toByteArray(), requestClient, ServerConstants.MOVE_DATA_TO_BIN_URL);
            return handleResponse(httpResponse);
        } catch (CloudDataConversionException | CloudDataAdapterException | CloudInteractionException e) {
            log.info(" Exception {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.info(" Exception {}", e.getMessage());
            throw new CloudInteractionException(ErrorCodes.BIN_ERROR, e.getMessage());
        }

    }

    @Override
    public Boolean restoreObjectFromBin(final BinRequest binRequest) throws CloudDataAdapterException, CloudInteractionException {
        validateRequest(binRequest);
        try {
            BinOpsProto.RestoreFromBinRequest restoreFromBinRequestProto = binAdapter.getRestoreFromBinRequestProto(binRequest);
            HttpResponse httpResponse = getHttpResponse(restoreFromBinRequestProto.toByteArray(), requestClient, ServerConstants.RESTORE_DATA_FROM_BIN_URL);
            return handleResponse(httpResponse);
        } catch (CloudDataConversionException | CloudDataAdapterException | CloudInteractionException e) {
            log.info(" Exception {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.info(" Exception {}", e.getMessage());
            throw new CloudInteractionException(ErrorCodes.BIN_ERROR, e.getMessage());
        }
    }

    @Override
    public Boolean emptyBin(final BinRequest binRequest) throws CloudDataAdapterException, CloudInteractionException {
        validateRequest(binRequest);
        try {
            BinOpsProto.EmptyBinRequest emptyBinRequestProto = binAdapter.getEmptyBinRequestProto(binRequest);
            HttpResponse httpResponse = getHttpResponse(emptyBinRequestProto.toByteArray(), requestClient, ServerConstants.EMPTY_BIN_URL);
            return handleResponse(httpResponse);
        } catch (CloudDataConversionException | CloudDataAdapterException | CloudInteractionException e) {
            log.info(" Exception {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.info(" Exception {}", e.getMessage());
            throw new CloudInteractionException(ErrorCodes.BIN_ERROR, e.getMessage());
        }
    }

    @Override
    public Boolean removeBinnedObjectFromBin(final BinRequest binRequest) throws CloudDataAdapterException, CloudInteractionException {
        validateRequest(binRequest);
        try {
            BinOpsProto.RemoveBinnedRequest removeBinnedRequestProto = binAdapter.getRemoveBinnedRequestProto(binRequest);
            HttpResponse httpResponse = getHttpResponse(removeBinnedRequestProto.toByteArray(), requestClient, ServerConstants.REMOVE_BINNED_OBJECT_FROM_BIN_URL);
            return handleResponse(httpResponse);
        } catch (CloudDataConversionException | CloudDataAdapterException | CloudInteractionException e) {
            log.info(" Exception {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.info(" Exception {}", e.getMessage());
            throw new CloudInteractionException(ErrorCodes.BIN_ERROR, e.getMessage());
        }
    }

    private HttpResponse getHttpResponse(final byte[] protoByte, final RequestClient requestClient, final String serviceUrl) throws IOException {
        ByteArrayEntity byteArrayEntity = new ByteArrayEntity(protoByte);
        HttpResponse httpResponse = postDataAndGetResponse(requestClient, byteArrayEntity, serviceUrl);
        return httpResponse;
    }

    private Boolean handleResponse(final HttpResponse httpResponse) {
        if (httpResponse.getStatusLine().getStatusCode() != 200) {
            throw new CloudInteractionException(ErrorCodes.CLOUD_INSERT_ERROR, String.format("Status %d returned from the Content Cloud Service", httpResponse.getStatusLine().getStatusCode()));
        }
        return Boolean.TRUE;
    }

    private void validateRequest(final BinRequest binRequest) {
        if (binRequest == null) {
            log.info("The bin request is null");
            throw new CloudInteractionException(ErrorCodes.NULL_INPUT, ErrorMessages.NULL_OBJ_MSG);
        }
    }
}
