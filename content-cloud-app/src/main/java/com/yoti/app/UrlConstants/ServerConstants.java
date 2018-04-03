package com.yoti.app.UrlConstants;

import com.yoti.signedrequests.utils.YotiHeaders;

public class ServerConstants {

    public static final String CONTENT_CLOUD_SERVER = "preprod0.ccloud.yoti.com:23443";

    public static final String CONTENT_TYPE_HEADER = "Content-Type";
    public static final String AUTH_DIGEST = YotiHeaders.REQUEST_DIGEST.headerName;
    public static final String AUTH_KEY = YotiHeaders.REQUEST_PUBLIC_KEY.headerName;

    public static final String CIPHER_ALGORITHM = "RSA";

    public static final String BC_PROVIDER = "BC";
    public static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
}
