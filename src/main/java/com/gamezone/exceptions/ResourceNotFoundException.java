package com.gamezone.exceptions;

/**
 * Exception thrown when a requested resource (product, accessory, customer,
 * seller, sale, promotion, or warranty) does not exist in the system.
 */
public class ResourceNotFoundException extends GameZoneException {

    private static final String DEFAULT_ERROR_CODE = "ERR_RESOURCE_NOT_FOUND";

    /**
     * Creates a new ResourceNotFoundException building a descriptive message
     * in Spanish from the resource type and identifier.
     *
     * @param resourceType type of the resource searched (e.g., "producto", "cliente", "venta")
     * @param resourceId   identifier that was not found
     */
    public ResourceNotFoundException(String resourceType, String resourceId) {
        super("El " + resourceType + " con identificador " + resourceId + " no fue encontrado.",
                DEFAULT_ERROR_CODE);
    }

    /**
     * Creates a new ResourceNotFoundException with a custom descriptive message.
     *
     * @param message descriptive error message in Spanish
     */
    public ResourceNotFoundException(String message) {
        super(message, DEFAULT_ERROR_CODE);
    }
}
