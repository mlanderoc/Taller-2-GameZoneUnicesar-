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
