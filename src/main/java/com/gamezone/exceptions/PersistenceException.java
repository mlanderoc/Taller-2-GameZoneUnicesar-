package com.gamezone.exceptions;

/**
 * Exception thrown when an error occurs while reading from or writing to
 * persistent storage files (corrupted file, permission denied, I/O failure).
 */
public class PersistenceException extends GameZoneException {

    private static final String DEFAULT_ERROR_CODE = "ERR_PERSISTENCE";

    /**
     * Creates a new PersistenceException with a contextual message and the
     * underlying cause to preserve stack trace traceability.
     *
     * @param message contextual message indicating the file and operation (read/write)
     * @param cause   original I/O or serialization exception
     */
    public PersistenceException(String message, Throwable cause) {
        super(message, DEFAULT_ERROR_CODE, cause);
    }

    /**
     * Creates a new PersistenceException with a contextual message.
     *
     * @param message contextual message indicating the file and operation
     */
    public PersistenceException(String message) {
        super(message, DEFAULT_ERROR_CODE);
    }
}
