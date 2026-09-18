# Team

## Members

| Name | Student Code | GitHub Username | Role | Modules | Feature Branches |
|---|---|---|---|---|---|
| Merlin Landero | 1065563683 | mlanderoc | Technical Lead | Sales, UI (Console & Swing), System Integration & Reports | `feature/sale-module`, `feature/view-module` |
| Carlos Gomez | 1066867142 | craulgomez | Developer 1 | Domain Models: Products, Accessories (RE1), Returns (RE3), Warranties (RE4) | `feature/product-module`, `feature/accessory-module`, `feature/return-module`, `feature/warranty-module` |
| Héctor Guevara | 1067602882 | Hectorguevara22 | Developer 2 | Persistence & Services: People, Accessories (RE1), Returns (RE3), Warranties (RE4) | `feature/person-module`, `feature/accessory-module`, `feature/return-module`, `feature/warranty-module` |

## Class Distribution

### Technical Lead (Merlin Landero)
- **Base / Sales:** `Sale`, `SaleRecord`, `SaleRepository`, `SaleService`
- **UI & Presentation:** `ConsoleMenu`, `MainFrame`, `SalePanel`, `SaleDialog`, `CustomerPanel`, `CustomerDialog`, `SellerPanel`, `ProductPanel`, `ProductDialog`, `AccessoryPanel`, `AccessoryDialog`, `ReturnPanel`, `ReturnDialog`, `WarrantyPanel`, `PromotionPanel`, `PromotionDialog`, `ReportsPanel`, `DetailDialog`
- **Application Core:** `Main`

### Developer 1 (Carlos Gomez) - Domain Models
- **Products (Base):** `Product` (abstract), `VideoGame`, `Console`
- **Accessories (RE1):** `Accessory` (abstract), `Controller`, `Cable`, `Memory`
- **Returns (RE3):** `Return`, additive method `Sale.canBeReturned()`
- **Warranties (RE4):** `Warranty` (abstract), `BasicWarranty`, `ExtendedWarranty`
- **Promotions:** `Promotion` (abstract), `PercentageDiscount`, `CategoryDiscount`, `BulkPurchaseDiscount`

### Developer 2 (Héctor Guevara) - Persistence & Services
- **People (Base):** `Person` (abstract), `Customer`, `Seller`, `PersonRepository`, `PersonService`
- **Accessories (RE1):** `AccessoryRepository`, `AccessoryService` (console compatibility logic)
- **Returns (RE3):** `ReturnRecord`, `ReturnRepository`, `ReturnService` (30-day rule, return validation, monthly balance report)
- **Warranties (RE4):** `WarrantyRecord`, `WarrantyRepository`, `WarrantyService` (automatic basic warranty, extended warranty, expiration tracking)
- **Promotions:** `PromotionRepository`, `PromotionService` (best promotion calculation)

## Committed Activities

### Technical Lead (Merlin Landero)
1. Repository initialization, branching model configuration (`main`, `develop`), and branch protection rules.
2. Initial Maven project structure with multi-layer architecture packages.
3. Implementation of `Sale`, `SaleRepository`, and `SaleService` with base transaction validations.
4. Additive system integrations in `SaleService`:
   - Unified accessory sales with inventory deduction delegation.
   - Automatic basic warranty assignment on console sales and extended warranty fee application.
5. Implementation of full Swing GUI (`MainFrame`, sub-panels, modal dialogs, and balance/expiry reports).
6. Cross-review and approval of Pull Requests from Developer 1 and Developer 2.
7. Documentation maintenance (`README.md`, `TEAM.md`).

### Developer 1 (Carlos Gomez)
1. Implementation of the `Product` hierarchy (`Product`, `VideoGame`, `Console`) with polymorphic description formatting.
2. Implementation of the `Accessory` hierarchy (`Accessory`, `Controller`, `Cable`, `Memory`) including console compatibility attributes (RE1).
3. Implementation of the `Return` domain entity (`calculateRefundAmount`, `generateReturnReceipt`) and `Sale.canBeReturned` (RE3).
4. Implementation of the `Warranty` hierarchy (`Warranty`, `BasicWarranty`, `ExtendedWarranty`) with duration and cost calculation (RE4).
5. Comprehensive JavaDoc documentation in English for all domain classes.
6. Pull Request submissions to Technical Lead for each feature module.

### Developer 2 (Héctor Guevara)
1. Implementation of the `Person` hierarchy (`Customer`, `Seller`), `PersonRepository`, and `PersonService` with preloaded seller data.
2. Implementation of `AccessoryRepository` and `AccessoryService` with console compatibility filtering (RE1).
3. Implementation of `ReturnRepository` and `ReturnService` with 30-day time window validation, item-sale verification, and stock restoration (RE3).
4. Implementation of `generateMonthlyBalance` calculating net revenue (sales minus returns) (RE3).
5. Implementation of `WarrantyRepository` and `WarrantyService` for warranty lookup, active warranties, and expiring soon queries (RE4).
6. Comprehensive JavaDoc documentation in English for repositories and services.
7. Pull Request submissions to Technical Lead for each feature module.
