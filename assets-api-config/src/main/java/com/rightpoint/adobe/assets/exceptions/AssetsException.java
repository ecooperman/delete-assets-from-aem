package com.rightpoint.adobe.assets.exceptions;

@SuppressWarnings("serial")
public class AssetsException extends Exception {

    public AssetsException() {
    }

    public AssetsException(String message) {
        super(message);
    }

    public AssetsException(Throwable cause) {
        super(cause);
    }

    public AssetsException(String message, Throwable cause) {
        super(message, cause);
    }
}
