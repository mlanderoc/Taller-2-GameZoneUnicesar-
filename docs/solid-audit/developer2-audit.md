# Individual SOLID Audit Report — Developer 2 (`Hectorguevara22`)

- **Auditor:** Héctor Guevara (`Hectorguevara22`)
- **Role:** Developer 2 — Person Module, Persistence Repositories, Business Services, and Validator/Exception Integration
- **Evaluated Branch & Commit:** `develop` (`1bfc5f6`)
- **Requirement:** Requirement 8 — Phase 1: SOLID Principles Audit

---

## 1. Audit Scope

In accordance with Section 4.1 of the Requirement 8 specification, `TEAM.md`, and `docs/grasp-refactor-log.md`, this audit covers all classes, repositories, services, and methods developed or modified under Developer 2's responsibility across Taller 1 and Requirements 1, 2, 3, 4, 6, and 7:

| Module / Requirement | Audited Classes & Methods | Package |
| :--- | :--- | :--- |
| **People (Taller 1)** | `Person` (`getId`, `getFirstName`, `getLastName`, `getPhone`, `getFullName`, `getSummary`), `Customer` (`getEmail`, `setEmail`, `getSummary`), `Seller` (`getEmployeeCode`, `getShift`, `getSummary`), `PersonRepository` (`saveAllCustomers`, `loadAllCustomers`, `saveAllSellers`, `loadAllSellers`), `PersonService` (`registerCustomer`, `registerSeller`, `findCustomerById`, `findSellerById`, `listAllCustomers`, `listAllSellers`) | `com.gamezone.model`, `com.gamezone.persistence`, `com.gamezone.service` |
| **Accessories Persistence & Service (Req. 1 & Req. 7)** | `AccessoryRepository` (`saveAll`, `loadAll`), `AccessoryService` (`AccessoryService(AccessoryRepository)`, `findById`, `registerCable`, `registerController`, `registerMemory`, `listAllaccessories`, `listAccesoriesByType`, `findAcessoriesCOmpatibleWith`, `updateStock`, `restoreStock`) | `com.gamezone.persistence`, `com.gamezone.service` |
| **Promotions Persistence & Service (Req. 2 & Req. 7)** | `PromotionRepository` (`saveAll`, `loadAll`), `PromotionService` (`findById`, `registerPercentageDiscount`, `registerCategoryDiscount`, `registerBulkPurchaseDiscount`, `validatePromotionBase`, `listAllPromotions`, `listActivePromotions`, `findBestPromotionFor`) | `com.gamezone.persistence`, `com.gamezone.service` |
| **Returns Persistence, Service & Monthly Balance (Req. 3, Req. 5 & Req. 7)** | `ReturnRecord`, `ReturnRepository` (`saveAll`, `loadAll`), `ReturnService` (`registerReturn`, `viewAllReturns`, `viewReturnsByCustomer`, `viewReturnsBySale`, `calculateMonthlySales`, `calculateMonthlyReturns`, `generateMonthlyBalance`, `findInSale`, `toRecord`, `toReturn`), `Sale.findProductById(String)` | `com.gamezone.model`, `com.gamezone.persistence`, `com.gamezone.service` |
| **Warranties Persistence & Service (Req. 4 & Req. 5)** | `WarrantyRecord`, `WarrantyRepository` (`saveAll`, `loadAll`), `WarrantyService` (`assignBasicWarranty`, `assignExtendedWarranty`, `findWarrantyByProduct`, `listAllWarranties`, `listActiveWarranties`, `listWarrantiesExpiringSoon`, `cancelWarranties`, `save`, `toRecord`, `toWarranty`, `resolveSaleById`) | `com.gamezone.persistence`, `com.gamezone.service` |
| **Exceptions & Validators Usage in Services/Repositories (Req. 6 & Req. 7)** | Wrapping of `IOException` / `ClassNotFoundException` into `PersistenceException` across `PersonRepository`, `AccessoryRepository`, `PromotionRepository`, `ReturnRepository`, `WarrantyRepository`; invocation of `PersonValidator`, `ProductValidator`, and `SaleValidator` in `PersonService`, `AccessoryService`, `PromotionService`, and `ReturnService` | `com.gamezone.persistence`, `com.gamezone.service` |

---

## 2. Evaluation by SOLID Principle

### 2.1 Single Responsibility Principle (SRP)

- **Overall Verdict for Audited Scope:** **Violation** (1 violation identified in `WarrantyService` and 1 boundary finding reported on `PromotionService` / `ProductValidator`; remaining classes are **Correct**).

#### Identified Violations

| Field | Content |
| :--- | :--- |
| **ID** | `V-01` |
| **Principle** | Single Responsibility Principle (SRP) |
| **Module** | Warranties Persistence & Service (Requirement 4 & Requirement 5 Adjustment A2) |
| **Evidence** | Class `com.gamezone.service.WarrantyService`, method `resolveSaleById(String saleId, Product fallbackProduct)` (`src/main/java/com/gamezone/service/WarrantyService.java`, lines 163–187):<br>`private Sale resolveSaleById(String saleId, Product fallbackProduct) {`<br>`    for (SaleRecord saleRecord : saleRepository.loadAll()) {`<br>`        if (saleRecord.getSaleId().equals(saleId)) {`<br>`            List<Product> products = new ArrayList<>();`<br>`            for (String pid : saleRecord.getProductIds()) {`<br>`                Product item = productService.findById(pid);`<br>`                if (item != null) { products.add(item); }`<br>`            }`<br>`            ...`<br>`            Sale sale = new Sale(saleRecord.getSaleId(), saleRecord.getDate(), null, null, products);`<br>`            sale.setSubtotal(saleRecord.getSubtotal());`<br>`            sale.setAppliedPromotionName(saleRecord.getAppliedPromotionName());`<br>`            sale.setDiscountAmount(saleRecord.getDiscountAmount());`<br>`            return sale;`<br>`        }`<br>`    }`<br>`    return null;`<br>`}` |
| **Explanation** | `WarrantyService` has two distinct reasons to change: (1) managing warranty business rules (`assignBasicWarranty`, `assignExtendedWarranty`, `cancelWarranties`, `listActiveWarranties`, `listWarrantiesExpiringSoon`), and (2) reconstructing `Sale` domain objects from raw `SaleRecord` persistence DTOs in `resolveSaleById`. Reconstructing a `Sale` from a `SaleRecord` is a sale-domain mapping responsibility that duplicates `SaleService.toSale(SaleRecord)`. |
| **Consequence** | Whenever `SaleRecord` or `Sale` reconstruction rules change (for example, how products, discounts, or extended warranty costs are restored on a `Sale`), `WarrantyService` must be modified alongside `SaleService`, risking inconsistent `Sale` objects across the two services. |
| **Proposed Solution** | Extract the `SaleRecord`-to-`Sale` resolution responsibility out of `WarrantyService` into a dedicated helper/lookup collaborator (or delegate to a `SaleCatalogLookup` / `SaleRecordResolver` component) so `WarrantyService` is solely responsible for warranty lifecycle operations. |
| **Other Member's Code** | **No** — `WarrantyService` is under Developer 2's responsibility (though it interacts with `SaleRecord` and `Sale` from the Technical Lead's module). |

#### Boundary SRP Observation Reported to Technical Lead

- **`PromotionService` Validation Delegation (`src/main/java/com/gamezone/service/PromotionService.java`, lines 52, 60, 67):** `PromotionService` delegates promotional rule validation (`validatePromotionBase`, `validateCategoryObjective`, `validateMinimumQuantity`) to `ProductValidator` (`com.gamezone.validation.ProductValidator`, owned by Developer 1). As reported by Developer 1 (`V-01`), placing promotion rules inside `ProductValidator` violates SRP in `ProductValidator`. Once Developer 1 extracts `PromotionValidator`, Developer 2 will update `PromotionService` to invoke `PromotionValidator`.

#### Compliant Classes Under SRP (`Correct`)

- **`Person`, `Customer`, `Seller` (`com.gamezone.model`):** `Person` (`Person.java` lines 10–139), `Customer` (`Customer.java` lines 11–79), and `Seller` (`Seller.java` lines 11–98) encapsulate only personal, customer-contact, and staff-shift attributes and summaries.
- **`PersonRepository`, `AccessoryRepository`, `PromotionRepository`, `ReturnRepository`, `WarrantyRepository` (`com.gamezone.persistence`):** Each repository class has a single responsibility: binary file serialization and deserialization (`saveAll` / `loadAll`) for its respective entity or DTO record, wrapping low-level I/O failures into `PersistenceException`.
- **`ReturnRecord` and `WarrantyRecord` (`com.gamezone.persistence`):** Pure immutable data transfer objects holding flat identifiers for persistence (`ReturnRecord.java` lines 13–101, `WarrantyRecord.java` lines 10–77).
- **`PersonService`, `AccessoryService`, `PromotionService`, `ReturnService` (`com.gamezone.service`):** After the Requirement 7 GRASP refactorings (`V-03`, `V-04`, `V-05`), each of these services coordinates only the use cases of its respective module while delegating input validations to the `validation` layer and entity queries (`isCompatibleWith`, `findProductById`) to the `model` layer.
- **`Sale.findProductById(String)` (`com.gamezone.model.Sale`, lines 204–214):** Cohesive query method on `Sale` that searches `this.products` by ID.

---

### 2.2 Open/Closed Principle (OCP)

- **Overall Verdict for Audited Scope:** **Violation** (2 violations identified in services owned by Developer 2: `V-02` in `AccessoryService` and `V-03` in `WarrantyService`; `Person` summary formatting and `PromotionService.findBestPromotionFor` are **Correct**).

#### Identified Violations

| Field | Content |
| :--- | :--- |
| **ID** | `V-02` |
| **Principle** | Open/Closed Principle (OCP) |
| **Module** | Accessories Persistence & Service (Requirement 1) |
| **Evidence** | Class `com.gamezone.service.AccessoryService`, method `listAccesoriesByType(String type)` (`src/main/java/com/gamezone/service/AccessoryService.java`, lines 74–76):<br>`public List<Accessory> listAccesoriesByType(String type) {`<br>`    return accessories.stream().filter(accessory -> accessory.getClass().getSimpleName().equalsIgnoreCase(type)).collect(Collectors.toList());`<br>`}` |
| **Explanation** | `AccessoryService.listAccesoriesByType(String)` inspects the runtime Java class name via reflection (`accessory.getClass().getSimpleName().equalsIgnoreCase(type)`) instead of invoking an abstract polymorphic method on `Accessory`. If a new `Accessory` subtype is added whose domain type key differs from its simple Java class name, or if an existing subtype is subclassed/wrapped, `listAccesoriesByType` fails to match it unless modified. |
| **Consequence** | Filtering accessories by type is coupled to low-level Java class identifiers rather than domain polymorphism, breaking OCP whenever subtype naming or hierarchy extensions diverge from `Class.getSimpleName()`. |
| **Proposed Solution** | Coordinate with Developer 1 (`craulgomez`) to add `public abstract String getAccessoryType()` and `public boolean matchesAccessoryType(String type)` on `Accessory`, and refactor `AccessoryService.listAccesoriesByType(String)` to delegate to `accessory.matchesAccessoryType(type)`. |
| **Other Member's Code** | **Yes — Developer 1 (`craulgomez`):** `Accessory`, `Controller`, `Cable`, and `Memory` (`com.gamezone.model`) must define and override `getAccessoryType()` / `matchesAccessoryType(String)`. |

| Field | Content |
| :--- | :--- |
| **ID** | `V-03` |
| **Principle** | Open/Closed Principle (OCP) |
| **Module** | Warranties Persistence & Service (Requirement 4) |
| **Evidence** | Class `com.gamezone.service.WarrantyService`, method `toWarranty(WarrantyRecord record)` (`src/main/java/com/gamezone/service/WarrantyService.java`, lines 156–160):<br>`if ("Garantía Extendida".equals(record.getWarrantyType())) {`<br>`    return new ExtendedWarranty(record.getWarrantyId(), product, sale, record.getStartDate());`<br>`} else {`<br>`    return new BasicWarranty(record.getWarrantyId(), product, sale, record.getStartDate());`<br>`}` |
| **Explanation** | `WarrantyService.toWarranty(WarrantyRecord)` uses a hardcoded conditional branch comparing `record.getWarrantyType()` against the literal `"Garantía Extendida"` to decide which concrete `Warranty` subclass to instantiate. Adding a new `Warranty` subclass (for example, a manufacturer or promotional warranty) forces modifying `WarrantyService.toWarranty` to add another `else if` branch. |
| **Consequence** | `WarrantyService` is not closed for modification against new `Warranty` types, and any change to the warranty type label in `ExtendedWarranty.getWarrantyType()` silently causes `toWarranty` to reconstruct extended warranties as `BasicWarranty` instances. |
| **Proposed Solution** | Coordinate with Developer 1 (`craulgomez`) to centralize warranty type constants and subtype creation in `Warranty.createFromType(String warrantyType, String warrantyId, Product product, Sale sale, LocalDate startDate)`, and refactor `WarrantyService.toWarranty(WarrantyRecord)` to delegate directly to `Warranty.createFromType(...)`. |
| **Other Member's Code** | **Yes — Developer 1 (`craulgomez`):** `Warranty`, `BasicWarranty`, and `ExtendedWarranty` (`com.gamezone.model`) must expose type constants and `Warranty.createFromType(...)`. |

#### Compliant Classes Under OCP (`Correct`)

- **`Person`, `Customer`, `Seller` (`com.gamezone.model`):** `Person` (`Person.java` line 137) declares `public abstract String getSummary();`, which `Customer` (`Customer.java` lines 74–77) and `Seller` (`Seller.java` lines 92–96) override polymorphically. Any new `Person` subtype can be added without modifying callers of `getSummary()`.
- **`PromotionService.findBestPromotionFor(Sale)` (`com.gamezone.service.PromotionService`, lines 85–98):** Iterates over `List<Promotion>` and invokes the polymorphic `p.calculateDiscount(sale)` method without any `instanceof` or type-switching conditionals. New `Promotion` subclasses are evaluated automatically.

---

### 2.3 Liskov Substitution Principle (LSP)

- **Overall Verdict for Audited Scope:** **Correct** (All inheritance and interface implementations under Developer 2's responsibility satisfy their supertype behavioral contracts).

#### Evidence of Compliance (`Correct`)

1. **`Person` Hierarchy (`Person` ← `Customer`, `Seller`):**
   - `Person` (`src/main/java/com/gamezone/model/Person.java`, lines 10–139) defines `getId()`, `getFirstName()`, `getLastName()`, `getPhone()`, `getFullName()`, and abstract `getSummary()`.
   - `Customer` (`Customer.java` lines 11–79) and `Seller` (`Seller.java` lines 11–98) extend `Person` and implement `getSummary()` returning a non-null formatted string without weakening postconditions or throwing unexpected exceptions. Both subclasses are substituted as `Person` instances seamlessly inside `PersonValidator.validateUniquePerson(List<? extends Person>, String)` (`PersonValidator.java` lines 95–104) and `PersonValidator.validatePersonExists(Person, String)` (`PersonValidator.java` lines 82–86).
2. **`Repository<T>` Implementations (`PromotionRepository`, `ReturnRepository`, `WarrantyRepository`):**
   - `PromotionRepository` (`PromotionRepository.java` lines 15–52), `ReturnRepository` (`ReturnRepository.java` lines 14–51), and `WarrantyRepository` (`WarrantyRepository.java` lines 13–50) implement `Repository<T>` (`saveAll(List<T>)` and `loadAll()`), returning an empty `ArrayList<>()` when the backing `.dat` file does not yet exist and throwing `PersistenceException` on I/O failure, honoring the interface contract uniformly.

---

### 2.4 Interface Segregation Principle (ISP)

- **Overall Verdict for Audited Scope:** **Violation** (1 boundary violation identified across service collaborators: `V-04`; `Repository<T>` is **Correct**).

#### Identified Violations

| Field | Content |
| :--- | :--- |
| **ID** | `V-04` |
| **Principle** | Interface Segregation Principle (ISP) |
| **Module** | Warranties & Returns Services (Requirements 3, 4 & 5) |
| **Evidence** | 1. Class `com.gamezone.service.WarrantyService` (`src/main/java/com/gamezone/service/WarrantyService.java`, lines 27, 37–41, 150, 168):<br>`private final ProductService productService;`<br>`public WarrantyService(WarrantyRepository repository, SaleRepository saleRepository, ProductService productService)`<br>*(only invokes `productService.findById(...)` at lines 150 and 168)*<br>2. Class `com.gamezone.service.ReturnService` (`src/main/java/com/gamezone/service/ReturnService.java`, lines 29, 42–66, 80, 166, 231):<br>`private final SaleService saleService;`<br>`public ReturnService(ReturnRepository returnRepository, SaleService saleService, ProductService productService, AccessoryService accessoryService, WarrantyService warrantyService)`<br>*(only invokes `saleService.findSaleById(...)` at lines 80, 231 and `saleService.viewAllSales()` at line 166)* |
| **Explanation** | `WarrantyService` only needs a single read-only operation (`findById(String)`) to resolve `Product` references, yet it is forced to depend on the entire concrete `ProductService` class (which also exposes `registerVideoGame`, `registerConsole`, `listAllProducts`, `updateStock`, and `restoreStock`). Likewise, `ReturnService` only needs read-only sale queries (`findSaleById(String)` and `viewAllSales()`) from `SaleService`, yet it depends on the full concrete `SaleService` class (which also exposes `registerSale`, `viewSalesByCustomer`, `viewSalesBySeller`, and `setWarrantyService`). |
| **Consequence** | Clients are coupled to broad service contracts containing write and registration operations they never call, violating ISP and increasing ripple effects when unrelated methods in `ProductService` or `SaleService` change. |
| **Proposed Solution** | Introduce segregated interfaces `ProductCatalogLookup` (`Product findById(String id);`, created by Developer 1 and implemented by `ProductService` and `AccessoryService`) and `SaleQueryService` (`Sale findSaleById(String saleId); List<Sale> viewAllSales();`, created by the Technical Lead and implemented by `SaleService`), and update `WarrantyService` and `ReturnService` to depend on those narrow interfaces while preserving overloaded constructors for backward compatibility. |
| **Other Member's Code** | **Yes — Developer 1 (`craulgomez`) & Technical Lead (`mlanderoc`):** `ProductService` (Developer 1) must implement `ProductCatalogLookup`, and `SaleService` (Technical Lead) must implement `SaleQueryService`. |

#### Compliant Interfaces Under ISP (`Correct`)

- **`Repository<T>` (`com.gamezone.persistence.Repository`, lines 5–8):** Defines only two cohesive persistence operations (`void saveAll(List<T> items)` and `List<T> loadAll()`), both of which are used by every service that persists a collection of entities or records.
