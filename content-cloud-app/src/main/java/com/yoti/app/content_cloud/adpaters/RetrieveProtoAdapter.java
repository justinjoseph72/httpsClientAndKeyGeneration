package com.yoti.app.content_cloud.adpaters;

import com.google.protobuf.ByteString;
import com.yoti.app.content_cloud.model.ResponseRecord;
import com.yoti.app.content_cloud.model.RetrieveMessageRequest;
import com.yoti.app.exception.CloudDataAdapterException;
import com.yoti.ccloudpubapi_v1.RetrieveProto;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class RetrieveProtoAdapter {

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ssZ");

    public RetrieveProto.RetrieveRequest getRetrieveRequestProtoFromRetrieveRequest(@NonNull final RetrieveMessageRequest retrieveMessageRequest) throws CloudDataAdapterException {
        RetrieveProto.RetrieveRequest retrieveRequest = null;
        try {

            retrieveRequest = RetrieveProto.RetrieveRequest.newBuilder()
                    .setCloudId(ByteString.copyFromUtf8(retrieveMessageRequest.getCloudId()))
                    .setDataGroup(retrieveMessageRequest.getDataGroup())
                    .setRequesterPubKey(ByteString.copyFromUtf8(retrieveMessageRequest.getRequesterPublicKey()))
                    .setStartDate(getFormattedDate(retrieveMessageRequest.getStartDate()))
                    .setEndDate(getFormattedDate(retrieveMessageRequest.getEndDate()))
                    .setSearchType(RetrieveProto.RetrieveRequest.SearchType.forNumber(retrieveMessageRequest.getSearchType()))
                    .buildPartial();
            // TODO this is something to be checked with the CC team
            //check about this implementation from CC team
            if (retrieveMessageRequest.getQueryTags() != null && !retrieveMessageRequest.getQueryTags().isEmpty()) {
                for (int tagIndex = 0; tagIndex < retrieveMessageRequest.getQueryTags().size(); tagIndex++) {
                    RetrieveProto.Tag tag = RetrieveProto.Tag.newBuilder().setKey(retrieveMessageRequest.getQueryTags().get(tagIndex))
                            .setValue(retrieveMessageRequest.getQueryTags().get(tagIndex)).build();
                    retrieveRequest = RetrieveProto.RetrieveRequest.newBuilder(retrieveRequest).addQueryTags(tag).buildPartial();
                }
            }

        } catch (Exception e) {
            log.info(e.getMessage());
            throw new CloudDataAdapterException(e.getMessage());
        }
        retrieveRequest = RetrieveProto.RetrieveRequest.newBuilder(retrieveRequest)
                .build();
        return retrieveRequest;

    }

    private String getFormattedDate(Date date) {
        return simpleDateFormat.format(date);
    }

    public ResponseRecord getResponseRecordFromRecordProto(RetrieveProto.Record recordProto) throws ParseException {
        return ResponseRecord.builder()
                .encryptedRecordData(recordProto.getEncryptedContent().toStringUtf8())
                .encryptionKeyId(recordProto.getEncryptionKeyId().toStringUtf8())
                .binned(recordProto.getIsBinned())
                .recordTimestamp(getDateFromString(recordProto.getTimestamp()))
                .build();

        //TODO Tags
    }

    private Date getDateFromString(final String timestamp) throws ParseException {
        return simpleDateFormat.parse(timestamp);
    }


}
