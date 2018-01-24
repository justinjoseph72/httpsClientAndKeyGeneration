package com.yoti.app.content_cloud.service.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.yoti.app.UrlConstants.ErrorCodes;
import com.yoti.app.UrlConstants.ErrorMessages;
import com.yoti.app.UrlConstants.ServerConstants;
import com.yoti.app.content_cloud.adpaters.RetrieveProtoAdapter;
import com.yoti.app.content_cloud.model.ResponseRecord;
import com.yoti.app.content_cloud.model.RetrieveMessageRequest;
import com.yoti.app.content_cloud.model.RetrieveMessageResponse;
import com.yoti.app.content_cloud.service.RetrieveObject;
import com.yoti.app.exception.CloudInteractionException;
import com.yoti.app.httpClient.RequestClient;
import com.yoti.ccloudpubapi_v1.RetrieveProto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Singleton
public class RetrieveObjectImpl implements RetrieveObject {

    @Inject
    private RetrieveProtoAdapter retrieveProtoAdapter;

    @Inject
    private RequestClient requestClient;

    @Override
    public RetrieveMessageResponse fetchRecordsFromCloud(final RetrieveMessageRequest retrieveMessageRequest) throws CloudInteractionException {
        validateRequest(retrieveMessageRequest);
        try {
            RetrieveProto.RetrieveRequest retrieveRequest = retrieveProtoAdapter.getRetreiveRequestProtoFromRetrieveRequest(retrieveMessageRequest);
            ByteArrayEntity byteEntity = new ByteArrayEntity(retrieveRequest.toByteArray());
            HttpPost httpPost = new HttpPost(ServerConstants.TEST_URL);
            httpPost.setEntity(byteEntity);
            populateHeaders(httpPost);
            HttpResponse httpResponse = requestClient.getCloudContentInvoker().execute(httpPost);
            return handleResponse(httpResponse);
        } catch (Exception e) {
            throw new CloudInteractionException(ErrorCodes.CLOUD_RETRIEVE_ERROR, e.getMessage());
        }

    }


    private void validateRequest(final RetrieveMessageRequest obj) {
        if (obj == null) {
            throw new CloudInteractionException(ErrorCodes.NULL_INPUT, ErrorMessages.NULL_OBJ_MSG);
        }
    }

    private RetrieveMessageResponse handleResponse(final HttpResponse httpResponse) throws CloudInteractionException {
        //TODO have to check the response from the server and convert accordingly
        if (httpResponse.getStatusLine().getStatusCode() != 200) {
            throw new CloudInteractionException(ErrorCodes.CLOUD_INSERT_ERROR, String.format("Status %d returned from the Content Cloud Service", httpResponse.getStatusLine().getStatusCode()));
        }
        try {
            byte[] responseByte = IOUtils.toByteArray(httpResponse.getEntity().getContent());
            RetrieveProto.RetrieveResponse retrieveResponse = RetrieveProto.RetrieveResponse.parseFrom(responseByte);
            List<RetrieveProto.Record> recordProtoList = retrieveResponse.getRecordsList();
            if (recordProtoList != null && !recordProtoList.isEmpty()) {
                List<ResponseRecord> records = new ArrayList<>();
                recordProtoList.forEach((RetrieveProto.Record recordProto) -> {
                    try {
                        ResponseRecord responseRecord = retrieveProtoAdapter.getResponseRecordFromRecordProto(recordProto);
                        records.add(responseRecord);
                    } catch (ParseException e) {
                        throw new CloudInteractionException(ErrorCodes.CLOUD_RETRIEVE_ERROR, e.getMessage());
                    }
                });
                return RetrieveMessageResponse.builder()
                        .records(records)
                        .hasMoreRecords(retrieveResponse.getMoreRecords())
                        .build();
            }
        } catch (IOException e) {
            throw new CloudInteractionException(ErrorCodes.CLOUD_RETRIEVE_ERROR, e.getMessage());
        }
        return null;
    }
}
