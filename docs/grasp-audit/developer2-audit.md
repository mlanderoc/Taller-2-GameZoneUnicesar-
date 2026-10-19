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
