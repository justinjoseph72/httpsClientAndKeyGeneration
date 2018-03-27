package com.yoti.app.content_cloud.adpaters;

import com.google.protobuf.ByteString;
import com.yoti.app.content_cloud.model.InsertMessageRequest;
import com.yoti.app.content_cloud.model.InsertMessageResponse;
import com.yoti.app.content_cloud.service.PayloadConversion;
import com.yoti.app.exception.CloudDataAdapterException;
import com.yoti.ccloudpubapi_v1.InsertProto;
import com.yoti.ccloudpubapi_v1.RetrieveProto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
@Slf4j
@RequiredArgsConstructor
public class InsertProtoAdapter {

    private final PayloadConversion payloadConversion;

    public InsertProto.InsertRequest getInsertProtoFromInsertMessageRequest(InsertMessageRequest insertMessageRequest) {
        try {
            InsertProto.InsertRequest insertRequest = InsertProto.InsertRequest.newBuilder()
                    .setCloudId(ByteString.copyFromUtf8(insertMessageRequest.getCloudId()))
                    .setDataGroup(insertMessageRequest.getDataGroup())
                    .setEncryptedContent(ByteString.copyFromUtf8(getEncryptedObjectJson(insertMessageRequest.getDataObj(), insertMessageRequest.getEncryptionKeyId())))
                    .setEncryptionKeyId(ByteString.copyFromUtf8(insertMessageRequest.getEncryptionKeyId()))
                    .buildPartial();
            if (!CollectionUtils.isEmpty(insertMessageRequest.getTag())) {
                for (int tagIndex = 0; tagIndex < insertMessageRequest.getTag().size(); tagIndex++) {
                    RetrieveProto.Tag tag = RetrieveProto.Tag.newBuilder().setKey(insertMessageRequest.getTag().get(tagIndex).toString())
                            .setValue(insertMessageRequest.getTag().get(tagIndex).toString()).build();
                    insertRequest = InsertProto.InsertRequest.newBuilder(insertRequest)
                            .addTags(tag).buildPartial();
                }
            }
            return InsertProto.InsertRequest.newBuilder(insertRequest).build();
        } catch (Exception e) {
            log.warn("Error in mapping to InsertRequest Proto {} {} ", e.getClass().getName(), e.getMessage());
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
            log.warn("Error in mapping to InsertMessageResponse {} {}",e.getClass().getName(), e.getMessage());
            throw new CloudDataAdapterException(e.getMessage());
        }
    }


}
