package com.yoti.app.content_cloud.service.impl;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import com.yoti.app.UrlConstants.ErrorCodes;
import com.yoti.app.UrlConstants.ErrorMessages;
import com.yoti.app.config.EndpointsProperties;
import com.yoti.app.content_cloud.adpaters.BinOpsProtoAdapter;
import com.yoti.app.content_cloud.model.BinRequest;
import com.yoti.app.content_cloud.model.PostDataModel;
import com.yoti.app.content_cloud.service.BinInteractions;
import com.yoti.app.content_cloud.service.PostDataService;
import com.yoti.app.controllers.model.ContentCloudModel;
import com.yoti.app.exception.CloudDataAdapterException;
import com.yoti.app.exception.CloudDataConversionException;
import com.yoti.app.exception.CloudInteractionException;
import com.yoti.ccloudpubapi_v1.BinOpsProto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BinInteractionsImpl implements BinInteractions {

    private final PostDataService postDataService;
    private final BinOpsProtoAdapter binAdapter;
    private final EndpointsProperties endpointsProperties;
    private final JsonFormat.Printer jsonPrinter = JsonFormat.printer().preservingProtoFieldNames();

    @Override
    public Boolean moveObjectToBin(final ContentCloudModel<BinRequest> binRequest) throws CloudDataAdapterException, CloudInteractionException {
        validateRequest(binRequest);
        validateRequest(binRequest.getData());
        try {
            PostDataModel postDataModel = getPostDataModel(binRequest,endpointsProperties.getMoveDataToBin());
            ResponseEntity<?> responseEntity = postDataService.postData(postDataModel);
            return handleResponse(responseEntity);
        } catch (CloudDataConversionException | CloudDataAdapterException | CloudInteractionException e) {
            log.warn(" Exception {} {}", e.getClass().getName(), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.warn(" Exception {} {}", e.getClass().getName(), e.getMessage());
            throw new CloudInteractionException(ErrorCodes.BIN_ERROR, e.getMessage());
        }
    }

    @Override
    public Boolean restoreObjectFromBin(final ContentCloudModel<BinRequest> binRequest) throws CloudDataAdapterException, CloudInteractionException {
        validateRequest(binRequest);
        validateRequest(binRequest.getData());
        try {
            PostDataModel postDataModel = getPostDataModel(binRequest,endpointsProperties.getRestoreDataFromBin());
            ResponseEntity<?> responseEntity = postDataService.postData(postDataModel);
            return handleResponse(responseEntity);
        } catch (CloudDataConversionException | CloudDataAdapterException | CloudInteractionException e) {
            log.warn(" Exception {} {}", e.getClass().getName(), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.info(" Exception {}", e.getMessage());
            throw new CloudInteractionException(ErrorCodes.BIN_ERROR, e.getMessage());
        }
    }

    @Override
    public Boolean emptyBin(final ContentCloudModel<BinRequest> binRequest) throws CloudDataAdapterException, CloudInteractionException {
        validateRequest(binRequest);
        validateRequest(binRequest.getData());
        try {
            PostDataModel postDataModel = getPostDataModel(binRequest,endpointsProperties.getEmptyBin());
            ResponseEntity<?> responseEntity = postDataService.postData(postDataModel);
            return handleResponse(responseEntity);
        } catch (CloudDataConversionException | CloudDataAdapterException | CloudInteractionException e) {
            log.warn(" Exception {} {}", e.getClass().getName(), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.warn(" Exception {} {}", e.getClass().getName(), e.getMessage());
            throw new CloudInteractionException(ErrorCodes.BIN_ERROR, e.getMessage());
        }
    }

    @Override
    public Boolean removeBinnedObjectFromBin(final ContentCloudModel<BinRequest> binRequest) throws CloudDataAdapterException, CloudInteractionException {
        validateRequest(binRequest);
        validateRequest(binRequest.getData());
        try {
            PostDataModel postDataModel = getPostDataModel(binRequest,endpointsProperties.getRemoveBinnedObject());
            ResponseEntity<?> responseEntity = postDataService.postData(postDataModel);
            return handleResponse(responseEntity);
        } catch (CloudDataConversionException | CloudDataAdapterException | CloudInteractionException e) {
            log.info(" Exception {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.info(" Exception {}", e.getMessage());
            throw new CloudInteractionException(ErrorCodes.BIN_ERROR, e.getMessage());
        }
    }


    private Boolean handleResponse(final ResponseEntity<?> httpResponse) {
        if (httpResponse.getStatusCodeValue() != 200) {
            throw new CloudInteractionException(ErrorCodes.CLOUD_INSERT_ERROR, String.format("Status %d returned from the Content Cloud Service",
                    httpResponse.getStatusCodeValue()));
        }
        return Boolean.valueOf(true);
    }

    private PostDataModel getPostDataModel(final ContentCloudModel<BinRequest> binRequest, final String postUrl) throws InvalidProtocolBufferException {
        BinOpsProto.RemoveBinnedRequest removeBinnedRequestProto = binAdapter.getRemoveBinnedRequestProto(binRequest.getData());
        String jsonPayload = jsonPrinter.print(removeBinnedRequestProto);
        return PostDataModel.builder().keyData(binRequest.getKeyData())
                .postUrl(postUrl)
                .payload(jsonPayload)
                .build();
    }

    private <T> void validateRequest(final T binRequest) {
        if (binRequest == null) {
            log.info("The bin request is null");
            throw new CloudInteractionException(ErrorCodes.NULL_INPUT, ErrorMessages.NULL_OBJ_MSG);
        }
    }
}
