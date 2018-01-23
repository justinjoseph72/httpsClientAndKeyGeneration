package com.yoti.app.content_Cloud.model;

import lombok.Data;

import java.util.Date;

@Data
public class RegisterPublicKeyModel {
    private byte[] keyId;
    private byte[] wrapper;
    private byte[] wrappedKey;
    private Date validTo;
}
