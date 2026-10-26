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
