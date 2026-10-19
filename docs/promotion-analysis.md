# Requirement 2: Promotions Module — Analysis

### 1. The three promotions have different calculation rules but share common attributes and behaviors. How is this situation reflected in the class hierarchy design? Which object-oriented programming mechanism allows each promotion type to calculate its discount differently without the rest of the system needing to know the concrete types?

It is reflected in the abstract `Promotion` class and its subclasses: `PercentageDiscount`, `CategoryDiscount`, and `BulkPurchaseDiscount`. The mechanism allowing each promotion type to calculate its discount differently is polymorphism, as it enables the same method call to execute differently depending on the specific subclass implementation.

### 2. The base class Promotion cannot implement the discount calculation method because each type has different logic. How is this method declared in the base class, and what does this declaration guarantee regarding the subclasses?

The method `calculateDiscount(Sale)` is declared as an abstract method in the abstract `Promotion` class. This declaration guarantees that every concrete subclass must provide its own specific discount calculation logic, preventing incomplete implementations.

### 3. The business rule states that only the promotion offering the highest discount is applied. In which class is this selection logic located, and why is this placement consistent with the layered architecture principle? Why should this logic NOT be placed in the Sale class or the console menu?

This selection logic is located in `PromotionService` (via `findBestPromotionFor(Sale)`), which is part of the `service` layer where business rules belong. It must not be placed in `Sale` because domain models should only manage their own state, nor in `ConsoleMenu` because UI classes should not contain business rules.

### 4. What modifications are required in the Sale class and the generateReceipt method so that the receipt displays the applied discount? Do these modifications break any existing system behavior?

`Sale` requires attributes for `appliedPromotionName` and `discountAmount`, along with an `applyDiscount(...)` method. `calculateTotal()` subtracts `discountAmount` from `subtotal`. `generateReceipt()` is updated to display these fields. These modifications add optional functionality without breaking existing sale logic.

### 5. Active promotions are determined by comparing the current date with the start and end dates of each promotion. Where is this validation performed (in the Promotion class, in PromotionService, or in both)? Justify your answer.

The basic validation is performed in `Promotion` using `isActive(LocalDate currentDate)`, because the start and end dates belong to the promotion entity. `PromotionService` uses this method to filter all promotions and retrieve currently active ones for a sale.
