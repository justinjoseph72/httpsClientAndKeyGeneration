package com.yoti.app.content_cloud;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import com.yoti.app.content_cloud.model.BinRequest;
import com.yoti.app.content_cloud.model.InsertMessageRequest;
import com.yoti.app.content_cloud.model.RetrieveMessageRequest;

import java.util.Date;
import java.util.List;

public class RequestHelper {

    public static BinRequest getBinRequest(final String recordId, final String publicKey, final String cloudId) {
        return BinRequest.builder()
                .recordId(recordId)
                .requesterPublicKey(publicKey)
                .cloudId(cloudId)
                .dataGroup("CON")
                .build();
    }

    public static <T> InsertMessageRequest<T> getInsertMessagRequest(final T dataObj, final String publicKey, final String cloudId, final List<String> tags, final String encryptionId) {
        InsertMessageRequest insertMessageRequest = InsertMessageRequest.builder()
                .requesterPublicKey(publicKey)
                .dataGroup("CON")
                .dataObj(dataObj)
                .encryptionKeyId(encryptionId)
                .cloudId(cloudId)
                .tag(ImmutableList.copyOf(tags))
                .build();
        return insertMessageRequest;
    }

    public static RetrieveMessageRequest getRetrieveMessageRequest(final String cloudId,
                                                                   final String requesterPublicKey,
                                                                   final List<String> queryTags,
                                                                   final Date startDate,
                                                                   final Date endDate,
                                                                   final int searchType,
                                                                   final Boolean retrieveBin) {
        return RetrieveMessageRequest.builder()
                .cloudId(cloudId)
                .requesterPublicKey(requesterPublicKey)
                .dataGroup("CON")
                .queryTags(ImmutableList.copyOf(queryTags))
                .startDate(startDate)
                .endDate(endDate)
                .searchType(searchType)
                .retrieveBin(retrieveBin)
                .build();
    }

}
