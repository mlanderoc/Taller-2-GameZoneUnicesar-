# Consolidated Team GRASP Audit — GameZone Unicesar (Requirement 7)

- **Consolidated by:** Merlin Landero (`mlanderoc` — Technical Lead)
- **Source Individual Reports:**
  - [Developer 1 Audit (`craulgomez` — Carlos Gomez)](developer1-audit.md)
  - [Developer 2 Audit (`Hectorguevara22` — Héctor Guevara)](developer2-audit.md)
  - [Technical Lead Audit (`mlanderoc` — Merlin Landero)](leader-audit.md)
- **Audited `develop` Baseline Commit:** `3f14c06`

---

## 1. System-Wide Audit Coverage Table

Every class and method in the `com.gamezone` repository was audited by its responsible team member according to Section 4 of Requirement 7 and `TEAM.md`. Where a class received additive methods from another member in a later requirement (`Sale.canBeReturned` by Developer 1; `ProductService.restoreStock` by the Technical Lead), each member audited the exact methods they developed:

| Package | Class / Interface | Module / Requirement | Audited By | Individual Report Reference |
| :--- | :--- | :--- | :--- | :--- |
| `com.gamezone` | `Main` | Startup (Taller 1 / RE1 / RE2 / RE5) | Technical Lead (`mlanderoc`) | `leader-audit.md` §1, §4 |
| `com.gamezone.model` | `Product` (abstract) | Products (Taller 1) | Developer 1 (`craulgomez`) | `developer1-audit.md` §1, §4 |
| `com.gamezone.model` | `VideoGame` | Products (Taller 1) | Developer 1 (`craulgomez`) | `developer1-audit.md` §1, §4 |
| `com.gamezone.model` | `Console` | Products (Taller 1) | Developer 1 (`craulgomez`) | `developer1-audit.md` §1, §4 |
| `com.gamezone.model` | `Accessory` (abstract) | Accessories (RE1) | Developer 1 (`craulgomez`) | `developer1-audit.md` §1, §4 |
| `com.gamezone.model` | `Controller` | Accessories (RE1) | Developer 1 (`craulgomez`) | `developer1-audit.md` §1, §4 |
| `com.gamezone.model` | `Cable` | Accessories (RE1) | Developer 1 (`craulgomez`) | `developer1-audit.md` §1, §4 |
| `com.gamezone.model` | `Memory` | Accessories (RE1) | Developer 1 (`craulgomez`) | `developer1-audit.md` §1, §4 |
| `com.gamezone.model` | `Promotion` (abstract) | Promotions (RE2) | Developer 1 (`craulgomez`) | `developer1-audit.md` §1, §4 |
| `com.gamezone.model` | `PercentageDiscount` | Promotions (RE2) | Developer 1 (`craulgomez`) | `developer1-audit.md` §1, §4 |
| `com.gamezone.model` | `CategoryDiscount` | Promotions (RE2 / RE5-A1) | Developer 1 (`craulgomez`) | `developer1-audit.md` §1, §4 |
| `com.gamezone.model` | `BulkPurchaseDiscount` | Promotions (RE2) | Developer 1 (`craulgomez`) | `developer1-audit.md` §1, §4 |
| `com.gamezone.model` | `Return` | Returns (RE3 / RE5-A5) | Developer 1 (`craulgomez`) | `developer1-audit.md` §1, §4 |
| `com.gamezone.model` | `Warranty` (abstract) | Warranties (RE4) | Developer 1 (`craulgomez`) | `developer1-audit.md` §1, §4 |
| `com.gamezone.model` | `BasicWarranty` | Warranties (RE4) | Developer 1 (`craulgomez`) | `developer1-audit.md` §1, §4 |
| `com.gamezone.model` | `ExtendedWarranty` | Warranties (RE4) | Developer 1 (`craulgomez`) | `developer1-audit.md` §1, §4 |
| `com.gamezone.model` | `Person` (abstract) | People (Taller 1) | Developer 2 (`Hectorguevara22`) | `developer2-audit.md` §1, §4 |
| `com.gamezone.model` | `Customer` | People (Taller 1) | Developer 2 (`Hectorguevara22`) | `developer2-audit.md` §1, §4 |
| `com.gamezone.model` | `Seller` | People (Taller 1) | Developer 2 (`Hectorguevara22`) | `developer2-audit.md` §1, §4 |
| `com.gamezone.model` | `Sale` *(core, promotions, warranties)* | Sales (Taller 1 / RE2 / RE4 / RE5) | Technical Lead (`mlanderoc`) | `leader-audit.md` §1, §4 |
| `com.gamezone.model` | `Sale.canBeReturned()` *(method)* | Returns (RE3) | Developer 1 (`craulgomez`) | `developer1-audit.md` §1, §4 |
| `com.gamezone.exceptions` | `GameZoneException` (abstract) | Exceptions (RE6) | Developer 1 (`craulgomez`) | `developer1-audit.md` §1, §4 |
| `com.gamezone.exceptions` | `ResourceNotFoundException` | Exceptions (RE6) | Developer 1 (`craulgomez`) | `developer1-audit.md` §1, §4 |
| `com.gamezone.exceptions` | `BusinessRuleException` | Exceptions (RE6) | Developer 1 (`craulgomez`) | `developer1-audit.md` §1, §4 |
| `com.gamezone.exceptions` | `InvalidDataException` | Exceptions (RE6) | Developer 1 (`craulgomez`) | `developer1-audit.md` §1, §4 |
| `com.gamezone.exceptions` | `PersistenceException` | Exceptions (RE6) | Developer 1 (`craulgomez`) | `developer1-audit.md` §1, §4 |
| `com.gamezone.validation` | `ProductValidator` | Validations (RE6) | Developer 1 (`craulgomez`) | `developer1-audit.md` §1, §4 |
| `com.gamezone.validation` | `PersonValidator` | Validations (RE6) | Developer 1 (`craulgomez`) | `developer1-audit.md` §1, §4 |
| `com.gamezone.validation` | `SaleValidator` | Validations (RE6) | Developer 1 (`craulgomez`) | `developer1-audit.md` §1, §4 |
| `com.gamezone.persistence` | `Repository<T>` (interface) | Persistence Base (Taller 1) | Technical Lead (`mlanderoc`) | `leader-audit.md` §2.9 |
| `com.gamezone.persistence` | `ProductRepository` | Products (Taller 1 / RE6) | Developer 1 (`craulgomez`) | `developer1-audit.md` §1, §4 |
| `com.gamezone.persistence` | `PersonRepository` | People (Taller 1 / RE6) | Developer 2 (`Hectorguevara22`) | `developer2-audit.md` §1, §4 |
| `com.gamezone.persistence` | `AccessoryRepository` | Accessories (RE1 / RE6) | Developer 2 (`Hectorguevara22`) | `developer2-audit.md` §1, §4 |
| `com.gamezone.persistence` | `PromotionRepository` | Promotions (RE2 / RE6) | Developer 2 (`Hectorguevara22`) | `developer2-audit.md` §1, §4 |
| `com.gamezone.persistence` | `ReturnRecord` | Returns (RE3) | Developer 2 (`Hectorguevara22`) | `developer2-audit.md` §1, §4 |
| `com.gamezone.persistence` | `ReturnRepository` | Returns (RE3 / RE6) | Developer 2 (`Hectorguevara22`) | `developer2-audit.md` §1, §4 |
| `com.gamezone.persistence` | `WarrantyRecord` | Warranties (RE4) | Developer 2 (`Hectorguevara22`) | `developer2-audit.md` §1, §4 |
| `com.gamezone.persistence` | `WarrantyRepository` | Warranties (RE4 / RE6) | Developer 2 (`Hectorguevara22`) | `developer2-audit.md` §1, §4 |
| `com.gamezone.persistence` | `SaleRecord` | Sales (Taller 1 / RE2 / RE4) | Technical Lead (`mlanderoc`) | `leader-audit.md` §1, §4 |
| `com.gamezone.persistence` | `SaleRepository` | Sales (Taller 1 / RE6) | Technical Lead (`mlanderoc`) | `leader-audit.md` §1, §4 |
| `com.gamezone.service` | `ProductService` *(except `restoreStock`)* | Products (Taller 1 / RE6) | Developer 1 (`craulgomez`) | `developer1-audit.md` §1, §4 |
| `com.gamezone.service` | `ProductService.restoreStock` *(method)* | Returns Integration (RE3) | Technical Lead (`mlanderoc`) | `leader-audit.md` §1, §4 |
| `com.gamezone.service` | `PersonService` | People (Taller 1 / RE6) | Developer 2 (`Hectorguevara22`) | `developer2-audit.md` §1, §4 |
| `com.gamezone.service` | `AccessoryService` | Accessories (RE1 / RE5-A4 / RE6) | Developer 2 (`Hectorguevara22`) | `developer2-audit.md` §1, §4 |
| `com.gamezone.service` | `PromotionService` | Promotions (RE2 / RE5-A1 / RE6) | Developer 2 (`Hectorguevara22`) | `developer2-audit.md` §1, §4 |
| `com.gamezone.service` | `ReturnService` | Returns & Balance (RE3 / RE5 / RE6) | Developer 2 (`Hectorguevara22`) | `developer2-audit.md` §1, §4 |
| `com.gamezone.service` | `WarrantyService` | Warranties (RE4 / RE5-A2/A7) | Developer 2 (`Hectorguevara22`) | `developer2-audit.md` §1, §4 |
| `com.gamezone.service` | `SaleService` | Sales & Unified Checkout (Taller 1 / RE1–RE6) | Technical Lead (`mlanderoc`) | `leader-audit.md` §1, §4 |
| `com.gamezone.ui` | `ConsoleMenu` | Console UI (Taller 1 / RE1–RE6) | Technical Lead (`mlanderoc`) | `leader-audit.md` §1, §4 |
| `com.gamezone.ui` | Swing GUI (`MainFrame`, 9 `*Panel` classes, 7 `*Dialog` classes) | Graphical UI (Taller 1 / RE5 / RE6) | Technical Lead (`mlanderoc`) | `leader-audit.md` §1, §4 |

---

## 2. Final Team Verdicts Across the Nine GRASP Patterns

| # | GRASP Pattern | Developer 1 Verdict | Developer 2 Verdict | Technical Lead Verdict | **Final Team Verdict** | Consolidated Violations |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| 1 | **Information Expert** | Violation (`V-01`) | Violation (`V-01`, `V-02`) | Violation (`V-01`) | **Violation** | **`V-01`, `V-03`, `V-04`** |
| 2 | **Creator** | Correct | Correct | Correct | **Correct** | None |
| 3 | **Controller** | Not Applicable | Correct | Correct | **Correct** | None |
| 4 | **Low Coupling** | Correct | Violation (`V-03`) | Violation (`V-02`) | **Violation** | **`V-06`** |
| 5 | **High Cohesion** | Correct | Violation (`V-04`) | Violation (`V-03`) | **Violation** | **`V-05`, `V-07`** |
| 6 | **Polymorphism** | Violation (`V-02`) | Correct | Correct | **Violation** | **`V-02`** |
| 7 | **Pure Fabrication** | Violation (`V-03`) | Correct *(linked to `V-04`)* | Correct | **Violation** | **`V-05`** *(unified with High Cohesion)* |
| 8 | **Indirection** | Correct | Correct | Correct *(linked to `V-03`)* | **Correct** | None *(helper extraction covered in `V-07`)* |
| 9 | **Protected Variations** | Correct *(linked to `V-02`)* | Correct | Correct | **Correct** | None *(category variation covered in `V-02`)* |
