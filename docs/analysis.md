# Analysis — GameZone Unicesar

## People

### Q1: What attributes are common to all people who interact with the store, and which are specific to each type of person? How is this distinction reflected in a class hierarchy?

All people share `id`, `firstName`, `lastName`, and `phone`, since every actor in the store needs to be identified and contacted regardless of role. `Customer` adds `email` and `purchaseHistory`, which only make sense for someone buying products, while `Seller` adds `employeeCode` and `shift`, which only make sense for an employee. This distinction is reflected by placing the shared attributes in an abstract `Person` class and the role-specific attributes in `Customer` and `Seller`, which extend it.

### Q2: Should there be a class representing a "generic person" without specifying a role? Why or why not? What implication does this decision have on the possibility of instantiating this class?

Yes, a generic `Person` class is useful to hold the attributes and behavior common to both roles and avoid duplicating them, but it should never represent a real, standalone actor in the store — every person interacting with GameZone is either a customer or a seller, never an unqualified "person". This is why `Person` is declared `abstract`: it cannot be instantiated directly, and the compiler enforces that only its concrete subclasses (`Customer`, `Seller`) can be created.

## Products

### Q3: What characteristics do all products sold by the store share, regardless of type? Which characteristics are specific to each product type?

Every product, whether a video game or a console, shares `id`, `title`, `price`, and `stock`, since these attributes are required to catalog, price, and track inventory for any item sold. `VideoGame` adds `platform`, `genre`, and `ageRating`, while `Console` adds `brand`, `model`, and `generation` — attributes that only make sense for their respective product type.

### Q4: Each type of product must be able to present a description that integrates its particular characteristics. How should this behavior be declared in the base class to guarantee that all subclasses implement it in their own way? What object-oriented programming mechanism enables this?

The base `Product` class declares `getDescription()` as an abstract method, which forces every concrete subclass to provide its own implementation that incorporates its specific attributes (for example, platform and genre for `VideoGame`, or brand and generation for `Console`). This is enabled by the OOP mechanism of abstraction combined with polymorphism: callers can invoke `getDescription()` on any `Product` reference without knowing its concrete type, and the correct subclass implementation runs at execution time.

## Sales and relationships

### Q5: A sale involves a customer, a seller, and one or more products. What kinds of relationships exist between the class representing the sale and the other classes of the system? Are these relationships of inheritance, association, composition, or another type? Justify.

`Sale` is related to `Customer` and `Seller` through association: a `Sale` references one `Customer` and one `Seller`, but neither the customer nor the seller depend on the sale to exist, and the same customer or seller participates in many independent sales. `Sale` is related to `Product` through aggregation: a sale holds a list of one or more products, but those products exist in the inventory independently of any particular sale and are not destroyed when the sale is removed. None of these are inheritance relationships, since a sale is not a kind of customer, seller, or product.

### Q6: Should the sale be responsible for calculating its own total, or should this responsibility fall on another class? Justify your decision.

The sale should calculate its own total through a `calculateTotal()` method, because the total is derived directly from data the `Sale` already owns — its list of products — and keeping the calculation there follows the single responsibility principle by cohering the sale's own state and behavior in one place. `SaleService` is still responsible for orchestrating the registration process (validating stock, updating inventory), but the arithmetic of summing the sale's own products belongs on `Sale` itself.

## Business constraints

### Q7: How does the design guarantee that a sale cannot be registered without at least one product? At what point in the system should this rule be validated?

The rule is validated in `SaleService.registerSale(Customer, Seller, List<Product>)`, before a `Sale` object is constructed or persisted: the service checks that the incoming product list is neither null nor empty and rejects the operation otherwise. Validating at the service layer, rather than inside the UI, keeps the business rule in one place, independent of how the UI collected the input and reusable by any future caller of the service. As an additional safeguard, the `Sale` constructor itself also rejects a null or empty product list, so it is impossible to construct an invalid `Sale` object in memory even if some future caller bypasses `SaleService`.

### Q8: How does the design reflect the automatic update of inventory when a sale is registered? Which classes are involved in this operation?

When `SaleService.registerSale(...)` runs, it validates that each requested product has enough stock, then calls `Product.updateStock(int)` (via `ProductService`) to decrement the inventory for every product included in the sale, before finally saving the new `Sale` through `SaleRepository`. This operation involves `SaleService` as the orchestrator, `ProductService` and `Product` for the stock update itself, and `SaleRepository`/`ProductRepository` to persist the resulting state.

## Layered organization

### Q9: The system must be organized into four layers: model, persistence, services, and user interface. What type of classes belong in each layer? What criterion allows one to decide in which layer a class should be placed?

The `model` layer holds pure domain classes (`Product`, `Person`, `Sale`, and their subclasses) with no I/O or UI references; `persistence` holds classes that read and write model objects to files (`ProductRepository`, `PersonRepository`, `SaleRepository`); `service` holds the business rules that operate on model objects through the repositories (`ProductService`, `PersonService`, `SaleService`); and `ui` holds the console menu that talks only to services. The criterion for placement is the class's responsibility: does it represent business data (model), does it perform I/O (persistence), does it enforce business rules and coordinate persistence (service), or does it interact with the end user (ui)?

### Q10: Why should the logic for saving and retrieving data from files not be inside the domain classes? What problems arise when these responsibilities are mixed?

Domain classes should represent business concepts and behavior only; mixing in file I/O would violate the single responsibility principle and couple the domain model to a specific storage format. If persistence logic lived inside `Product` or `Sale`, changing the storage format (e.g., from plain text to CSV) would require modifying the domain classes themselves, and testing the domain logic would require a file system, making the design harder to maintain, test, and extend.

### Q11: What dependencies are allowed between the layers, and which are forbidden? Justify the meaning of the allowed dependencies.

The allowed dependencies are `ui → service`, `service → persistence`, `service → model`, and `persistence → model`; `model` depends on nothing. Any dependency in the opposite direction — `model` depending on `persistence`, `persistence` depending on `service`, or `service`/`persistence`/`model` depending on `ui` — is forbidden. This unidirectional flow keeps the domain model stable and reusable at the core of the application, while outer layers (persistence, service, ui) depend inward on it rather than the reverse, preventing circular dependencies and letting each layer be replaced or tested independently.

A concrete example of this rule in practice is `SaleRepository`. Persisted sales only store raw ids (customer id, seller id, product ids) rather than full domain objects, so reconstructing a complete `Sale` from disk requires resolving those ids into real `Customer`, `Seller`, and `Product` instances. Instead of letting `SaleRepository` call into `ProductService`/`PersonService` to do that resolution — which would create a forbidden `persistence → service` dependency — the repository works only with a plain data-transfer class, `SaleRecord`, that holds the raw ids and lives in the `persistence` layer without depending on anything outside `model`-level data. The resolution of those ids into full domain objects happens in `SaleService`, which is already allowed to depend on `ProductService` and `PersonService`. This keeps `persistence` depending only on plain data, and keeps the reconstruction logic where the architecture permits it: in `service`.

## Requirement 1: Accessories Module

### 1. Should accessories be integrated into the existing product hierarchy by extending Product, or should they form an independent hierarchy? Justify your decision considering code reuse and model coherence.

Accessories should be integrated into the existing product hierarchy by extending `Product`. They share common attributes such as identifier, title, price, and stock, so extending `Product` avoids duplicating code. An abstract class called `Accessory` can manage the common accessory behavior—especially console compatibility—while `Controller`, `Cable`, and `Memory` extend it. This design is coherent because accessories are still products that can be sold in the store system.

### 2. What attributes are common to the three types of accessories, and which ones are specific to each type? How is this distinction reflected in the module class hierarchy?

The common attributes are identifier, title, price, and stock (inherited from `Product`), as well as a `List<String>` called `compatibleConsoleIds`, which stores the IDs of the consoles they are compatible with. Specific attributes include connection type for `Controller`, length in meters and connector type for `Cable`, and storage capacity in gigabytes along with memory type for `Memory`. This is reflected by having `Accessory` extend `Product`, while `Controller`, `Cable`, and `Memory` extend `Accessory`.

### 3. Compatibility between an accessory and a console is a relationship between two system entities. How is this relationship represented in the design and persistence? Is compatibility an attribute of the accessory, the console, or both?

Compatibility is mainly represented as an attribute of the accessory. Each `Accessory` stores a `List<String>` named `compatibleConsoleIds`, containing the IDs of compatible consoles. `AccessoryService` can use this list to find accessories compatible with a selected console. Since the project uses Java serialization, this list is saved automatically as part of the serialized `Accessory` object without needing manual text conversions.

### 4. What modifications are necessary in the sales service class, SaleService, so that sales can include accessories without breaking the existing behavior for video games and consoles?

`SaleService` must allow a sale to contain regular products and accessories within the same `List<Product>`. Since `Accessory` extends `Product`, no separate list is necessary in `Sale`. However, `SaleService` must use `instanceof Accessory` to determine which service manages each item: `AccessoryService` for accessories and `ProductService` for video games and consoles. When reconstructing a sale from a `SaleRecord`, it must look up each item ID in the correct service repository.

### 5. In which layer of the system architecture should the new accessory module classes be located? Justify your decision based on the responsibilities of each layer.

`Accessory`, `Controller`, `Cable`, and `Memory` belong to the `model` layer because they represent entity data and compatibility behavior. `AccessoryRepository` belongs to `persistence` because it saves and loads serialized accessory data. `AccessoryService` belongs to `service` because it manages business rules for registering, querying, and updating stock for accessories.

## Requirement 2: Promotions Module (PARCIAL CORRESPONDIENTE)

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

## Requirement 3: Returns Module

### 1. A return is a new system entity that refers to an existing sale. What type of relationship exists between the Return class and the Sale class? Is this relationship inheritance, association, aggregation, or composition? Justify your answer.

The relationship between `Return` and `Sale` is an association. A return requires a reference to an existing sale to verify that returned products belonged to that transaction. `Return` is not a `Sale` (not inheritance), and a sale exists independently of any return (not composition or aggregation).

### 2. A return can contain only some products from the original sale, not necessarily all of them. How is this situation represented in the attributes of the Return class? What is stored in the returned products attribute?

This is represented using a `List<Product>` attribute in the `Return` class. This list holds the specific subset of products being returned by the customer after validating that each item belonged to the original sale's product list.

### 3. The business rule states that returns can only be registered within 30 days after the sale. In which system layer is this validation located and why? What Java mechanism is used to calculate the difference between two dates?

This validation is located in `ReturnService` (and supported by `Sale.canBeReturned()`) within the `service` layer, as it represents a core business rule. Java uses `LocalDate` and `ChronoUnit.DAYS.between(saleDate, currentDate)` to compute calendar days elapsed.

### 4. Returning products increases stock. What existing method from the Workshop 1 system is reused for this operation, and in which class is it invoked from the returns module? Why is it important to reuse existing methods instead of duplicating stock-update logic?

The method reused is `restoreStock(String productId, int quantity)` from `ProductService` (and `AccessoryService`). It is invoked from `ReturnService` upon successful registration of a return. Reusing this method prevents duplicate code and centralizes inventory modification logic.

### 5. The monthly balance report needs to combine information from two different modules: sales and returns. In which service class is this report located, and why is this location consistent with the layered architecture? What dependencies does this class need to generate it?

The report is located in `ReturnService` via `generateMonthlyBalance(int month, int year)`. This fits the layered architecture because services coordinate cross-module business rules. It depends on `ReturnRepository` for return refunds and `SaleService` for total sales.

## Requirement 4: Warranties Module

### 1. The two warranty types have common attributes such as dates and associated product, but they also have different attributes and behaviors such as duration, coverage, and cost. How is this situation reflected in the class hierarchy design? What object-oriented programming mechanism allows each warranty type to have its own duration without duplicating code?

This is represented with an abstract `Warranty` class and two concrete subclasses: `BasicWarranty` and `ExtendedWarranty`. Common fields (`warrantyId`, `product`, `sale`, `startDate`, `endDate`) reside in `Warranty`. Subclasses override `getDurationInMonths()`. Inheritance and polymorphism enable custom duration logic without code duplication.

### 2. The business rule states that only consoles generate an automatic basic warranty, not video games. In which system layer is this decision located and what Java mechanism is used to verify the real type of a product? Justify your answer.

This decision is in `SaleService` within the `service` layer because automatic warranty assignment is part of the sale processing flow. Java's `instanceof Console` operator verifies if a product item is a console before triggering `WarrantyService.assignBasicWarranty(...)`.

### 3. Each warranty type has a different duration: 6 months or 12 months. How is the expiration date calculated in each subclass? Should this calculation be done in the warranty constructor or in a separate method? Justify your answer.

`Warranty` defines the abstract method `getDurationInMonths()`, implemented as `6` by `BasicWarranty` and `12` by `ExtendedWarranty`. The expiration date (`endDate`) is calculated inside the `Warranty` constructor by adding the subclass duration to `startDate`. Calculating this on initialization guarantees a valid state upon object instantiation.

### 4. The extended warranty adds a cost equal to 10% of the product price to the sale total. At what point in the sale-registration flow is this additional cost calculated and applied? What modifications are necessary in the SaleService.registerSale method?

The cost is applied after validating stock and constructing the `Sale`. `SaleService.registerSale` receives a `List<String> productIdsWithExtendedWarranty` parameter. For each matching console, it calls `WarrantyService.assignExtendedWarranty(...)` and adds `warranty.getAdditionalCost()` to the sale's subtotal.

### 5. The "warranties expiring soon" query requires iterating through all warranties and filtering those whose end date is within the next 30 days. In which class is this method located and what dependencies does it need? Why is this location consistent with the layered architecture?

This query is located in `WarrantyService` (via `listWarrantiesExpiringSoon(int daysAhead)`). It depends on `WarrantyRepository` to load warranty data. This location is consistent with layered architecture because data filtering and business queries belong in the `service` layer.
