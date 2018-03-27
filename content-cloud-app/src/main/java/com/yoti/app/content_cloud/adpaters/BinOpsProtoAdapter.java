package com.yoti.app.content_cloud.adpaters;

import com.google.protobuf.ByteString;
import com.yoti.app.content_cloud.model.BinRequest;
import com.yoti.ccloudpubapi_v1.BinOpsProto;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class BinOpsProtoAdapter {

    public BinOpsProto.EmptyBinRequest getEmptyBinRequestProto(@NonNull  BinRequest binRequest) {
        log.debug("Input bin Request is {}", binRequest.toString());
        return BinOpsProto.EmptyBinRequest.newBuilder()
                .setCloudId(ByteString.copyFromUtf8(binRequest.getCloudId()))
                .setRequesterPubKey(ByteString.copyFromUtf8(binRequest.getRequesterPublicKey()))
                .setDataGroup(binRequest.getDataGroup())
                .build();
    }

    public BinOpsProto.MoveToBinRequest getMoveToBinRequestProto(@NonNull BinRequest binRequest) {
        log.debug("Input bin Request is {}", binRequest.toString());
        return BinOpsProto.MoveToBinRequest.newBuilder()
                .setCloudId(ByteString.copyFromUtf8(binRequest.getCloudId()))
                .setRecordId(ByteString.copyFromUtf8(binRequest.getRecordId()))
                .setRequesterPubKey(ByteString.copyFromUtf8(binRequest.getRequesterPublicKey()))
                .setDataGroup(binRequest.getDataGroup())
                .build();
    }

    public BinOpsProto.RemoveBinnedRequest getRemoveBinnedRequestProto(@NonNull BinRequest binRequest) {
        log.debug("Input bin Request is {}", binRequest.toString());
        return BinOpsProto.RemoveBinnedRequest.newBuilder()
                .setCloudId(ByteString.copyFromUtf8(binRequest.getCloudId()))
                .setRecordId(ByteString.copyFromUtf8(binRequest.getRecordId()))
                .setRequesterPubKey(ByteString.copyFromUtf8(binRequest.getRequesterPublicKey()))
                .setDataGroup(binRequest.getDataGroup())
                .build();
    }

    public BinOpsProto.RestoreFromBinRequest getRestoreFromBinRequestProto(@NonNull BinRequest binRequest) {
        log.debug("Input bin Request is {}", binRequest.toString());
        return BinOpsProto.RestoreFromBinRequest.newBuilder()
                .setCloudId(ByteString.copyFromUtf8(binRequest.getCloudId()))
                .setRecordId(ByteString.copyFromUtf8(binRequest.getRecordId()))
                .setRequesterPubKey(ByteString.copyFromUtf8(binRequest.getRequesterPublicKey()))
                .setDataGroup(binRequest.getDataGroup())
                .build();
    }
}
