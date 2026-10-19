package com.gamezone.validation;

import com.gamezone.exceptions.BusinessRuleException;
import com.gamezone.exceptions.InvalidDataException;
import com.gamezone.exceptions.ResourceNotFoundException;
import com.gamezone.model.Person;

import java.util.List;

/**
 * Provides static validation methods for customer and seller data,
 * existence verification, and duplicate identifier checks.
 */
public class PersonValidator {

    private PersonValidator() {
        // Utility class with static methods only
    }

    /**
     * Validates that mandatory person data is non-empty and that the email
     * has a valid basic format (contains '@' and '.').
     *
     * @param id    person identifier
     * @param name  person name or full name
     * @param email customer email address
     * @param phone contact phone number
     * @throws InvalidDataException if any field is empty or malformed
     */
    public static void validatePersonData(String id, String name, String email, String phone) {
        if (id == null || id.trim().isEmpty()) {
            throw new InvalidDataException("id", "El identificador de la persona no puede estar vacío.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidDataException("nombre", "El nombre de la persona no puede estar vacío.");
        }
        if (phone == null || phone.trim().isEmpty()) {
            throw new InvalidDataException("teléfono", "El teléfono de contacto no puede estar vacío.");
        }
        if (email == null || email.trim().isEmpty() || !email.contains("@") || !email.contains(".")) {
            throw new InvalidDataException("correo", "El correo electrónico debe contener '@' y '.'.");
        }
    }

    /**
     * Validates that mandatory seller data is non-empty.
     *
     * @param id           seller identifier
     * @param firstName    seller first name
     * @param lastName     seller last name
     * @param phone        seller phone number
     * @param employeeCode seller employee code
     * @param shift        seller work shift
     * @throws InvalidDataException if any mandatory field is null or empty
     */
    public static void validateSellerData(String id, String firstName, String lastName,
                                          String phone, String employeeCode, String shift) {
        if (id == null || id.trim().isEmpty()) {
            throw new InvalidDataException("id", "El identificador del vendedor no puede estar vacío.");
        }
        if (firstName == null || firstName.trim().isEmpty() || lastName == null || lastName.trim().isEmpty()) {
            throw new InvalidDataException("nombre", "El nombre y apellido del vendedor son obligatorios.");
        }
        if (phone == null || phone.trim().isEmpty()) {
            throw new InvalidDataException("teléfono", "El teléfono del vendedor no puede estar vacío.");
        }
        if (employeeCode == null || employeeCode.trim().isEmpty()) {
            throw new InvalidDataException("código de empleado", "El código de empleado no puede estar vacío.");
        }
        if (shift == null || shift.trim().isEmpty()) {
            throw new InvalidDataException("turno", "El turno del vendedor no puede estar vacío.");
        }
    }

    /**
     * Validates that a looked-up person reference is not null.
     *
     * @param person   person reference returned from a lookup
     * @param personId identifier that was searched
     * @throws ResourceNotFoundException if the person is null
     */
    public static void validatePersonExists(Person person, String personId) {
        if (person == null) {
            throw new ResourceNotFoundException("persona", personId);
        }
    }

    /**
     * Validates that no existing person in the given list has the same identifier.
     *
     * @param existing list of already registered people (customers or sellers)
     * @param newId    identifier of the new person being registered
     * @throws BusinessRuleException if a person with the same identifier already exists
     */
    public static void validateUniquePerson(List<? extends Person> existing, String newId) {
        if (existing != null) {
            for (Person person : existing) {
                if (person != null && person.getId().equals(newId)) {
                    throw new BusinessRuleException(
                            "Ya existe una persona registrada con el identificador " + newId + ".");
                }
            }
        }
    }
}
