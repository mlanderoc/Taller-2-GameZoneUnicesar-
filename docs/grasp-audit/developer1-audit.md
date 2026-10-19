# Individual GRASP Audit Report — Developer 1 (Carlos Gomez)

- **Author:** Carlos Gomez (`craulgomez`)
- **Role:** Developer 1 — Domain Models, Hierarchies, Products Module, Exceptions & Validators
- **Audited Branch / Commit (`develop`):** `3f14c06`

---

## 1. Audit Scope

In accordance with Section 4 of Requirement 7 and `TEAM.md`, this audit covers all classes and methods developed under Developer 1's responsibility across Taller 1 and Requirements 1, 2, 3, 4, 5, and 6:

| Module (Requirement) | Classes Audited | Methods Audited |
| :--- | :--- | :--- |
| **Products (Taller 1)** | `Product` (abstract), `VideoGame`, `Console`, `ProductRepository`, `ProductService` *(except `restoreStock`)* | `Product`: constructor, getters/setters, `getDescription()`, `updateStock(int)`<br>`VideoGame`: constructor, getters/setters, `getDescription()`<br>`Console`: constructor, getters/setters, `getDescription()`<br>`ProductRepository`: `save(Product)`, `findAll()`, `findById(String)`, `saveAll(List<Product>)`, `loadAll()`<br>`ProductService`: constructor, `findById(String)`, `registerVideoGame(...)`, `registerConsole(...)`, `listAllProducts()`, `updateStock(String, int)` |
| **Accessories (Requirement 1)** | `Accessory` (abstract), `Controller`, `Cable`, `Memory` | `Accessory`: constructor, `getCompatibleConsoleIds()`, `setCompatibleConsoleIds(List<String>)`, `isCompatibleWith(String)`, `getDescription()`<br>`Controller`: constructor, `getConnectionType()`, `setConnectionType(String)`, `getDescription()`<br>`Cable`: constructor, `getLengthInMeters()`, `setLengthInMeters(double)`, `getConnectorType()`, `setConnectorType(String)`, `getDescription()`<br>`Memory`: constructor, `getStorageCapacityGb()`, `setStorageCapacityGb(int)`, `getMemoryType()`, `setMemoryType(String)`, `getDescription()` |
| **Promotions (Requirement 2 & RE5-A1)** | `Promotion` (abstract), `PercentageDiscount`, `CategoryDiscount`, `BulkPurchaseDiscount` | `Promotion`: constructor, getters/setters, `isActive(LocalDate)`, `calculateDiscount(Sale)`<br>`PercentageDiscount`: constructor, getters/setters, `calculateDiscount(Sale)`<br>`CategoryDiscount`: constructor, getters/setters, `calculateDiscount(Sale)`<br>`BulkPurchaseDiscount`: constructor, getters/setters, `calculateDiscount(Sale)` |
| **Returns (Requirement 3 & RE5-A5)** | `Return`, `Sale.canBeReturned()` | `Return`: constructor, getters/setters, `calculateRefundAmount()`, `getDiscountRatio()`, `generateReturnReceipt()`<br>`Sale`: `canBeReturned()` |
| **Warranties (Requirement 4)** | `Warranty` (abstract), `BasicWarranty`, `ExtendedWarranty` | `Warranty`: constructor, getters, `getDurationInMonths()`, `getWarrantyType()`, `getAdditionalCost()`, `isActive(LocalDate)`, `generateWarrantyCertificate()`<br>`BasicWarranty`: constructor, `getDurationInMonths()`, `getWarrantyType()`, `getAdditionalCost()`<br>`ExtendedWarranty`: constructor, `getDurationInMonths()`, `getWarrantyType()`, `getAdditionalCost()` |
| **Exceptions & Validations (Requirement 6)** | `GameZoneException`, `ResourceNotFoundException`, `BusinessRuleException`, `InvalidDataException`, `PersistenceException`, `ProductValidator`, `PersonValidator`, `SaleValidator` | All constructors and `getErrorCode()` in `com.gamezone.exceptions`; all static validation methods in `ProductValidator`, `PersonValidator`, and `SaleValidator` |

---

## 2. Evaluation by GRASP Pattern

### 2.1 Information Expert

- **Verdict:** **Violation** (1 violation found; properly applied in remaining domain classes).

#### Correct Applications
- **`Product.updateStock(int quantity)` (`Product.java`, lines 118–122):** `Product` owns the `stock` attribute and directly mutates it (`this.stock += quantity`).
- **`Accessory.isCompatibleWith(String consoleId)` (`Accessory.java`, lines 56–63):** `Accessory` owns `compatibleConsoleIds` and is the Information Expert to determine whether a given console ID is compatible.
- **`Promotion.isActive(LocalDate currentDate)` (`Promotion.java`, lines 110–112) & `Warranty.isActive(LocalDate date)` (`Warranty.java`, lines 107–110):** Both abstract base classes own `startDate` and `endDate` and directly evaluate date-range validity.
- **`Sale.canBeReturned()` (`Sale.java`, lines 177–183):** `Sale` owns the `date` attribute and evaluates whether `ChronoUnit.DAYS.between(date, LocalDate.now())` is between `0` and `30`.
- **`ExtendedWarranty.getAdditionalCost()` (`ExtendedWarranty.java`, lines 55–58):** Uses its associated `getProduct().getPrice() * 0.10` to compute the 10% surcharge.

#### Identified Violation (`V-01`)

| Field | Content |
| :--- | :--- |
| **ID** | `V-01` |
| **Pattern** | Information Expert (`Experto en Información`) |
| **Module** | Returns (Requirement 3 / Requirement 5 Adjustment A5) |
| **Evidence** | `com.gamezone.model.Return`, method `getDiscountRatio()` (lines 139–144 of `Return.java`):<br>```java
private double getDiscountRatio() {
    if (originalSale != null && originalSale.getSubtotal() > 0) {
        return originalSale.getDiscountAmount() / originalSale.getSubtotal();
    }
    return 0.0;
}
``` |
| **Explanation** | `Sale` is the Information Expert that owns both `subtotal` and `discountAmount`. However, `Return` pulls both raw attributes out of `originalSale` (`originalSale.getDiscountAmount()` and `originalSale.getSubtotal()`) to compute the sale's discount ratio on its own. |
| **Consequence** | `Return` is coupled to the internal arithmetic relationship between `subtotal` and `discountAmount` inside `Sale`. If `Sale` changes how discounts or item-level effective prices are stored or calculated, `Return` would have to be modified in tandem. |
| **Proposed Solution** | Apply **Information Expert** by moving `getDiscountRatio()` into `Sale` (`public double getDiscountRatio()`), and have `Return.getDiscountRatio()` (or `calculateRefundAmount()` and `generateReturnReceipt()`) delegate directly to `originalSale.getDiscountRatio()`. |
| **Other Member's Code** | **Yes (Boundary Finding):** Requires adding `getDiscountRatio()` to `Sale`, which is under the responsibility of the **Technical Lead (`mlanderoc`)**, while `Return` is under **Developer 1 (`craulgomez`)**. |

---

### 2.2 Creator

- **Verdict:** **Correct**

#### Justification & Evidence
- **`ProductService.registerVideoGame(...)` (`ProductService.java`, lines 56–71) and `ProductService.registerConsole(...)` (`ProductService.java`, lines 84–99):** `ProductService` aggregates, manages, and persists the collection of `Product` instances (`List<Product> products`) and receives the initialization data from the presentation layer. Under the **Creator** pattern, `ProductService` is the rightful creator of `VideoGame` and `Console` objects.
- **`ProductValidator`, `PersonValidator`, `SaleValidator` (`com.gamezone.validation`):** Have all-validating methods that instantiate `InvalidDataException`, `BusinessRuleException`, and `ResourceNotFoundException` because the validators hold the exact field names, resource IDs, and rule descriptions required to construct those exception objects.
- **`ProductRepository` (`ProductRepository.java`, lines 53–61 & 105–121):** Instantiates `PersistenceException` when catching `IOException | ClassNotFoundException` because it holds both the file path (`data/products.dat`) and the underlying `Throwable` cause.

---

### 2.3 Controller

- **Verdict:** **Not Applicable**

#### Justification
The classes under Developer 1's responsibility belong to the domain model (`com.gamezone.model`), cross-cutting exceptions (`com.gamezone.exceptions`), stateless validators (`com.gamezone.validation`), and the product persistence/service vertical (`ProductRepository`, `ProductService`). System events from the user interface (`ConsoleMenu` and Swing dialogs) are received and coordinated by the UI/service controllers audited by the Technical Lead and Developer 2. While `ProductService` acts as a domain facade for product operations, user-interaction event handling does not take place in Developer 1's domain hierarchies or validators.
