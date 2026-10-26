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

## Guiding Question 2 — Boundary SOLID Findings Across Team Members' Code

> **Question:** *Some violations manifest at the boundary between two team members' code, for example, when a class owned by one member depends inadequately on a class owned by another. What findings of this type did the team identify? How were they recorded during consolidation, and to whom was their correction assigned? Present at least one concrete case or, if there were none, explain how you verified they did not exist.*
>
> *(Drafted by Developer 2 — `Hectorguevara22`, following team alignment)*

### 1. Identification and Consolidation Protocol for Boundary Findings

Because GameZone Unicesar is organized into horizontal layers (`model`, `validation`, `persistence`, `service`, `ui`) owned by different team members (Developer 1 owning `model` hierarchies and `validation`, Developer 2 owning most `persistence` and `service` modules, and the Technical Lead owning `Sale`, `SaleRepository`, `SaleService`, `ConsoleMenu`, and `Main`), several SOLID violations naturally span the boundary between two or all three members.

During consolidation in `docs/solid-audit/consolidated-audit.md`, the Technical Lead applied three rules to every boundary finding:
1. **Single Unified Identifier (`V-XX`):** When two or three individual audit reports flag the provider side and the consumer side of the same design problem, they are merged into a single consolidated violation ID (`V-01`, `V-03`, `V-04`, `V-05`, `V-06`), referencing each origin report (`developer1-audit`, `developer2-audit`, `leader-audit`).
2. **Strict Ownership Assignment:** Each member modifies **only** the classes under their responsibility as defined in Section 4.1 and `TEAM.md`. Any new abstraction (`PromotionValidator`, `ProductCatalogLookup`, `SaleQueryService`) is created by the owner of the provider module.
3. **Provider-First Commit Sequencing:** In Phase 2 (`feature/solid-refactor`), the provider side (e.g., new interfaces or polymorphic methods in `model`, `validation`, or `persistence`) is committed and merged first so that the consumer side (`service`) can delegate to it without compilation conflicts.

### 2. Concrete Boundary Cases Identified by the Team

| Consolidated ID & Principle | Provider Side (Class & Owner) | Consumer Side (Class & Owner) | How Correction Was Split |
| :--- | :--- | :--- | :--- |
| **`V-01` (SRP)** | `ProductValidator` (`com.gamezone.validation`) — **Developer 1 (`craulgomez`)** | `PromotionService` (`com.gamezone.service`) — **Developer 2 (`Hectorguevara22`)** | **Developer 1** extracts promotion validation rules into `PromotionValidator` (`com.gamezone.validation.PromotionValidator`); **Developer 2** updates `PromotionService` (`validatePromotionBase`, `registerCategoryDiscount`, `registerBulkPurchaseDiscount`) to invoke `PromotionValidator`. |
| **`V-03` (OCP)** | `Accessory`, `Controller`, `Cable`, `Memory` (`com.gamezone.model`) — **Developer 1 (`craulgomez`)** | `AccessoryService.listAccesoriesByType(String)` (`com.gamezone.service`) — **Developer 2 (`Hectorguevara22`)** | **Developer 1** adds abstract `getAccessoryType()` and `matchesAccessoryType(String)` to `Accessory` and overrides `getAccessoryType()` in `Controller`, `Cable`, and `Memory`; **Developer 2** replaces `accessory.getClass().getSimpleName().equalsIgnoreCase(type)` in `AccessoryService` with `accessory.matchesAccessoryType(type)`. |
| **`V-04` (OCP)** | `Warranty`, `BasicWarranty`, `ExtendedWarranty` (`com.gamezone.model`) — **Developer 1 (`craulgomez`)** | `WarrantyService.toWarranty(WarrantyRecord)` (`com.gamezone.service`) — **Developer 2 (`Hectorguevara22`)** | **Developer 1** adds `TYPE` constants on `BasicWarranty` and `ExtendedWarranty` and static factory `Warranty.createFromType(...)` in `Warranty`; **Developer 2** replaces the hardcoded `"Garantía Extendida".equals(...)` branch in `WarrantyService.toWarranty` with `Warranty.createFromType(...)`. |
| **`V-05` (DIP)** | `ProductRepository` (**Developer 1**) & `AccessoryRepository` (**Developer 2**) in `com.gamezone.persistence` | `ProductService` (**Developer 1**), `AccessoryService`, `PromotionService`, `ReturnService`, `WarrantyService` (**Developer 2**), and `SaleService` (**Technical Lead**) in `com.gamezone.service` | **Developer 1** makes `ProductRepository` implement `Repository<Product>` and types `ProductService.repository` as `Repository<Product>`; **Developer 2** makes `AccessoryRepository` implement `Repository<Accessory>` and types repository fields in `AccessoryService`, `PromotionService`, `ReturnService`, and `WarrantyService` as `Repository<T>`; **Technical Lead** types `SaleService.saleRepository` as `Repository<SaleRecord>`. |
| **`V-06` (ISP & DIP)** | `ProductService` (**Developer 1**), `AccessoryService` (**Developer 2**), and `SaleService` (**Technical Lead**) in `com.gamezone.service` | `WarrantyService` and `ReturnService` (**Developer 2**) in `com.gamezone.service` | **Developer 1** creates `ProductCatalogLookup` (`findById(String)`) and implements it in `ProductService`; **Developer 2** implements `ProductCatalogLookup` in `AccessoryService` and updates `WarrantyService` to depend on `ProductCatalogLookup` and `ReturnService` to depend on `SaleQueryService`; **Technical Lead** creates `SaleQueryService` (`findSaleById`, `viewAllSales`) and implements it in `SaleService`. |

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

---

## Guiding Question 4 — Non-Regression Verification Strategy and Evidence

> **Question:** *The refactoring must preserve the external behavior of the system. What strategy does the team follow to guarantee that all menu operations continue working identically? What evidence is shown at the end to demonstrate that nothing was broken?*
>
> *(Drafted by Developer 2 — `Hectorguevara22`, following team alignment)*

### 1. Three-Checkpoint Non-Regression Strategy

Because Requirement 8 reorganizes internal responsibilities, extracts cohesive validators and helpers (`PromotionValidator`, `SaleRecordResolver`, `DataInitializer`), introduces segregated abstractions (`Repository<T>`, `ProductCatalogLookup`, `SaleQueryService`), and replaces runtime reflection/conditionals with polymorphism (`Accessory.matchesAccessoryType`, `Warranty.createFromType`) **without adding or altering any user-facing functionality**, the team enforces a strict three-checkpoint verification protocol documented in `docs/solid-refactor-log.md`:

1. **Checkpoint 1 — Baseline Reference Execution on `develop` (`1bfc5f6`, Before First Refactoring Commit):**
   - Before applying any code change on `feature/solid-refactor`, the team executes all Console Menu flows across all seven main submenus (`[1] Gestion de productos`, `[2] Gestion de personas`, `[3] Gestion de ventas`, `[4] Gestion de promociones`, `[5] Gestion de accesorios`, `[6] Gestion de devoluciones` including `[5] Generar balance mensual`, and `[7] Gestion de garantias`) as well as the Swing GUI (`MainFrame`) and the four differentiated exception categories (`ResourceNotFoundException`, `BusinessRuleException`, `InvalidDataException`, `PersistenceException`).
   - For each operation, the exact test input data, expected Spanish output/receipt format, and persisted `.dat` file effects (`products.dat`, `Accessory.dat`, `customers.dat`, `sellers.dat`, `sales.dat`, `promotion.dat`, `returns.dat`, `warranties.dat`) are recorded in the **Baseline Reference Table** in Section 2 of `docs/solid-refactor-log.md`.
2. **Checkpoint 2 — Per-Pull-RequestIncremental Verification:**
   - Before opening each of the three Phase 2 Pull Requests (`craulgomez`, `Hectorguevara22`, `mlanderoc`), the responsible member compiles the entire project cleanly (`mvn clean compile` / `javac` with zero errors), preserves all existing public constructor signatures via constructor overloading where interfaces are introduced (`Repository<T>`, `ProductCatalogLookup`, `SaleQueryService`), preserves `serialVersionUID` and field layouts on all `Serializable` classes so existing `.dat` files load without `InvalidClassException`, and tests every menu operation affected by their corrected violations (`V-01` through `V-07`).
3. **Checkpoint 3 — Final End-to-End Verification After Integrating All Corrections:**
   - Once all three Pull Requests are integrated on `feature/solid-refactor`, the Technical Lead re-executes the complete baseline verification suite from start to finish and records the post-refactoring results side-by-side against the baseline reference in Section 4 of `docs/solid-refactor-log.md`.

### 2. Concrete Evidence Presented at Completion and Oral Defense

To demonstrate conclusively that zero regressions were introduced, the team provides:
- **Compilation & Binary Compatibility Evidence:** Clean compilation of all source files (`mvn clean compile` / `javac` exit code `0`) and verified deserialization of all eight `.dat` persistence files.
- **Baseline vs. Final Comparison Table in `docs/solid-refactor-log.md`:** A complete scenario-by-scenario matrix covering all 24 menu operations and error-handling paths, mapping each scenario to the specific SOLID violations (`V-01`–`V-07`) that touched its underlying classes and confirming 100% identical outputs, Spanish messages, receipt layouts, proportional refund amounts, monthly financial balances, and warranty certificates.
- **Pull Request Traceability:** Each of the three Phase 2 Pull Requests explicitly lists the corrected violation IDs (`V-XX`) and the verified menu operations in its PR description.

---

## Guiding Question 5 — Correction Order and Multi-Member Branch Coordination

> **Question:** *In what order are the violations corrected and why? When correcting a violation involves code from more than one team member, how is the work coordinated to avoid conflicts on the shared branch?*
>
> *(Drafted by Technical Lead — `mlanderoc`, following team alignment)*

### 1. Rationale for Bottom-Up, Provider-First Correction Sequencing

In Phase 2 (`feature/solid-refactor`), the eight consolidated violations (`V-01` through `V-08`) are corrected in a strict **bottom-up, provider-first sequence** following the architectural dependency graph (`model` & `validation` & `persistence` → `service` → `ui` & `Main`):

1. **Wave 1 — Provider Contracts, Polymorphic Hierarchies, and Core Abstractions (PR #1 — Developer 1 `craulgomez`):**
   - **Why First:** Developer 1 owns the foundational domain hierarchies (`Accessory`, `Warranty`, `Promotion`), the `validation` layer (`ProductValidator`), and the `Product` persistence/service module (`ProductRepository`, `ProductService`). By adding the new polymorphic methods (`Accessory.getAccessoryType()` / `matchesAccessoryType` for `V-03`, `Warranty.createFromType` for `V-04`, `Promotion.getPromotionType()` for `V-08`), extracting `PromotionValidator` (`V-01`), simplifying `ProductService.updateStock` (`V-02`), making `ProductRepository` implement `Repository<Product>` (`V-06`), and creating the segregated `ProductCatalogLookup` interface (`V-07`) **first**—while keeping backward-compatible forwarding methods in `ProductValidator`—every existing caller in `service` and `ui` continues compiling with zero errors.
2. **Wave 2 — Service-Layer Delegations, Repository DIP, and Warranty SRP Extraction (PR #2 — Developer 2 `Hectorguevara22`):**
   - **Why Second:** Once PR #1 is merged into `develop` and synced onto `feature/solid-refactor`, all provider abstractions (`PromotionValidator`, `Accessory.matchesAccessoryType`, `Warranty.createFromType`, `ProductCatalogLookup`) are already available. Developer 2 can then cleanly update `PromotionService` (`V-01`), `AccessoryService.listAccesoriesByType` (`V-03`), `WarrantyService.toWarranty` (`V-04`), extract `SaleRecordResolver` from `WarrantyService` (`V-05`), make `AccessoryRepository` implement `Repository<Accessory>` and type all repository fields in `AccessoryService`, `PromotionService`, `ReturnService`, and `WarrantyService` to `Repository<T>` (`V-06`), and update `AccessoryService` and `WarrantyService` to implement/consume `ProductCatalogLookup` (`V-07`).
3. **Wave 3 — Technical Lead Service/UI Refactoring, Before/After Class Diagrams, and Final Non-Regression Verification (PR #3 — Technical Lead `mlanderoc`):**
   - **Why Third:** After PR #2 is merged into `develop`, the Technical Lead completes the remaining consumer/provider pieces on `feature/solid-refactor`: typing `SaleService.saleRepository` to `Repository<SaleRecord>` (`V-06`), introducing `SaleQueryService` in `SaleService` and wiring `ReturnService` (`V-07`), extracting `DataInitializer` from `Main`, centralizing `ConsoleMenu.handleGameZoneException`, and calling `promotion.getPromotionType()` in `ConsoleMenu.printPromotion` (`V-08`), alongside the `Before` and `After` Mermaid diagrams in `docs/solid-class-diagram.md` and the final end-to-end verification in `docs/solid-refactor-log.md`.

### 2. Coordination Protocol on the Shared `feature/solid-refactor` Branch

To guarantee zero merge conflicts across shared files (`docs/solid-refactor-log.md` and cross-module boundary classes):
1. **Sequential Pull Request Windows:** Only one member commits and pushes to `feature/solid-refactor` at a time. When that member finishes their atomic commits and verifies `mvn clean compile`, they open a Pull Request to `develop`, which is reviewed and merged before the next member syncs `feature/solid-refactor` with `origin/develop`.
2. **Additive Contract Evolution:** When a boundary violation (`V-01`, `V-03`, `V-04`, `V-06`, `V-07`, `V-08`) spans two members, the provider adds the new interface, class, or method without breaking the old signature, so the codebase compiles at every single commit.
3. **Append-Only Traceability Logging:** Each member appends their rows chronologically to Section 3 of `docs/solid-refactor-log.md` during their own PR window and logs their AI consultations exclusively in their own `docs/ai-usage/<role>-ai-log.md` file.
