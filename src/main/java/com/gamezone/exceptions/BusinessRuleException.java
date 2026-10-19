package com.gamezone.exceptions;

/**
 * Exception thrown when an operation violates a business rule of the store,
 * such as insufficient stock, duplicate entity identifier, empty sale item list,
 * or expired 30-day return window.
 */
public class BusinessRuleException extends GameZoneException {

    private static final String DEFAULT_ERROR_CODE = "ERR_BUSINESS_RULE";

    /**
     * Creates a new BusinessRuleException describing the violated business rule.
     *
     * @param message descriptive message in Spanish explaining the violated rule
     */
    public BusinessRuleException(String message) {
        super(message, DEFAULT_ERROR_CODE);
    }

    /**
     * Creates a new BusinessRuleException with a custom error code.
     *
     * @param message   descriptive message in Spanish explaining the violated rule
     * @param errorCode specific error code
     */
    public BusinessRuleException(String message, String errorCode) {
        super(message, errorCode);
    }
}
