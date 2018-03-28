package com.yoti.app.controllers;

import com.yoti.app.UrlConstants.ApiConstants;
import com.yoti.app.content_cloud.model.*;
import com.yoti.app.content_cloud.service.BinInteractions;
import com.yoti.app.content_cloud.service.InsertObject;
import com.yoti.app.content_cloud.service.RetrieveObject;
import com.yoti.app.controllers.model.ContentCloudModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping(ApiConstants.API_BASE)
@RequiredArgsConstructor
public class ContentCloudController {

    private final InsertObject insertService;
    private final RetrieveObject retrieveService;
    private final BinInteractions binService;

    @PostMapping(path = ApiConstants.INSERT_RECORD, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public <T> ResponseEntity<InsertMessageResponse> insertDataToContentCloud(@Valid @RequestBody ContentCloudModel<InsertMessageRequest<T>> messageRequest) {
        log.info("starting the insert process");
        InsertMessageResponse response = insertService.insertObjectToCloud(messageRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping(path = ApiConstants.RETRIEVE_RECORD, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RetrieveMessageResponse> retrieveDataFromContentCloud(@Valid @RequestBody RetrieveMessageRequest messageRequest) {
        log.info("starting the retrieve process");
        RetrieveMessageResponse response = retrieveService.fetchRecordsFromCloud(messageRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping(path = ApiConstants.MOVE_TO_BIN, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> moveDataToBin(@Valid @RequestBody BinRequest binRequest) {
        log.info("starting the move to bin process");
        Boolean response = binService.moveObjectToBin(binRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping(path = ApiConstants.REMOVE_FROM_BIN, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> removeDataToBin(@Valid @RequestBody BinRequest binRequest) {
        log.info("starting the remove from bin process");
        Boolean response = binService.removeBinnedObjectFromBin(binRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping(path = ApiConstants.RESTORE_FROM_BIN, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> restoreDataFromBin(@Valid @RequestBody BinRequest binRequest) {
        log.info("starting the restore from bin process");
        Boolean response = binService.restoreObjectFromBin(binRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping(path = ApiConstants.EMPTY_BIN, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> emptyBinData(@Valid @RequestBody BinRequest binRequest) {
        log.info("starting the empty bin process");
        Boolean response = binService.emptyBin(binRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping(path = ApiConstants.NEW_PATH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> testEndpoint(@Valid @RequestBody ContentCloudModel<InsertMessageRequest> insertMessageRequest) {
        log.info("the private Key is {}", insertMessageRequest.getPrivateKey());
        log.info("the insert message is {}", insertMessageRequest.getData());
        return ResponseEntity.ok(Boolean.valueOf(true));
    }
}
