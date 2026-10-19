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
