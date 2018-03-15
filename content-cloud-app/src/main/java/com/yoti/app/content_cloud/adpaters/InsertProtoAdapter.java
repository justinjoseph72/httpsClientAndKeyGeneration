package com.yoti.app.content_cloud.adpaters;

import com.google.inject.Inject;
import com.google.protobuf.ByteString;
import com.yoti.app.content_cloud.model.InsertMessageRequest;
import com.yoti.app.content_cloud.model.InsertMessageResponse;
import com.yoti.app.content_cloud.service.PayloadConversion;
import com.yoti.app.exception.CloudDataAdapterException;
import com.yoti.ccloudpubapi_v1.InsertProto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class InsertProtoAdapter {

    @Inject
    private PayloadConversion payloadConversion;

    public InsertProto.InsertRequest getInsertProtoFromInsertMessageRequest(InsertMessageRequest insertMessageRequest) {
        try {
            return InsertProto.InsertRequest.newBuilder()
                    .setCloudId(ByteString.copyFromUtf8(insertMessageRequest.getCloudId()))
                    .setDataGroup(insertMessageRequest.getDataGroup())
                    .setEncryptedContent(ByteString.copyFromUtf8(getEncryptedObjectJson(insertMessageRequest.getDataObj(), insertMessageRequest.getEncryptionKeyId())))
                    .setEncryptionKeyId(ByteString.copyFromUtf8(insertMessageRequest.getEncryptionKeyId()))
                    //TODO tags mapping
                    .build();
        } catch (Exception e) {
            log.info("Error in mapping to InsertRequest Proto {}", e.getMessage());
            throw new CloudDataAdapterException(e.getMessage());
        }
    }

    private String getEncryptedObjectJson(final Object dataObj, final String encryptionKeyId) {
        return payloadConversion.getEncryptedPayload(dataObj, encryptionKeyId);
    }

    public InsertMessageResponse getInsertMessageResponse(InsertProto.InsertResponse responseProto) {
        try {
            return InsertMessageResponse.builder()
                    .recordId(responseProto.getRecordId().toStringUtf8())
                    .build();
        } catch (Exception e) {
            log.info("Error in mapping to InsertMessageResponse {}", e.getMessage());
            throw new CloudDataAdapterException(e.getMessage());
        }
    }


}
