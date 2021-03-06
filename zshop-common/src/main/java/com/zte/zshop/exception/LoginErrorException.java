package com.zte.zshop.exception;

/**
 * Author:helloboy
 * Date:2021-06-23 10:59
 * Description:<描述>
 */
public class LoginErrorException extends Exception {

    public LoginErrorException() {
        super();
    }

    public LoginErrorException(String message) {
        super(message);
    }

    public LoginErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginErrorException(Throwable cause) {
        super(cause);
    }

    protected LoginErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
