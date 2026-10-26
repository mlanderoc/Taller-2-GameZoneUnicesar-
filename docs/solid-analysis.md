# SOLID Principles Analysis and Design Discussion (Requirement 8 — Phase 1)

- **Repository:** GameZone Unicesar (`Taller-2-GameZoneUnicesar-`)
- **Evaluated Baseline Commit on `develop`:** `1bfc5f6`
- **Team Members:**
  - **Technical Lead:** Merlin Landero (`mlanderoc`)
  - **Developer 1:** Carlos Gomez (`craulgomez`)
  - **Developer 2:** Héctor Guevara (`Hectorguevara22`)

---

## Guiding Question 1 — Criteria for Verdicts (`Violation`, `Correct`, `Not Applicable`) and Minimum Required Evidence

> **Question:** *What criterion did the team use to decide whether a code fragment constitutes a violation of a SOLID principle, whether the principle is fulfilled, or whether the principle does not apply? What minimum evidence must accompany each verdict for it to be accepted during consolidation?*
>
> *(Drafted by Developer 1 — `craulgomez`, following team alignment)*

### 1. Decision Criteria per SOLID Principle

To ensure objective, consistent evaluations across all three individual audit reports (`developer1-audit.md`, `developer2-audit.md`, and `leader-audit.md`), the team established an operational decision test for each of the five SOLID principles based on the baseline state of `develop` (`1bfc5f6`):

| Principle | Operational Criterion for `Violation` | Operational Criterion for `Correct` | Operational Criterion for `Not Applicable` |
| :--- | :--- | :--- | :--- |
| **Single Responsibility Principle (SRP)** | A class combines two or more independent axes of change (e.g., `ProductValidator` validating both physical product/accessory catalog rules and marketing promotion rules, `WarrantyService` managing both warranty lifecycles and `SaleRecord`-to-`Sale` reconstruction, or `Main` combining dependency-injection wiring with initial sample data seeding). | Every method and field in the class serves a single cohesive actor or domain concept, so only one type of requirement change forces modifying the class. | Not used for concrete classes, as every class has at least one responsibility to evaluate. |
| **Open/Closed Principle (OCP)** | Adding a new domain subtype or variant forces modifying existing client code because the client relies on runtime reflection (`getClass().getSimpleName()` in `AccessoryService.listAccesoriesByType` or `ConsoleMenu.printPromotion`) or hardcoded string conditionals (`"Garantía Extendida".equals(...)` in `WarrantyService.toWarranty`) instead of an abstract polymorphic contract. | Behavior varies through abstract methods or interfaces (such as `Product.getCategory()` / `belongsToCategory(String)` in `CategoryDiscount` and `Promotion.calculateDiscount(Sale)`), allowing new variants to be added by creating a new subclass without modifying existing callers. | Leaf value objects, DTO records (`SaleRecord`, `ReturnRecord`, `WarrantyRecord`), or utility classes that do not represent extensible behavioral variations. |
| **Liskov Substitution Principle (LSP)** | A subclass strengthens preconditions, weakens postconditions, breaks a state invariant of its superclass, or throws unexpected runtime exceptions for inherited operations when used through a base-type reference. | Every concrete subclass (`VideoGame`, `Console`, `Controller`, `Cable`, `Memory`, `Customer`, `Seller`, `PercentageDiscount`, `CategoryDiscount`, `BulkPurchaseDiscount`, `BasicWarranty`, `ExtendedWarranty`, and `GameZoneException` subtypes) can replace its base class transparently in all client calls. | Classes that neither extend a domain superclass nor serve as a base class for subclasses (e.g., standalone services, repositories without subtyping, and static validators). |
| **Interface Segregation Principle (ISP)** | A client class is forced to depend on a broad concrete class or fat interface containing operations it never uses (e.g., `WarrantyService` depending on the full `ProductService` class—including `registerVideoGame`, `registerConsole`, `updateStock`—when it only calls `findById(String)`, or `ReturnService` depending on the full `SaleService` class when it only queries existing sales). | Clients depend on narrow, role-specific interfaces (such as `Repository<T>` with only `saveAll` and `loadAll`, or segregated query interfaces `ProductCatalogLookup` and `SaleQueryService`) where every declared method is relevant to the consumer. | Pure domain entities and leaf classes that do not act as service/repository contracts consumed by collaborating modules. |
| **Dependency Inversion Principle (DIP)** | A high-level module (`service` or `ui`) depends directly on a low-level concrete implementation (`ProductRepository`, `AccessoryRepository`, `PromotionRepository`, `ReturnRepository`, `WarrantyRepository`, `SaleRepository`) or a concrete repository omits implementing `Repository<T>`, preventing substitution of persistence or collaborator implementations. | High-level modules declare fields and constructor parameters typed to abstractions (`Repository<T>`, `ProductCatalogLookup`, `SaleQueryService`), and low-level implementations implement those abstractions. | Pure domain entities (`model`), DTO records, and custom exceptions (`exceptions`) that sits at the bottom of the dependency graph and do not depend on lower-level infrastructure modules. |

### 2. Minimum Evidence Required for Consolidation Acceptance

During consolidation in `docs/solid-audit/consolidated-audit.md`, the Technical Lead only accepts a verdict from an individual report if it includes:
1. **For a `Violation` verdict:**
   - The **exact class name, method signature, file path, and line numbers** on `develop` (`1bfc5f6`), accompanied by the literal code snippet exhibiting the flaw.
   - A technical **explanation** linking the snippet to the violated SOLID principle.
   - A concrete **consequence** describing the maintenance fragility or ripple effect triggered by a foreseeable change.
   - A behavior-preserving **proposed solution** respecting the layered architecture (`ui → service → persistence → model`, plus `validation` and `exceptions`).
   - Explicit identification of whether fixing the violation requires touching **another team member's code** (boundary finding).
2. **For a `Correct` verdict:**
   - The specific **class and method names** (with line ranges) and a concrete explanation of how the implementation satisfies the principle.
3. **For a `Not Applicable` verdict:**
   - A clear structural **justification** explaining why the principle has no point of application in the audited class (for example, absence of inheritance hierarchies for LSP).

---

## Guiding Question 3 — Relationship Between the SOLID Audit and the Requirement 7 GRASP Audit

> **Question:** *What relationship did the team find between the findings of this audit and those of the Requirement 7 GRASP audit? Were there SOLID violations in code that had already been corrected by GRASP, or GRASP corrections that simultaneously solved a SOLID problem? Present at least one concrete case of each situation found.*
>
> *(Drafted by Developer 1 — `craulgomez`, following team alignment)*

### 1. Conceptual Synergy Between GRASP and SOLID

While **GRASP** focuses on *how responsibilities are assigned to classes and collaborations* (*Information Expert*, *Creator*, *Controller*, *Low Coupling*, *High Cohesion*, *Polymorphism*, *Pure Fabrication*, *Indirection*, *Protected Variations*), **SOLID** evaluates the *structural stability, extensibility, and dependency direction of those classes and abstractions* over time (**SRP**, **OCP**, **LSP**, **ISP**, **DIP**). Comparing our Requirement 7 baseline (`docs/grasp-refactor-log.md`) against our Requirement 8 SOLID audit revealed three concrete relationships:

### 2. Case A — GRASP Corrections That Simultaneously Solved a SOLID Problem (`GRASP Polymorphism` → `SOLID OCP`)

- **Concrete Case:** **`CategoryDiscount.calculateDiscount(Sale)` and the `Product` Hierarchy (`GRASP V-02` solved `SOLID OCP`).**
- **Before Requirement 7:** `CategoryDiscount.calculateDiscount(Sale)` checked product categories using an explicit chain of type checks (`if ("VIDEOGAME".equals(categoryObjective) && product instanceof VideoGame) ... else if ("CONSOLE".equals(...) && product instanceof Console) ... else if ("ACCESSORY".equals(...) && product instanceof Accessory)`). That design violated both **GRASP Polymorphism** and the **Open/Closed Principle (OCP)**, because introducing a new `Product` category required modifying `CategoryDiscount`.
- **Impact of the GRASP Correction:** By adding `public abstract String getCategory()` and `public boolean belongsToCategory(String category)` to `Product` (`src/main/java/com/gamezone/model/Product.java`, lines 119–131) and overriding `getCategory()` in `VideoGame`, `Console`, and `Accessory`, `CategoryDiscount.calculateDiscount(Sale)` (`src/main/java/com/gamezone/model/CategoryDiscount.java`, line 60) now simply calls `product.belongsToCategory(categoryObjective)`. Consequently, when audited in Requirement 8 under **OCP**, `Product` and `CategoryDiscount` received a **`Correct`** verdict without needing further modification.

### 3. Case B — SOLID Violations in Code Already Refactored by GRASP (`GRASP V-05` → `SOLID SRP V-01`)

- **Concrete Case:** **`ProductValidator` Accumulating Both Product/Accessory Rules and Promotional Campaign Rules (`GRASP V-05` introduced `SOLID V-01` under SRP).**
- **What Happened in Requirement 7 (`V-05`):** During the GRASP audit, `AccessoryService` (`registerCable`, `registerMemory`) and `PromotionService` (`validatePromotionBase`, `registerCategoryObjective`, `registerBulkPurchaseDiscount`) contained inline validation checks instead of delegating to the `com.gamezone.validation` package. To restore **GRASP Pure Fabrication** and **High Cohesion** in `PromotionService` without creating new classes during Requirement 7, the team placed `validatePromotionBase`, `validateCategoryObjective`, and `validateMinimumQuantity` inside the existing `ProductValidator` class (`src/main/java/com/gamezone/validation/ProductValidator.java`, lines 124–169).
- **Why It Failed the SOLID SRP Audit (`V-01`):** Under the **Single Responsibility Principle (SRP)**, a class must have only one reason to change. Placing promotional campaign validation inside `ProductValidator` coupled two independent business domains: (1) physical catalog/inventory rules (`Product`, `Cable`, `Memory`, stock quantities) and (2) marketing discount campaign rules (`Promotion` date windows, discount percentages, target category names, and bulk volume thresholds). In Requirement 8 Phase 2, this is resolved by extracting `PromotionValidator` (`com.gamezone.validation.PromotionValidator`) and updating `PromotionService` to depend on it.

### 4. Case C — Structural Observations Left Out of Scope in GRASP That Directly Manifest as SOLID Violations (`GRASP F-01` & `V-03` → `SOLID DIP / ISP / OCP`)

- **Concrete Case 1 (`AccessoryService.listAccesoriesByType`):** In Requirement 7 (`V-03`), `AccessoryService.findAcessoriesCOmpatibleWith(String)` was refactored to delegate to `accessory.isCompatibleWith(consoleId)` (*Information Expert*), but `AccessoryService.listAccesoriesByType(String)` (lines 74–76) continued using `accessory.getClass().getSimpleName().equalsIgnoreCase(type)`. Under the Requirement 8 **OCP** audit, this reflection-based filtering was flagged as violation `V-03` and is solved by introducing `Accessory.getAccessoryType()` and `matchesAccessoryType(String)`.
- **Concrete Case 2 (`Service-to-Repository` and `Service-to-Service` Concrete Coupling):** In Requirement 7, the high number of concrete collaborator references in `SaleService`, `ReturnService`, and `WarrantyService` was recorded as Out-of-Scope Observation `F-01` because resolving it required introducing new interfaces across modules. Under Requirement 8, **ISP** and **DIP** directly address this root cause (`V-05` and `V-06`) by having `ProductRepository` and `AccessoryRepository` implement `Repository<T>`, typing service repository references to `Repository<T>`, and introducing segregated lookup interfaces (`ProductCatalogLookup` and `SaleQueryService`).
