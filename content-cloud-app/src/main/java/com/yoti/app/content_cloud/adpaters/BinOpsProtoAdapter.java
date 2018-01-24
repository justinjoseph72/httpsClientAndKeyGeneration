package com.yoti.app.content_cloud.adpaters;

import com.google.protobuf.ByteString;
import com.yoti.app.content_cloud.model.BinRequest;
import com.yoti.ccloudpubapi_v1.BinOpsProto;

public class BinOpsProtoAdapter {

    public BinOpsProto.EmptyBinRequest getEmptyBinequestProto(BinRequest binRequest) {
        return BinOpsProto.EmptyBinRequest.newBuilder()
                .setCloudId(ByteString.copyFromUtf8(binRequest.getCloudId()))
                .setRequesterPubKey(ByteString.copyFromUtf8(binRequest.getRequesterPublicKey()))
                .setDataGroup(binRequest.getDataGroup())
                .build();
    }

    public BinOpsProto.MoveToBinRequest getMoveToBinRequestProto(BinRequest binRequest) {
        return BinOpsProto.MoveToBinRequest.newBuilder()
                .setCloudId(ByteString.copyFromUtf8(binRequest.getCloudId()))
                .setRequesterPubKey(ByteString.copyFromUtf8(binRequest.getRequesterPublicKey()))
                .setDataGroup(binRequest.getDataGroup())
                .build();
    }

    public BinOpsProto.RemoveBinnedRequest getRemoveBinnedRequestProto(BinRequest binRequest) {
        return BinOpsProto.RemoveBinnedRequest.newBuilder()
                .setCloudId(ByteString.copyFromUtf8(binRequest.getCloudId()))
                .setRequesterPubKey(ByteString.copyFromUtf8(binRequest.getRequesterPublicKey()))
                .setDataGroup(binRequest.getDataGroup())
                .build();
    }

    public BinOpsProto.RestoreFromBinRequest getRestoreFromBinRequestProto(BinRequest binRequest) {
        return BinOpsProto.RestoreFromBinRequest.newBuilder()
                .setCloudId(ByteString.copyFromUtf8(binRequest.getCloudId()))
                .setRequesterPubKey(ByteString.copyFromUtf8(binRequest.getRequesterPublicKey()))
                .setDataGroup(binRequest.getDataGroup())
                .build();
    }
}
