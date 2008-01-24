package org.seasar.rest;

public class NoncompliantResourceException extends RestRuntimeException {
    private static final long serialVersionUID = 1L;

    public NoncompliantResourceException(String message, Throwable cause) {
        super(message, cause);
    }
    public NoncompliantResourceException(String message) {
        super(message);
    }
}
