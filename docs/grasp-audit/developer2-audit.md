# Individual GRASP Audit Report — Developer 2 (Héctor Guevara)

- **Author:** Héctor Guevara (`Hectorguevara22`)
- **Role:** Developer 2 — People Module, Persistence Repositories & Business Services
- **Audited Branch / Commit (`develop`):** `3f14c06`

---

## 1. Audit Scope

In accordance with Section 4 of Requirement 7 and `TEAM.md`, this audit covers all classes and methods developed under Developer 2's responsibility across Taller 1 and Requirements 1, 2, 3, 4, 5, and 6:

| Module (Requirement) | Classes Audited | Methods Audited |
| :--- | :--- | :--- |
| **People (Taller 1)** | `Person` (abstract), `Customer`, `Seller`, `PersonRepository`, `PersonService` | `Person`: constructor, getters/setters, `getFullName()`, `getSummary()`<br>`Customer`: constructor, `getSummary()`<br>`Seller`: constructor, `getEmployeeCode()`, `setEmployeeCode(String)`, `getWorkShift()`, `setWorkShift(String)`, `getSummary()`<br>`PersonRepository`: `save(Person)`, `findAll()`, `findById(String)`, `saveAll(List<Person>)`, `loadAllCustomers()`, `loadAllSellers()`, `saveAllCustomers(List<Customer>)`, `saveAllSellers(List<Seller>)`<br>`PersonService`: constructor, `registerCustomer(...)`, `registerSeller(...)`, `findCustomerById(String)`, `findSellerById(String)`, `listAllCustomers()`, `listAllSellers()` |
| **Accessories (Requirement 1 & RE5-A4)** | `AccessoryRepository`, `AccessoryService` | `AccessoryRepository`: `save(Accessory)`, `findAll()`, `findById(String)`, `saveAll(List<Accessory>)`, `loadAll()`<br>`AccessoryService`: constructor, `findById(String)`, `registerCable(...)`, `registerController(...)`, `registerMemory(...)`, `listAllaccessories()`, `listAccesoriesByType(String)`, `findAcessoriesCOmpatibleWith(String)`, `updateStock(String, int)`, `restoreStock(String, int)` |
| **Promotions (Requirement 2 & RE5-A1)** | `PromotionRepository`, `PromotionService` | `PromotionRepository`: `save(Promotion)`, `findAll()`, `findById(String)`, `saveAll(List<Promotion>)`, `loadAll()`<br>`PromotionService`: constructor, `findById(String)`, `registerPercentageDiscount(...)`, `registerCategoryDiscount(...)`, `registerBulkPurchaseDiscount(...)`, `validatePromotionBase(...)`, `listAllPromotions()`, `listActivePromotions()`, `findBestPromotionFor(Sale)` |
| **Returns & Monthly Balance (Requirement 3 & RE5-A4/A6/A7)** | `ReturnRecord`, `ReturnRepository`, `ReturnService` | `ReturnRecord`: constructor, getters<br>`ReturnRepository`: `save(ReturnRecord)`, `findAll()`, `findById(String)`, `saveAll(List<ReturnRecord>)`, `loadAll()`<br>`ReturnService`: constructors, `registerReturn(String, List<String>, String)`, `viewAllReturns()`, `viewReturnsByCustomer(String)`, `viewReturnsBySale(String)`, `calculateMonthlySales(int, int)`, `calculateMonthlyReturns(int, int)`, `generateMonthlyBalance(int, int)`, `findInSale(Sale, String)`, `toRecord(Return)`, `toReturn(ReturnRecord)` |
| **Warranties (Requirement 4 & RE5-A2/A7)** | `WarrantyRecord`, `WarrantyRepository`, `WarrantyService` | `WarrantyRecord`: constructor, getters<br>`WarrantyRepository`: `save(WarrantyRecord)`, `findAll()`, `findById(String)`, `saveAll(List<WarrantyRecord>)`, `loadAll()`<br>`WarrantyService`: constructor, `assignBasicWarranty(...)`, `assignExtendedWarranty(...)`, `findWarrantyByProduct(String, String)`, `listAllWarranties()`, `listActiveWarranties()`, `listWarrantiesExpiringSoon(int)`, `cancelWarranties(String, String)`, `save(Warranty)`, `toRecord(Warranty)`, `toWarranty(WarrantyRecord)`, `resolveSaleById(String, Product)` |
| **Exceptions & Validations Usage (Requirement 6)** | All repositories and services listed above | Delegation to `PersonValidator`, `ProductValidator`, `SaleValidator`; throwing of `BusinessRuleException`, `InvalidDataException`, `ResourceNotFoundException`, and wrapping of `IOException | ClassNotFoundException` into `PersistenceException` |

---

## 2. Evaluation by GRASP Pattern

### 2.1 Information Expert

- **Verdict:** **Violation** (2 violations found in `AccessoryService` and `ReturnService`; properly applied in `Person` hierarchy, `PromotionService.findBestPromotionFor`, and `WarrantyService.listActiveWarranties`).

#### Correct Applications
- **`Person.getFullName()` (`Person.java`, lines 142–144):** `Person` owns `firstName` and `lastName` and constructs the full name directly.
- **`PromotionService.findBestPromotionFor(Sale)` (`PromotionService.java`, lines 107–120) & `listActivePromotions()` (`PromotionService.java`, lines 96–105):** Delegates `p.isActive(today)` and `p.calculateDiscount(sale)` to each `Promotion` (the Information Expert on its own date range and discount rule).
- **`WarrantyService.listActiveWarranties()` (`WarrantyService.java`, lines 77–86):** Delegates `warranty.isActive(today)` to `Warranty`.

#### Identified Violations (`V-01` and `V-02`)

| Field | Content |
| :--- | :--- |
| **ID** | `V-01` |
| **Pattern** | Information Expert (`Experto en Información`) |
| **Module** | Accessories (Requirement 1) |
| **Evidence** | `com.gamezone.service.AccessoryService`, method `findAcessoriesCOmpatibleWith(String consoleId)` (lines 72–78 of `AccessoryService.java`):<br>```java
public List<Accessory> findAcessoriesCOmpatibleWith(String consoleId) {
    return accessories.stream()
            .filter(accessory ->
                    accessory.getCompatibleConsoleIds() != null &&
                    accessory.getCompatibleConsoleIds().contains(consoleId))
            .collect(Collectors.toList());
}
``` |
| **Explanation** | `Accessory` (`Accessory.java`, lines 56–63) already owns the `compatibleConsoleIds` list and implements the expert method `public boolean isCompatibleWith(String consoleId)`. However, `AccessoryService.findAcessoriesCOmpatibleWith` bypasses `accessory.isCompatibleWith(consoleId)` and instead pulls the raw list `accessory.getCompatibleConsoleIds()` to call `.contains(consoleId)` externally. |
| **Consequence** | Duplicates compatibility logic outside the `Accessory` domain entity and exposes `Accessory`'s internal collection structure to the service layer. |
| **Proposed Solution** | Apply **Information Expert** by ensuring null-safety inside `Accessory.isCompatibleWith(String consoleId)` and refactoring `AccessoryService.findAcessoriesCOmpatibleWith(String consoleId)` to filter via `accessory.isCompatibleWith(consoleId)`. |
| **Other Member's Code** | **Yes (Boundary Finding):** `Accessory` belongs to **Developer 1 (`craulgomez`)**, while `AccessoryService` belongs to **Developer 2 (`Hectorguevara22`)**. |

| Field | Content |
| :--- | :--- |
| **ID** | `V-02` |
| **Pattern** | Information Expert (`Experto en Información`) |
| **Module** | Returns (Requirement 3) |
| **Evidence** | `com.gamezone.service.ReturnService`, method `findInSale(Sale sale, String productId)` (lines 210–217 of `ReturnService.java`):<br>```java
private Product findInSale(Sale sale, String productId) {
    for (Product product : sale.getProducts()) {
        if (product.getId().equals(productId)) {
            return product;
        }
    }
    return null;
}
``` |
| **Explanation** | `Sale` owns the `List<Product> products` collection and is the Information Expert on which products belong to that sale. Instead of asking `Sale` to find a product by ID (`sale.findProductById(productId)`), `ReturnService` extracts `sale.getProducts()` and iterates through the list itself. |
| **Consequence** | Couples `ReturnService` to the internal collection traversal of `Sale` and prevents other callers from reusing product lookup within a sale. |
| **Proposed Solution** | Apply **Information Expert** by adding `public Product findProductById(String productId)` to `Sale` and refactoring `ReturnService.findInSale(Sale, String)` to delegate to `sale.findProductById(productId)`. |
| **Other Member's Code** | **Yes (Boundary Finding):** `Sale` belongs to the **Technical Lead (`mlanderoc`)**, while `ReturnService` belongs to **Developer 2 (`Hectorguevara22`)**. |

---

### 2.2 Creator

- **Verdict:** **Correct** *(with constructor wiring between `Main` and `AccessoryService` noted as a boundary finding with the Technical Lead)*

#### Justification & Evidence
- **`PersonService.registerCustomer(...)` and `registerSeller(...)` (`PersonService.java`, lines 43–78):** Instantiates `Customer` and `Seller` and persists them into `PersonRepository`.
- **`AccessoryService.registerCable(...)`, `registerController(...)`, `registerMemory(...)` (`AccessoryService.java`, lines 35–64):** Instantiates `Cable`, `Controller`, and `Memory` and aggregates them in `accessories`.
- **`PromotionService.registerPercentageDiscount(...)`, `registerCategoryDiscount(...)`, `registerBulkPurchaseDiscount(...)` (`PromotionService.java`, lines 34–72):** Instantiates the concrete `Promotion` subclasses and persists them.
- **`ReturnService.registerReturn(...)` & `toRecord(...)` (`ReturnService.java`, lines 94 & 219–232):** Instantiates `Return` and `ReturnRecord` using the resolved `Sale` and `List<Product>`.
- **`WarrantyService.assignBasicWarranty(...)`, `assignExtendedWarranty(...)`, `toRecord(...)` (`WarrantyService.java`, lines 43–53 & 138–147):** Instantiates `BasicWarranty`, `ExtendedWarranty`, and `WarrantyRecord`.

---

### 2.3 Controller

- **Verdict:** **Correct**

#### Justification & Evidence
- **`PersonService`, `AccessoryService`, `PromotionService`, `ReturnService`, and `WarrantyService` (`com.gamezone.service`):** Act as domain/use-case facade controllers behind the presentation layer (`ConsoleMenu` and Swing dialogs/panels). Neither `ConsoleMenu` nor the Swing panels execute business calculations or touch `Repository` classes directly; instead, they delegate every system operation (`registerCustomer`, `registerReturn`, `generateMonthlyBalance`, `listWarrantiesExpiringSoon`, etc.) to these service classes.

---

### 2.4 Low Coupling

- **Verdict:** **Violation** (1 violation found in `AccessoryService` constructor coupling with `Main`; properly applied in `WarrantyService`, `ReturnRecord`, and `WarrantyRecord`).

#### Correct Applications
- **Acyclic Service Wiring in `WarrantyService` (`WarrantyService.java`, lines 25–41):** `WarrantyService` resolves sale references through `SaleRepository` and `ProductService` rather than depending on `SaleService`, preventing circular dependencies (`SaleService ↔ WarrantyService`).
- **DTO Persistence Records (`ReturnRecord`, `WarrantyRecord`):** Store only primitive/String IDs (`saleId`, `productIds`, `productId`) instead of serializing full `Sale` or `Product` object graphs across repositories.

#### Identified Violation (`V-03`)

| Field | Content |
| :--- | :--- |
| **ID** | `V-03` |
| **Pattern** | Low Coupling (`Bajo Acoplamiento`) |
| **Module** | Accessories (Requirement 1) |
| **Evidence** | `com.gamezone.service.AccessoryService`, constructor `AccessoryService(AccessoryRepository repository, List<Accessory> accessories)` (lines 19–22 of `AccessoryService.java`):<br>```java
public AccessoryService(AccessoryRepository repository, List<Accessory> accessories) {
    this.repository = repository;
    this.accessories = repository.loadAll();
}
```<br>invoked from `Main.java` (line 40): `new AccessoryService(accessoryRepository, accessoryRepository.loadAll());` |
| **Explanation** | Unlike `ProductService(ProductRepository)`, `PersonService(PersonRepository)`, and `PromotionService(PromotionRepository)`, `AccessoryService` only exposes a 2-argument constructor requiring the caller (`Main`) to pass `List<Accessory> accessories`, which the constructor immediately ignores in favor of calling `this.accessories = repository.loadAll()`. |
| **Consequence** | Forces callers (`Main`) to couple to `accessoryRepository.loadAll()` during service construction and triggers a redundant disk read of `data/Accessory.dat` at startup. |
| **Proposed Solution** | Apply **Low Coupling** by adding a clean single-argument constructor `public AccessoryService(AccessoryRepository repository)` that loads `this.accessories = repository.loadAll()` internally (retaining the 2-argument overload delegating to `this(repository)` for backward compatibility), and updating `Main` to call `new AccessoryService(accessoryRepository)`. |
| **Other Member's Code** | **Yes (Boundary Finding):** `AccessoryService` belongs to **Developer 2 (`Hectorguevara22`)**, while `Main` belongs to the **Technical Lead (`mlanderoc`)**. |

---

### 2.5 High Cohesion

- **Verdict:** **Violation** (1 violation found in `PromotionService` and `AccessoryService`; properly applied in `PersonService`, `ReturnService`, `WarrantyService`, and all repositories).

#### Correct Applications
- **`PersonService` (`PersonService.java`, lines 14–96):** Delegates all data and uniqueness validations to `PersonValidator.validatePersonData(...)` and `PersonValidator.validateUniquePerson(...)`, keeping its methods focused solely on person registration and lookup.
- **Repositories (`PersonRepository`, `AccessoryRepository`, `PromotionRepository`, `ReturnRepository`, `WarrantyRepository`):** Focused exclusively on binary file serialization/deserialization and wrapping I/O failures into `PersistenceException`.

#### Identified Violation (`V-04`)

| Field | Content |
| :--- | :--- |
| **ID** | `V-04` |
| **Pattern** | High Cohesion (`Alta Cohesión`) / Pure Fabrication (`Fabricación Pura`) |
| **Module** | Promotions (Requirement 2) & Accessories (Requirement 1) with Validations (Requirement 6) |
| **Evidence** | 1. `com.gamezone.service.PromotionService`, methods `registerCategoryDiscount` (lines 53–58), `registerBulkPurchaseDiscount` (lines 66–68), and `validatePromotionBase` (lines 74–90 of `PromotionService.java`):<br>```java
private void validatePromotionBase(String id, String name, LocalDate startDate, LocalDate endDate, double discountPercentage) {
    if (id == null || id.trim().isEmpty()) {
        throw new InvalidDataException("id", "El identificador de la promoción no puede estar vacío.");
    }
    ...
}
```<br>2. `com.gamezone.service.AccessoryService`, methods `registerCable` (lines 38–40) and `registerMemory` (lines 57–59 of `AccessoryService.java`):<br>```java
if (lengthInMeters <= 0) {
    throw new InvalidDataException("longitud", "La longitud del cable debe ser mayor que cero.");
}
``` |
| **Explanation** | While `ProductService`, `PersonService`, and `SaleService` delegate all input and rule validations to dedicated validators in `com.gamezone.validation`, `PromotionService` and `AccessoryService` still contain inline field/range/date validation logic mixed with service orchestration. |
| **Consequence** | Reduces the **High Cohesion** of `PromotionService` and `AccessoryService` and splits validation rules across `service` and `validation`. |
| **Proposed Solution** | Apply **High Cohesion** and **Pure Fabrication** by extracting promotion validation rules into `ProductValidator` / `SaleValidator` (or delegating accessory subtype checks to `ProductValidator.validateCableData`, `validateControllerData`, and `validateMemoryData` and promotion checks to `ProductValidator.validatePromotionBase` / `validateCategoryObjective` / `validateMinimumQuantity`) so `PromotionService` and `AccessoryService` remain purely focused on service coordination. |
| **Other Member's Code** | **Yes (Boundary Finding):** Connects with `ProductValidator` under **Developer 1 (`craulgomez`, `developer1-audit` `V-03`)**. |

---

### 2.6 Polymorphism

- **Verdict:** **Correct**

#### Justification & Evidence
- **`Person.getSummary()` (`Person.java` line 151; overridden in `Customer.java` lines 31–39 and `Seller.java` lines 78–88):** Allows callers to format summaries of any `Person` instance polymorphically.
- **`PromotionService.findBestPromotionFor(Sale)` (`PromotionService.java`, lines 107–120):** Evaluates `p.calculateDiscount(sale)` polymorphically across `PercentageDiscount`, `CategoryDiscount`, and `BulkPurchaseDiscount` without checking concrete promotion subclasses.
- **`WarrantyService.cancelWarranties(String, String)` (`WarrantyService.java`, line 118):** Computes `refundableCost += warranty.getAdditionalCost()` polymorphically (`0.0` for `BasicWarranty`, `10%` of console price for `ExtendedWarranty`).

---

### 2.7 Pure Fabrication

- **Verdict:** **Correct** *(with inline service validations covered under `V-04` High Cohesion above)*

#### Justification & Evidence
- **`PersonRepository`, `AccessoryRepository`, `PromotionRepository`, `ReturnRepository`, `WarrantyRepository` (`com.gamezone.persistence`):** All five repository classes are textbook **Pure Fabrications** created to encapsulate binary serialization (`data/customers.dat`, `data/sellers.dat`, `data/Accessory.dat`, `data/promotion.dat`, `data/returns.dat`, `data/warranties.dat`) outside the `Person`, `Accessory`, `Promotion`, `Return`, and `Warranty` domain entities.
- **`ReturnRecord` and `WarrantyRecord` (`com.gamezone.persistence`):** Pure Fabrication DTOs created to persist relational identifiers (`saleId`, `productIds`, `productId`) without serializing live domain object graphs.

---

### 2.8 Indirection

- **Verdict:** **Correct**

#### Justification & Evidence
- **Repositories & DTOs (`ReturnRepository` + `ReturnRecord`, `WarrantyRepository` + `WarrantyRecord`):** Decouple the service layer (`ReturnService`, `WarrantyService`) and domain model (`Return`, `Warranty`) from the physical storage layout on disk (`ObjectInputStream` / `ObjectOutputStream`).
- **`ReturnService` as an Intermediary Coordinator (`ReturnService.java`, lines 79–117):** Coordinates `SaleService`, `ProductService`, `AccessoryService`, `WarrantyService`, and `ReturnRepository` so that `ConsoleMenu` and `ReturnDialog` do not couple directly to multiple repositories or inventory/warranty cancellation internals when registering a return.

---

### 2.9 Protected Variations

- **Verdict:** **Correct**

#### Justification & Evidence
- **`Repository<T>` Contract Implementation:** `PersonRepository` (`implements Repository<Person>`), `AccessoryRepository` (`implements Repository<Accessory>`), `PromotionRepository` (`implements Repository<Promotion>`), `ReturnRepository` (`implements Repository<ReturnRecord>`), and `WarrantyRepository` (`implements Repository<WarrantyRecord>`) implement the common `Repository<T>` interface, protecting services against variations in how entities are stored.
- **Exception Wrapping in Repositories (Requirement 6):** All five repositories catch checked `IOException | ClassNotFoundException` internally and rethrow `PersistenceException`, shielding the service and presentation layers from low-level storage API variations.

---

## 3. Summary of Verdicts (Developer 2)

| # | GRASP Pattern | Verdict | Findings |
| :--- | :--- | :--- | :--- |
| 1 | **Information Expert** | **Violation** | `V-01`: `AccessoryService.findAcessoriesCOmpatibleWith` checks `getCompatibleConsoleIds().contains(...)` instead of calling `accessory.isCompatibleWith(consoleId)`.<br>`V-02`: `ReturnService.findInSale` iterates `sale.getProducts()` externally instead of delegating to `Sale.findProductById(productId)`. |
| 2 | **Creator** | **Correct** | Properly applied across `PersonService`, `AccessoryService`, `PromotionService`, `ReturnService`, and `WarrantyService`. |
| 3 | **Controller** | **Correct** | Services act as use-case facade controllers behind `ConsoleMenu` and Swing UI components. |
| 4 | **Low Coupling** | **Violation** | `V-03`: `AccessoryService` constructor requires an unused `List<Accessory>` parameter from `Main`, causing redundant repository loading coupling. |
| 5 | **High Cohesion** | **Violation** | `V-04`: `PromotionService` and `AccessoryService` retain inline input/range validation logic instead of delegating to `com.gamezone.validation`. |
| 6 | **Polymorphism** | **Correct** | Properly applied in `Person.getSummary()`, `PromotionService.findBestPromotionFor`, and `WarrantyService.cancelWarranties`. |
| 7 | **Pure Fabrication** | **Correct** | Repositories and DTO records (`ReturnRecord`, `WarrantyRecord`) isolate persistence concerns outside the domain model. |
| 8 | **Indirection** | **Correct** | Repositories and `ReturnService` decouple UI and domain components from storage and cross-module workflows. |
| 9 | **Protected Variations** | **Correct** | `Repository<T>` interface and `PersistenceException` wrapping shield callers from storage variations. |

---

## 4. Detailed Class & Method Verification Matrix

| Class | Requirement / Module | Audited Methods | GRASP Patterns Evaluated | Status |
| :--- | :--- | :--- | :--- | :--- |
| `Person` | Taller 1 — People | Constructor, `getId`, `setId`, `getFirstName`, `setFirstName`, `getLastName`, `setLastName`, `getPhoneNumber`, `setPhoneNumber`, `getFullName`, `getSummary` (abstract) | Information Expert (`getFullName`), Polymorphism (`getSummary`), Low Coupling | Correct |
| `Customer` | Taller 1 — People | Constructor, `getEmail`, `setEmail`, `getSummary` | Information Expert, Polymorphism (`@Override getSummary`), High Cohesion | Correct |
| `Seller` | Taller 1 — People | Constructor, `getEmployeeCode`, `setEmployeeCode`, `getWorkShift`, `setWorkShift`, `getSummary` | Information Expert, Polymorphism (`@Override getSummary`), High Cohesion | Correct |
| `PersonRepository` | Taller 1 / RE6 — People | `save`, `findAll`, `findById`, `saveAll`, `loadAllCustomers`, `loadAllSellers`, `saveAllCustomers`, `saveAllSellers` | Pure Fabrication, Indirection, Protected Variations (`Repository<Person>`), Creator (`PersistenceException`) | Correct |
| `PersonService` | Taller 1 / RE6 — People | Constructor, `registerCustomer`, `registerSeller`, `findCustomerById`, `findSellerById`, `listAllCustomers`, `listAllSellers` | Creator (`Customer`, `Seller`), Controller, High Cohesion (delegates to `PersonValidator`) | Correct |
| `AccessoryRepository` | RE1 / RE6 — Accessories | `save`, `findAll`, `findById`, `saveAll`, `loadAll` | Pure Fabrication, Indirection, Protected Variations (`Repository<Accessory>`) | Correct |
| `AccessoryService` | RE1 / RE5-A4 / RE6 — Accessories | Constructor, `findById`, `registerCable`, `registerController`, `registerMemory`, `listAllaccessories`, `listAccesoriesByType`, `findAcessoriesCOmpatibleWith`, `updateStock`, `restoreStock` | Information Expert, Creator, Low Coupling, High Cohesion | **Violations (`V-01`, `V-03`, `V-04`)**: `findAcessoriesCOmpatibleWith` bypasses `accessory.isCompatibleWith`; constructor requires redundant `List<Accessory>`; inline subtype validations in `registerCable` and `registerMemory` |
| `PromotionRepository` | RE2 / RE6 — Promotions | `save`, `findAll`, `findById`, `saveAll`, `loadAll` | Pure Fabrication, Indirection, Protected Variations (`Repository<Promotion>`) | Correct |
| `PromotionService` | RE2 / RE5-A1 / RE6 — Promotions | Constructor, `findById`, `registerPercentageDiscount`, `registerCategoryDiscount`, `registerBulkPurchaseDiscount`, `validatePromotionBase`, `listAllPromotions`, `listActivePromotions`, `findBestPromotionFor` | Information Expert (`findBestPromotionFor`), Creator, Polymorphism, High Cohesion | **Violation (`V-04`)**: `validatePromotionBase`, `registerCategoryDiscount`, and `registerBulkPurchaseDiscount` keep inline validation logic instead of delegating to `com.gamezone.validation` |
| `ReturnRecord` | RE3 — Returns | Constructor, getters | Pure Fabrication, Low Coupling (DTO with raw IDs) | Correct |
| `ReturnRepository` | RE3 / RE6 — Returns | `save`, `findAll`, `findById`, `saveAll`, `loadAll` | Pure Fabrication, Indirection, Protected Variations (`Repository<ReturnRecord>`) | Correct |
| `ReturnService` | RE3 / RE5-A4/A6/A7 / RE6 — Returns | Constructors, `registerReturn`, `viewAllReturns`, `viewReturnsByCustomer`, `viewReturnsBySale`, `calculateMonthlySales`, `calculateMonthlyReturns`, `generateMonthlyBalance`, `findInSale`, `toRecord`, `toReturn` | Information Expert, Creator (`Return`, `ReturnRecord`), Controller, Indirection | **Violation (`V-02`)**: `findInSale(Sale, String)` iterates `sale.getProducts()` externally instead of delegating to `Sale` |
| `WarrantyRecord` | RE4 — Warranties | Constructor, getters | Pure Fabrication, Low Coupling (DTO with raw IDs) | Correct |
| `WarrantyRepository` | RE4 / RE6 — Warranties | `save`, `findAll`, `findById`, `saveAll`, `loadAll` | Pure Fabrication, Indirection, Protected Variations (`Repository<WarrantyRecord>`) | Correct |
| `WarrantyService` | RE4 / RE5-A2/A7 — Warranties | Constructor, `assignBasicWarranty`, `assignExtendedWarranty`, `findWarrantyByProduct`, `listAllWarranties`, `listActiveWarranties`, `listWarrantiesExpiringSoon`, `cancelWarranties`, `save`, `toRecord`, `toWarranty`, `resolveSaleById` | Information Expert (`listActiveWarranties`), Creator (`BasicWarranty`, `ExtendedWarranty`), Low Coupling (acyclic wiring), Polymorphism (`cancelWarranties`) | Correct |
