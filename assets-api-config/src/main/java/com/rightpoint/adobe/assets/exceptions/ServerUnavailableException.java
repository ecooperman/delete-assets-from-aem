package com.rightpoint.adobe.assets.exceptions;

/** Exception generated when application must stop */
public class ServerUnavailableException extends AssetsException {

    public ServerUnavailableException() {
        super();
    }

    public ServerUnavailableException(String message) {
        super(message);
    }

    public ServerUnavailableException(Throwable cause) {
        super(cause);
    }

    public ServerUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
