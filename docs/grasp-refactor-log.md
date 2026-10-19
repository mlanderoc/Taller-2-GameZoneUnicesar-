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
