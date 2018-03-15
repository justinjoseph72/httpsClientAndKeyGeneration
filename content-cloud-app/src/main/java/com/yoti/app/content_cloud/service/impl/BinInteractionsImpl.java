package com.yoti.app.content_cloud.service.impl;

import com.google.protobuf.util.JsonFormat;
import com.yoti.app.UrlConstants.ErrorCodes;
import com.yoti.app.UrlConstants.ErrorMessages;
import com.yoti.app.config.EndpointsProperties;
import com.yoti.app.content_cloud.adpaters.BinOpsProtoAdapter;
import com.yoti.app.content_cloud.model.BinRequest;
import com.yoti.app.content_cloud.service.BinInteractions;
import com.yoti.app.content_cloud.service.PostDataService;
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
    public Boolean moveObjectToBin(final BinRequest binRequest) throws CloudDataAdapterException, CloudInteractionException {
        validateRequest(binRequest);
        try {
            BinOpsProto.MoveToBinRequest moveToBinRequestProto = binAdapter.getMoveToBinRequestProto(binRequest);
            String jsonPayload = jsonPrinter.print(moveToBinRequestProto);
            log.info("the move to bin request is {}", jsonPayload);
            log.info("url to use {}", endpointsProperties.getMoveDataToBin());
            ResponseEntity<?> responseEntity = postDataService.postData(endpointsProperties.getMoveDataToBin(), jsonPayload);
            return handleResponse(responseEntity);
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
            String jsonPayload = jsonPrinter.print(restoreFromBinRequestProto);
            log.info("the restore object from bin request is {}", jsonPayload);
            log.info("url to use {}", endpointsProperties.getRestoreDataToBin());
            ResponseEntity<?> responseEntity = postDataService.postData(endpointsProperties.getRestoreDataToBin(), jsonPayload);
            return handleResponse(responseEntity);
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
            String jsonPayload = jsonPrinter.print(emptyBinRequestProto);
            log.info("the empty bin request is {}", jsonPayload);
            log.info("url to use {}", endpointsProperties.getEmptyBin());
            ResponseEntity<?> responseEntity = postDataService.postData(endpointsProperties.getEmptyBin(), jsonPayload);
            return handleResponse(responseEntity);
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
            String jsonPayload = jsonPrinter.print(removeBinnedRequestProto);
            log.info("the remove object from bin request is {}", jsonPayload);
            log.info("url to use {}", endpointsProperties.getRemoveBinnedObject());
            ResponseEntity<?> responseEntity = postDataService.postData(endpointsProperties.getRemoveBinnedObject(), jsonPayload);
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
        return Boolean.TRUE;
    }

    private void validateRequest(final BinRequest binRequest) {
        if (binRequest == null) {
            log.info("The bin request is null");
            throw new CloudInteractionException(ErrorCodes.NULL_INPUT, ErrorMessages.NULL_OBJ_MSG);
        }
    }
}
