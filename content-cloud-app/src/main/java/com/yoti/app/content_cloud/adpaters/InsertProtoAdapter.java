package com.yoti.app.content_cloud.adpaters;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
@Slf4j
@RequiredArgsConstructor
public class InsertProtoAdapter {

    private final PayloadConversion payloadConversion;
    private final ObjectMapper mapper;

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
            String str_ISO = responseProto.getRecordId().toString(Charset.defaultCharset());
            String str_utf_16 = responseProto.getRecordId().toString(StandardCharsets.UTF_16);
            String str_BE = responseProto.getRecordId().toString(StandardCharsets.UTF_16BE);
            String str_LE = responseProto.getRecordId().toString(StandardCharsets.UTF_16LE);
            String str_output = Base64.getEncoder().encodeToString(responseProto.getRecordId().toByteArray());
            log.info("standard charset {} {} {} {}", str_ISO, str_utf_16, str_BE, str_LE, str_output);

            return InsertMessageResponse.builder()
                    .recordId(responseProto.getRecordId().toStringUtf8())
                    .build();
        } catch (Exception e) {
            log.warn("Error in mapping to InsertMessageResponse {} {}", e.getClass().getName(), e.getMessage());
            throw new CloudDataAdapterException(e.getMessage());
        }
    }

    public InsertMessageResponse getInsertMessageResponseFromJson(final String jsonStr) {
        try {
            JsonNode recordNode = mapper.readTree(jsonStr);
            return InsertMessageResponse.builder()
                    .recordId(recordNode.get("recordId").textValue())
                    .build();
        } catch (Exception e) {
            throw new CloudDataAdapterException(e.getMessage());
        }
    }


}
