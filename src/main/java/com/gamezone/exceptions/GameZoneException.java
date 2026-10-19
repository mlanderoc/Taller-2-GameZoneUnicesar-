package com.gamezone.exceptions;

/**
 * Abstract root exception for all domain, validation, business rule,
 * and persistence errors in the GameZone Unicesar system.
 * Extends {@link RuntimeException} so service and repository method signatures
 * remain clean without mandatory checked-exception declarations.
 */
public abstract class GameZoneException extends RuntimeException {

    private final String errorCode;

    /**
     * Creates a new GameZoneException with the specified detail message
     * and a default error code.
     *
     * @param message descriptive error message
     */
    public GameZoneException(String message) {
        this(message, "ERR_GAMEZONE");
    }

    /**
     * Creates a new GameZoneException with the specified detail message
     * and error code.
     *
     * @param message   descriptive error message
     * @param errorCode specific error code identifying the exception category
     */
    public GameZoneException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Creates a new GameZoneException with a message, error code, and root cause.
     *
     * @param message   descriptive error message
     * @param errorCode specific error code identifying the exception category
     * @param cause     underlying cause of the exception
     */
    protected GameZoneException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    /**
     * Returns the error code associated with this exception.
     *
     * @return error code string
     */
    public String getErrorCode() {
        return errorCode;
    }
}
