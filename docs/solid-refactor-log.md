# SOLID Refactoring Change Log and Non-Regression Verification (Requirement 8 — Phase 2)

- **Branch:** `feature/solid-refactor`
- **Baseline `develop` Commit (After Phase 1 Merge):** `ead4cbc` (Audited Code Baseline: `1bfc5f6`)
- **Reference Consolidated Audit:** [docs/solid-audit/consolidated-audit.md](solid-audit/consolidated-audit.md)

---

## 1. Non-Regression Verification Strategy

In coherence with Guiding Question 4 of [docs/solid-analysis.md](solid-analysis.md), all Phase 2 corrections are behavior-preserving internal design refactorings that apply the five SOLID principles (**SRP**, **OCP**, **LSP**, **ISP**, **DIP**) without altering user-visible operations, Spanish console messages, or persisted `.dat` file formats:

1. **Checkpoint 1 — Baseline Reference Verification on `develop` (`ead4cbc` / `1bfc5f6`):**
   Before applying the first refactoring commit on `feature/solid-refactor`, every Console Menu operation across all seven main submenus (Products, People, Sales, Promotions, Accessories, Returns & Monthly Financial Balance, and Warranties) and the four differentiated exception categories (`ResourceNotFoundException`, `BusinessRuleException`, `InvalidDataException`, `PersistenceException`) are verified and recorded in Section 2 below.
2. **Checkpoint 2 — Per-Pull-Request Verification:**
   Before opening each Pull Request (`craulgomez`, `Hectorguevara22`, `mlanderoc`), the full project is compiled with zero errors (`mvn clean compile` / `javac`), binary serialization compatibility (`serialVersionUID`) and overloaded constructor signatures are preserved, and all menu operations affected by that member's corrected violations (`V-01` through `V-08`) are verified.
3. **Checkpoint 3 — Final End-to-End Verification:**
   After integrating all three Pull Requests, the Technical Lead executes the full verification suite and records the final comparison against the baseline in Section 4.

---

## 2. Baseline Reference Verification on `develop` (Before First SOLID Correction)

| Menu Option / Scenario | Test Input Data | Expected Reference Output / Behavior | Baseline Status (`develop`) |
| :--- | :--- | :--- | :--- |
| **`[1][1]` Register VideoGame** | `id="VG101"`, `title="Elden Ring"`, `price=220000.0`, `stock=5`, `platform="PS5"`, `genre="RPG"`, `rating="M"` | Registers `VideoGame`, persists to `data/products.dat`, prints `"OK: Videojuego registrado correctamente."` | PASS |
| **`[1][2]` Register Console** | `id="C001"`, `title="PlayStation 5"`, `price=2500000.0`, `stock=4`, `brand="Sony"`, `model="Slim"`, `gen="9th"` | Registers `Console`, persists to `data/products.dat`, prints `"OK: Consola registrada correctamente."` | PASS |
| **`[1][3]` List All Products** | Submenu `[1]` → Option `3` | Prints `product.getDescription()` for all products in `data/products.dat` | PASS |
| **`[2][1]` Register Customer** | `id="1065001"`, `firstName="Mateo"`, `lastName="Diaz"`, `phone="3001112233"`, `email="mateo@unicesar.edu.co"` | Registers `Customer`, persists to `customers.dat`, prints `"OK: Cliente registrado correctamente."` | PASS |
| **`[2][2]`–`[2][3]` List Customers / Sellers** | Submenu `[2]` → Options `2` and `3` | Displays registered customers and preloaded sellers (`V001`, `V002`, `V003`) | PASS |
| **`[3][1]` Register Sale (Unified Flow)** | `customerId="1065001"`, `sellerId="V001"`, items `["C001", "A001"]`, extended warranty on `"C001"` | Calculates subtotal, applies best active promotion (`PROMO001`–`PROMO004`), assigns `BasicWarranty` + `ExtendedWarranty` (`+10%`), decrements stock in `ProductService` & `AccessoryService`, prints `sale.generateReceipt()` | PASS |
| **`[3][2]`–`[3][4]` View Sales (All / By Customer / By Seller)** | Submenu `[3]` → Options `2`, `3` (`1065001`), `4` (`V001`) | Reconstructs `Sale` from `SaleRecord` and prints full receipt with subtotal, discount, warranty cost, and total | PASS |
| **`[4][1]`–`[4][3]` Register Promotions (Percentage / Category / Bulk)** | `id="PROMO010"`, `name="Promo Consolas"`, `2026-01-01` to `2026-12-31`, `10%`, `"CONSOLE"` | Validates via `PromotionValidator` / `ProductValidator`, persists to `data/promotion.dat`, prints `"OK: Promocion registrada."` | PASS |
| **`[4][4]`–`[4][5]` List All / Active Promotions** | Submenu `[4]` → Options `4` and `5` | Prints `"ID: ... | Nombre: ... | Tipo: <PromotionType>"` for all or active promotions | PASS |
| **`[5][1]`–`[5][2]` List All Accessories & Find Compatible with Console** | Submenu `[5]` → Option `1` and Option `2` (`consoleId="C001"`) | Lists preloaded accessories (`A001`, `A002`, `A003`), filters by type (`Controller`, `Cable`, `Memory`), and returns accessories compatible with `"C001"` | PASS |
| **`[6][1]` Register Return** | `saleId=<registered sale>`, `productIds=["C001"]`, `reason="Falla de encendido"` | Validates 30-day window & sale membership, restores stock (`+1`), cancels console warranties, refunds proportional item price + extended warranty cost, prints `generateReturnReceipt()` | PASS |
| **`[6][2]`–`[6][4]` View Returns (All / By Customer / By Sale)** | Submenu `[6]` → Options `2`, `3`, `4` | Reconstructs `Return` from `ReturnRecord` and prints formatted return receipt | PASS |
| **`[6][5]` Generate Monthly Financial Balance** | `month=10`, `year=2026` | Prints `Total de ventas del mes`, `Total de devoluciones del mes`, and `Balance neto del mes` | PASS |
| **`[7][1]`–`[7][3]` List Warranties (All / Active / Expiring Soon)** | Submenu `[7]` → Options `1`, `2`, `3` (`days=30`) | Reconstructs `BasicWarranty` and `ExtendedWarranty` from `WarrantyRecord` and prints `generateWarrantyCertificate()` | PASS |
| **Differentiated Exception Catching (Req. 6)** | Missing ID / Insufficient Stock / Negative Price / I/O Error | Prints `"No se encontró el recurso solicitado: ..."`, `"No se puede completar la operación: ..."`, `"Los datos ingresados no son válidos: ..."`, or administrator persistence notice | PASS |

---

## 3. Phase 2 SOLID Refactoring Traceability Log

| Class / Interface | Change Made | Violation ID | Applied SOLID Principle | Commit / PR | Responsible |
| :--- | :--- | :--- | :--- | :--- | :--- |
| `PromotionValidator`, `ProductValidator` | Extracted promotional campaign validations (`validatePromotionBase`, `validateCategoryObjective`, `validateMinimumQuantity`) into `PromotionValidator` and kept delegating calls in `ProductValidator` | `V-01` | Single Responsibility Principle (`SRP`) | `feature/solid-refactor` (PR #1) | Carlos Gomez (`craulgomez`) |
| `ProductService` | Simplified `updateStock(String, int)` to mutate the in-memory catalog product and persist `repository.saveAll(products)` directly without secondary snapshot reload/index merge | `V-02` | Single Responsibility Principle (`SRP`) | `feature/solid-refactor` (PR #1) | Carlos Gomez (`craulgomez`) |
| `Accessory`, `Controller`, `Cable`, `Memory` | Added abstract `getAccessoryType()` and `matchesAccessoryType(String)` in `Accessory` and `@Override getAccessoryType()` in `Controller`, `Cable`, and `Memory` | `V-03` | Open/Closed Principle (`OCP`) | `feature/solid-refactor` (PR #1) | Carlos Gomez (`craulgomez`) |
| `Warranty`, `BasicWarranty`, `ExtendedWarranty` | Added `TYPE` constants on `BasicWarranty` (`"Garantía Básica"`) and `ExtendedWarranty` (`"Garantía Extendida"`) and static factory `Warranty.createFromType(...)` in `Warranty` | `V-04` | Open/Closed Principle (`OCP`) | `feature/solid-refactor` (PR #1) | Carlos Gomez (`craulgomez`) |
| `ProductRepository`, `ProductService` | Implemented `Repository<Product>` in `ProductRepository` and inverted `ProductService` field and primary constructor to depend on `Repository<Product>` | `V-06` | Dependency Inversion Principle (`DIP`) | `feature/solid-refactor` (PR #1) | Carlos Gomez (`craulgomez`) |
| `ProductCatalogLookup`, `ProductService` | Created segregated read-only interface `ProductCatalogLookup` (`findById(String)`) and implemented it in `ProductService` | `V-07` | Interface Segregation Principle (`ISP`) & Dependency Inversion Principle (`DIP`) | `feature/solid-refactor` (PR #1) | Carlos Gomez (`craulgomez`) |
| `Promotion`, `PercentageDiscount`, `CategoryDiscount`, `BulkPurchaseDiscount` | Added abstract `getPromotionType()` in `Promotion` and `@Override getPromotionType()` in concrete subclasses | `V-08` | Open/Closed Principle (`OCP`) | `feature/solid-refactor` (PR #1) | Carlos Gomez (`craulgomez`) |
