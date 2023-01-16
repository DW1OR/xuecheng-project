package com.xuecheng.base.exception;

public class XueChengException extends RuntimeException {
    private String errMessage;

    public XueChengException() {
        super();
    }

    public XueChengException(String message) {
        super(message);
        this.errMessage = message;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public static void cast(String errMessage) {
        throw new XueChengException(errMessage);
    }

    public static void cast(CommonError commonError) {
        throw new XueChengException(commonError.getErrMessage());
    }
}
