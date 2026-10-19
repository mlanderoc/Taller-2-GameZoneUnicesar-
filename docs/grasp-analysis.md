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
