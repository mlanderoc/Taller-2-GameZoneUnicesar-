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
