package com.yoti.app.content_cloud.adpaters;

import com.google.protobuf.ByteString;
import com.yoti.app.content_cloud.model.ResponseRecord;
import com.yoti.app.content_cloud.model.RetrieveMessageRequest;
import com.yoti.app.exception.CloudDataAdapterException;
import com.yoti.ccloudpubapi_v1.RetrieveProto;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class RetrieveProtoAdapter {

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");

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
                    .build();
            // TODO this is something to be checked with the CC team
            //this is a wrong implementation
            if (retrieveMessageRequest.getQueryTags() != null && !retrieveMessageRequest.getQueryTags().isEmpty()) {
                for (int tagIndex = 0; tagIndex < retrieveMessageRequest.getQueryTags().size(); tagIndex++) {
                    RetrieveProto.Tag tag = RetrieveProto.Tag.newBuilder().setKey("key").setValue(retrieveMessageRequest.getQueryTags().get(tagIndex)).build();
                    retrieveRequest = RetrieveProto.RetrieveRequest.newBuilder(retrieveRequest).setQueryTags(tagIndex, tag).build();
                }

            }
        } catch (Exception e) {
            log.info(e.getMessage());
            throw new CloudDataAdapterException(e.getMessage());
        }
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
