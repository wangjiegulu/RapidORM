package com.wangjie.rapidorm.exception;


import android.annotation.TargetApi;
import android.os.Build;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/25/15.
 */
public class RapidORMException extends Exception {
    public RapidORMException() {
    }

    public RapidORMException(String error) {
        super(error);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public RapidORMException(String error, Throwable cause) {
        super(error, cause);
    }
}
