# Individual SOLID Audit Report — Technical Lead (`mlanderoc`)

- **Auditor:** Merlin Landero (`mlanderoc`)
- **Role:** Technical Lead — Sales Module, Console UI, Application Entry Point (`Main`), System Integration, and Team Consolidation
- **Evaluated Branch & Commit:** `develop` (`1bfc5f6`)
- **Requirement:** Requirement 8 — Phase 1: SOLID Principles Audit

---

## 1. Audit Scope

In accordance with Section 4.1 of the Requirement 8 specification, `TEAM.md`, and `docs/grasp-refactor-log.md`, this audit covers all classes, methods, and cross-module integrations developed or modified under the Technical Lead's responsibility across Taller 1 and Requirements 1 through 7:

| Module / Requirement | Audited Classes & Methods | Package |
| :--- | :--- | :--- |
| **Sales, UI & Startup (Taller 1)** | `Sale` (`Sale` constructor, `getId`, `getDate`, `getCustomer`, `getSeller`, `getProducts`, `setProducts`, `getSubtotal`, `setSubtotal`, `getTotalAmount`, `calculateTotal`, `generateReceipt`), `SaleRecord`, `SaleRepository` (`saveAll`, `loadAll`), `SaleService` (`registerSale`, `viewAllSales`, `viewSalesByCustomer`, `viewSalesBySeller`, `findSaleById`, `toRecord`, `toSale`), `ConsoleMenu` (`start`, `printMainMenu`, `showProductMenu`, `showPeopleMenu`, `showSalesMenu`, `registerSale`, `viewAllSales`, `viewSalesByCustomer`, `viewSalesBySeller`, `registerVideoGame`, `registerConsole`, `listAllProducts`, `registerCustomer`, `listAllCustomers`, `listAllSellers`), `Main` (`main`, `preloadSellersIfNeeded`) | `com.gamezone`, `com.gamezone.model`, `com.gamezone.persistence`, `com.gamezone.service`, `com.gamezone.ui` |
| **Accessories Integration (Req. 1 & Req. 5)** | Integration of `AccessoryService` in `SaleService.registerSale` and `SaleService.toSale`, `ConsoleMenu.showAccessoryMenu`, and `Main.preloadAccessoriesIfNeeded` | `com.gamezone`, `com.gamezone.service`, `com.gamezone.ui` |
| **Promotions Integration (Req. 2 & Req. 5)** | Discount application in `SaleService.registerSale`, `Sale.applyDiscount(String, double)`, promotional breakdown in `Sale.generateReceipt()`, `ConsoleMenu.showPromotionMenu` (`registerPercentageDiscount`, `registerCategoryDiscount`, `registerBulkPurchaseDiscount`, `listAllPromotions`, `listActivePromotions`, `printPromotion`), and `Main.preloadPromotionsIfNeeded` | `com.gamezone`, `com.gamezone.model`, `com.gamezone.service`, `com.gamezone.ui` |
| **Returns Integration (Req. 3 & Req. 5)** | `ProductService.restoreStock(String, int)` (`src/main/java/com/gamezone/service/ProductService.java`, lines 141–146) and `ConsoleMenu.showReturnMenu` / `registerReturn` (including monthly balance display) | `com.gamezone.service`, `com.gamezone.ui` |
| **Warranties Integration (Req. 4 & Req. 5)** | Basic and extended warranty assignment and cost aggregation in `SaleService.registerSale`, `Sale.getExtendedWarrantyCost` / `setExtendedWarrantyCost`, and `ConsoleMenu.showWarrantyMenu` | `com.gamezone.model`, `com.gamezone.service`, `com.gamezone.ui` |
| **Differentiated Exception Handling (Req. 6)** | Multi-catch blocks for `ResourceNotFoundException`, `BusinessRuleException`, `InvalidDataException`, and `PersistenceException` in `ConsoleMenu` | `com.gamezone.ui` |
| **GRASP Phase 2 Refactorings (Req. 7)** | `Main.main` (`new AccessoryService(accessoryRepository)`), `SaleService` cohesive helpers (`findCatalogProduct`, `findCatalogProductById`, `decrementStock`), `ProductService.restoreStock` positive quantity guard, and `Sale.getDiscountRatio()` (co-authored with Developer 1) | `com.gamezone`, `com.gamezone.model`, `com.gamezone.service` |

---

## 2. Evaluation by SOLID Principle

### 2.1 Single Responsibility Principle (SRP)

- **Overall Verdict for Audited Scope:** **Violation** (2 violations identified: `V-01` in `Main` and `V-02` in `ConsoleMenu`; `Sale`, `SaleRecord`, `SaleRepository`, `SaleService`, and `ProductService.restoreStock` are **Correct**).

#### Identified Violations

| Field | Content |
| :--- | :--- |
| **ID** | `V-01` |
| **Principle** | Single Responsibility Principle (SRP) |
| **Module** | Application Entry Point & Module Integrations (Taller 1 & Requirements 1, 2, 5) |
| **Evidence** | Class `com.gamezone.Main`, methods `main(String[] args)` (lines 28–53), `preloadSellersIfNeeded(PersonService)` (lines 59–66), `preloadPromotionsIfNeeded(PromotionService)` (lines 68–83), and `preloadAccessoriesIfNeeded(AccessoryService)` (lines 85–101) in `src/main/java/com/gamezone/Main.java`:<br>`public static void preloadSellersIfNeeded(PersonService personService) { ... }`<br>`public static void preloadPromotionsIfNeeded(PromotionService promotionService) { ... }`<br>`public static void preloadAccessoriesIfNeeded(AccessoryService accessoryService) { ... }` |
| **Explanation** | `Main` combines two independent responsibilities with different reasons to change: (1) bootstrapping the application's dependency-injection graph (instantiating repositories, services, and launching `MainFrame` / `ConsoleMenu`), and (2) defining hardcoded initial seed data for sellers (`V001`–`V003`), promotions (`PROMO001`–`PROMO004`), and accessories (`A001`–`A003`). |
| **Consequence** | Any change to the default sample catalog, staff shifts, or promotional dates forces modifying `Main.java`, coupling application composition/startup to initial dataset provisioning. |
| **Proposed Solution** | Extract the data-seeding logic (`preloadSellersIfNeeded`, `preloadPromotionsIfNeeded`, `preloadAccessoriesIfNeeded`) into a dedicated `DataInitializer` class (`com.gamezone.DataInitializer`), while delegating from `Main` so `Main` focuses on dependency wiring and startup while preserving backward compatibility. |
| **Other Member's Code** | **No** — `Main` is exclusively under the Technical Lead's responsibility. |

| Field | Content |
| :--- | :--- |
| **ID** | `V-02` |
| **Principle** | Single Responsibility Principle (SRP) |
| **Module** | Console UI & Differentiated Exception Handling (Requirement 6) |
| **Evidence** | Class `com.gamezone.ui.ConsoleMenu` (`src/main/java/com/gamezone/ui/ConsoleMenu.java`), repeated 4-branch `catch` blocks in `start()` (lines 100–112), `registerSale()` (lines 425–433), `registerReturn()` (lines 458–466), `registerVideoGame()` (lines 484–492), `registerConsole()` (lines 510–518), `registerCustomer()` (lines 539–547), `registerPercentageDiscount()` (lines 602–610), `registerCategoryDiscount()` (lines 627–635), and `registerBulkPurchaseDiscount()` (lines 652–660):<br>`} catch (ResourceNotFoundException e) {`<br>`    printError("No se encontró el recurso solicitado: " + e.getMessage());`<br>`} catch (BusinessRuleException e) {`<br>`    printError("No se puede completar la operación: " + e.getMessage());`<br>`} catch (InvalidDataException e) {`<br>`    printError("Los datos ingresados no son válidos: " + e.getMessage());`<br>`} catch (PersistenceException e) {`<br>`    printError("Ocurrió un problema al acceder a los datos del sistema. Contacte al administrador.");`<br>`}` |
| **Explanation** | Every interactive registration method in `ConsoleMenu` combines two responsibilities: (1) reading console user input and invoking the target service method, and (2) classifying and formatting `GameZoneException` subtypes into Spanish error messages across 9 duplicated `try/catch` blocks. |
| **Consequence** | Any adjustment to how `GameZoneException` categories are formatted or logged requires editing 9 different methods in `ConsoleMenu`, risking inconsistent error messages across menu options. |
| **Proposed Solution** | Extract a single cohesive helper method `handleGameZoneException(GameZoneException e)` in `ConsoleMenu` and delegate all `GameZoneException` catch blocks to it, preserving 100% identical Spanish console messages. |
| **Other Member's Code** | **No** — `ConsoleMenu` is exclusively under the Technical Lead's responsibility. |

#### Compliant Classes Under SRP (`Correct`)

- **`Sale` (`com.gamezone.model.Sale`, lines 17–215):** Encapsulates only the state and domain calculations of a sale transaction (`calculateTotal`, `getTotalAmount`, `getDiscountRatio`, `applyDiscount`, `canBeReturned`, `findProductById`, `generateReceipt`).
- **`SaleRecord` and `SaleRepository` (`com.gamezone.persistence`):** `SaleRecord` (`SaleRecord.java` lines 13–133) is a pure persistence DTO, and `SaleRepository` (`SaleRepository.java` lines 20–71) handles only binary serialization of `SaleRecord` lists to `data/sales.dat`.
- **`SaleService` (`com.gamezone.service.SaleService`, lines 28–275):** Coordinates the 8-step sale registration workflow, sale history queries, and `SaleRecord` mapping (`toRecord`, `toSale`), aided by the cohesive private helpers `findCatalogProduct`, `findCatalogProductById`, and `decrementStock` extracted in Requirement 7 (`V-07`).
- **`ProductService.restoreStock(String, int)` (`com.gamezone.service.ProductService`, lines 141–146):** Validates that the restored quantity is positive and delegates stock increment to `updateStock(productId, quantity)`.
