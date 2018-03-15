package com.yoti.app.content_cloud.service.impl;

import com.google.protobuf.util.JsonFormat;
import com.yoti.app.UrlConstants.ErrorCodes;
import com.yoti.app.UrlConstants.ErrorMessages;
import com.yoti.app.UrlConstants.ServerConstants;
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
    private final JsonFormat.Printer jsonPrinter = JsonFormat.printer().preservingProtoFieldNames();

    @Override
    public Boolean moveObjectToBin(final BinRequest binRequest) throws CloudDataAdapterException, CloudInteractionException {
        validateRequest(binRequest);
        try {
            BinOpsProto.MoveToBinRequest moveToBinRequestProto = binAdapter.getMoveToBinRequestProto(binRequest);
            String jsonPayload = jsonPrinter.print(moveToBinRequestProto);
            ResponseEntity<?> responseEntity = postDataService.postData(ServerConstants.MOVE_DATA_TO_BIN_URL, jsonPayload);
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
            ResponseEntity<?> responseEntity = postDataService.postData(ServerConstants.RESTORE_DATA_FROM_BIN_URL, jsonPayload);
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
            ResponseEntity<?> responseEntity = postDataService.postData(ServerConstants.EMPTY_BIN_URL, jsonPayload);
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
            ResponseEntity<?> responseEntity = postDataService.postData(ServerConstants.REMOVE_BINNED_OBJECT_FROM_BIN_URL, jsonPayload);
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
