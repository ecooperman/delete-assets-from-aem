package com.rightpoint.adobe.assets.exceptions;

public class UnexpectedResponseException extends AssetsException {

    public UnexpectedResponseException() {
        super();
    }

    public UnexpectedResponseException(String message) {
        super(message);
    }

    public UnexpectedResponseException(Throwable cause) {
        super(cause);
    }

    public UnexpectedResponseException(String message, Throwable cause) {
        super(message, cause);
    }
}
