# System Integration Analysis — GameZone Unicesar (RE5)

During the independent implementation of the four evolutionary modules — **Accessories (RE1)**, **Promotions (RE2)**, **Returns (RE3)**, and **Warranties (RE4)** — each module operated against the baseline system from Workshop 1. When all four modules were integrated into a single system, seven cross-module inconsistencies and gaps (`A1` through `A7`) emerged. This document analyzes the root cause and technical solution applied for each adjustment.

---

## Adjustment A1 — Category Discounts Extended to Accessories (`feature/accessory-category-discount`)

### 1. Description of the Adjustment
Enable `CategoryDiscount` and `PromotionService` to support `"ACCESSORY"` as a valid promotional category in addition to `"VIDEOGAME"` and `"CONSOLE"`, and preload a fourth default promotion (`PROMO004` — 25% discount on accessories).

### 2. Root Cause
When the Promotions module (RE2) was designed against the baseline Workshop 1 product hierarchy, only `VideoGame` and `Console` existed as subclasses of `Product`. Meanwhile, the Accessories module (RE1) introduced `Accessory` (`Controller`, `Cable`, `Memory`) as a third branch of `Product`. Because `CategoryDiscount.matchesCategory(Product)` and `PromotionService.validateCategory(String)` only checked for `"VIDEOGAME"` and `"CONSOLE"`, accessories could never receive category-specific discounts.

### 3. Applied Solution
- **`CategoryDiscount`**: Updated `matchesCategory(Product)` to evaluate `"ACCESSORY".equals(category) && product instanceof Accessory` alongside `"VIDEOGAME"` and `"CONSOLE"`. Because `Controller`, `Cable`, and `Memory` inherit from `Accessory`, a single polymorphic `instanceof Accessory` check covers all three subtypes.
- **`PromotionService`**: Updated `validateCategory(String)` to accept `"VIDEOGAME"`, `"CONSOLE"`, and `"ACCESSORY"`.
- **Presentation & Preload (`ConsoleMenu`, `PromotionDialog`, `Main`)**: Updated the category prompts and combo boxes to include `"ACCESSORY"` and preloaded `PROMO004` (`"Descuento accesorios"`, 25% off `"ACCESSORY"`).

---

## Adjustment A2 — Elimination of Circular Dependency Between `SaleService` and `WarrantyService` (`fix/warranty-circular-dependency`)

### 1. Description of the Adjustment
Remove the circular dependency between `SaleService` and `WarrantyService` so both services can be initialized purely via constructor injection without temporary `null` state or setter workarounds.

### 2. Root Cause
`SaleService` required `WarrantyService` during `registerSale` to automatically assign `BasicWarranty` and optional `ExtendedWarranty` to sold consoles. Simultaneously, `WarrantyService` depended on `SaleService` (`saleService.findSaleById(saleId)`) to reconstruct `Warranty` objects from `WarrantyRecord` entries stored in `warranties.dat`. This cycle (`SaleService ↔ WarrantyService`) forced `Main` to instantiate `SaleService` with a missing dependency and wire `WarrantyService` afterward via `setWarrantyService`.

### 3. Applied Solution
- **`WarrantyService`**: Replaced the `SaleService` dependency with `(WarrantyRepository, SaleRepository, ProductService)`. `WarrantyService` now resolves `SaleRecord` and `Product` references directly from persistence and `ProductService` when reconstructing `Warranty` objects.
- **`SaleService` & `Main`**: Passed `WarrantyService` directly into the `SaleService` constructor, establishing a strictly acyclic initialization order in `Main`:
  1. `ProductService`, `PersonService`, `AccessoryService`, `PromotionService`
  2. `WarrantyService` (depends on `WarrantyRepository`, `SaleRepository`, `ProductService`)
  3. `SaleService` (receives all required services in its constructor)
  4. `ReturnService` (receives `SaleService`, `ProductService`, `AccessoryService`, `WarrantyService`)

---

## Adjustment A3 — Unified Sale Registration Flow (`refactor/unified-sale-registration`)

### 1. Description of the Adjustment
Unify the `SaleService.registerSale` workflow so that accessories, promotional discounts, and extended warranties interact in a deterministic order without contaminating the product subtotal or miscalculating percentage discounts.

### 2. Root Cause
Each module modified `SaleService.registerSale` and `Sale` for its own concern. Previously, when an `ExtendedWarranty` (10% of the console's price) was assigned, its additional cost was added directly to `sale.subtotal` (`sale.setSubtotal(sale.getSubtotal() + warranty.getAdditionalCost())`). If a percentage promotion was evaluated afterward, the discount was erroneously applied to the warranty cost as well, or the receipt failed to distinguish between product subtotal, promotional discount, and extended warranty charges.

### 3. Applied Solution
- **`Sale` & `SaleRecord`**: Added a dedicated `extendedWarrantyCost` attribute (separate from `subtotal` and `discountAmount`) and persisted it in `sales.dat`.
- **Financial Formula**: Defined `Sale.calculateTotal()` as:
  $$\text{total} = \text{subtotal} - \text{discountAmount} + \text{extendedWarrantyCost}$$
  Promotional discounts (`Promotion.calculateDiscount(Sale)`) are computed strictly over the product `subtotal` before extended warranties are added.
- **Unified 8-Step Sequence in `SaleService.registerSale`**:
  1. Validate customer, seller, and non-empty product list (`VideoGame`, `Console`, `Accessory`).
  2. Verify available stock for each item across `ProductService` and `AccessoryService`.
  3. Create the `Sale` instance and compute the initial product `subtotal`.
  4. Evaluate active promotions via `PromotionService.findBestPromotionFor(sale)` and apply the highest discount over the product `subtotal`.
  5. Deduct stock in `ProductService` (for video games/consoles) or `AccessoryService` (for accessories).
  6. Assign `BasicWarranty` to every `Console` and `ExtendedWarranty` to consoles whose IDs are in `productIdsWithExtendedWarranty`, accumulating `extendedWarrantyCost`.
  7. Persist the `SaleRecord` in `sales.dat` and update customer/seller histories.
  8. Return the registered `Sale` with its complete receipt breakdown (`Subtotal`, `Promoción aplicada`, `Descuento aplicado`, `Costo garantía extendida`, and `Total final a pagar`).

---

## Adjustment A4 — Stock Restoration of Returned Accessories (`fix/return-accessory-stock`)

### 1. Description of the Adjustment
Ensure that returning an `Accessory` properly restores its inventory in `AccessoryRepository` (`Accessory.dat`) through `AccessoryService.restoreStock`.

### 2. Root Cause
The Returns module (RE3) was originally built to restore stock via `ProductService.restoreStock`, which only looks up items in `products.dat` (`VideoGame` and `Console`). Because `Accessory` instances are persisted separately in `Accessory.dat` via `AccessoryRepository`, returning an accessory requires delegating stock restoration and ID resolution to `AccessoryService`.

### 3. Applied Solution
- **`AccessoryService`**: Verified `restoreStock(String accessoryId, int quantity)` to increment the accessory's stock (`accessory.getStock() + quantity`) and persist the updated list in `AccessoryRepository`.
- **`ReturnService`**: In `registerReturn`, checked `if (product instanceof Accessory)` to delegate stock restoration to `accessoryService.restoreStock(product.getId(), 1)`, and delegating `VideoGame`/`Console` items to `productService.restoreStock(product.getId(), 1)`. In `toReturn(ReturnRecord)`, resolved product IDs across both `ProductService` and `AccessoryService`.

---

## Adjustment A5 — Proportional Refund Calculation for Discounted Sales (`fix/return-discounted-refund`)

### 1. Description of the Adjustment
Calculate product refunds proportionally when returning items from a sale that had a promotional discount applied, rather than refunding the full list price.

### 2. Root Cause
`Return.calculateRefundAmount()` originally summed `product.getPrice()` for each returned item without checking whether `originalSale` had a promotional discount (`discountAmount > 0`). If a customer bought items with a 20% discount and later returned one item, the system refunded 100% of the list price—more than the customer had actually paid.

### 3. Applied Solution
- **`Return.calculateRefundAmount()`**: Computed the discount ratio of the original sale (`discountRatio = originalSale.getDiscountAmount() / originalSale.getSubtotal()` when `subtotal > 0`) and calculated each returned unit's refund as:
  $$\text{itemRefund} = \text{price} \times \left(1 - \frac{\text{discountAmount}}{\text{subtotal}}\right)$$
- **`Return.generateReturnReceipt()`**: Updated the receipt to display, for every returned item, its list price, proportional discount deducted, and net refunded amount.
- **`ReturnDialog`**: Updated the live refund preview in the Swing dialog to reflect the proportional formula.

---

## Adjustment A6 — Complete Monthly Financial Balance Breakdown (`fix/monthly-balance-report`)

### 1. Description of the Adjustment
Expose dedicated methods in `ReturnService` to query monthly gross sales and monthly refunded returns individually alongside the net monthly balance, and display all three metrics in the UI.

### 2. Root Cause
`ReturnService.generateMonthlyBalance(int month, int year)` previously computed total sales and total returns in local variables and returned only `totalSales - totalReturns`. As a result, `ConsoleMenu` only printed a single net balance figure without showing total monthly sales or total monthly returns, and `ReportsPanel` had to duplicate the summation loops in the UI layer.

### 3. Applied Solution
- **`ReturnService`**:
  - Added `calculateMonthlySales(int month, int year): double`, summing `sale.getTotalAmount()` (final amount after discounts and including extended warranties) for all sales in that month and year.
  - Added `calculateMonthlyReturns(int month, int year): double`, summing `returnObj.getRefundAmount()` for all returns in that month and year.
  - Refactored `generateMonthlyBalance(int month, int year): double` to return `calculateMonthlySales(month, year) - calculateMonthlyReturns(month, year)` while preserving its method signature.
- **`ConsoleMenu` & `ReportsPanel`**: Updated both interfaces to call all three `ReturnService` methods and display total monthly sales, total monthly returns, and net monthly balance.

---

## Adjustment A7 — Warranty Cancellation and Refund on Console Return (`feature/return-warranty-cancellation`)

### 1. Description of the Adjustment
Automatically cancel associated warranties (`BasicWarranty` and `ExtendedWarranty`) when a `Console` is returned, remove them from `warranties.dat`, and refund the extended warranty cost as part of the return.

### 2. Root Cause
The Returns module (RE3) and the Warranties module (RE4) were developed independently. When a console was returned within the 30-day window, its stock was restored and its purchase price refunded, but its warranties remained active in `warranties.dat` and any 10% `ExtendedWarranty` surcharge paid by the customer was not refunded.

### 3. Applied Solution
- **`WarrantyService`**: Added `cancelWarranties(String productId, String saleId): double`, which removes all `WarrantyRecord` entries matching `productId` and `saleId` from `warranties.dat` and returns the sum of `warranty.getAdditionalCost()` (`0.0` for `BasicWarranty`, `10%` of console price for `ExtendedWarranty`).
- **`ReturnService`**: Injected `WarrantyService` via constructor (`Main` and `MainFrame`). During `registerReturn`, for every returned item where `product instanceof Console`, invoked `warrantyService.cancelWarranties(product.getId(), saleId)` and added the returned amount to the `Return` instance (`setWarrantyRefundAmount`).
- **`Return`**: Added `warrantyRefundAmount` to `calculateRefundAmount()` and displayed the refunded extended warranty line in `generateReturnReceipt()`.
