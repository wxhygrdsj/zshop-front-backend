package com.zte.zshop.exception;

/**
 * Author:helloboy
 * Date:2021-06-03 9:03
 * Description:<描述>
 */
public class ProductTypeExistsException extends Exception{

    public ProductTypeExistsException() {
        super();
    }

    public ProductTypeExistsException(String message) {
        super(message);
    }

    public ProductTypeExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProductTypeExistsException(Throwable cause) {
        super(cause);
    }

    protected ProductTypeExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
