# AI Usage Log — Technical Leader

This log documents the main uses of AI assistance (Claude) during the development of my assigned module (Sale domain class, persistence, service, and the Console UI / Main entry point), plus architecture and Git guidance that affected the whole project.

## 1. Layering exception in SaleRepository — architecture review

**Context:** the original class diagram had `SaleRepository` (persistence) depending directly on `ProductService` and `PersonService` (service layer), justified as a "documented exception" to resolve customer/seller/product references when reconstructing a `Sale` from `data/sales.csv`.

**What I asked:** whether this exception was actually acceptable given the workshop's strict layering rule (`ui → service → persistence → model`, restriction 6 and code restriction 11).

**Outcome:** the AI pointed out that the workshop rules do not allow exceptions ("el incumplimiento afectará la calificación"), and that a reverse dependency from `persistence` to `service` breaks the whole point of layered architecture. The fix agreed on: introduce `SaleRecord`, a plain DTO in `persistence` holding only raw ids (`customerId`, `sellerId`, `productIds`). `SaleRepository` only depends on `SaleRecord`; the resolution of ids into full domain objects (`Customer`, `Seller`, `Product`) moved to `SaleService`, which already had access to `ProductService` and `PersonService`. I understood and applied this before writing any persistence code, so no rework was needed later.

## 2. Sale class — design decisions

Discussed where to place the "at least one product" validation (workshop Q7) and the total calculation (Q6). Decision: `calculateTotal()` stays in `Sale` (derived from its own product list, single responsibility), and the "at least one product" rule is validated both in `SaleService.registerSale(...)` (business rule) and defensively in the `Sale` constructor (so no invalid `Sale` object can ever exist in memory).

**Peer review fix:** a teammate reviewing my PR correctly flagged that `setProducts(...)` did not repeat the validation from the constructor, that `totalAmount` was not recalculated when products changed, and that `setTotalAmount(...)` should not exist at all since the total is derived data, not something that should be set manually. I removed the setter and fixed `setProducts` to validate and recalculate.

## 3. SaleRecord — resolving the layering problem concretely

Built `SaleRecord` as a `Serializable` DTO with only ids (no `Customer`/`Seller`/`Product` references), matching the serialization approach my teammate had already started with `ObjectOutputStream`/`ObjectInputStream` for `ProductRepository`. Discussed why storing full objects instead of ids would create duplicated/desynchronized copies of the same entity across `sales.dat` and `customers.dat`/`products.dat` — this is the reason `SaleRecord` exists instead of persisting `Sale` directly.

## 4. SaleService and ConsoleMenu — implementation support

Asked for help implementing `SaleService` (registerSale with stock validation before updating inventory in two separate passes, to avoid leaving inventory partially updated if a later product fails validation; viewAllSales/viewSalesByCustomer/viewSalesBySeller resolving `SaleRecord` back into full `Sale` objects) and `ConsoleMenu` (single shared `Scanner` instead of one per method, to avoid closing `System.in` prematurely; product selection one id at a time instead of comma-separated input, for clearer validation and error handling during the live demo). I reviewed both classes method by method before integrating them, confirming the expected method signatures against what Dev1 and Dev2 had implemented in `ProductService`/`PersonService`.

## 5. Errors debugged with AI help

- **`saveALL` typo** in a teammate's `ProductRepository`: method name didn't match the `Repository<T>` interface (`saveAll`), which would have failed to compile once `implements Repository<Product>` was added.
- **`long cannot be dereferenced`** compiling `SaleService`: traced to `Person.getId()` returning `long` instead of `String`, inconsistent with the rest of the model (`Product.id`, `Sale.id`) and with the class diagram / analysis.md. Fixed in the model, not patched around in `SaleService`.
- **`Customer` constructor requiring `purchaseHistory`**: identified as a design flaw, not just a compile error — a new customer cannot have a purchase history at registration time, and maintaining it as a stored attribute would duplicate data already available through `SaleService.viewSalesByCustomer(...)`. Removed the attribute from `Customer` and updated the class diagram accordingly instead of adding a workaround constructor.
- **Missing `registerSeller(...)` in `PersonService`**: written following the same pattern already validated for `registerCustomer` (duplicate-id check, object construction inside the service).

## 6. Git and commit conventions

Used AI guidance to confirm the correct commit type for structural changes (`chore:`, per the Conventional Commits examples already given in the workshop guide) when creating the four-layer package structure, and to fix a workflow mistake where package folders had been created directly on `develop` before realizing it was branch-protected — resolved by moving the uncommitted files to a new `feature/project-structure` branch instead of committing to `develop` directly. Also used AI guidance on syncing a teammate's feature branch with an updated `develop` after a structural fix, without merging feature branches into each other directly.

## 7. Other tools

Used GitHub Copilot to translate commit messages and code comments into English where needed, to keep the Conventional Commits convention and JavaDoc consistent with the workshop's English-only requirement for commit messages and documentation.
