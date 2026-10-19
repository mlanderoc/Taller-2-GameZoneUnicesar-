# GRASP Patterns Audit and Refactoring Analysis (Requirement 7)

This document records the team's answers to the five guiding questions defined in **Requirement 7 — GRASP Patterns Audit and Refactoring** for the **GameZone Unicesar** system.

---

## 1. Criteria for Verdict Classification and Minimum Required Evidence

**Question:** *What criteria did the team use to decide whether a code fragment constitutes a violation of a GRASP pattern, whether the pattern is properly applied, or whether the pattern does not apply? What minimum evidence must accompany each verdict so that it is accepted in the consolidation?*

### Verdict Decision Criteria

To ensure consistency across all three individual reports (`developer1-audit.md`, `developer2-audit.md`, and `leader-audit.md`), the team established an objective three-way decision rubric anchored on the responsibility question of each of the nine GRASP patterns:

1. **Violation (`Violation`):**
   A code fragment is classified as a violation when a responsibility is placed in a class that lacks the primary domain information to fulfill it (**Information Expert**), instantiates objects without owning or aggregating them (**Creator**), uses explicit type-checking conditionals (`instanceof` or `getClass().getSimpleName()`) instead of dynamic dispatch across a hierarchy (**Polymorphism**), mixes unrelated concerns such as business orchestration and low-level input validation inside the same class (**High Cohesion** / **Pure Fabrication**), or introduces unnecessary dependencies across modules (**Low Coupling** / **Indirection** / **Protected Variations**). Crucially, a finding is only accepted as a design violation if correcting it reorganizes internal responsibilities **without altering the external behavior** perceived by store clerks.
2. **Correct (`Correct`):**
   A pattern is classified as correctly applied when the audited classes actively face the design problem addressed by that GRASP pattern and resolve it according to the pattern's principle—for example, `Product.updateStock(int)` mutating its own inventory count (**Information Expert**), `Promotion.calculateDiscount(Sale)` and `Warranty.getAdditionalCost()` dispatching behavior polymorphically across subclasses (**Polymorphism**), or repositories (`ProductRepository`, `PersonRepository`, `SaleRepository`) acting as non-domain persistence classes (**Pure Fabrication** and **Indirection**).
3. **Not Applicable (`Not Applicable`):**
   A pattern is classified as not applicable when the subset of classes audited by a team member does not contain a structural application point for that pattern's responsibility question. For example, **Controller** (`Who receives and coordinates system events from the UI?`) does not apply to pure domain value/entity hierarchies (`Product`, `Accessory`, `Promotion`, `Warranty`) or exception classes (`GameZoneException`), because those classes sit at the bottom of the dependency graph and never receive UI events.

### Minimum Evidence Required for Consolidation Acceptance

For the Technical Lead to accept any verdict into `docs/grasp-audit/consolidated-audit.md`, each individual report must provide:
- **For a `Violation` verdict:**
  1. Unique local identifier (`V-01`, `V-02`, ...), violated GRASP pattern name, and the module/requirement where the code was developed.
  2. **Exact code evidence** on the audited `develop` commit (`3f14c06`): fully qualified class name, method signature, line numbers, and the verbatim code snippet where the violation occurs.
  3. **Design explanation and concrete consequence:** why the fragment breaks the pattern and what maintenance hazard it creates today or under foreseeable changes (e.g., adding a new product category or changing discount rules).
  4. **Actionable refactoring proposal** and explicit declaration of whether fixing it touches another team member's code (boundary finding).
- **For a `Correct` verdict:**
  Concrete citation of the class name, method signature, and line range demonstrating how the responsibility is properly assigned in `develop`.
- **For a `Not Applicable` verdict:**
  Architectural justification explaining why the audited classes do not participate in the interaction governed by that pattern.

---

## 2. Boundary Findings Between Team Members' Code

**Question:** *Some violations manifest at the boundary between the code of two team members, for example when a class of one member depends inappropriately on a class of another. What findings of this type did the team identify? How were they recorded in the consolidation and to whom was their correction assigned? Present at least one concrete case or, if there were none, explain how you verified that none existed.*

### Boundary Findings Identified by the Team

During the individual audits, the team identified **four concrete boundary findings** where a responsibility crossed the ownership boundary between two team members:

1. **Boundary Case 1 — Discount Ratio Calculation Between `Return` (Developer 1) and `Sale` (Technical Lead):**
   - **Evidence:** In `Return.getDiscountRatio()` (`Return.java`, lines 139–144, developed by Developer 1), `Return` extracts `originalSale.getDiscountAmount()` and `originalSale.getSubtotal()` from `Sale` (developed by the Technical Lead) to compute `discountAmount / subtotal` externally.
   - **Consolidation & Assignment:** Unified in `consolidated-audit.md` as violation `V-01` (**Information Expert**). In Phase 2, the **Technical Lead (`mlanderoc`)** adds `public double getDiscountRatio()` to `Sale`, and **Developer 1 (`craulgomez`)** refactors `Return.getDiscountRatio()` to delegate to `originalSale.getDiscountRatio()`.
2. **Boundary Case 2 — Product Lookup Within a Sale Between `ReturnService` (Developer 2) and `Sale` (Technical Lead):**
   - **Evidence:** In `ReturnService.findInSale(Sale sale, String productId)` (`ReturnService.java`, lines 210–217, developed by Developer 2), `ReturnService` iterates over `sale.getProducts()` externally because `Sale` (Technical Lead) did not expose an expert lookup method `findProductById(String productId)`.
   - **Consolidation & Assignment:** Unified in `consolidated-audit.md` as violation `V-04` (**Information Expert**). In Phase 2, the **Technical Lead (`mlanderoc`)** adds `public Product findProductById(String productId)` to `Sale`, and **Developer 2 (`Hectorguevara22`)** refactors `ReturnService.findInSale` to delegate to `sale.findProductById(productId)`.
3. **Boundary Case 3 — Console Compatibility Check Between `AccessoryService` (Developer 2) and `Accessory` (Developer 1):**
   - **Evidence:** In `AccessoryService.findAcessoriesCOmpatibleWith(String consoleId)` (`AccessoryService.java`, lines 72–78, Developer 2), the service calls `accessory.getCompatibleConsoleIds().contains(consoleId)` instead of invoking `accessory.isCompatibleWith(consoleId)` defined in `Accessory` (`Accessory.java`, lines 56–63, Developer 1).
   - **Consolidation & Assignment:** Recorded in `consolidated-audit.md` as `V-03` (**Information Expert**). **Developer 1 (`craulgomez`)** ensures null-safe list traversal in `Accessory.isCompatibleWith(String)`, and **Developer 2 (`Hectorguevara22`)** refactors `AccessoryService.findAcessoriesCOmpatibleWith` to call `accessory.isCompatibleWith(consoleId)`.
4. **Boundary Case 4 — Subtype & Promotion Validations Between `ProductValidator` (Developer 1) and `AccessoryService` / `PromotionService` (Developer 2) & Constructor Coupling Between `AccessoryService` (Developer 2) and `Main` (Technical Lead):**
   - **Evidence:** `AccessoryService` (`AccessoryService.java`, lines 38–40 & 57–59) and `PromotionService` (`PromotionService.java`, lines 53–58, 66–68, 74–90) retained inline `InvalidDataException` / `BusinessRuleException` checks because `ProductValidator` (Developer 1) only covered base `Product` attributes (`V-05`). Additionally, `Main.java` (line 40, Technical Lead) had to pass `accessoryRepository.loadAll()` into `new AccessoryService(accessoryRepository, accessoryRepository.loadAll())` (`V-06`).
   - **Consolidation & Assignment:** **Developer 1 (`craulgomez`)** adds the missing static validator methods to `ProductValidator`, **Developer 2 (`Hectorguevara22`)** delegates validation in `AccessoryService` and `PromotionService` and adds the single-argument `AccessoryService(AccessoryRepository)` constructor, and the **Technical Lead (`mlanderoc`)** updates `Main` to invoke `new AccessoryService(accessoryRepository)`.

---

## 3. Overlapping GRASP Patterns and De-Duplication of Findings

**Question:** *Some patterns are closely related to each other. Did the team find code fragments that violate more than one pattern at the same time? How were they recorded without duplicating findings and how was it decided which pattern guides the correction?*

### Overlapping Violations Identified

Yes. Because several GRASP patterns reinforce one another (for instance, **Polymorphism** and **Protected Variations**, or **Pure Fabrication** and **High Cohesion**, or **Information Expert** and **Low Coupling**), the team identified three code fragments that simultaneously touch more than one pattern:

1. **`CategoryDiscount.calculateDiscount(Sale)` (`CategoryDiscount.java`, lines 78–90):**
   - **Patterns involved:** **Polymorphism** and **Protected Variations** (and **Information Expert** on `Product`).
   - **Why both apply:** Using `product instanceof VideoGame`, `product instanceof Console`, and `product instanceof Accessory` violates **Polymorphism** (dispatching behavior via type conditionals instead of overriding a method in the hierarchy) and simultaneously weakens **Protected Variations** (failing to shield `CategoryDiscount` from the addition of new `Product` subclasses).
   - **How it was recorded and guided:** Recorded once (`V-02` in `developer1-audit.md`, consolidated as `V-02`) under **Polymorphism** as the primary guiding pattern, because introducing `public abstract String getCategory()` and `belongsToCategory(String)` in `Product` is the direct polymorphic mechanism that automatically satisfies Protected Variations.
2. **Inline subtype validations in `AccessoryService` & `PromotionService` vs. `com.gamezone.validation` (`ProductValidator` / `PromotionValidator`):**
   - **Patterns involved:** **Pure Fabrication** and **High Cohesion**.
   - **Why both apply:** Keeping format/range validations (`lengthInMeters <= 0`, `storageCapacityGb <= 0`, promotion date/percentage checks) inside `AccessoryService` and `PromotionService` lowers the **High Cohesion** of the services while leaving the **Pure Fabrication** validation layer (`com.gamezone.validation`) incomplete.
   - **How it was recorded and guided:** Recorded without duplication by assigning the missing validator methods in `ProductValidator` under **Pure Fabrication** and the extraction of inline checks from `PromotionService` / `AccessoryService` under **High Cohesion / Pure Fabrication**, guided by extracting validation rules into `com.gamezone.validation`.
3. **External calculation of `discountAmount / subtotal` in `Return.getDiscountRatio()` (`Return.java`, lines 139–144):**
   - **Patterns involved:** **Information Expert** and **Low Coupling**.
   - **Why both apply:** `Return` reaches into `Sale`'s internal state (`getDiscountAmount()` and `getSubtotal()`) to compute a ratio that belongs to `Sale`.
   - **How it was recorded and guided:** Recorded under **Information Expert** as the guiding pattern, since placing `getDiscountRatio()` in `Sale` (the owner of `subtotal` and `discountAmount`) directly resolves the coupling in `Return`.

### De-Duplication Rule

To avoid inflating or duplicating violations across the nine pattern sections:
- Each problematic code fragment is registered **only once** as a numbered `Violation` (`V-XX`) under the **most specific root-cause pattern** that dictates the concrete code refactoring (e.g., **Polymorphism** when replacing `instanceof` with an overridden method; **Information Expert** when moving a calculation to the class that owns the attributes; **Pure Fabrication / High Cohesion** when moving non-domain validation rules to a dedicated validator).
- In the section of the secondary related pattern (e.g., **Protected Variations**), the report cross-references the primary violation ID (`V-XX`) instead of creating a duplicate entry.

---

## 4. Strategy and Evidence for Preserving External System Behavior (Non-Regression)

**Question:** *The refactoring must preserve the external behavior of the system. What strategy does the team follow to guarantee that all menu operations continue working the same way? What evidence is shown at the end to demonstrate that nothing was broken?*

### Non-Regression Verification Strategy

Because Phase 2 of Requirement 7 reorganizes internal responsibilities (such as moving calculations to Information Experts, replacing `instanceof` category checks with polymorphic dispatch, and delegating inline service validations to `com.gamezone.validation`) without altering user-visible functionality, the team follows a three-checkpoint verification protocol documented in `docs/grasp-refactor-log.md`:

1. **Baseline Reference Execution on `develop` (Before First Correction):**
   Before applying any refactoring on `feature/grasp-refactor`, the team runs and records a complete reference test suite covering every Console Menu operation (`1` through `28` across Products, Customers/Sellers, Sales, Accessories, Promotions, Returns/Monthly Balance, and Warranties) along with the four differentiated exception paths (`ResourceNotFoundException`, `BusinessRuleException`, `InvalidDataException`, `PersistenceException`).
2. **Per-Pull-Request Verification (During Phase 2):**
   After each team member completes their assigned `refactor:` commits on `feature/grasp-refactor`, they run `mvn clean compile` / `javac` and execute the specific menu operations affected by their changes (e.g., Developer 1 verifies category discount calculation and return receipt generation; Developer 2 verifies console compatibility lookup, promotion registration validation, and return registration; Technical Lead verifies sale registration, stock restoration, and startup initialization).
3. **Final End-to-End Verification (Before Merging Phase 2):**
   Once all violations `V-01` through `V-07` are integrated on `feature/grasp-refactor`, the Technical Lead executes the full 28-operation verification matrix and compares every output, receipt, Spanish error message, and `.dat` file state against the baseline reference.

### Evidence Presented at Completion

The team records and presents in `docs/grasp-refactor-log.md` and during the oral defense:
- **Zero-error compilation evidence** (`mvn clean compile`).
- **Complete Reference vs. Post-Refactor Verification Table** covering all menu operations with exact input values, expected outputs/receipts, and pass/match status (`IDENTICAL`).
- **Confirmation of unchanged `.dat` binary serialization compatibility** (`products.dat`, `Accessory.dat`, `customers.dat`, `sellers.dat`, `sales.dat`, `promotion.dat`, `returns.dat`, `warranties.dat`).
