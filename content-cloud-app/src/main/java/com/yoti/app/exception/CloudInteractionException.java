package com.yoti.app.exception;

public class CloudInteractionException extends RuntimeException {

    private String errorCode;
    private String message;

    public CloudInteractionException(String errorCode,String message){
        super(message);
        this.errorCode = errorCode;
        this.message = message;
    }

}
