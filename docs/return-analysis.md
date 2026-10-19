# Requirement 3: Returns Module — Analysis

### 1. A return is a new system entity that refers to an existing sale. What type of relationship exists between the Return class and the Sale class? Is this relationship inheritance, association, aggregation, or composition? Justify your answer.

The relationship between `Return` and `Sale` is an association. A return requires a reference to an existing sale to verify that returned products belonged to that transaction. `Return` is not a `Sale` (not inheritance), and a sale exists independently of any return (not composition or aggregation).

### 2. A return can contain only some products from the original sale, not necessarily all of them. How is this situation represented in the attributes of the Return class? What is stored in the returned products attribute?

This is represented using a `List<Product>` attribute in the `Return` class. This list holds the specific subset of products being returned by the customer after validating that each item belonged to the original sale's product list.

### 3. The business rule states that returns can only be registered within 30 days after the sale. In which system layer is this validation located and why? What Java mechanism is used to calculate the difference between two dates?

This validation is located in `ReturnService` (and supported by `Sale.canBeReturned()`) within the `service` layer, as it represents a core business rule. Java uses `LocalDate` and `ChronoUnit.DAYS.between(saleDate, currentDate)` to compute calendar days elapsed.

### 4. Returning products increases stock. What existing method from the Workshop 1 system is reused for this operation, and in which class is it invoked from the returns module? Why is it important to reuse existing methods instead of duplicating stock-update logic?

The method reused is `restoreStock(String productId, int quantity)` from `ProductService` (and `AccessoryService`). It is invoked from `ReturnService` upon successful registration of a return. Reusing this method prevents duplicate code and centralizes inventory modification logic.

### 5. The monthly balance report needs to combine information from two different modules: sales and returns. In which service class is this report located, and why is this location consistent with the layered architecture? What dependencies does this class need to generate it?

The report is located in `ReturnService` via `calculateMonthlySales(int month, int year)`, `calculateMonthlyReturns(int month, int year)`, and `generateMonthlyBalance(int month, int year)`. This fits the layered architecture because services coordinate cross-module business rules. It depends on `ReturnRepository` for return refunds and `SaleService` for total sales.
