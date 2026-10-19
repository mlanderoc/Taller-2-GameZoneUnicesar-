# AI Usage Log - Developer 1

## Student Information

 **Full name:** Carlos Gomez
 
 **Role:** Developer 1
 
 **Assigned module:** Product management
 
 **Project:** GameZone Unicesar

## Purpose of AI Use

I used Artificial Intelligence as a support tool during the development of the product module. The tool was used to clarify object-oriented programming concepts, review my code decisions, understand Java features, and identify possible improvements. I reviewed all suggestions and implemented the final code myself.

## AI Tool Used

 **Tool:** Perplexity AI
 
 **Use:** Concept clarification and code review.

## Consultation Log

###  Product hierarchy design

 **Topic:** Inheritance in the product module
 **Consultation:** I asked for clarification about how to organize common product attributes and specific attributes for video games and consoles.
 
 **What I learned:** Common data such as ID, title, price, and stock should belong to the abstract `Product` class. Attributes that only apply to video games or consoles should remain in their respective subclasses.
 
 **How I applied it:** I used this clarification to understand and review the inheritance relationship between `Product`, `VideoGame`, and `Console`.

###  Abstract description method

 **Topic:** Polymorphism and abstract methods.
 **Consultation:** I asked how each product type could display its own detailed description while sharing a common base class.
 
 **What I learned:** A method whose behavior varies by product type can be declared as abstract in the base class and implemented with `@Override` in each subclass.
 
 **How I applied it:** I used this concept to understand the purpose of the product description method and verify that each concrete product type provides its own implementation.

###  Stock update validation

 **Topic:** Product inventory management.
 
 **Consultation:** I asked about validating stock updates and how to prevent inventory values from becoming invalid.
 
 **What I learned:** Stock must be handled through a method that validates the new quantity or requested change. Business rules should be placed in the service layer when they involve application operations.
 
 **How I applied it:** I reviewed the stock-related behavior in my product module and made sure that the product service is responsible for coordinating inventory updates and persistence.

 ### Product persistence

 **Topic:** File-based persistence in the product module.
 
 **Consultation:** I asked about the responsibilities of a repository class when saving and loading products from files.
 
 **What I learned:** The repository is responsible for file access, while the model classes should not contain file handling logic. The service layer calls the repository and applies business rules.
 
 **How I applied it:** I used this guidance to keep `ProductRepository` focused on saving and loading data and to keep file operations outside the product model classes.

### Code review and naming

 **Topic:** Code quality and Java naming conventions.
 
 **Consultation:** I asked for a review of naming and structure in my product module.
 
 **What I learned:** Classes should use PascalCase, while attributes and methods should use camelCase. Names should be descriptive and written in English.
 
 **How I applied it:** I reviewed identifiers in the product module and kept names consistent with Java conventions and the project requirements.

 ### Spanish-to-English Translation


 **Inquiry:** Requested translations of Spanish words into English.
 
 **What I learned:** I learned several unfamiliar words whose meanings I did not previously know.
 
 **How ​​I applied it:** I applied the corresponding translations to each task I was assigned.

### Category discount for accessories (Adjustment A1)

 **Topic:** Polymorphic type checking with `instanceof` in `CategoryDiscount`
 
 **Consultation:** I asked whether `product instanceof Accessory` also matches subclasses such as `Controller`, `Cable`, and `Memory` when calculating category discounts.
 
 **What I learned:** In Java, `instanceof` evaluates to `true` for any concrete subclass of `Accessory`, so a single check covers all accessory types.
 
 **How I applied it:** I added the `"ACCESSORY"` condition in `CategoryDiscount.calculateDiscount` and validated the allowed category names in `PromotionService.registerCategoryDiscount`.

### Proportional refund calculation (Adjustment A5)

 **Topic:** Proportional discount formula in `Return.calculateRefundAmount`
 
 **Consultation:** I reviewed how to avoid division by zero when calculating the discount ratio `discount / subtotal` for a returned sale.
 
 **What I learned:** Checking `originalSale != null && originalSale.getSubtotal() > 0` before dividing ensures safe calculation of `price * (1 - discount / subtotal)`.
 
 **How I applied it:** I updated `calculateRefundAmount` and `generateReturnReceipt` in `Return` to compute and display the list price, proportional discount, and net refund per item.

### Custom unchecked exception hierarchy (Requirement 6)

 **Topic:** Extending `RuntimeException` and chaining constructors with `super(message, cause)`
 
 **Consultation:** I asked how to preserve the original `IOException` stack trace inside `PersistenceException` while keeping an `errorCode` attribute in the abstract `GameZoneException` base class.
 
 **What I learned:** Adding a protected constructor `GameZoneException(String message, String errorCode, Throwable cause)` that calls `super(message, cause)` allows `PersistenceException` to store both the error code and the root cause.
 
 **How I applied it:** I implemented `GameZoneException`, its four specialized subclasses in `com.gamezone.exceptions`, and the static validator classes in `com.gamezone.validation`.

---

## Requirement 7 — GRASP Audit and Refactoring

### Phase 1 - Audit

#### Conceptual difference between Polymorphism and Protected Variations in GRASP
- **Topic:** Object-oriented design concepts (GRASP patterns)
- **Consultation:** I asked for a conceptual explanation of how GRASP distinguishes *Polymorphism* from *Protected Variations* when evaluating an inheritance hierarchy in Java.
- **What I learned:** *Polymorphism* focuses on how to assign responsibility for type-dependent behavior using overridden methods instead of `if/instanceof` conditionals, whereas *Protected Variations* focuses on shielding client classes from foreseeable points of instability through a stable interface or abstraction.
- **How I applied it:** When I audited my own `Product` and `Promotion` classes, I classified the `instanceof` checks in `CategoryDiscount.calculateDiscount` primarily under *Polymorphism* (`V-02`) and cross-referenced *Protected Variations* without duplicating the finding.

#### English terminology for audit tables
- **Topic:** Spanish-to-English translation of technical terms
- **Consultation:** I asked for standard English phrasing for terms such as *"hallazgo de frontera"* (*boundary finding*) and *"veredicto"* (*verdict*).
- **What I learned:** Using consistent terminology across the individual audit tables makes consolidation clearer.
- **How I applied it:** I used those English terms when writing `docs/grasp-audit/developer1-audit.md` and Questions 1 and 3 in `docs/grasp-analysis.md`.

### Phase 2 - Refactoring

#### Polymorphic category dispatch in `Product` and `CategoryDiscount` (`V-02`)
- **Topic:** Replacing `instanceof` type checks with an abstract method in the `Product` hierarchy.
- **Consultation:** I asked for a review of my proposed `getCategory()` and `belongsToCategory(String category)` methods in `Product` to make sure existing serialized `.dat` files remain compatible (`serialVersionUID = 1L`).
- **What I learned:** Adding methods without modifying serialized instance fields preserves binary serialization compatibility with `products.dat` and `Accessory.dat` while eliminating the `instanceof` branches in `CategoryDiscount.calculateDiscount(Sale)`.
- **How I applied it:** I implemented `getCategory()` and `belongsToCategory(String)` in `Product`, overrode `getCategory()` in `VideoGame`, `Console`, and `Accessory`, and refactored `CategoryDiscount.calculateDiscount(Sale)` (`V-02`), `Return.getDiscountRatio()` (`V-01`), `Accessory.isCompatibleWith` (`V-03`), and `ProductValidator` (`V-05`).
