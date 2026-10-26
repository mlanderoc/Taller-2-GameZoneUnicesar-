# Individual SOLID Audit Report — Developer 1 (`craulgomez`)

- **Auditor:** Carlos Gomez (`craulgomez`)
- **Role:** Developer 1 — Domain Models, Hierarchies, Custom Exceptions, Validators, and Product Module
- **Evaluated Branch & Commit:** `develop` (`1bfc5f6`)
- **Requirement:** Requirement 8 — Phase 1: SOLID Principles Audit

---

## 1. Audit Scope

In accordance with Section 4.1 of the Requirement 8 specification, `TEAM.md`, and `docs/grasp-refactor-log.md`, this audit covers all classes, hierarchies, packages, and methods developed or modified under Developer 1's responsibility across Taller 1 and Requirements 1, 2, 3, 4, 6, and 7:

| Module / Requirement | Audited Classes & Methods | Package |
| :--- | :--- | :--- |
| **Products (Taller 1 & Req. 7)** | `Product` (`getId`, `getTitle`, `getPrice`, `getStock`, `updateStock`, `getDescription`, `getCategory`, `belongsToCategory`), `VideoGame` (`getPlatform`, `getGenre`, `getAgeRating`, `getDescription`, `getCategory`), `Console` (`getBrand`, `getModel`, `getGeneration`, `getDescription`, `getCategory`), `ProductRepository` (`saveAll`, `loadAll`), `ProductService` (`findById`, `registerVideoGame`, `registerConsole`, `listAllProducts`, `updateStock` — *excluding `restoreStock`, audited by the Technical Lead*) | `com.gamezone.model`, `com.gamezone.persistence`, `com.gamezone.service` |
| **Accessories Hierarchy (Req. 1 & Req. 7)** | `Accessory` (`getCompatibleConsoleIds`, `setCompatibleConsoleIds`, `isCompatibleWith`, `getDescription`, `getCategory`), `Controller` (`getConnectionType`, `setConnectionType`, `getDescription`), `Cable` (`getLengthInMeters`, `setLengthInMeters`, `getConnectorType`, `setConnectorType`, `getDescription`), `Memory` (`getStorageCapacityGb`, `setStorageCapacityGb`, `getMemoryType`, `setMemoryType`, `getDescription`) | `com.gamezone.model` |
| **Promotions Hierarchy (Req. 2 & Req. 7)** | `Promotion` (`getId`, `getName`, `getStartDate`, `getEndDate`, `isActive`, `calculateDiscount`), `PercentageDiscount` (`getDiscountPercentage`, `calculateDiscount`), `CategoryDiscount` (`getDiscountPercentage`, `getCategoryObjective`, `calculateDiscount`), `BulkPurchaseDiscount` (`getMinimumQuantity`, `getDiscountPercentage`, `calculateDiscount`) | `com.gamezone.model` |
| **Returns Domain & Return Window (Req. 3 & Req. 7)** | `Return` (`calculateRefundAmount`, `getDiscountRatio`, `setWarrantyRefundAmount`, `generateReturnReceipt`, getters/setters), `Sale.canBeReturned()`, `Sale.getDiscountRatio()` | `com.gamezone.model` |
| **Warranties Hierarchy (Req. 4)** | `Warranty` (`getDurationInMonths`, `getWarrantyType`, `getAdditionalCost`, `isActive`, `generateWarrantyCertificate`, getters), `BasicWarranty` (`getDurationInMonths`, `getWarrantyType`, `getAdditionalCost`), `ExtendedWarranty` (`getDurationInMonths`, `getWarrantyType`, `getAdditionalCost`) | `com.gamezone.model` |
| **Exceptions & Validation Layers (Req. 6 & Req. 7)** | `GameZoneException`, `ResourceNotFoundException`, `BusinessRuleException`, `InvalidDataException`, `PersistenceException`, `ProductValidator` (`validateProductData`, `validateStockAvailability`, `validateProductExists`, `validateUniqueProduct`, `validateCableData`, `validateMemoryData`, `validatePromotionBase`, `validateCategoryObjective`, `validateMinimumQuantity`), `PersonValidator` (`validatePersonData`, `validateSellerData`, `validatePersonExists`, `validateUniquePerson`), `SaleValidator` (`validateSaleData`, `validateSaleExists`, `validateReturnData`) | `com.gamezone.exceptions`, `com.gamezone.validation` |

---

## 2. Evaluation by SOLID Principle

### 2.1 Single Responsibility Principle (SRP)

- **Overall Verdict for Audited Scope:** **Violation** (2 violations identified: `V-01` and `V-02`; remaining classes are **Correct**).

#### Identified Violations

| Field | Content |
| :--- | :--- |
| **ID** | `V-01` |
| **Principle** | Single Responsibility Principle (SRP) |
| **Module** | Exceptions and Validations (Requirement 6) & GRASP Refactoring (Requirement 7) |
| **Evidence** | Class `com.gamezone.validation.ProductValidator`, methods `validatePromotionBase` (lines 124–141), `validateCategoryObjective` (lines 150–157), and `validateMinimumQuantity` (lines 165–169) in `src/main/java/com/gamezone/validation/ProductValidator.java`:<br>`public static void validatePromotionBase(String id, String name, LocalDate startDate, LocalDate endDate, double discountPercentage, Promotion existingPromotion) { ... }`<br>`public static void validateCategoryObjective(String categoryObjective) { ... }`<br>`public static void validateMinimumQuantity(int minimumQuantity) { ... }` |
| **Explanation** | During Requirement 7 (`V-05`), promotional campaign validations were moved out of `PromotionService` into `ProductValidator`. While that removed inline validation from the service, it gave `ProductValidator` two unrelated reasons to change: (1) changes to product and accessory catalog validation rules (`Product`, `Cable`, `Memory`, inventory stock), and (2) changes to promotional campaign validation rules (`Promotion` date ranges, percentage limits, allowed category names, and minimum bulk quantities). |
| **Consequence** | Any change to marketing promotion rules (for example, allowing a new promotional rule parameter or modifying percentage bounds) forces modifying and recompiling `ProductValidator`, a class whose primary responsibility is validating physical inventory products and accessories. |
| **Proposed Solution** | Extract promotional campaign validation methods (`validatePromotionBase`, `validateCategoryObjective`, `validateMinimumQuantity`) into a dedicated `PromotionValidator` class inside `com.gamezone.validation` ( retaining thin delegating methods in `ProductValidator` only if needed for backward compatibility), and update `PromotionService` to invoke `PromotionValidator`. |
| **Other Member's Code** | **Yes — Developer 2 (`Hectorguevara22`):** `PromotionService` (`src/main/java/com/gamezone/service/PromotionService.java`, lines 52, 60, 67) calls `ProductValidator` for promotion validations and must be updated to call `PromotionValidator`. |

| Field | Content |
| :--- | :--- |
| **ID** | `V-02` |
| **Principle** | Single Responsibility Principle (SRP) |
| **Module** | Products Module (Taller 1) |
| **Evidence** | Class `com.gamezone.service.ProductService`, method `updateStock(String productId, int quantity)` (lines 116–134) in `src/main/java/com/gamezone/service/ProductService.java`:<br>`product.updateStock(quantity);`<br>`List<Product> products = repository.loadAll();`<br>`for (int i = 0; i < products.size(); i++) {`<br>`    if (products.get(i).getId().equals(productId)) {`<br>`        products.set(i, product);`<br>`        break;`<br>`    }`<br>`}`<br>`repository.saveAll(products);` |
| **Explanation** | `ProductService.updateStock` combines two distinct responsibilities: (1) coordinating the business operation of validating and updating the stock of a product in the service's in-memory catalog (`this.products`), and (2) manually reloading a separate snapshot from `repository.loadAll()`, searching by index, replacing the element, and saving that secondary list. In contrast, `registerVideoGame` (lines 69–70) and `registerConsole` (lines 97–98) directly persist `repository.saveAll(this.products)`. |
| **Consequence** | If the persistence synchronization strategy or internal list structure changes, `ProductService.updateStock` must be modified separately from the rest of `ProductService`, and any unpersisted in-memory state in `this.products` risks being overwritten by the secondary `repository.loadAll()` snapshot. |
| **Proposed Solution** | Remove the manual snapshot reload and index-replacement loop in `ProductService.updateStock(String, int)` so that after `product.updateStock(quantity)` is executed on the catalog instance, the service simply persists `repository.saveAll(this.products)`. |
| **Other Member's Code** | **No** — `ProductService.updateStock` is exclusively under Developer 1's responsibility. |

#### Compliant Classes Under SRP (`Correct`)

- **`Product`, `VideoGame`, `Console`, `Accessory`, `Controller`, `Cable`, `Memory` (`com.gamezone.model`):** Each domain class encapsulates only the attributes, stock state, category identification, and descriptive formatting of its respective product or accessory entity (`Product.java` lines 9–144, `Accessory.java` lines 11–88).
- **`Promotion`, `PercentageDiscount`, `CategoryDiscount`, `BulkPurchaseDiscount` (`com.gamezone.model`):** Each class has a single reason to change: the mathematical rule for calculating its specific promotional discount over a `Sale` (`Promotion.java` lines 10–98, `CategoryDiscount.java` lines 10–67).
- **`Return` and `Sale.canBeReturned()` / `Sale.getDiscountRatio()` (`com.gamezone.model`):** `Return` (`Return.java` lines 13–175) is solely responsible for representing a product return and computing its proportional refund (`calculateRefundAmount`) and receipt (`generateReturnReceipt`), while `Sale.canBeReturned()` (lines 190–196) and `Sale.getDiscountRatio()` (lines 178–183) evaluate sale-level state.
- **`Warranty`, `BasicWarranty`, `ExtendedWarranty` (`com.gamezone.model`):** Each class represents warranty coverage duration, additional cost, active status, and certificate formatting (`Warranty.java` lines 10–131).
- **`ProductRepository` (`com.gamezone.persistence`):** Solely responsible for binary serialization (`saveAll`) and deserialization (`loadAll`) of `Product` lists in `data/products.dat` (`ProductRepository.java` lines 20–68).
- **`PersonValidator` and `SaleValidator` (`com.gamezone.validation`):** `PersonValidator` (`PersonValidator.java` lines 14–105) validates only `Person`/`Customer`/`Seller` data, and `SaleValidator` (`SaleValidator.java` lines 18–81) validates only `Sale` and `Return` preconditions.
- **`GameZoneException` Hierarchy (`com.gamezone.exceptions`):** Each exception class (`GameZoneException`, `ResourceNotFoundException`, `BusinessRuleException`, `InvalidDataException`, `PersistenceException`) represents a single semantic error category with its error code.

---

### 2.2 Open/Closed Principle (OCP)

- **Overall Verdict for Audited Scope:** **Violation** (2 boundary violations identified in hierarchies owned by Developer 1: `V-03` in the `Accessory` hierarchy and `V-04` in the `Warranty` hierarchy; `Product` category dispatch and `Promotion` discount calculation are **Correct**).

#### Identified Violations

| Field | Content |
| :--- | :--- |
| **ID** | `V-03` |
| **Principle** | Open/Closed Principle (OCP) |
| **Module** | Accessories Hierarchy (Requirement 1) |
| **Evidence** | Class `com.gamezone.model.Accessory` (`src/main/java/com/gamezone/model/Accessory.java`, lines 11–88) and subclasses `Controller` (`Controller.java`, lines 11–60), `Cable` (`Cable.java`, lines 10–83), and `Memory` (`Memory.java`, lines 10–82), in conjunction with `AccessoryService.listAccesoriesByType(String type)` (`src/main/java/com/gamezone/service/AccessoryService.java`, lines 74–76):<br>`public List<Accessory> listAccesoriesByType(String type) {`<br>`    return accessories.stream().filter(accessory -> accessory.getClass().getSimpleName().equalsIgnoreCase(type)).collect(Collectors.toList());`<br>`}` |
| **Explanation** | While `Product` was refactored in Requirement 7 (`V-02`) to expose `getCategory()` and `belongsToCategory(String)`, the `Accessory` hierarchy does not expose a polymorphic method (`getAccessoryType()` / `matchesAccessoryType(String)`) for identifying specific accessory subtypes (`Controller`, `Cable`, `Memory`). As a result, `AccessoryService.listAccesoriesByType(String)` relies on Java reflection (`accessory.getClass().getSimpleName()`) over concrete class names. This violates OCP because introducing an accessory subtype whose domain type identifier differs from its Java class identifier, or wrapping/subclassing an accessory, requires modifying the filtering logic instead of extending `Accessory`. |
| **Consequence** | Coupling accessory type filtering to `getClass().getSimpleName()` makes the service fragile to class renames (for example, `Memory` vs. `ExternalMemory`) and prevents polymorphic extension of accessory types without modifying client filtering code. |
| **Proposed Solution** | Add `public abstract String getAccessoryType();` and `public boolean matchesAccessoryType(String type)` in `Accessory`, implement `@Override public String getAccessoryType()` in `Controller` (`"Controller"`), `Cable` (`"Cable"`), and `Memory` (`"Memory"`), and delegate `AccessoryService.listAccesoriesByType(String)` to `accessory.matchesAccessoryType(type)`. |
| **Other Member's Code** | **Yes — Developer 2 (`Hectorguevara22`):** `AccessoryService.listAccesoriesByType(String)` (`src/main/java/com/gamezone/service/AccessoryService.java`, lines 74–76) consumes the new polymorphic contract. |

| Field | Content |
| :--- | :--- |
| **ID** | `V-04` |
| **Principle** | Open/Closed Principle (OCP) |
| **Module** | Warranties Hierarchy (Requirement 4) |
| **Evidence** | Class `com.gamezone.model.Warranty` (`src/main/java/com/gamezone/model/Warranty.java`, lines 10–131), `BasicWarranty` (`BasicWarranty.java`, lines 40–42), and `ExtendedWarranty` (`ExtendedWarranty.java`, lines 40–42), in conjunction with `WarrantyService.toWarranty(WarrantyRecord)` (`src/main/java/com/gamezone/service/WarrantyService.java`, lines 156–160):<br>`if ("Garantía Extendida".equals(record.getWarrantyType())) {`<br>`    return new ExtendedWarranty(record.getWarrantyId(), product, sale, record.getStartDate());`<br>`} else {`<br>`    return new BasicWarranty(record.getWarrantyId(), product, sale, record.getStartDate());`<br>`}` |
| **Explanation** | `BasicWarranty` and `ExtendedWarranty` return hardcoded string literals (`"Garantía Básica"` and `"Garantía Extendida"`) in `getWarrantyType()`, but the `Warranty` hierarchy provides no centralized factory method or type constants for reconstructing a `Warranty` subtype from its persisted type name. Consequently, `WarrantyService.toWarranty` hardcodes an `if/else` check on `"Garantía Extendida"`. Adding a new warranty subtype requires modifying `WarrantyService.toWarranty`. |
| **Consequence** | Every new `Warranty` subclass forces modifications in `WarrantyService` to add another `else if` branch, and any discrepancy in the Spanish literal `"Garantía Extendida"` silently defaults to `BasicWarranty`. |
| **Proposed Solution** | Define `public static final String TYPE = "Garantía Básica";` in `BasicWarranty` and `public static final String TYPE = "Garantía Extendida";` in `ExtendedWarranty`, and add a static factory method `Warranty.createFromType(String warrantyType, String warrantyId, Product product, Sale sale, LocalDate startDate)` in `Warranty` so `WarrantyService.toWarranty` delegates subtype instantiation without hardcoded conditionals. |
| **Other Member's Code** | **Yes — Developer 2 (`Hectorguevara22`):** `WarrantyService.toWarranty(WarrantyRecord)` (`src/main/java/com/gamezone/service/WarrantyService.java`, lines 156–160) will delegate to `Warranty.createFromType(...)`. |

#### Compliant Classes Under OCP (`Correct`)

- **`Product` and `CategoryDiscount` (`com.gamezone.model`):** Following Requirement 7 (`V-02`), `Product` defines `public abstract String getCategory()` and `public boolean belongsToCategory(String category)` (`Product.java` lines 119–131), overridden in `VideoGame` (`"VIDEOGAME"`), `Console` (`"CONSOLE"`), and `Accessory` (`"ACCESSORY"`). `CategoryDiscount.calculateDiscount(Sale)` (`CategoryDiscount.java` lines 55–66) is closed for modification and open for extension when new `Product` subclasses are added.
- **`Promotion` Hierarchy (`com.gamezone.model`):** `Promotion` (`Promotion.java` line 96) declares `public abstract double calculateDiscount(Sale sale)`, allowing `PercentageDiscount`, `CategoryDiscount`, and `BulkPurchaseDiscount` (and any future promotional strategy) to be evaluated polymorphically by `PromotionService.findBestPromotionFor(Sale)` without modifying the service.
- **`GameZoneException` Hierarchy (`com.gamezone.exceptions`):** `GameZoneException` (`GameZoneException.java` lines 10–57) provides an extensible base class for domain, validation, lookup, and persistence exceptions.

---

### 2.3 Liskov Substitution Principle (LSP)

- **Overall Verdict for Audited Scope:** **Correct** (All inheritance hierarchies under Developer 1's responsibility honor their base-class behavioral contracts without strengthening preconditions, weakening postconditions, or throwing unexpected unsupported-operation exceptions).

#### Evidence of Compliance (`Correct`)

1. **`Product` Hierarchy (`Product` ← `VideoGame`, `Console`, `Accessory` ← `Controller`, `Cable`, `Memory`):**
   - `Product` (`src/main/java/com/gamezone/model/Product.java`, lines 111–142) defines the behavioral contract for `getDescription()`, `getCategory()`, `belongsToCategory(String)`, `getPrice()`, `getStock()`, and `updateStock(int)`.
   - Every concrete subclass (`VideoGame.java` lines 95–107, `Console.java` lines 95–107, `Accessory.java` lines 74–87, `Controller.java` lines 55–58, `Cable.java` lines 76–82, `Memory.java` lines 76–81) implements `getDescription()` and `getCategory()` returning non-null strings and inherits `updateStock(int)` without altering its state invariant. Any `Product` reference in `Sale.calculateTotal()`, `CategoryDiscount.calculateDiscount(Sale)`, or `ConsoleMenu.listAllProducts()` can be substituted with any concrete subclass transparently.
2. **`Promotion` Hierarchy (`Promotion` ← `PercentageDiscount`, `CategoryDiscount`, `BulkPurchaseDiscount`):**
   - `Promotion` (`src/main/java/com/gamezone/model/Promotion.java`, lines 84–96) defines `isActive(LocalDate)` and `calculateDiscount(Sale)`.
   - `PercentageDiscount.calculateDiscount(Sale)` (`PercentageDiscount.java` lines 42–45), `CategoryDiscount.calculateDiscount(Sale)` (`CategoryDiscount.java` lines 55–66), and `BulkPurchaseDiscount.calculateDiscount(Sale)` (`BulkPurchaseDiscount.java` lines 59–66) all return a non-negative monetary discount (`>= 0.0` and `<= sale.getSubtotal()`) for any valid `Sale` without mutating the `Sale` object, honoring the base contract expected by `PromotionService.findBestPromotionFor(Sale)`.
3. **`Warranty` Hierarchy (`Warranty` ← `BasicWarranty`, `ExtendedWarranty`):**
   - `Warranty` (`src/main/java/com/gamezone/model/Warranty.java`, lines 27–33, 85–129) computes `endDate = startDate.plusMonths(getDurationInMonths())` and formats certificates using `getWarrantyType()` and `getAdditionalCost()`.
   - `BasicWarranty` (`BasicWarranty.java` lines 30–52: `6` months, `"Garantía Básica"`, `0.0`) and `ExtendedWarranty` (`ExtendedWarranty.java` lines 30–52: `12` months, `"Garantía Extendida"`, `getProduct().getPrice() * 0.10`) fulfill the positive duration and non-negative cost postconditions seamlessly.
4. **`GameZoneException` Hierarchy (`GameZoneException` ← `ResourceNotFoundException`, `BusinessRuleException`, `InvalidDataException`, `PersistenceException`):**
   - Every subclass provides a valid non-null `errorCode` (`ERR-NOT-FOUND`, `ERR-BUSINESS-RULE`, `ERR-INVALID-DATA`, `ERR-PERSISTENCE`) and descriptive Spanish message, allowing any caller catching `GameZoneException` to inspect `getMessage()` and `getErrorCode()` uniformly.

---

### 2.4 Interface Segregation Principle (ISP)

- **Overall Verdict for Audited Scope:** **Violation** (1 boundary violation identified in `ProductService`: `V-06` in Developer 1's report; `Repository<T>` and domain/validation classes are **Correct** or **Not Applicable**).

#### Identified Violations

| Field | Content |
| :--- | :--- |
| **ID** | `V-06` *(Reported to Technical Lead for boundary consolidation)* |
| **Principle** | Interface Segregation Principle (ISP) |
| **Module** | Products Module (Taller 1) & Service Layer Integration (Requirements 4 & 5) |
| **Evidence** | Class `com.gamezone.service.ProductService` (`src/main/java/com/gamezone/service/ProductService.java`, lines 14–149) and its consumer `WarrantyService` (`src/main/java/com/gamezone/service/WarrantyService.java`, lines 27, 37–41, 150, 168):<br>`public class ProductService { ... public Product findById(String id) ... public void registerVideoGame(...) ... public void registerConsole(...) ... public List<Product> listAllProducts() ... public void updateStock(...) ... public void restoreStock(...) }`<br>In `WarrantyService`:<br>`private final ProductService productService;`<br>`Product product = productService.findById(record.getProductId());` |
| **Explanation** | `ProductService` exposes a broad set of product management operations (registration of video games and consoles, full listing, stock updates, and stock restoration) without implementing a narrow read-only lookup interface. Clients such as `WarrantyService` only require `findById(String)` to resolve product references when reconstructing `WarrantyRecord` and `SaleRecord` instances, yet they are forced to depend on the entire concrete `ProductService` class and all its registration and stock-mutation methods. |
| **Consequence** | Collaborating services that only need catalog lookup by ID are coupled to write and stock-management operations they do not use, making it harder to substitute lightweight lookup stubs or unified catalog lookups across `ProductService` and `AccessoryService`. |
| **Proposed Solution** | Define a segregated read-only interface `ProductCatalogLookup` (`Product findById(String id);`) in `com.gamezone.service`, have `ProductService` (and `AccessoryService`) implement `ProductCatalogLookup`, and allow read-only clients such as `WarrantyService` to depend on `ProductCatalogLookup`. |
| **Other Member's Code** | **Yes — Developer 2 (`Hectorguevara22`):** `WarrantyService` (`src/main/java/com/gamezone/service/WarrantyService.java`, lines 27, 37) and `AccessoryService` (`src/main/java/com/gamezone/service/AccessoryService.java`, line 14). |

---

### 2.5 Dependency Inversion Principle (DIP)

- **Overall Verdict for Audited Scope:** **Violation** (1 violation identified in `ProductRepository` and `ProductService`: `V-05`).

#### Identified Violations

| Field | Content |
| :--- | :--- |
| **ID** | `V-05` |
| **Principle** | Dependency Inversion Principle (DIP) |
| **Module** | Products Module (Taller 1) |
| **Evidence** | 1. Class `com.gamezone.persistence.ProductRepository` (`src/main/java/com/gamezone/persistence/ProductRepository.java`, line 20):<br>`public class ProductRepository {` *(does not implement `Repository<Product>`)*<br>2. Class `com.gamezone.service.ProductService` (`src/main/java/com/gamezone/service/ProductService.java`, lines 17, 25–28):<br>`private final ProductRepository repository;`<br>`public ProductService(ProductRepository repository) { this.repository = repository; this.products = repository.loadAll(); }` |
| **Explanation** | Even though the persistence layer defines the generic abstraction `Repository<T>` (`src/main/java/com/gamezone/persistence/Repository.java`, lines 5–8) with `void saveAll(List<T> items)` and `List<T> loadAll()`, `ProductRepository` does not implement `Repository<Product>`, and the high-level module `ProductService` depends directly on the low-level concrete binary-file class `ProductRepository` rather than on `Repository<Product>`. |
| **Consequence** | `ProductService` is tightly coupled to binary file serialization (`data/products.dat`) in `ProductRepository`; it cannot be instantiated with an alternative `Repository<Product>` implementation (such as an in-memory or database repository) without modifying `ProductService` and `ProductRepository`. |
| **Proposed Solution** | Update `ProductRepository` to declare `public class ProductRepository implements Repository<Product>` with `@Override` annotations on `saveAll` and `loadAll`, and update `ProductService` so its field and primary constructor depend on the abstraction `Repository<Product>`. |
| **Other Member's Code** | **Yes — Developer 2 (`Hectorguevara22`) & Technical Lead (`mlanderoc`):** The same DIP pattern affects `AccessoryRepository`/`AccessoryService`, `PromotionService`, `ReturnService`, `WarrantyService`, and `SaleService` across the persistence-to-service boundary. |
