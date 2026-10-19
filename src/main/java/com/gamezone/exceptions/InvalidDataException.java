package com.gamezone.exceptions;

/**
 * Exception thrown when an input datum fails format, range, or type criteria,
 * such as a negative price, negative stock, empty mandatory text field,
 * or malformed email address.
 */
public class InvalidDataException extends GameZoneException {

    private static final String DEFAULT_ERROR_CODE = "ERR_INVALID_DATA";

    /**
     * Creates a new InvalidDataException specifying the invalid field name
     * and a description of the validation problem in Spanish.
     *
     * @param fieldName          name of the invalid input field
     * @param problemDescription description of why the datum is invalid
     */
    public InvalidDataException(String fieldName, String problemDescription) {
        super("El campo '" + fieldName + "' no es válido: " + problemDescription,
                DEFAULT_ERROR_CODE);
    }

    /**
     * Creates a new InvalidDataException with a custom descriptive message.
     *
     * @param message descriptive message in Spanish
     */
    public InvalidDataException(String message) {
        super(message, DEFAULT_ERROR_CODE);
    }
}
