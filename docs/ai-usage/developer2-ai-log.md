# AI Usage Log - Developer

## Student Information

**Full name:** Hector Guevara
**Role:** Developer2
**Assigned module:** Person management
**Project:** GameZone Unicesar

## Purpose of AI Use

I used Artificial Intelligence as a support tool during the development of the person management module. The tool was used to clarify Java programming concepts, review code structure, understand object-oriented programming, document classes and methods, and solve Git-related problems during the development process. I reviewed the suggestions provided by the AI and used them as guidance to implement and improve my own code.

## AI Tool Used

**Tool:** ChatGPT
**Use:** Concept clarification, code review, documentation, and Git support.

## Consultation Log

### Person class structure

**Topic:** Object-oriented programming and inheritance in the person module.

**Consultation:** I asked for help understanding and organizing the classes related to people in the GameZone Unicesar project, including `Person`, `Customer`, and `Seller`.

**What I learned:** Common attributes and behaviors can be placed in a base class, while specific characteristics can be implemented in subclasses. This helps avoid code duplication and makes the project easier to maintain.

**How I applied it:** I used this guidance to understand and review the inheritance structure between the person-related classes in the project.

### Customer class

**Topic:** Creating and reviewing the `Customer` model.

**Consultation:** I provided the `Customer` class code and asked for help reviewing its structure, attributes, constructors, getters, and setters.

**What I learned:** A Java model class should clearly define its attributes and provide appropriate constructors and accessor methods. The attributes should represent the information required by the application.

**How I applied it:** I reviewed the `Customer` class and its attributes, including `email` and `purchaseHistory`, and ensured that the class followed the structure required by the project.

### PersonRepository

**Topic:** Persistence and repository design.

**Consultation:** I asked for help creating and understanding the `PersonRepository` class responsible for managing customer and seller information.

**What I learned:** A repository is responsible for persistence operations, such as loading and saving information. Keeping these operations in a repository prevents the model classes from being responsible for file management.

**How I applied it:** I used this concept to organize `PersonRepository` as the persistence component for the person management module.

### PersonService

**Topic:** Service layer and business logic.

**Consultation:** I asked for help understanding and documenting the `PersonService` class, which uses `PersonRepository` to manage customers and sellers.

**What I learned:** The service layer coordinates application operations and communicates with the repository. This separation allows business logic and persistence logic to remain organized in different layers.

**How I applied it:** I reviewed the relationship between `PersonService` and `PersonRepository` and used JavaDoc to document the responsibilities of the service methods.

### JavaDoc documentation

**Topic:** Code documentation in Java.

**Consultation:** I asked for help creating JavaDoc documentation for the `PersonService` class and its methods.

**What I learned:** JavaDoc allows developers to document classes, constructors, methods, parameters, and return values. Good documentation makes the code easier for other developers to understand and maintain.

**How I applied it:** I added JavaDoc documentation to the person management code, explaining the purpose and behavior of the classes and methods.

### Git branches and commits

**Topic:** Git version control.

**Consultation:** I asked how to create branches, switch between branches, add files, make commits, and push changes to a specific branch.

**What I learned:** Git branches allow developers to work on different features independently. Changes can be staged with `git add`, recorded with `git commit`, and uploaded to a specific remote branch using `git push`.

**How I applied it:** I used these commands to work on the `feature/person` branch and manage the changes made to the person management module.

### Git error troubleshooting

**Topic:** Solving Git errors.

**Consultation:** I asked about errors such as files not being found when using Git commands and messages related to staged or untracked files.

**What I learned:** Git status messages indicate whether files are untracked, modified, staged, or ready to be committed. Understanding these states helps prevent mistakes when managing project changes.

**How I applied it:** I used the explanations to correctly add my files, create commits, and prepare the changes for pushing to the appropriate branch.

### Circular dependency resolution (Adjustment A2)

**Topic:** Breaking circular dependencies in constructor injection.

**Consultation:** I asked how to avoid the circular dependency between `SaleService` and `WarrantyService` when initializing objects in `Main`.

**What I learned:** If `WarrantyService` receives `SaleRepository` and `ProductService` instead of `SaleService` to resolve persisted IDs, `WarrantyService` can be instantiated first and injected into `SaleService` by constructor.

**How I applied it:** I updated the `WarrantyService` constructor and adjusted the initialization order in `Main`.

### Accessory stock restoration on returns (Adjustment A4)

**Topic:** Delegating inventory restoration by subtype in `ReturnService`.

**Consultation:** I asked how `ReturnService` should restore stock when a returned item is an `Accessory` rather than a `VideoGame` or `Console`.

**What I learned:** Since accessories are stored in `AccessoryRepository` and managed by `AccessoryService`, `ReturnService` should check `product instanceof Accessory` and delegate to `AccessoryService.restoreStock`.

**How I applied it:** I validated `AccessoryService.restoreStock` and ensured `ReturnService` delegates stock restoration and record resolution across both `ProductService` and `AccessoryService`.

### Monthly balance breakdown methods (Adjustment A6)

**Topic:** Filtering dates with `LocalDate.getMonthValue()` and `getYear()` in `ReturnService`.

**Consultation:** I asked how to separate the monthly balance calculation into helper methods so the UI can display total sales, total returns, and net balance independently.

**What I learned:** Extracting `calculateMonthlySales(int month, int year)` and `calculateMonthlyReturns(int month, int year)` allows `generateMonthlyBalance(month, year)` to reuse both methods while exposing the intermediate totals to `ConsoleMenu` and `ReportsPanel`.

**How I applied it:** I implemented `calculateMonthlySales` and `calculateMonthlyReturns` in `ReturnService` and updated the UI to display all three values.

### Warranty cancellation on console return (Adjustment A7)

**Topic:** Filtering and persisting remaining records in `WarrantyService.cancelWarranties`.

**Consultation:** I asked how to remove all warranty records (`BasicWarranty` and `ExtendedWarranty`) matching a returned `Console` and `saleId` while accumulating the refundable extended warranty cost.

**What I learned:** Iterating through `repository.loadAll()`, summing `warranty.getAdditionalCost()` for matching `(productId, saleId)` records (`0.0` for basic, `10%` of console price for extended), and saving only the non-matching records cleanly cancels the warranties and returns the exact refundable amount.

**How I applied it:** I implemented `cancelWarranties(String productId, String saleId)` in `WarrantyService` and invoked it from `ReturnService.registerReturn` whenever a returned product is a `Console`.

### Exception wrapping in repositories and validator delegation in services (Requirement 6)

**Topic:** Wrapping `IOException` into `PersistenceException` and delegating service checks to static validators.

**Consultation:** I asked how to refactor `PersonRepository` so missing `.dat` files on first startup return an empty list while actual read/write failures throw `PersistenceException` with the file path and operation context.

**What I learned:** Checking `file.exists()` before opening `ObjectInputStream` handles initial startup cleanly, while catching `IOException | ClassNotFoundException` and rethrowing `new PersistenceException("Error de lectura/escritura en el archivo ...", e)` preserves the root cause for debugging.

**How I applied it:** I refactored the repositories to throw `PersistenceException` and updated `ProductService`, `PersonService`, `SaleService`, `AccessoryService`, `PromotionService`, and `ReturnService` to invoke `ProductValidator`, `PersonValidator`, and `SaleValidator`.



---

## Requirement 7 — GRASP Audit and Refactoring

### Phase 1 - Audit

#### Role of Services as GRASP Controllers and Pure Fabrications
- **Topic:** Conceptual clarification of GRASP *Controller*, *Pure Fabrication*, and *Indirection* in a layered Java architecture.
- **Consultation:** I asked for a conceptual explanation of how service and repository classes relate to the *Controller*, *Pure Fabrication*, and *Indirection* patterns in GRASP.
- **What I learned:** Repository classes (`PersonRepository`, `AccessoryRepository`, `ReturnRepository`, `WarrantyRepository`, `PromotionRepository`) and DTO records (`ReturnRecord`, `WarrantyRecord`) are *Pure Fabrications* that also provide *Indirection* between business logic and binary file I/O, while service classes act as use-case *Controllers* coordinating domain objects behind the UI.
- **How I applied it:** I used these definitions to evaluate my persistence and service classes in `docs/grasp-audit/developer2-audit.md` and to document the boundary findings in Question 2 of `docs/grasp-analysis.md`.

### Phase 2 - Refactoring

#### Delegating Domain Lookups and Centralizing Validations (`V-03`, `V-04`, `V-05`, `V-06`)
- **Topic:** Applying *Information Expert*, *Pure Fabrication*, *High Cohesion*, and *Low Coupling* in `AccessoryService`, `PromotionService`, `ReturnService`, and `Sale`.
- **Consultation:** I asked how to refactor service-layer methods that manually traversed internal collections of domain entities (`Accessory.getCompatibleConsoleIds()`, `Sale.getProducts()`) and how to reduce coupling in `AccessoryService` initialization.
- **What I learned:** Under *Information Expert*, the class that owns a collection (`Accessory` for `compatibleConsoleIds`, `Sale` for `products`) should expose the query method (`isCompatibleWith(String)`, `findProductById(String)`), while the service simply delegates to it. Furthermore, overloading `AccessoryService(AccessoryRepository)` so the service loads its own initial list from the repository eliminates unnecessary coupling in `Main` (*Low Coupling*), and delegating inline input checks to `ProductValidator` restores *High Cohesion* in `AccessoryService` and `PromotionService`.
- **How I applied it:** I refactored `AccessoryService.findCompatibleWithConsole` (`V-03`), added `Sale.findProductById` and delegated `ReturnService.findInSale` (`V-04`), replaced inline subtype and promotion validations in `AccessoryService` and `PromotionService` with calls to `ProductValidator` (`V-05`), and added the single-argument constructor `AccessoryService(AccessoryRepository)` (`V-06`).


---

## Requirement 8 — SOLID Audit and Refactoring

### Phase 1 - Audit

#### Conceptual distinction between Interface Segregation (ISP) and Dependency Inversion (DIP) in service collaborations
- **Topic:** Object-oriented design concepts (SOLID ISP and DIP)
- **Consultation:** I asked for a conceptual explanation of how the *Interface Segregation Principle (ISP)* and the *Dependency Inversion Principle (DIP)* complement each other when one service class collaborates with another service or repository.
- **What I learned:** *DIP* states that high-level services should depend on abstractions (`Repository<T>` or service interfaces) rather than concrete low-level classes (`AccessoryRepository`, `ProductService`, `SaleService`), whereas *ISP* states that those abstractions should be narrow and tailored to the specific operations required by each client rather than exposing every registration and mutation method of a concrete service.
- **How I applied it:** I used this conceptual distinction to evaluate my repositories and services (`AccessoryRepository`, `AccessoryService`, `PromotionService`, `ReturnService`, `WarrantyService`) in `docs/solid-audit/developer2-audit.md` and to document the boundary findings in Question 2 of `docs/solid-analysis.md`.


### Phase 2 - Refactoring

#### Extracting `SaleRecordResolver` and inverting repository and lookup dependencies (`V-01`, `V-03`, `V-04`, `V-05`, `V-06`, `V-07`)
- **Topic:** Applying *SRP*, *OCP*, *ISP*, and *DIP* across `PromotionService`, `AccessoryRepository`, `AccessoryService`, `SaleRecordResolver`, `WarrantyService`, and `ReturnService`.
- **Consultation:** I asked how to extract persisted `SaleRecord` resolution from `WarrantyService` into a focused collaborator without breaking existing constructor signatures or creating circular dependencies, and how to replace string-based warranty reconstruction and accessory type filtering with polymorphic dispatch.
- **What I learned:** Extracting `SaleRecordResolver(Repository<SaleRecord>, ProductCatalogLookup)` isolates the responsibility of reconstructing a `Sale` aggregate from `SaleRecord` (*SRP*) while depending exclusively on the `Repository<SaleRecord>` and `ProductCatalogLookup` abstractions (*ISP* & *DIP*). Meanwhile, keeping backward-compatible overloaded constructors in `PromotionService`, `AccessoryService`, `WarrantyService`, and `ReturnService` that delegate to the abstraction-based constructors preserves full compatibility with `Main` and other callers. Finally, delegating `AccessoryService.listAccesoriesByType(String)` to `accessory.matchesAccessoryType(type)` and `WarrantyService.toWarranty(WarrantyRecord)` to `Warranty.createFromType(...)` satisfies *OCP*.
- **How I applied it:** I updated `PromotionService` (`V-01`, `V-06`), `AccessoryRepository` and `AccessoryService` (`V-03`, `V-06`, `V-07`), created `SaleRecordResolver` (`V-05`, `V-06`, `V-07`), refactored `WarrantyService` (`V-04`, `V-05`, `V-06`, `V-07`), and updated `ReturnService` (`V-06`).
