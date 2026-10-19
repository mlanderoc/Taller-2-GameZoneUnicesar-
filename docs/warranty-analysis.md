# Requirement 4: Warranties Module — Analysis

### 1. The two warranty types have common attributes such as dates and associated product, but they also have different attributes and behaviors such as duration, coverage, and cost. How is this situation reflected in the class hierarchy design? What object-oriented programming mechanism allows each warranty type to have its own duration without duplicating code?

This is represented with an abstract `Warranty` class and two concrete subclasses: `BasicWarranty` and `ExtendedWarranty`. Common fields (`warrantyId`, `product`, `sale`, `startDate`, `endDate`) reside in `Warranty`. Subclasses override `getDurationInMonths()`. Inheritance and polymorphism enable custom duration logic without code duplication.

### 2. The business rule states that only consoles generate an automatic basic warranty, not video games. In which system layer is this decision located and what Java mechanism is used to verify the real type of a product? Justify your answer.

This decision is in `SaleService` within the `service` layer because automatic warranty assignment is part of the sale processing flow. Java's `instanceof Console` operator verifies if a product item is a console before triggering `WarrantyService.assignBasicWarranty(...)`.

### 3. Each warranty type has a different duration: 6 months or 12 months. How is the expiration date calculated in each subclass? Should this calculation be done in the warranty constructor or in a separate method? Justify your answer.

`Warranty` defines the abstract method `getDurationInMonths()`, implemented as `6` by `BasicWarranty` and `12` by `ExtendedWarranty`. The expiration date (`endDate`) is calculated inside the `Warranty` constructor by adding the subclass duration to `startDate`. Calculating this on initialization guarantees a valid state upon object instantiation.

### 4. The extended warranty adds a cost equal to 10% of the product price to the sale total. At what point in the sale-registration flow is this additional cost calculated and applied? What modifications are necessary in the SaleService.registerSale method?

The cost is applied after validating stock, constructing the `Sale`, and calculating promotional discounts over the product subtotal. `SaleService.registerSale` receives a `List<String> productIdsWithExtendedWarranty` parameter. For each matching console, it calls `WarrantyService.assignExtendedWarranty(...)` and adds `warranty.getAdditionalCost()` to `sale.extendedWarrantyCost`.

### 5. The "warranties expiring soon" query requires iterating through all warranties and filtering those whose end date is within the next 30 days. In which class is this method located and what dependencies does it need? Why is this location consistent with the layered architecture?

This query is located in `WarrantyService` (via `listWarrantiesExpiringSoon(int daysAhead)`). It depends on `WarrantyRepository` to load warranty data. This location is consistent with layered architecture because data filtering and business queries belong in the `service` layer.
