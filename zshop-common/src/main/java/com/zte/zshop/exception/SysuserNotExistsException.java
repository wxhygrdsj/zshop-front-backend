package com.zte.zshop.exception;

/**
 * Author:helloboy
 * Date:2021-06-17 8:37
 * Description:<描述>
 */
public class SysuserNotExistsException extends Exception {

    public SysuserNotExistsException() {
        super();
    }

    public SysuserNotExistsException(String message) {
        super(message);
    }

    public SysuserNotExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public SysuserNotExistsException(Throwable cause) {
        super(cause);
    }

    protected SysuserNotExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
