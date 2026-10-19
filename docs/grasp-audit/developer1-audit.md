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

---

### 2.4 Low Coupling

- **Verdict:** **Correct**

#### Justification & Evidence
- **Domain Model Independence (`com.gamezone.model`):** None of the domain classes (`Product`, `VideoGame`, `Console`, `Accessory`, `Controller`, `Cable`, `Memory`, `Promotion`, `PercentageDiscount`, `CategoryDiscount`, `BulkPurchaseDiscount`, `Return`, `Warranty`, `BasicWarranty`, `ExtendedWarranty`) import or depend on `persistence`, `service`, `validation`, or `ui`.
- **Exception Hierarchy (`com.gamezone.exceptions`):** `GameZoneException` and its four subclasses (`ResourceNotFoundException`, `BusinessRuleException`, `InvalidDataException`, `PersistenceException`) depend only on standard `java.lang.RuntimeException`.
- **Validation Layer (`com.gamezone.validation`):** `ProductValidator`, `PersonValidator`, and `SaleValidator` depend strictly on `com.gamezone.model` and `com.gamezone.exceptions`, with zero coupling to `com.gamezone.persistence` or `com.gamezone.service`.

---

### 2.5 High Cohesion

- **Verdict:** **Correct**

#### Justification & Evidence
- **`ProductService` (`ProductService.java`, lines 14–145):** After delegating input and stock checks to `ProductValidator.validateProductData(...)`, `ProductValidator.validateUniqueProduct(...)`, `ProductValidator.validateProductExists(...)`, and `ProductValidator.validateStockAvailability(...)` in Requirement 6, `ProductService` remains focused exclusively on coordinating product registration, lookup, and inventory persistence.
- **`Warranty`, `BasicWarranty`, `ExtendedWarranty` (`Warranty.java`, `BasicWarranty.java`, `ExtendedWarranty.java`):** Each class has a single, cohesive domain focus—representing warranty validity, duration (`6` vs `12` months), additional cost (`0.0` vs `10%` of product price), and certificate formatting (`generateWarrantyCertificate()`).

---

### 2.6 Polymorphism

- **Verdict:** **Violation** (1 violation found in `CategoryDiscount`; properly applied in `Product.getDescription()`, `Promotion.calculateDiscount(Sale)`, and `Warranty`).

#### Correct Applications
- **`Product.getDescription()` (`Product.java` line 111; overridden in `VideoGame.java` lines 96–105, `Console.java` lines 95–104, `Accessory.java` lines 70–74, `Controller.java` lines 53–57, `Cable.java` lines 78–83, `Memory.java` lines 77–82):** Receipt generation and listings call `product.getDescription()` polymorphically without type-checking.
- **`Promotion.calculateDiscount(Sale)` (`Promotion.java` line 121; overridden in `PercentageDiscount`, `CategoryDiscount`, `BulkPurchaseDiscount`):** `PromotionService.findBestPromotionFor(Sale)` invokes `p.calculateDiscount(sale)` polymorphically across all promotion subtypes.
- **`Warranty.getDurationInMonths()`, `getWarrantyType()`, `getAdditionalCost()` (`Warranty.java` lines 85–99; overridden in `BasicWarranty` and `ExtendedWarranty`):** Eliminates conditional branches on warranty type when computing end dates (`startDate.plusMonths(getDurationInMonths())`), certificates, and cancellation refunds.

#### Identified Violation (`V-02`)

| Field | Content |
| :--- | :--- |
| **ID** | `V-02` |
| **Pattern** | Polymorphism (`Polimorfismo`) |
| **Module** | Promotions & Products/Accessories Hierarchy (Requirement 2 / Requirement 5 Adjustment A1) |
| **Evidence** | `com.gamezone.model.CategoryDiscount`, method `calculateDiscount(Sale sale)` (lines 78–90 of `CategoryDiscount.java`):<br>```java
for(Product product : sale.getProducts()){
    if("VIDEOGAME".equalsIgnoreCase(categoryObjective) && product instanceof VideoGame){
        categoryTotal += product.getPrice(); 
    }
    if("CONSOLE".equalsIgnoreCase(categoryObjective) && product instanceof Console){
        categoryTotal += product.getPrice(); 
    }
    if("ACCESSORY".equalsIgnoreCase(categoryObjective) && product instanceof Accessory){
        categoryTotal += product.getPrice(); 
    }
}
``` |
| **Explanation** | Instead of relying on polymorphic dispatch in the `Product` hierarchy to identify which category each product belongs to, `CategoryDiscount.calculateDiscount` uses explicit conditional type checks (`product instanceof VideoGame`, `product instanceof Console`, `product instanceof Accessory`). This directly violates the **Polymorphism** GRASP pattern ("How to handle alternatives based on type without `if/instanceof` conditionals"). |
| **Consequence** | Adding any new product category to the store catalog in the future forces developers to modify `CategoryDiscount.calculateDiscount` to add a new `if (... && product instanceof NewType)` branch, coupling `CategoryDiscount` to every concrete subclass of `Product`. |
| **Proposed Solution** | Apply **Polymorphism** (and **Information Expert**) by declaring `public abstract String getCategory();` and `public boolean belongsToCategory(String category)` in `Product`, overriding `getCategory()` in `VideoGame` (`"VIDEOGAME"`), `Console` (`"CONSOLE"`), and `Accessory` (`"ACCESSORY"`), and simplifying `CategoryDiscount.calculateDiscount(Sale)` to iterate with `if (product.belongsToCategory(categoryObjective))`. |
| **Other Member's Code** | **No:** `Product`, `VideoGame`, `Console`, `Accessory`, and `CategoryDiscount` are all under the responsibility of **Developer 1 (`craulgomez`)**. |

---

### 2.7 Pure Fabrication

- **Verdict:** **Violation** (1 incomplete Pure Fabrication coverage in `ProductValidator`; properly applied in `ProductRepository`, `ProductValidator`, `PersonValidator`, and `SaleValidator`).

#### Correct Applications
- **`ProductRepository` (`com.gamezone.persistence.ProductRepository`):** Created as a Pure Fabrication class so that `Product`, `VideoGame`, and `Console` do not contain file serialization (`ObjectOutputStream` / `ObjectInputStream`) logic, preserving High Cohesion and Low Coupling in the domain model.
- **`ProductValidator`, `PersonValidator`, `SaleValidator` (`com.gamezone.validation`):** Created in Requirement 6 as Pure Fabrication classes to house cross-field format, range, and precondition validation rules outside domain entities and service workflows.

#### Identified Violation (`V-03`)

| Field | Content |
| :--- | :--- |
| **ID** | `V-03` |
| **Pattern** | Pure Fabrication (`Fabricación Pura`) |
| **Module** | Exceptions & Validations (Requirement 6) / Accessories Hierarchy (Requirement 1) |
| **Evidence** | `com.gamezone.validation.ProductValidator` (`ProductValidator.java`, lines 18–79) only provides `validateProductData(id, title, price, stock)` for base `Product` attributes, leaving out subtype validation rules for `Accessory` subclasses (`Cable` length > 0 and connector type, `Controller` connection type, `Memory` storage capacity > 0 and memory type). Consequently, `AccessoryService` (`AccessoryService.java`, lines 38–40 & 57–59) had to keep inline `InvalidDataException` checks:<br>```java
if (lengthInMeters <= 0) {
    throw new InvalidDataException("longitud", "La longitud del cable debe ser mayor que cero.");
}
...
if (storageCapacityGb <= 0) {
    throw new InvalidDataException("capacidad", "La capacidad de almacenamiento debe ser mayor que cero.");
}
``` |
| **Explanation** | `ProductValidator` was designed as the Pure Fabrication validator for the entire `Product` hierarchy (which includes `Accessory`, `Cable`, `Controller`, and `Memory`). Because `ProductValidator` omitted subtype validation methods for accessories, `AccessoryService` could not fully delegate its input validation to the validation layer. |
| **Consequence** | Scatters product/accessory validation rules across `com.gamezone.validation.ProductValidator` and `com.gamezone.service.AccessoryService`, lowering service cohesion. |
| **Proposed Solution** | Apply **Pure Fabrication** by adding `validateCableData(double lengthInMeters, String connectorType)`, `validateControllerData(String connectionType)`, and `validateMemoryData(int storageCapacityGb, String memoryType)` to `ProductValidator` (Developer 1), and delegating to them from `AccessoryService` (Developer 2). |
| **Other Member's Code** | **Yes (Boundary Finding):** Involves `ProductValidator` (**Developer 1 — `craulgomez`**) and `AccessoryService` (**Developer 2 — `Hectorguevara22`**). |

---

### 2.8 Indirection

- **Verdict:** **Correct**

#### Justification & Evidence
- **`ProductRepository` as an Indirection Layer between `ProductService` and File Storage (`ProductRepository.java`, lines 16–122):** `ProductService` does not interact with `java.io.File`, `FileInputStream`, or `ObjectOutputStream`. Instead, `ProductRepository` acts as an intermediary that decouples product business operations from the underlying binary file `data/products.dat`.
- **Validators (`ProductValidator`, `PersonValidator`, `SaleValidator`) as Indirection for Rule Verification:** Services invoke static validator methods instead of coupling business workflows directly to low-level formatting checks.

---

### 2.9 Protected Variations

- **Verdict:** **Correct** *(with the category variation point covered under `V-02` Polymorphism above)*

#### Justification & Evidence
- **`Repository<T>` Implementation in `ProductRepository` (`ProductRepository.java`, line 16: `public class ProductRepository implements Repository<Product>`):** Shields callers from changes in the persistence mechanism behind the standard `save`, `findAll`, `findById`, and `saveAll` contract.
- **Abstract Hierarchies (`Product`, `Accessory`, `Promotion`, `Warranty`, `GameZoneException`):** Protect client layers (`service` and `ui`) against variations in product descriptions (`getDescription()`), promotional discount formulas (`calculateDiscount(Sale)`), warranty durations/costs (`getDurationInMonths()`, `getAdditionalCost()`), and error categories (`getErrorCode()`). Once `V-02` (`CategoryDiscount` `instanceof` checks) is resolved via polymorphic `Product.belongsToCategory(String)`, category-based discounts are also fully protected against new `Product` subclasses.

---

## 3. Summary of Verdicts (Developer 1)

| # | GRASP Pattern | Verdict | Findings |
| :--- | :--- | :--- | :--- |
| 1 | **Information Expert** | **Violation** | `V-01`: `Return.getDiscountRatio()` calculates the discount ratio from `Sale`'s raw attributes instead of delegating to `Sale`. |
| 2 | **Creator** | **Correct** | Properly applied in `ProductService`, `ProductRepository`, and validators. |
| 3 | **Controller** | **Not Applicable** | Domain entities, exceptions, and validators do not receive UI system events. |
| 4 | **Low Coupling** | **Correct** | `model`, `exceptions`, and `validation` respect strict one-directional dependencies. |
| 5 | **High Cohesion** | **Correct** | Domain classes, exceptions, validators, and `ProductService` are focused on single responsibilities. |
| 6 | **Polymorphism** | **Violation** | `V-02`: `CategoryDiscount.calculateDiscount(Sale)` uses `instanceof VideoGame`, `Console`, and `Accessory` instead of polymorphic dispatch on `Product`. |
| 7 | **Pure Fabrication** | **Violation** | `V-03`: `ProductValidator` lacks subtype validation methods for `Accessory` subclasses (`Cable`, `Controller`, `Memory`), leaving inline checks in `AccessoryService`. |
| 8 | **Indirection** | **Correct** | `ProductRepository` decouples `ProductService` from binary file I/O. |
| 9 | **Protected Variations** | **Correct** | Abstract hierarchies (`Product`, `Promotion`, `Warranty`, `GameZoneException`) and `Repository<Product>` shield clients from variations. |
