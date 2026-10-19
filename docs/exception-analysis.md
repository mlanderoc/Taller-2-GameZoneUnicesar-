# Exception Handling and Validation Layer Analysis (Requirement 6)

This document answers the five guiding questions defined in **Requirement 6 — Validations and Exception Handling** for the **GameZone Unicesar** system.

---

## 1. Checked (`Exception`) vs. Unchecked (`RuntimeException`) for `GameZoneException`

**Question:** *The root class `GameZoneException` can be declared as `Exception` (checked) or as `RuntimeException` (unchecked). Which of the two options is more appropriate for this project and why? What consequence does it have on the method signatures of the services and repositories?*

### Decision: `RuntimeException` (Unchecked)

`GameZoneException` is declared as an `abstract` class extending `java.lang.RuntimeException` (unchecked).

### Justification

1. **Cleaner Service and Repository Contracts (`Repository<T>` Interface Preservation):**
   In the existing architecture, the generic `Repository<T>` interface (`save`, `findAll`, `findById`, `saveAll`) and the service methods in `ProductService`, `PersonService`, `SaleService`, `AccessoryService`, `PromotionService`, and `ReturnService` do not declare `throws` clauses in their method signatures. Declaring `GameZoneException` as a checked `Exception` would force every method in `Repository<T>`, every concrete repository, and every service method to add `throws GameZoneException` (or specific checked subclasses), polluting interfaces across layers (`persistence`, `validation`, `service`, and `ui`).
2. **Unrecoverable Domain & Persistence Errors at Intermediate Layers:**
   Validation failures (`InvalidDataException`), violated domain rules (`BusinessRuleException`), missing entities (`ResourceNotFoundException`), and disk I/O errors (`PersistenceException`) cannot be meaningfully recovered inside `persistence` or `service` methods. Their proper handling point is the presentation boundary (`ConsoleMenu` and Swing dialogs), where the operation is aborted and a clear Spanish message is displayed to the user.
3. **Alignment with Modern Java Best Practices:**
   Modern enterprise Java frameworks (Spring, Jakarta EE, Hibernate) wrap low-level checked exceptions (`IOException`, `SQLException`) into unchecked domain/persistence exceptions so that intermediate business logic remains clean and free of boilerplate `try/catch` or `throws` declarations, while boundary controllers handle them centrally.

### Consequence on Method Signatures

- **Repositories (`ProductRepository`, `PersonRepository`, `SaleRepository`, etc.):** Keep their exact existing signatures (`public void save(T entity)`, `public List<T> findAll()`, `public T findById(String id)`, `public void saveAll(List<T> entities)`) without adding `throws IOException` or `throws PersistenceException`. Internally, each repository catches `IOException | ClassNotFoundException` and throws `new PersistenceException(message, cause)`.
- **Services (`ProductService`, `PersonService`, `SaleService`, etc.):** Keep their exact public method signatures unchanged, preserving 100% backward compatibility with both `ConsoleMenu` and the Swing GUI (`MainFrame`, panels, and dialogs) while propagating typed unchecked subclasses of `GameZoneException`.

---

## 2. Custom Exception Hierarchy vs. Generic `IllegalArgumentException`

**Question:** *Why is it preferable to have a custom exception hierarchy with a common root class (`GameZoneException`) instead of throwing a generic `IllegalArgumentException` in all cases? What design principles apply?*

### Why a Custom Hierarchy Is Preferable

Previously, the system threw `IllegalArgumentException` for completely different failure modes:
- A user typing a negative price or an empty product title (`InvalidDataException`).
- A customer attempting to buy 5 units of a console when only 2 are in stock, or registering a duplicate customer ID (`BusinessRuleException`).
- Searching for a non-existent product, customer, seller, or sale ID (`ResourceNotFoundException`).
- Meanwhile, repositories caught `IOException` and printed technical messages to `System.err`, returning empty lists or silently failing without notifying the presentation layer (`PersistenceException`).

Because `ConsoleMenu` could only catch `IllegalArgumentException` (or `Exception`), it could not distinguish whether the problem was caused by **invalid user input**, a **business rule constraint**, a **missing record**, or a **corrupted/inaccessible `.dat` file**. By introducing `GameZoneException` with four specialized subclasses (`ResourceNotFoundException`, `BusinessRuleException`, `InvalidDataException`, `PersistenceException`) and a structured `errorCode` (`ERR-NOT-FOUND`, `ERR-BUSINESS-RULE`, `ERR-INVALID-DATA`, `ERR-PERSISTENCE`), the presentation layer can catch each category distinctly and guide the store clerk appropriately—while never exposing raw technical stack traces or internal file paths on screen.

### Applicable Design Principles

1. **Single Responsibility Principle (SRP):**
   Separates *error classification and contextual message construction* (handled by the `com.gamezone.exceptions` classes) and *input/rule verification* (handled by `com.gamezone.validation` validators) from *business workflow orchestration* (`com.gamezone.service`) and *user message formatting* (`com.gamezone.ui`).
2. **Open/Closed Principle (OCP):**
   New error categories can be added in the future by extending `GameZoneException` without breaking existing catch blocks that handle current specific subclasses or fallback `GameZoneException` handlers.
3. **Liskov Substitution Principle (LSP) & Polymorphism:**
   Any subclass (`ResourceNotFoundException`, `BusinessRuleException`, `InvalidDataException`, `PersistenceException`) is substitutable as a `GameZoneException`, allowing callers to either catch individual subtypes for differentiated messaging or catch `GameZoneException` polymorphically as a safety net for any application-known error.
4. **Information Hiding / Encapsulation of Low-Level Details:**
   `PersistenceException` wraps the low-level `IOException` or `ClassNotFoundException` as its `cause` (`super(message, cause)`), preserving the full technical chain for debugging/logging while allowing `ConsoleMenu` to show a safe, non-technical message to the end user.

---

## 3. Role and Architectural Placement of the Validation Layer (`com.gamezone.validation`)

**Question:** *The validators form a new layer (`com.gamezone.validation`). At what point in the flow are they invoked and why is this location coherent with the layered architecture of the system? Can the validators access the persistence package? Justify.*

### Point of Invocation in the Execution Flow

Validators (`ProductValidator`, `PersonValidator`, `SaleValidator`) are invoked **at the beginning of service-layer methods** (`ProductService`, `PersonService`, `SaleService`, `AccessoryService`, `PromotionService`, `ReturnService`), **before** any state mutation or repository write (`save` / `saveAll`) takes place:

1. `ConsoleMenu` (or a Swing dialog) collects user parameters and invokes a service method.
2. The service method immediately delegates input format/range checks (`validateProductData`, `validatePersonData`, `validateSaleData`) to the corresponding validator.
3. When a service method queries a repository or checks business preconditions, it passes the retrieved in-memory domain objects (`Product`, `Person`, `Sale`, `List<Person>`) to the validator (`validateProductExists`, `validateStockAvailability`, `validatePersonExists`, `validateUniquePerson`, `validateSaleExists`).
4. Only after all validator checks succeed does the service mutate domain state (e.g., `reduceStock`) and persist the changes through the repository.

### Coherence with the Layered Architecture

The system follows the strict one-directional dependency rule:
`ui → service → validation → model` and `ui → service → persistence → model` (with `exceptions` as a cross-cutting package).

Placing `validation` logically **between `service` and `model`** is coherent because:
- The `ui` layer remains focused on user interaction and does not duplicate business validation rules across `ConsoleMenu` and Swing dialogs.
- The `service` layer orchestrates repositories and domain workflows without cluttering its methods with repetitive null/blank checks, email format checks, positive number checks, or stock comparisons.
- The `model` layer remains pure domain state and behavior.

### Can Validators Access the `persistence` Package?

**No. Validators are strictly forbidden from accessing `com.gamezone.persistence` (or `com.gamezone.service`).**

**Justification:**
1. **Architectural Dependency Rule:** As specified in the project constraints, `validation` sits logically between `service` and `model`. Validators depend **only** on `com.gamezone.model` and `com.gamezone.exceptions`.
2. **Pure, Stateless In-Memory Verification:** Validators operate exclusively on parameters and in-memory domain objects passed to them by the services (for instance, `PersonService` loads `List<Person> existing = personRepository.findAll()` and passes that list to `PersonValidator.validateUniquePerson(existing, id)`). Keeping validators free of file I/O or repository dependencies makes them 100% deterministic, fast, side-effect-free, and reusable in any context.

---

## 4. Strategy and Evidence for Preserving Existing System Behavior (Non-Regression)

**Question:** *The refactoring must preserve the external behavior of the system. What strategy does the team follow to guarantee that all 10 menu operations continue working the same way? What evidence is shown at the end to demonstrate that nothing was broken?*

### Non-Regression Strategy

1. **Behavior-Preserving Exception Replacement:**
   Every condition that previously triggered an `IllegalArgumentException` in `ProductService`, `PersonService`, `SaleService`, `AccessoryService`, `PromotionService`, and `ReturnService` was mapped 1-to-1 to the appropriate validator method (`InvalidDataException` for format/range errors, `BusinessRuleException` for domain rule violations, `ResourceNotFoundException` for missing entities). No business formula (discounts, extended warranty 10% surcharge, 30-day return window, proportional refund calculation, stock reduction/restoration, or monthly financial balance) was altered.
2. **Preserving Query Method Contracts Used by Internal Flows:**
   Methods that are called internally by other services or UI table loaders (such as `findById` when checking optional references) continue to work seamlessly, while explicit user lookup/action workflows invoke `validateProductExists`, `validatePersonExists`, and `validateSaleExists` to throw `ResourceNotFoundException` when a requested ID does not exist.
3. **Incremental Compilation and Cross-Review per Pull Request:**
   The work was divided across three sequential stages on `feature/exception-handling` (Developer 1: exceptions and validators; Developer 2: repositories and services; Technical Lead: `ConsoleMenu`, Swing dialogs, and documentation). After each stage, the full codebase was compiled and verified against both the Console Menu and Swing GUI.

### Evidence Demonstrated at Completion

1. **Clean Full-Project Compilation:**
   All Java source files across `model`, `exceptions`, `validation`, `persistence`, `service`, and `ui` compile with zero errors (`javac` / `mvn clean compile`).
2. **End-to-End Verification of All Console Menu Operations:**
   - **Normal Paths (Happy Paths):** Registering video games, consoles, accessories, customers, sellers, promotions, sales (with automatic best promotion and basic/extended console warranties), returns (with stock restoration, proportional refund, and warranty cancellation), and querying all listings and the monthly financial balance produce the exact same outputs and `.dat` persistence state as before Requirement 6.
   - **Error Paths (Differentiated Exception Handling):**
     - Entering an empty name, invalid email without `@`/`.`, or negative price triggers `InvalidDataException` → `"Los datos ingresados no son válidos: <detalle>"`.
     - Registering a duplicate customer/seller/product ID, requesting more stock than available, or creating a sale with no products triggers `BusinessRuleException` → `"No se puede completar la operación: <detalle>"`.
     - Looking up or selling with a non-existent product, customer, seller, or sale ID triggers `ResourceNotFoundException` → `"No se encontró el recurso solicitado: <detalle>"`.
     - Corrupting or locking a `.dat` file triggers `PersistenceException` → `"Ocurrió un problema al acceder a los datos del sistema. Contacte al administrador."` without crashing the menu loop or printing a raw stack trace.

---

## 5. Exception Catching Strategy in `ConsoleMenu`

**Question:** *The `ConsoleMenu` class previously caught a single generic exception. Now it must catch four distinct types. Is this done with a single `try` block with multiple `catch` clauses, with nested/cascading `try-catch`, or with a general `catch (GameZoneException)`? Justify the choice considering code clarity and differentiation of user messages.*

### Decision: Single `try` Block with Multiple Specific `catch` Clauses (Ordered from Specific to General)

In `ConsoleMenu`, interactive operations are wrapped in a **single `try` block followed by multiple sequential `catch` clauses**, one for each specific subclass of `GameZoneException`, plus a fallback `catch (GameZoneException e)`:

```java
try {
    // Execute menu operation
} catch (ResourceNotFoundException e) {
    System.out.println("No se encontró el recurso solicitado: " + e.getMessage());
} catch (BusinessRuleException e) {
    System.out.println("No se puede completar la operación: " + e.getMessage());
} catch (InvalidDataException e) {
    System.out.println("Los datos ingresados no son válidos: " + e.getMessage());
} catch (PersistenceException e) {
    System.out.println("Ocurrió un problema al acceder a los datos del sistema. Contacte al administrador.");
} catch (GameZoneException e) {
    System.out.println("Error en la operación: " + e.getMessage());
}
```

### Justification

1. **Why NOT a single general `catch (GameZoneException e)`:**
   Catching only the root class `GameZoneException` would force us either to display the same prefix for all errors (failing Requirement 6's differentiated messaging requirement) or to write `if (e instanceof ResourceNotFoundException) ... else if ...` chains inside the catch block, which is an anti-pattern that defeats Java's polymorphic exception dispatch. Furthermore, `PersistenceException` requires hiding internal file details (`"Ocurrió un problema al acceder a los datos del sistema. Contacte al administrador."`), whereas `ResourceNotFoundException`, `BusinessRuleException`, and `InvalidDataException` include `e.getMessage()` with user-oriented context.
2. **Why NOT nested/cascading `try-catch` blocks (`try { try { ... } catch ... } catch ...`):**
   Nesting multiple `try` blocks inside each other adds unnecessary indentation levels, obscures the main control flow of the menu method, and reduces readability without offering any functional advantage.
3. **Why a single `try` block with multiple specific `catch` blocks is optimal:**
   - **Maximum Readability:** The happy path stays flat inside a single `try` block, and the error-handling table immediately below it reads like a clear specification of every failure category.
   - **Clear User Message Differentiation:** Each exception subclass maps directly to its own Spanish message template tailored to the store clerk:
     - `ResourceNotFoundException`: indicates that the typed ID does not exist (`"No se encontró el recurso solicitado: ..."`).
     - `BusinessRuleException`: explains which domain rule blocked the operation (`"No se puede completar la operación: ..."`).
     - `InvalidDataException`: points out which input field failed format/range validation (`"Los datos ingresados no son válidos: ..."`).
     - `PersistenceException`: shields the user from low-level `IOException` details and instructs them to contact the administrator (`"Ocurrió un problema al acceder a los datos del sistema. Contacte al administrador."`).
   - **Centralized Helper (`handleOperationError`) + Local Multi-Catch:** `ConsoleMenu` both uses explicit multi-catch blocks in its operation methods and centralizes the main loop safety net so no unhandled exception or raw stack trace ever reaches the user.
