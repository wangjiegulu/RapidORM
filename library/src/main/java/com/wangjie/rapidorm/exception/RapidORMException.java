package com.wangjie.rapidorm.exception;

import java.sql.SQLException;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/25/15.
 */
public class RapidORMException extends SQLException {
    public RapidORMException() {
    }

    public RapidORMException(String detailMessage) {
        super(detailMessage);
    }

    public RapidORMException(String detailMessage, Throwable cause) {
        super(detailMessage, cause);
    }

    public RapidORMException(Throwable cause) {
        super(cause);
    }

}
