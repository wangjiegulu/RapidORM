package com.wangjie.rapidorm.exception;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/25/15.
 */
public class RapidORMRuntimeException extends RuntimeException{
    public RapidORMRuntimeException() {
    }

    public RapidORMRuntimeException(String detailMessage) {
        super(detailMessage);
    }

    public RapidORMRuntimeException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public RapidORMRuntimeException(Throwable throwable) {
        super(throwable);
    }
}
