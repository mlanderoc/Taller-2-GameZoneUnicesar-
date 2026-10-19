# Individual GRASP Audit Report — Technical Lead (Merlin Landero)

- **Author:** Merlin Landero (`mlanderoc`)
- **Role:** Technical Lead — Sales Module, System Integration, UI (`ConsoleMenu` & Swing GUI), `Main` & `ProductService.restoreStock`
- **Audited Branch / Commit (`develop`):** `3f14c06`

---

## 1. Audit Scope

In accordance with Section 4 of Requirement 7 and `TEAM.md`, this audit covers all classes, methods, and cross-module integrations developed under the Technical Lead's responsibility across Taller 1 and Requirements 1, 2, 3, 4, 5, and 6:

| Module (Requirement) | Classes Audited | Methods Audited |
| :--- | :--- | :--- |
| **Sales, UI & Startup (Taller 1)** | `Sale` *(except `canBeReturned`)*, `SaleRecord`, `SaleRepository`, `SaleService`, `ConsoleMenu`, `Main`, Swing UI (`MainFrame`, `SalePanel`, `SaleDialog`, `CustomerPanel`, `CustomerDialog`, `SellerPanel`, `ProductPanel`, `ProductDialog`, `AccessoryPanel`, `AccessoryDialog`, `PromotionPanel`, `PromotionDialog`, `ReturnPanel`, `ReturnDialog`, `WarrantyPanel`, `ReportsPanel`, `DetailDialog`) | `Sale`: constructor, getters/setters, `applyDiscount(String, double)`, `generateReceipt()`, `calculateTotal()`<br>`SaleRecord`: constructors, getters<br>`SaleRepository`: `save(SaleRecord)`, `findAll()`, `findById(String)`, `saveAll(List<SaleRecord>)`, `loadAll()`<br>`SaleService`: constructors, `setWarrantyService`, `registerSale(...)`, `viewAllSales()`, `viewSalesByCustomer(Customer)`, `viewSalesBySeller(Seller)`, `findSaleById(String)`, `toRecord(Sale)`, `toSale(SaleRecord)`<br>`ConsoleMenu`: constructor, `start()`, all menu operation handlers, `handleOperationError(GameZoneException)`<br>`Main`: `main(String[])`, `preloadSellersIfNeeded`, `preloadPromotionsIfNeeded`, `preloadAccessoriesIfNeeded` |
| **Accessories Integration (RE1 & RE5-A3)** | `SaleService`, `ConsoleMenu`, `AccessoryPanel`, `AccessoryDialog` | Item lookup and inventory deduction for accessories in `SaleService.registerSale` and `toSale`; options 11–13 in `ConsoleMenu` |
| **Promotions Integration (RE2 & RE5-A3)** | `Sale`, `SaleService`, `ConsoleMenu`, `PromotionPanel`, `PromotionDialog` | `Sale.applyDiscount`, `Sale.generateReceipt`, `SaleService.registerSale` step 4 (`findBestPromotionFor`), options 14–16 in `ConsoleMenu` |
| **Returns Integration (RE3 & RE5-A4/A6)** | `ProductService.restoreStock`, `ConsoleMenu`, `ReturnPanel`, `ReturnDialog`, `ReportsPanel` | `ProductService.restoreStock(String, int)` (`ProductService.java`, lines 142–144); options 17–21 in `ConsoleMenu` |
| **Warranties Integration (RE4 & RE5-A2/A3)** | `Sale`, `SaleRecord`, `SaleService`, `ConsoleMenu`, `WarrantyPanel`, `SaleDialog` | `extendedWarrantyCost` handling in `Sale` and `SaleRecord`, basic/extended console warranty assignment in `SaleService.registerSale` step 5, options 22–24 in `ConsoleMenu` |
| **Exceptions & Validations Integration (RE6)** | `ConsoleMenu`, `SaleDialog`, `ReturnDialog`, `ProductDialog`, `CustomerDialog` | Differentiated multi-catch blocks for `ResourceNotFoundException`, `BusinessRuleException`, `InvalidDataException`, and `PersistenceException` |

---

## 2. Evaluation by GRASP Pattern

### 2.1 Information Expert

- **Verdict:** **Violation** (1 violation with two missing expert queries in `Sale`; properly applied in `Sale.calculateTotal()`, `Sale.getTotalAmount()`, and `Sale.generateReceipt()`).

#### Correct Applications
- **`Sale.calculateTotal()` (`Sale.java`, lines 164–170) & `Sale.getTotalAmount()` (`Sale.java`, lines 85–87):** `Sale` owns `products`, `subtotal`, `discountAmount`, and `extendedWarrantyCost`, and computes both the product subtotal and the final net total (`subtotal - discountAmount + extendedWarrantyCost`).
- **`Sale.generateReceipt()` (`Sale.java`, lines 136–158):** Uses its own attributes and polymorphic `product.getDescription()` calls to build the formatted sale receipt.

#### Identified Violation (`V-01`)

| Field | Content |
| :--- | :--- |
| **ID** | `V-01` |
| **Pattern** | Information Expert (`Experto en Información`) |
| **Module** | Sales (Taller 1 / Requirement 2, Requirement 3 & Requirement 5) |
| **Evidence** | `com.gamezone.model.Sale` (`Sale.java`, lines 9–185) owns `private List<Product> products;`, `private double subtotal;`, and `private double discountAmount;`, but lacks expert methods to:<br>1. Compute its own proportional discount ratio (`discountAmount / subtotal`), forcing `Return.getDiscountRatio()` (`Return.java`, lines 139–144) to extract both raw fields externally.<br>2. Look up a sold product by identifier within `products`, forcing `ReturnService.findInSale(Sale, String)` (`ReturnService.java`, lines 210–217) to iterate `sale.getProducts()` externally. |
| **Explanation** | Under **Information Expert**, because `Sale` owns `products`, `subtotal`, and `discountAmount`, `Sale` should directly answer queries about its discount ratio and whether a given `productId` belongs to the sale. |
| **Consequence** | External classes (`Return` and `ReturnService`) are forced to inspect `Sale`'s internal state and duplicate arithmetic and list-search logic that belongs inside `Sale`. |
| **Proposed Solution** | Apply **Information Expert** by adding `public double getDiscountRatio()` and `public Product findProductById(String productId)` to `Sale`, allowing `Return` (Developer 1) and `ReturnService` (Developer 2) to delegate directly to `Sale`. |
| **Other Member's Code** | **Yes (Boundary Finding):** Connects directly with `Return` (**Developer 1 — `developer1-audit` `V-01`**) and `ReturnService` (**Developer 2 — `developer2-audit` `V-02`**). |

---

### 2.2 Creator

- **Verdict:** **Correct**

#### Justification & Evidence
- **`SaleService.registerSale(...)`, `toRecord(Sale)`, and `toSale(SaleRecord)` (`SaleService.java`, lines 90–91, 199–215, 222–246):** `SaleService` validates and resolves all participant entities (`Customer`, `Seller`, `List<Product>`) and instantiates `Sale` and `SaleRecord`.
- **`Main.main(String[])` (`Main.java`, lines 28–53):** Acts as the application composition root, instantiating the repositories, services, and `MainFrame`.

---

### 2.3 Controller

- **Verdict:** **Correct**

#### Justification & Evidence
- **`SaleService` (`SaleService.java`, lines 28–247):** Acts as the use-case controller for checkout and sale queries. `ConsoleMenu` and `SaleDialog` never manipulate `SaleRepository`, `PromotionRepository`, or `WarrantyRepository` directly; they invoke `saleService.registerSale(...)` and `saleService.viewAllSales()`.
- **`ConsoleMenu` (`ConsoleMenu.java`):** Exclusively handles console I/O (`Scanner` prompts and `System.out.println` messages in Spanish) and delegates every business operation to the injected service layer (`ProductService`, `PersonService`, `SaleService`, `AccessoryService`, `PromotionService`, `ReturnService`, `WarrantyService`).

---

### 2.4 Low Coupling

- **Verdict:** **Violation** (1 boundary violation in `Main` when constructing `AccessoryService`; properly applied in `SaleRecord` and `SaleRepository`).

#### Correct Applications
- **`SaleRecord` and `SaleRepository` (`com.gamezone.persistence`):** `SaleRecord` stores only raw identifiers (`saleId`, `customerId`, `sellerId`, `productIds`, `subtotal`, `appliedPromotionName`, `discountAmount`, `extendedWarrantyCost`) so `SaleRepository` has zero coupling to `ProductService`, `PersonService`, or `AccessoryService`.

#### Identified Violation (`V-02`)

| Field | Content |
| :--- | :--- |
| **ID** | `V-02` |
| **Pattern** | Low Coupling (`Bajo Acoplamiento`) |
| **Module** | Startup & Accessories Integration (Taller 1 / Requirement 1) |
| **Evidence** | `com.gamezone.Main`, method `main(String[] args)` (line 40 of `Main.java`):<br>```java
AccessoryService accessoryService = new AccessoryService(accessoryRepository,accessoryRepository.loadAll());
``` |
| **Explanation** | While `Main` instantiates `ProductService(productRepository)`, `PersonService(personRepository)`, and `PromotionService(promotionRepository)` by passing only their repository, line 40 couples `Main` to calling `accessoryRepository.loadAll()` explicitly and passing the loaded list into `AccessoryService`, which then calls `repository.loadAll()` a second time inside its constructor. |
| **Consequence** | Increases coupling between `Main` and the internal loading mechanics of `AccessoryRepository` and causes a duplicate file read of `data/Accessory.dat` on application startup. |
| **Proposed Solution** | Apply **Low Coupling** by having `AccessoryService` expose `public AccessoryService(AccessoryRepository repository)` (Developer 2) and updating `Main.main` (Technical Lead) to call `new AccessoryService(accessoryRepository)`. |
| **Other Member's Code** | **Yes (Boundary Finding):** Connects with `AccessoryService` (**Developer 2 — `developer2-audit` `V-03`**). |

---

### 2.5 High Cohesion

- **Verdict:** **Violation** (1 violation in `SaleService` & `ProductService.restoreStock`; properly applied in `SaleRepository` and `ConsoleMenu.handleOperationError`).

#### Correct Applications
- **`SaleRepository` (`SaleRepository.java`, lines 19–104):** Focused exclusively on reading and writing `SaleRecord` instances to `data/sales.dat` and wrapping I/O failures into `PersistenceException`.
- **`ConsoleMenu.handleOperationError(GameZoneException)` (`ConsoleMenu.java`):** Centralizes differentiated user error formatting in Spanish.

#### Identified Violation (`V-03`)

| Field | Content |
| :--- | :--- |
| **ID** | `V-03` |
| **Pattern** | High Cohesion (`Alta Cohesión`) / Indirection (`Indirection`) |
| **Module** | Sales & Returns Integration (Requirement 1, Requirement 3 & Requirement 5) |
| **Evidence** | 1. `com.gamezone.service.SaleService`, methods `registerSale` (lines 77–82 & 116–122) and `toSale` (lines 229–233 of `SaleService.java`):<br>```java
if (item instanceof Accessory) {
    current = accessoryService.findById(item.getId());
} else {
    current = productService.findById(item.getId());
}
...
if (item instanceof Accessory) {
    accessoryService.updateStock(item.getId(), -1);
} else {
    productService.updateStock(item.getId(), -1);
}
```<br>2. `com.gamezone.service.ProductService`, method `restoreStock(String productId, int quantity)` (lines 142–144 of `ProductService.java`):<br>```java
public void restoreStock(String productId, int quantity) {
    updateStock(productId, quantity);
}
``` |
| **Explanation** | First, `SaleService` repeats the catalog lookup and stock-update branching logic (`ProductService` vs `AccessoryService`) inline across `registerSale` (steps 2 and 7) and `toSale`, cluttering the 8-step checkout workflow with repetitive catalog-routing conditionals. Second, `ProductService.restoreStock` (developed by the Technical Lead in RE3) does not validate that `quantity > 0` before calling `updateStock`, unlike `AccessoryService.restoreStock` which validates positive restoration quantities. |
| **Consequence** | Duplicated routing branches inside `SaleService` lower method cohesion and readability, while `ProductService.restoreStock` lacks symmetric positive-quantity validation. |
| **Proposed Solution** | Apply **High Cohesion** and **Indirection** by extracting cohesive private helper methods `resolveCatalogItem(String productId)` and `adjustCatalogStock(Product item, int quantityDelta)` inside `SaleService`, and adding positive-quantity validation in `ProductService.restoreStock(String productId, int quantity)`. |
| **Other Member's Code** | **No:** Both `SaleService` and `ProductService.restoreStock` are under the responsibility of the **Technical Lead (`mlanderoc`)**. |

---

### 2.6 Polymorphism

- **Verdict:** **Correct**

#### Justification & Evidence
- **`Sale.generateReceipt()` (`Sale.java`, lines 144–146):** Iterates over `List<Product> products` calling `product.getDescription()` polymorphically (`VideoGame`, `Console`, `Controller`, `Cable`, `Memory`).
- **`SaleService.registerSale(...)` (`SaleService.java`, lines 94–98):** Calls `bestPromotion.calculateDiscount(sale)` polymorphically on the `Promotion` returned by `promotionService.findBestPromotionFor(sale)`.
- **Differentiated Exception Catching in `ConsoleMenu` and Swing Dialogs (`ConsoleMenu.java`, `SaleDialog.java`, `ReturnDialog.java`, `ProductDialog.java`, `CustomerDialog.java`):** Uses Java's polymorphic exception dispatch (`catch (ResourceNotFoundException e)`, `catch (BusinessRuleException e)`, `catch (InvalidDataException e)`, `catch (PersistenceException e)`, `catch (GameZoneException e)`) rather than `instanceof` chains.

---

### 2.7 Pure Fabrication

- **Verdict:** **Correct**

#### Justification & Evidence
- **`SaleRecord` and `SaleRepository` (`com.gamezone.persistence`):** Pure Fabrication classes created specifically to persist sale transactions as flat ID records in `data/sales.dat` without polluting the `Sale` domain entity with file I/O or circular layer dependencies.

---

### 2.8 Indirection

- **Verdict:** **Correct** *(with internal catalog-routing helper extraction noted in `V-03` above)*

#### Justification & Evidence
- **`SaleRecord` as Indirection Between `persistence` and `service`:** Decouples `SaleRepository` from `PersonService`, `ProductService`, and `AccessoryService`, while `SaleService.toSale(SaleRecord)` reconstructs the full `Sale` object graph in the service layer.

---

### 2.9 Protected Variations

- **Verdict:** **Correct**

#### Justification & Evidence
- **`SaleRepository implements Repository<SaleRecord>` (`SaleRepository.java`, line 19):** Implements the generic `Repository<T>` contract and wraps `IOException | ClassNotFoundException` into `PersistenceException`, shielding `SaleService` and the UI from storage format details.

---

## 3. Summary of Verdicts (Technical Lead)

| # | GRASP Pattern | Verdict | Findings |
| :--- | :--- | :--- | :--- |
| 1 | **Information Expert** | **Violation** | `V-01`: `Sale` lacks `getDiscountRatio()` and `findProductById(String)` methods for data it owns (`subtotal`, `discountAmount`, `products`). |
| 2 | **Creator** | **Correct** | Properly applied in `SaleService` (`Sale`, `SaleRecord`) and `Main` (composition root). |
| 3 | **Controller** | **Correct** | `SaleService` and `ConsoleMenu` cleanly separate UI input/output from domain workflow coordination. |
| 4 | **Low Coupling** | **Violation** | `V-02`: `Main` passes a redundant `accessoryRepository.loadAll()` list to `AccessoryService`. |
| 5 | **High Cohesion** | **Violation** | `V-03`: `SaleService` duplicates inline catalog lookup/stock branching between `ProductService` and `AccessoryService`, and `ProductService.restoreStock` lacks positive-quantity validation. |
| 6 | **Polymorphism** | **Correct** | Properly applied in `Sale.generateReceipt()`, `SaleService.registerSale()`, and `ConsoleMenu` exception dispatch. |
| 7 | **Pure Fabrication** | **Correct** | `SaleRecord` and `SaleRepository` isolate serialization outside `Sale`. |
| 8 | **Indirection** | **Correct** | `SaleRecord` decouples `SaleRepository` from other services. |
| 9 | **Protected Variations** | **Correct** | `Repository<SaleRecord>` and `PersistenceException` shield callers from I/O changes. |

---

## 4. Detailed Class & Method Verification Matrix

| Class | Requirement / Module | Audited Methods | GRASP Patterns Evaluated | Status |
| :--- | :--- | :--- | :--- | :--- |
| `Sale` *(except `canBeReturned`)* | Taller 1 / RE2 / RE4 / RE5 — Sales | Constructor, getters/setters, `applyDiscount`, `generateReceipt`, `calculateTotal` | Information Expert, Polymorphism (`generateReceipt`), Low Coupling | **Violation (`V-01`)**: lacks `getDiscountRatio()` and `findProductById(String)` |
| `SaleRecord` | Taller 1 / RE2 / RE4 — Sales | Constructors, getters | Pure Fabrication, Indirection, Low Coupling | Correct |
| `SaleRepository` | Taller 1 / RE6 — Sales | `save`, `findAll`, `findById`, `saveAll`, `loadAll` | Pure Fabrication, Indirection, Protected Variations (`Repository<SaleRecord>`) | Correct |
| `SaleService` | Taller 1 / RE1–RE6 — Sales | Constructors, `setWarrantyService`, `registerSale`, `viewAllSales`, `viewSalesByCustomer`, `viewSalesBySeller`, `findSaleById`, `toRecord`, `toSale` | Creator, Controller, High Cohesion, Indirection, Polymorphism | **Violation (`V-03`)**: repeated inline `ProductService` vs `AccessoryService` lookup and stock-update branches |
| `ProductService.restoreStock` | RE3 — Returns | `restoreStock(String, int)` | High Cohesion, Information Expert | **Violation (`V-03`)**: missing positive-quantity validation before `updateStock` |
| `ConsoleMenu` | Taller 1 / RE1–RE6 — UI | Constructor, `start`, options 1–28 handlers, `handleOperationError` | Controller, Polymorphism (multi-catch), Low Coupling | Correct |
| Swing UI (`MainFrame`, Panels & Dialogs) | Taller 1 / RE5 / RE6 — UI | Constructors, event handlers, table loaders, differentiated catch blocks | Controller delegation, Low Coupling, Polymorphism | Correct |
| `Main` | Taller 1 / RE1 / RE2 / RE5 — Startup | `main`, `preloadSellersIfNeeded`, `preloadPromotionsIfNeeded`, `preloadAccessoriesIfNeeded` | Creator, Low Coupling | **Violation (`V-02`)**: passes redundant `accessoryRepository.loadAll()` to `AccessoryService` constructor |
