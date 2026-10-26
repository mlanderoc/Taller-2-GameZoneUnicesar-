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
