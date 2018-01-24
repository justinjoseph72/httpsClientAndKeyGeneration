package com.yoti.app.content_cloud.adpaters;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.protobuf.ByteString;
import com.yoti.app.content_cloud.model.InsertMessageRequest;
import com.yoti.app.content_cloud.model.InsertMessageResponse;
import com.yoti.app.content_cloud.service.PayloadConversion;
import com.yoti.ccloudpubapi_v1.InsertProto;

@Singleton
public class InsertProtoAdapter {

    @Inject
    private PayloadConversion payloadConversion;

    public InsertProto.InsertRequest getInsertProtoFromInsertMessageRequest(InsertMessageRequest insertMessageRequest) {
        return InsertProto.InsertRequest.newBuilder()
                .setCloudId(ByteString.copyFromUtf8(insertMessageRequest.getCloudId()))
                .setDataGroup(insertMessageRequest.getDataGroup())
                .setEncryptedContent(ByteString.copyFromUtf8(getEncryptedObjectJson(insertMessageRequest.getDataObj(), insertMessageRequest.getEncryptionKeyId())))
                .setEncryptionKeyId(ByteString.copyFromUtf8(insertMessageRequest.getEncryptionKeyId()))
                //TODO tags mapping
                .build();
    }

    private String getEncryptedObjectJson(final Object dataObj, final String encryptionKeyId) {
        return payloadConversion.getEncryptedPayload(dataObj, encryptionKeyId);
    }

    public InsertMessageResponse getInsertMessageResponse(InsertProto.InsertResponse responseProto) {
        return InsertMessageResponse.builder()
                .recordId(responseProto.getRecordId().toStringUtf8())
                .build();
    }


}
