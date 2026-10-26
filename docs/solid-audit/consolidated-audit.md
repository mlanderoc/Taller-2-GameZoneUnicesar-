# Consolidated Team SOLID Audit Report — GameZone Unicesar (Requirement 8 — Phase 1)

- **Consolidated by:** Merlin Landero (`mlanderoc`) — Technical Lead
- **Baseline `develop` Commit Audited:** `1bfc5f6` (with Requirement 7 GRASP corrections integrated)
- **Individual Audit Reports Consolidated:**
  - [docs/solid-audit/developer1-audit.md](developer1-audit.md) — Carlos Gomez (`craulgomez`, Developer 1)
  - [docs/solid-audit/developer2-audit.md](developer2-audit.md) — Héctor Guevara (`Hectorguevara22`, Developer 2)
  - [docs/solid-audit/leader-audit.md](leader-audit.md) — Merlin Landero (`mlanderoc`, Technical Lead)

---

## 1. System-Wide Audit Coverage Verification Table

The table below verifies that all 41 classes and interfaces of the GameZone Unicesar system across `com.gamezone`, `com.gamezone.model`, `com.gamezone.exceptions`, `com.gamezone.validation`, `com.gamezone.persistence`, `com.gamezone.service`, and `com.gamezone.ui` were audited by their responsible owner without omissions or duplicate ownership (cross-owner methods on shared classes are explicitly partitioned as specified in Section 4.1):

| # | Class / Interface | Package | Module (Requirement) | Audited By | Individual Report |
| :--- | :--- | :--- | :--- | :--- | :--- |
| 1 | `Product` | `com.gamezone.model` | Products (Taller 1 & Req. 7) | Carlos Gomez (`craulgomez`) | `developer1-audit.md` |
| 2 | `VideoGame` | `com.gamezone.model` | Products (Taller 1 & Req. 7) | Carlos Gomez (`craulgomez`) | `developer1-audit.md` |
| 3 | `Console` | `com.gamezone.model` | Products (Taller 1 & Req. 7) | Carlos Gomez (`craulgomez`) | `developer1-audit.md` |
| 4 | `Accessory` | `com.gamezone.model` | Accessories (Req. 1 & Req. 7) | Carlos Gomez (`craulgomez`) | `developer1-audit.md` |
| 5 | `Controller` | `com.gamezone.model` | Accessories (Req. 1) | Carlos Gomez (`craulgomez`) | `developer1-audit.md` |
| 6 | `Cable` | `com.gamezone.model` | Accessories (Req. 1) | Carlos Gomez (`craulgomez`) | `developer1-audit.md` |
| 7 | `Memory` | `com.gamezone.model` | Accessories (Req. 1) | Carlos Gomez (`craulgomez`) | `developer1-audit.md` |
| 8 | `Person` | `com.gamezone.model` | People (Taller 1) | Héctor Guevara (`Hectorguevara22`) | `developer2-audit.md` |
| 9 | `Customer` | `com.gamezone.model` | People (Taller 1) | Héctor Guevara (`Hectorguevara22`) | `developer2-audit.md` |
| 10 | `Seller` | `com.gamezone.model` | People (Taller 1) | Héctor Guevara (`Hectorguevara22`) | `developer2-audit.md` |
| 11 | `Sale` *(core, promotions, warranty cost, `getDiscountRatio`)* | `com.gamezone.model` | Sales & Integrations (Taller 1, Req. 2, 4, 5, 7) | Merlin Landero (`mlanderoc`) *(with `canBeReturned` by `craulgomez` and `findProductById` by `Hectorguevara22`)* | `leader-audit.md`, `developer1-audit.md`, `developer2-audit.md` |
| 12 | `Promotion` | `com.gamezone.model` | Promotions (Req. 2) | Carlos Gomez (`craulgomez`) | `developer1-audit.md` |
| 13 | `PercentageDiscount` | `com.gamezone.model` | Promotions (Req. 2) | Carlos Gomez (`craulgomez`) | `developer1-audit.md` |
| 14 | `CategoryDiscount` | `com.gamezone.model` | Promotions (Req. 2 & Req. 7) | Carlos Gomez (`craulgomez`) | `developer1-audit.md` |
| 15 | `BulkPurchaseDiscount` | `com.gamezone.model` | Promotions (Req. 2) | Carlos Gomez (`craulgomez`) | `developer1-audit.md` |
| 16 | `Return` | `com.gamezone.model` | Returns (Req. 3 & Req. 7) | Carlos Gomez (`craulgomez`) | `developer1-audit.md` |
| 17 | `Warranty` | `com.gamezone.model` | Warranties (Req. 4) | Carlos Gomez (`craulgomez`) | `developer1-audit.md` |
| 18 | `BasicWarranty` | `com.gamezone.model` | Warranties (Req. 4) | Carlos Gomez (`craulgomez`) | `developer1-audit.md` |
| 19 | `ExtendedWarranty` | `com.gamezone.model` | Warranties (Req. 4) | Carlos Gomez (`craulgomez`) | `developer1-audit.md` |
| 20 | `GameZoneException` | `com.gamezone.exceptions` | Exceptions (Req. 6) | Carlos Gomez (`craulgomez`) | `developer1-audit.md` |
| 21 | `ResourceNotFoundException` | `com.gamezone.exceptions` | Exceptions (Req. 6) | Carlos Gomez (`craulgomez`) | `developer1-audit.md` |
| 22 | `BusinessRuleException` | `com.gamezone.exceptions` | Exceptions (Req. 6) | Carlos Gomez (`craulgomez`) | `developer1-audit.md` |
| 23 | `InvalidDataException` | `com.gamezone.exceptions` | Exceptions (Req. 6) | Carlos Gomez (`craulgomez`) | `developer1-audit.md` |
| 24 | `PersistenceException` | `com.gamezone.exceptions` | Exceptions (Req. 6) | Carlos Gomez (`craulgomez`) | `developer1-audit.md` |
| 25 | `ProductValidator` | `com.gamezone.validation` | Validations (Req. 6 & Req. 7) | Carlos Gomez (`craulgomez`) | `developer1-audit.md` |
| 26 | `PersonValidator` | `com.gamezone.validation` | Validations (Req. 6) | Carlos Gomez (`craulgomez`) | `developer1-audit.md` |
| 27 | `SaleValidator` | `com.gamezone.validation` | Validations (Req. 6) | Carlos Gomez (`craulgomez`) | `developer1-audit.md` |
| 28 | `Repository<T>` | `com.gamezone.persistence` | Persistence Contract (Taller 1) | Team Shared (`craulgomez`, `Hectorguevara22`, `mlanderoc`) | `developer1-audit.md`, `developer2-audit.md`, `leader-audit.md` |
| 29 | `ProductRepository` | `com.gamezone.persistence` | Products (Taller 1 & Req. 6) | Carlos Gomez (`craulgomez`) | `developer1-audit.md` |
| 30 | `PersonRepository` | `com.gamezone.persistence` | People (Taller 1 & Req. 6) | Héctor Guevara (`Hectorguevara22`) | `developer2-audit.md` |
| 31 | `AccessoryRepository` | `com.gamezone.persistence` | Accessories (Req. 1 & Req. 6) | Héctor Guevara (`Hectorguevara22`) | `developer2-audit.md` |
| 32 | `PromotionRepository` | `com.gamezone.persistence` | Promotions (Req. 2 & Req. 6) | Héctor Guevara (`Hectorguevara22`) | `developer2-audit.md` |
| 33 | `ReturnRecord` | `com.gamezone.persistence` | Returns DTO (Req. 3) | Héctor Guevara (`Hectorguevara22`) | `developer2-audit.md` |
| 34 | `ReturnRepository` | `com.gamezone.persistence` | Returns (Req. 3 & Req. 6) | Héctor Guevara (`Hectorguevara22`) | `developer2-audit.md` |
| 35 | `WarrantyRecord` | `com.gamezone.persistence` | Warranties DTO (Req. 4) | Héctor Guevara (`Hectorguevara22`) | `developer2-audit.md` |
| 36 | `WarrantyRepository` | `com.gamezone.persistence` | Warranties (Req. 4 & Req. 6) | Héctor Guevara (`Hectorguevara22`) | `developer2-audit.md` |
| 37 | `SaleRecord` | `com.gamezone.persistence` | Sales DTO (Taller 1 & Req. 5) | Merlin Landero (`mlanderoc`) | `leader-audit.md` |
| 38 | `SaleRepository` | `com.gamezone.persistence` | Sales (Taller 1 & Req. 6) | Merlin Landero (`mlanderoc`) | `leader-audit.md` |
| 39 | `ProductService` *(core methods by `craulgomez`; `restoreStock` by `mlanderoc`)* | `com.gamezone.service` | Products & Returns (Taller 1, Req. 3, 6, 7) | Carlos Gomez (`craulgomez`) & Merlin Landero (`mlanderoc`) | `developer1-audit.md`, `leader-audit.md` |
| 40 | `PersonService` | `com.gamezone.service` | People (Taller 1 & Req. 6) | Héctor Guevara (`Hectorguevara22`) | `developer2-audit.md` |
| 41 | `AccessoryService` | `com.gamezone.service` | Accessories (Req. 1, 5, 6, 7) | Héctor Guevara (`Hectorguevara22`) | `developer2-audit.md` |
| 42 | `PromotionService` | `com.gamezone.service` | Promotions (Req. 2, 6, 7) | Héctor Guevara (`Hectorguevara22`) | `developer2-audit.md` |
| 43 | `ReturnService` | `com.gamezone.service` | Returns & Balance (Req. 3, 5, 6, 7) | Héctor Guevara (`Hectorguevara22`) | `developer2-audit.md` |
| 44 | `WarrantyService` | `com.gamezone.service` | Warranties (Req. 4, 5, 6) | Héctor Guevara (`Hectorguevara22`) | `developer2-audit.md` |
| 45 | `SaleService` | `com.gamezone.service` | Sales & Unified Flow (Taller 1, Req. 1, 2, 4, 5, 6, 7) | Merlin Landero (`mlanderoc`) | `leader-audit.md` |
| 46 | `ConsoleMenu` | `com.gamezone.ui` | Console UI (Taller 1 & Req. 1–6) | Merlin Landero (`mlanderoc`) | `leader-audit.md` |
| 47 | `Main` | `com.gamezone` | Entry Point & Preloading (Taller 1 & Req. 1, 2, 5, 7) | Merlin Landero (`mlanderoc`) | `leader-audit.md` |

---

## 2. Final Team Verdict by SOLID Principle

| SOLID Principle | Developer 1 Verdict (`craulgomez`) | Developer 2 Verdict (`Hectorguevara22`) | Technical Lead Verdict (`mlanderoc`) | **Final Consolidated Team Verdict** | Consolidated Violations Linked |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **Single Responsibility Principle (SRP)** | Violation (`V-01`, `V-02`) | Violation (`V-01` + boundary `PromotionService`) | Violation (`V-01`, `V-02`) | **Violation** | **`V-01`**, **`V-02`**, **`V-05`**, **`V-08`** |
| **Open/Closed Principle (OCP)** | Violation (`V-03`, `V-04`) | Violation (`V-02`, `V-03`) | Violation (`V-03`) | **Violation** | **`V-03`**, **`V-04`**, **`V-08`** |
| **Liskov Substitution Principle (LSP)** | Correct | Correct | Correct | **Correct** | *None (100% compliant across all hierarchies)* |
| **Interface Segregation Principle (ISP)** | Violation (`V-06`) | Violation (`V-04`) | Violation (`V-04`) | **Violation** | **`V-07`** |
| **Dependency Inversion Principle (DIP)** | Violation (`V-05`) | Violation (`V-05`) | Violation (`V-05`) | **Violation** | **`V-06`**, **`V-07`** |

---

## 3. Definitive List of Consolidated SOLID Violations (`V-01` – `V-08`)

| Consolidated ID | Principle | Evidence on `develop` (`1bfc5f6`) | Explanation | Consequence | Agreed Solution (Phase 2) | Origin Report(s) | Responsible for Correction |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **`V-01`** | **Single Responsibility Principle (SRP)** | `ProductValidator.java` (lines 124–169: `validatePromotionBase`, `validateCategoryObjective`, `validateMinimumQuantity`) and `PromotionService.java` (lines 52, 60, 67). | `ProductValidator` combines two unrelated axes of change: physical catalog/inventory rules (`Product`, `Cable`, `Memory`, stock) and marketing promotional campaign rules (`Promotion`, `CategoryDiscount`, `BulkPurchaseDiscount`). | Any change to promotional campaign rules forces modifying `ProductValidator`, coupling inventory validation to marketing rules. | **Dev 1** creates `PromotionValidator` in `com.gamezone.validation` with `validatePromotionBase`, `validateCategoryObjective`, and `validateMinimumQuantity` (keeping forwarding calls in `ProductValidator`); **Dev 2** updates `PromotionService` to invoke `PromotionValidator`. | `developer1-audit V-01` & `developer2-audit` SRP boundary note | **Carlos Gomez (`craulgomez`)** & **Héctor Guevara (`Hectorguevara22`)** |
| **`V-02`** | **Single Responsibility Principle (SRP)** | `ProductService.java` (`updateStock`, lines 116–134): `List<Product> products = repository.loadAll(); for (int i = 0; i < products.size(); i++) { ... } repository.saveAll(products);` | `ProductService.updateStock` mixes in-memory stock update coordination on `this.products` with manual file-snapshot reloading and index-replacement merging. | Changes to snapshot synchronization require modifying `ProductService.updateStock` separately from `registerVideoGame` and `registerConsole`. | **Dev 1** refactors `ProductService.updateStock(String, int)` to mutate the catalog product and persist `repository.saveAll(this.products)` directly. | `developer1-audit V-02` | **Carlos Gomez (`craulgomez`)** |
| **`V-03`** | **Open/Closed Principle (OCP)** | `Accessory.java` (lines 11–88), `Controller.java`, `Cable.java`, `Memory.java`, and `AccessoryService.java` (`listAccesoriesByType`, lines 74–76: `accessory.getClass().getSimpleName().equalsIgnoreCase(type)`). | Filtering accessories by type relies on Java reflection (`getClass().getSimpleName()`) in `AccessoryService` instead of a polymorphic contract on `Accessory`. | Adding a new `Accessory` subtype whose type identifier differs from its simple Java class name, or wrapping an accessory, breaks type filtering unless `AccessoryService` is modified. | **Dev 1** adds `public abstract String getAccessoryType()` and `public boolean matchesAccessoryType(String)` in `Accessory` and overrides `getAccessoryType()` in `Controller`, `Cable`, and `Memory`; **Dev 2** updates `AccessoryService.listAccesoriesByType(String)` to call `accessory.matchesAccessoryType(type)`. | `developer1-audit V-03` & `developer2-audit V-02` | **Carlos Gomez (`craulgomez`)** & **Héctor Guevara (`Hectorguevara22`)** |
| **`V-04`** | **Open/Closed Principle (OCP)** | `Warranty.java` (lines 10–131), `BasicWarranty.java`, `ExtendedWarranty.java`, and `WarrantyService.java` (`toWarranty`, lines 156–160: `if ("Garantía Extendida".equals(record.getWarrantyType())) ...`). | `WarrantyService.toWarranty(WarrantyRecord)` uses a hardcoded string conditional on `"Garantía Extendida"` to instantiate concrete `Warranty` subclasses. | Introducing a new `Warranty` subclass forces modifying `WarrantyService.toWarranty` to add another conditional branch. | **Dev 1** defines `TYPE` constants on `BasicWarranty` and `ExtendedWarranty` and adds `Warranty.createFromType(...)`; **Dev 2** updates `WarrantyService.toWarranty(WarrantyRecord)` to delegate to `Warranty.createFromType(...)`. | `developer1-audit V-04` & `developer2-audit V-03` | **Carlos Gomez (`craulgomez`)** & **Héctor Guevara (`Hectorguevara22`)** |
| **`V-05`** | **Single Responsibility Principle (SRP)** | `WarrantyService.java` (`resolveSaleById`, lines 163–187). | `WarrantyService` combines warranty lifecycle management with `SaleRecord`-to-`Sale` object-graph reconstruction (`resolveSaleById`), duplicating sale-mapping logic from `SaleService`. | Changes to how `SaleRecord` is reconstructed into a `Sale` require modifying `WarrantyService` alongside `SaleService`. | **Dev 2** extracts `SaleRecord`-to-`Sale` resolution from `WarrantyService.resolveSaleById` into a dedicated helper (`SaleRecordResolver` in `com.gamezone.service`) or delegates sale lookup cleanly. | `developer2-audit V-01` | **Héctor Guevara (`Hectorguevara22`)** |
| **`V-06`** | **Dependency Inversion Principle (DIP)** | `ProductRepository.java` (line 20), `AccessoryRepository.java` (line 9), `ProductService.java` (lines 16, 24), `AccessoryService.java` (lines 16, 24), `PromotionService.java` (lines 16, 19), `ReturnService.java` (lines 28, 42, 58), `WarrantyService.java` (lines 25–26, 37), and `SaleService.java` (lines 29, 36, 40). | `ProductRepository` and `AccessoryRepository` do not implement `Repository<T>`, and the six services declare their repository fields/constructors using concrete file-repository classes instead of `Repository<T>`. | High-level services are tightly coupled to low-level binary file serialization classes, preventing substitution of persistence implementations. | **Dev 1** makes `ProductRepository` implement `Repository<Product>` and updates `ProductService` to depend on `Repository<Product>`; **Dev 2** makes `AccessoryRepository` implement `Repository<Accessory>` and updates `AccessoryService`, `PromotionService`, `ReturnService`, and `WarrantyService` to depend on `Repository<T>`; **Technical Lead** updates `SaleService` to depend on `Repository<SaleRecord>`. | `developer1-audit V-05`, `developer2-audit V-05`, `leader-audit V-05` | **Carlos Gomez (`craulgomez`)**, **Héctor Guevara (`Hectorguevara22`)** & **Merlin Landero (`mlanderoc`)** |
| **`V-07`** | **Interface Segregation Principle (ISP) & Dependency Inversion Principle (DIP)** | `ProductService.java` (lines 14–149), `AccessoryService.java` (lines 14–113), `SaleService.java` (lines 28–275), `WarrantyService.java` (lines 27, 37), and `ReturnService.java` (lines 29, 42, 58). | `WarrantyService` only needs `findById(String)` from `ProductService`, and `ReturnService` only needs `findSaleById(String)` and `viewAllSales()` from `SaleService`, yet both depend on the full concrete service classes exposing registration and stock-mutation methods they never use. | Collaborating services are coupled to fat concrete service contracts, violating ISP and DIP. | **Dev 1** creates `ProductCatalogLookup` (`Product findById(String id);`) in `com.gamezone.service` and implements it in `ProductService`; **Technical Lead** creates `SaleQueryService` (`findSaleById`, `viewAllSales`) and implements it in `SaleService`; **Dev 2** implements `ProductCatalogLookup` in `AccessoryService` and updates `WarrantyService` and `ReturnService` to depend on `ProductCatalogLookup` and `SaleQueryService` (preserving overloaded constructors). | `developer1-audit V-06`, `developer2-audit V-04`, `leader-audit V-04` | **Carlos Gomez (`craulgomez`)**, **Héctor Guevara (`Hectorguevara22`)** & **Merlin Landero (`mlanderoc`)** |
| **`V-08`** | **Single Responsibility Principle (SRP) & Open/Closed Principle (OCP)** | `Main.java` (lines 59–101: `preloadSellersIfNeeded`, `preloadPromotionsIfNeeded`, `preloadAccessoriesIfNeeded`) and `ConsoleMenu.java` (lines 100–112, 425–660 repeated catch blocks; line 678 `promotion.getClass().getSimpleName()`). | `Main` mixes dependency-graph composition with sample data seeding (**SRP**); `ConsoleMenu` repeats 4-branch `GameZoneException` formatting in 9 methods (**SRP**) and uses reflection `promotion.getClass().getSimpleName()` in `printPromotion` (**OCP**). | Seed-data changes force editing `Main`; exception message changes require editing 9 methods in `ConsoleMenu`; `printPromotion` is coupled to Java class names. | **Dev 1** adds `getPromotionType()` to `Promotion` and its subclasses; **Technical Lead** extracts `DataInitializer` from `Main` (retaining delegating methods in `Main`), extracts `handleGameZoneException(GameZoneException)` in `ConsoleMenu`, and updates `ConsoleMenu.printPromotion` to call `promotion.getPromotionType()`. | `leader-audit V-01`, `leader-audit V-02`, `leader-audit V-03` | **Merlin Landero (`mlanderoc`)** & **Carlos Gomez (`craulgomez`)** |

---

## 4. Boundary Findings Unification (Hallazgos de Frontera)

Several SOLID violations identified during the individual audits span the boundary between classes developed by two or all three team members. In compliance with Section 3.3 and Section 4.1, each boundary problem is registered once in Section 3 above and its Phase 2 correction is partitioned strictly by code ownership:

1. **`V-01` — Single Responsibility Principle (`SRP`) Across `ProductValidator` and `PromotionService`:**
   - **Boundary Nature:** Developer 1 (`craulgomez`) owns the `com.gamezone.validation` package (`ProductValidator`), whereas Developer 2 (`Hectorguevara22`) owns `PromotionService` (`com.gamezone.service.PromotionService`).
   - **Division of Correction:**
     - **Developer 1 (`craulgomez`, PR #1):** Creates `PromotionValidator` (`com.gamezone.validation.PromotionValidator`) with `validatePromotionBase`, `validateCategoryObjective`, and `validateMinimumQuantity`, while retaining delegating methods in `ProductValidator` so the project compiles cleanly at every intermediate commit.
     - **Developer 2 (`Hectorguevara22`, PR #2):** Updates `PromotionService` (`validatePromotionBase`, `registerCategoryDiscount`, `registerBulkPurchaseDiscount`) to invoke `PromotionValidator` directly.

2. **`V-03` — Open/Closed Principle (`OCP`) Across the `Accessory` Hierarchy and `AccessoryService`:**
   - **Boundary Nature:** Developer 1 (`craulgomez`) owns `Accessory`, `Controller`, `Cable`, and `Memory` (`com.gamezone.model`), whereas Developer 2 (`Hectorguevara22`) owns `AccessoryService` (`com.gamezone.service`).
   - **Division of Correction:**
     - **Developer 1 (`craulgomez`, PR #1):** Adds `public abstract String getAccessoryType()` and `public boolean matchesAccessoryType(String type)` to `Accessory`, and implements `@Override public String getAccessoryType()` in `Controller` (`"Controller"`), `Cable` (`"Cable"`), and `Memory` (`"Memory"`).
     - **Developer 2 (`Hectorguevara22`, PR #2):** Refactors `AccessoryService.listAccesoriesByType(String)` to delegate to `accessory.matchesAccessoryType(type)` instead of `accessory.getClass().getSimpleName().equalsIgnoreCase(type)`.

3. **`V-04` — Open/Closed Principle (`OCP`) Across the `Warranty` Hierarchy and `WarrantyService`:**
   - **Boundary Nature:** Developer 1 (`craulgomez`) owns `Warranty`, `BasicWarranty`, and `ExtendedWarranty` (`com.gamezone.model`), whereas Developer 2 (`Hectorguevara22`) owns `WarrantyService` (`com.gamezone.service`).
   - **Division of Correction:**
     - **Developer 1 (`craulgomez`, PR #1):** Adds `public static final String TYPE` constants to `BasicWarranty` (`"Garantía Básica"`) and `ExtendedWarranty` (`"Garantía Extendida"`) and adds the static factory method `Warranty.createFromType(String warrantyType, String warrantyId, Product product, Sale sale, LocalDate startDate)` in `Warranty`.
     - **Developer 2 (`Hectorguevara22`, PR #2):** Refactors `WarrantyService.toWarranty(WarrantyRecord)` to delegate subtype creation to `Warranty.createFromType(...)`.

4. **`V-06` — Dependency Inversion Principle (`DIP`) Across Persistence Repositories and Business Services:**
   - **Boundary Nature:** `Repository<T>` (`com.gamezone.persistence.Repository`) is shared across all three members' modules: Developer 1 owns `ProductRepository` and `ProductService`; Developer 2 owns `AccessoryRepository`, `PromotionRepository`, `ReturnRepository`, `WarrantyRepository`, `AccessoryService`, `PromotionService`, `ReturnService`, and `WarrantyService`; and the Technical Lead owns `SaleRepository` and `SaleService`.
   - **Division of Correction:**
     - **Developer 1 (`craulgomez`, PR #1):** Updates `ProductRepository` to implement `Repository<Product>` and updates `ProductService` to depend on `Repository<Product>`.
     - **Developer 2 (`Hectorguevara22`, PR #2):** Updates `AccessoryRepository` to implement `Repository<Accessory>` and updates `AccessoryService`, `PromotionService`, `ReturnService`, and `WarrantyService` to type their repository fields and constructors to `Repository<T>`.
     - **Technical Lead (`mlanderoc`, PR #3):** Updates `SaleService` to type `saleRepository` and its constructors to `Repository<SaleRecord>`.

5. **`V-07` — Interface Segregation Principle (`ISP`) & Dependency Inversion Principle (`DIP`) Across Collaborating Services:**
   - **Boundary Nature:** `WarrantyService` (Developer 2) depends on `ProductService` (Developer 1) solely for `findById(String)`, and `ReturnService` (Developer 2) depends on `SaleService` (Technical Lead) solely for `findSaleById(String)` and `viewAllSales()`.
   - **Division of Correction:**
     - **Developer 1 (`craulgomez`, PR #1):** Creates the segregated interface `ProductCatalogLookup` (`Product findById(String id);`) in `com.gamezone.service` and implements it in `ProductService`.
     - **Developer 2 (`Hectorguevara22`, PR #2):** Implements `ProductCatalogLookup` in `AccessoryService` and updates `WarrantyService` (plus `SaleRecordResolver`) to depend on `ProductCatalogLookup`.
     - **Technical Lead (`mlanderoc`, PR #3):** Creates the segregated interface `SaleQueryService` (`Sale findSaleById(String saleId); List<Sale> viewAllSales();`) in `com.gamezone.service`, implements it in `SaleService`, and wires `ReturnService` against `SaleQueryService`.

6. **`V-08` — Open/Closed Principle (`OCP`) Across `Promotion` and `ConsoleMenu.printPromotion`:**
   - **Boundary Nature:** Developer 1 (`craulgomez`) owns the `Promotion` hierarchy (`com.gamezone.model`), whereas the Technical Lead (`mlanderoc`) owns `ConsoleMenu` (`com.gamezone.ui`).
   - **Division of Correction:**
     - **Developer 1 (`craulgomez`, PR #1):** Adds `public String getPromotionType()` to `Promotion` and overrides it in `PercentageDiscount`, `CategoryDiscount`, and `BulkPurchaseDiscount`.
     - **Technical Lead (`mlanderoc`, PR #3):** Refactors `ConsoleMenu.printPromotion(Promotion)` to call `promotion.getPromotionType()` instead of `promotion.getClass().getSimpleName()`.

---

## 5. Out-of-Scope Functional Findings (Hallazgos Fuera de Alcance)

In compliance with Section 2.2 of the Requirement 8 specification, functional defects whose correction would alter the observable console outputs or persisted file paths are **not** modified in Phase 2 (and findings `F-01` and `F-02` already recorded in Requirement 7 are not repeated here). The following two new functional observations were identified during the SOLID audit and are documented for a future functional maintenance cycle:

| Observation ID | Location & Evidence on `develop` (`1bfc5f6`) | Description of Functional Observation | Justification for Exclusion from Phase 2 (Behavior Preservation Rule) |
| :--- | :--- | :--- | :--- |
| **`OS-01`** | `Cable.getDescription()` (`src/main/java/com/gamezone/model/Cable.java`, lines 76–82) and `Memory.getDescription()` (`src/main/java/com/gamezone/model/Memory.java`, lines 76–81) vs. `Controller.getDescription()` (`Controller.java`, lines 55–58). | While `Controller.getDescription()` calls `super.getDescription()` (which includes `"Compatible console: " + compatibleConsoleIds`), `Cable.getDescription()` and `Memory.getDescription()` build their description strings from scratch (`"Title: " + getTitle() + ...`) without calling `super.getDescription()`, omitting the compatible console list when cables and memories are printed. | Modifying `Cable.getDescription()` or `Memory.getDescription()` to include `compatibleConsoleIds` would change the visible console text printed by Menu Options `[5][1] Listar todos los accesorios`, `[5][2] Buscar accesorios compatibles con consola`, and `Sale.generateReceipt()`, violating the strict behavior-preservation rule of Section 2 and Section 5.1. |
| **`OS-02`** | `PersonRepository` (`src/main/java/com/gamezone/persistence/PersonRepository.java`, lines 30 and 35):<br>`private final String CUSTOMER_FILE = "customers.dat";`<br>`private final String SELLER_FILE = "sellers.dat";` | Unlike `ProductRepository` (`"data/products.dat"`), `AccessoryRepository` (`"data/Accessory.dat"`), `SaleRepository` (`"data/sales.dat"`), `PromotionRepository` (`"data/promotion.dat"`), `ReturnRepository` (`"data/returns.dat"`), and `WarrantyRepository` (`"data/warranties.dat"`), `PersonRepository` stores `customers.dat` and `sellers.dat` in the project root directory rather than inside `data/`. | Changing `CUSTOMER_FILE` and `SELLER_FILE` to `"data/customers.dat"` and `"data/sellers.dat"` would alter the persisted file paths on disk, violating Section 2's requirement that all operations continue using the exact same data files. |
