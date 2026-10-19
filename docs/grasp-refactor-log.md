# GRASP Refactoring Change Log and Non-Regression Verification (Requirement 7 — Phase 2)

- **Branch:** `feature/grasp-refactor`
- **Baseline `develop` Commit (After Phase 1 Merge):** `c399303`
- **Reference Consolidated Audit:** [docs/grasp-audit/consolidated-audit.md](grasp-audit/consolidated-audit.md)

---

## 1. Non-Regression Verification Strategy

In coherence with Guiding Question 4 of [docs/grasp-analysis.md](grasp-analysis.md), all Phase 2 corrections are internal design refactorings that reorganize responsibilities according to the nine GRASP patterns **without altering external system behavior**. To guarantee zero regression across the application:

1. **Checkpoint 1 — Baseline Reference on `develop` (`c399303`):**
   Before applying the first refactoring commit, every Console Menu operation (Products, Customers, Sellers, Sales, Accessories, Promotions, Returns, Monthly Balance, and Warranties) and the four differentiated exception categories (`ResourceNotFoundException`, `BusinessRuleException`, `InvalidDataException`, `PersistenceException`) are verified and recorded in the Baseline Reference Table below.
2. **Checkpoint 2 — Per-Pull-Request Verification:**
   Each team member compiles the full project (`mvn clean compile` / `javac`) with zero errors and verifies all operations touched by their corrected violations (`V-01` through `V-07`) before opening their Pull Request to `develop`.
3. **Checkpoint 3 — Final End-to-End Verification:**
   Upon integrating all three Pull Requests, the Technical Lead executes the complete verification suite and records the final comparison against the baseline reference.

---

## 2. Baseline Reference Verification on `develop` (Before First Correction)

| Menu Option / Scenario | Test Input Data | Expected Reference Output / Behavior | Baseline Status (`develop`) |
| :--- | :--- | :--- | :--- |
| **1. Register VideoGame** | `id="VG101"`, `title="Elden Ring"`, `price=220000.0`, `stock=5`, `platform="PS5"`, `genre="RPG"`, `rating="M"` | Registers `VideoGame`, persists to `data/products.dat`, prints `"Videojuego registrado exitosamente."` | PASS |
| **2. Register Console** | `id="C001"`, `title="PlayStation 5"`, `price=2500000.0`, `stock=4`, `brand="Sony"`, `model="Slim"`, `gen="9th"` | Registers `Console`, persists to `data/products.dat`, prints `"Consola registrada exitosamente."` | PASS |
| **3. List All Products** | Menu option `3` | Displays formatted `getDescription()`, price, and current stock for all products in `data/products.dat` | PASS |
| **4. Register Customer** | `id="1065001"`, `firstName="Mateo"`, `lastName="Diaz"`, `phone="3001112233"`, `email="mateo@unicesar.edu.co"` | Registers `Customer`, persists to `data/customers.dat`, prints `"Cliente registrado exitosamente."` | PASS |
| **5–6. List Customers / Sellers** | Menu options `5` and `6` | Displays registered customers and preloaded sellers (`V001`, `V002`, `V003`) via `getSummary()` | PASS |
| **7. Register Sale (Unified Flow)** | `customerId="1065001"`, `sellerId="V001"`, items `["C001", "A001"]`, extended warranty on `"C001"` | Calculates subtotal, applies best active promotion (`PROMO001` / `PROMO004`), assigns `BasicWarranty` + `ExtendedWarranty` (`+10%`), deducts stock in `ProductService` & `AccessoryService`, prints receipt | PASS |
| **8–10. View Sales (All / By Customer / By Seller)** | Menu options `8`, `9` (`1065001`), `10` (`V001`) | Reconstructs `Sale` from `SaleRecord` and prints full receipt with subtotal, discount, warranty cost, and total | PASS |
| **11. Register Accessory** | `type=1 (Controller)`, `id="A010"`, `title="Control Pro"`, `price=180000.0`, `stock=6`, `conn="WIRELESS"`, `consoles=["C001"]` | Registers `Controller`, persists to `data/Accessory.dat`, prints confirmation | PASS |
| **12. List Accessories by Type** | `type="Controller"` | Lists all `Controller` accessories with `getDescription()` | PASS |
| **13. Find Accessories Compatible with Console** | `consoleId="C001"` | Returns all accessories (`A001`, `A002`, `A003`, `A010`) whose compatible console list includes `"C001"` | PASS |
| **14. Register Promotion** | `type=2 (CategoryDiscount)`, `id="PROMO010"`, `name="Promo Consolas"`, `2026-01-01` to `2026-12-31`, `10%`, `"CONSOLE"` | Registers `CategoryDiscount`, persists to `data/promotion.dat`, prints confirmation | PASS |
| **15–16. List All / Active Promotions** | Menu options `15` and `16` | Lists all promotions (`PROMO001`–`PROMO004`, `PROMO010`) and active ones for `LocalDate.now()` | PASS |
| **17. Register Return** | `saleId=<registered sale>`, `productIds=["C001"]`, `reason="Falla de encendido"` | Validates 30-day window & sale membership, restores stock (`+1`), cancels console warranties, refunds proportional item price + extended warranty cost, prints return receipt | PASS |
| **18–20. View Returns (All / By Customer / By Sale)** | Menu options `18`, `19`, `20` | Reconstructs `Return` from `ReturnRecord` and prints formatted return receipt | PASS |
| **21. Generate Monthly Financial Balance** | `month=10`, `year=2026` | Prints `Total de ventas del mes`, `Total de devoluciones del mes`, and `Balance neto del mes` | PASS |
| **22–24. Query Warranties (By Product & Sale / Active / Expiring Soon)** | Menu options `22`, `23`, `24` (`daysAhead=30`) | Displays `generateWarrantyCertificate()` for matching, active, or expiring warranties | PASS |
| **Error Paths (RE6 Differentiated Catching)** | Missing ID / Insufficient Stock / Negative Price / I/O Failure | Prints `"No se encontró el recurso solicitado: ..."`, `"No se puede completar la operación: ..."`, `"Los datos ingresados no son válidos: ..."`, or administrator persistence notice | PASS |

---

## 3. Phase 2 Refactoring Traceability Log

| Class | Change Made | Violation ID | Applied Pattern | Commit / PR | Responsible |
| :--- | :--- | :--- | :--- | :--- | :--- |
| `Product`, `VideoGame`, `Console`, `Accessory` | Added abstract `getCategory()` and `belongsToCategory(String)` in `Product` and `@Override getCategory()` in subclasses (`"VIDEOGAME"`, `"CONSOLE"`, `"ACCESSORY"`) | `V-02` | Polymorphism (`Polimorfismo`) | `feature/grasp-refactor` (PR #1) | Carlos Gomez (`craulgomez`) |
| `CategoryDiscount` | Replaced `instanceof VideoGame`, `Console`, and `Accessory` conditionals in `calculateDiscount(Sale)` with polymorphic `product.belongsToCategory(categoryObjective)` | `V-02` | Polymorphism (`Polimorfismo`) | `feature/grasp-refactor` (PR #1) | Carlos Gomez (`craulgomez`) |
| `Accessory` | Added null-safe guards on `compatibleConsoleIds` and `consoleId` inside `isCompatibleWith(String)` | `V-03` | Information Expert (`Experto en Información`) | `feature/grasp-refactor` (PR #1) | Carlos Gomez (`craulgomez`) |
| `ProductValidator` | Added static subtype and promotion validation methods (`validateCableData`, `validateMemoryData`, `validatePromotionBase`, `validateCategoryObjective`, `validateMinimumQuantity`) | `V-05` | Pure Fabrication (`Fabricación Pura`) | `feature/grasp-refactor` (PR #1) | Carlos Gomez (`craulgomez`) |
| `Sale`, `Return` | Added `Sale.getDiscountRatio()` and refactored `Return.getDiscountRatio()` to delegate to `originalSale.getDiscountRatio()` | `V-01` | Information Expert (`Experto en Información`) | `feature/grasp-refactor` (PR #1) | Carlos Gomez (`craulgomez`) & Merlin Landero (`mlanderoc`) |
| `AccessoryService` | Replaced external `accessory.getCompatibleConsoleIds().contains(consoleId)` traversal in `findCompatibleWithConsole(String)` with `accessory.isCompatibleWith(consoleId)` | `V-03` | Information Expert (`Experto en Información`) | `feature/grasp-refactor` (PR #2) | Héctor Guevara (`Hectorguevara22`) |
| `AccessoryService`, `PromotionService` | Delegated inline subtype and promotion validations (`Cable`, `ExternalMemory`, `Promotion` base fields, `CategoryDiscount`, `BulkPurchaseDiscount`) to `ProductValidator` | `V-05` | Pure Fabrication & High Cohesion (`Fabricación Pura` y `Alta Cohesión`) | `feature/grasp-refactor` (PR #2) | Héctor Guevara (`Hectorguevara22`) |
| `AccessoryService` | Added single-argument constructor `AccessoryService(AccessoryRepository)` that encapsulates `repository.loadAll()` internally | `V-06` | Low Coupling (`Bajo Acoplamiento`) | `feature/grasp-refactor` (PR #2) | Héctor Guevara (`Hectorguevara22`) |
| `Sale`, `ReturnService` | Added `Sale.findProductById(String)` and delegated `ReturnService.findInSale(Sale, String)` to `sale.findProductById(productId)` | `V-04` | Information Expert (`Experto en Información`) | `feature/grasp-refactor` (PR #2) | Héctor Guevara (`Hectorguevara22`) |
| `Main` | Updated `AccessoryService` initialization to `new AccessoryService(accessoryRepository)`, removing the direct `accessoryRepository.loadAll()` call in `Main` | `V-06` | Low Coupling (`Bajo Acoplamiento`) | `feature/grasp-refactor` (PR #3) | Merlin Landero (`mlanderoc`) |
| `SaleService` | Extracted private cohesive helpers `findCatalogProduct(Product)`, `findCatalogProductById(String)`, and `decrementStock(Product)` from `registerSale(...)` and `toSale(...)` | `V-07` | High Cohesion (`Alta Cohesión`) | `feature/grasp-refactor` (PR #3) | Merlin Landero (`mlanderoc`) |
| `ProductService` | Added positive quantity guard in `restoreStock(String, int)` throwing `InvalidDataException` when `quantity <= 0` for symmetry with `AccessoryService.restoreStock` | `V-07` | High Cohesion (`Alta Cohesión`) | `feature/grasp-refactor` (PR #3) | Merlin Landero (`mlanderoc`) |

---

## 4. Final Non-Regression Verification Results (After All Phase 2 Corrections)

After integrating PR #1 (`craulgomez`), PR #2 (`Hectorguevara22`), and PR #3 (`mlanderoc`) on `feature/grasp-refactor`, the complete verification suite was executed and compared against the Section 2 Baseline Reference Table:

| Menu Option / Scenario | Affected Refactorings | Post-Refactor Output / Behavior | Non-Regression Status |
| :--- | :--- | :--- | :--- |
| **1–3. Products Registration & Listing (`VideoGame`, `Console`)** | `V-02`, `V-07` | Registers and lists `VideoGame` and `Console` identically to baseline; `getCategory()` returns `"VIDEOGAME"` and `"CONSOLE"` | PASS (No Regression) |
| **4–6. Customers & Sellers Registration & Listing** | — | Registers customers and lists preloaded sellers (`V001`–`V003`) identically to baseline | PASS (No Regression) |
| **7–10. Unified Sale Registration & Queries** | `V-02`, `V-06`, `V-07` | `SaleService.registerSale` and `toSale` resolve items and deduct stock via `findCatalogProduct`, `findCatalogProductById`, and `decrementStock`; `CategoryDiscount` evaluates `product.belongsToCategory(...)` polymorphically; receipts match baseline 100% | PASS (No Regression) |
| **11–13. Accessories Registration, Type Filter & Console Compatibility** | `V-03`, `V-05`, `V-06` | `Main` initializes `AccessoryService(accessoryRepository)` cleanly; `registerCable` and `registerMemory` validate via `ProductValidator`; `findCompatibleWithConsole("C001")` delegates to `accessory.isCompatibleWith("C001")` and returns the exact same accessories | PASS (No Regression) |
| **14–16. Promotions Registration & Active Listing** | `V-02`, `V-05` | `PromotionService` delegates base, category, and minimum-quantity validations to `ProductValidator`; active and best promotions compute identical discounts | PASS (No Regression) |
| **17–20. Returns Registration, Proportional Refund & Warranty Cancellation** | `V-01`, `V-04`, `V-07` | `ReturnService.findInSale` delegates to `sale.findProductById(productId)`; `Return.getDiscountRatio()` delegates to `originalSale.getDiscountRatio()`; `ProductService.restoreStock` and `AccessoryService.restoreStock` restore stock and validate positive quantities | PASS (No Regression) |
| **21. Monthly Financial Balance** | `V-01`, `V-07` | `calculateMonthlySales`, `calculateMonthlyReturns`, and `generateMonthlyBalance` produce the exact same financial totals | PASS (No Regression) |
| **22–24. Warranties Queries (By Product & Sale / Active / Expiring Soon)** | — | Displays `generateWarrantyCertificate()` identically to baseline | PASS (No Regression) |
| **Error Paths (`ResourceNotFoundException`, `BusinessRuleException`, `InvalidDataException`, `PersistenceException`)** | `V-05`, `V-07` | Throws the exact same exception types and Spanish user-facing messages in `ConsoleMenu` and Swing dialogs | PASS (No Regression) |

