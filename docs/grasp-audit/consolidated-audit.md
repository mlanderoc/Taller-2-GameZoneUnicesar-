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

---

## 3. Definitive Catalog of Consolidated Violations (`V-01` to `V-07`)

After reviewing and unifying the three individual audit reports, the team established the following definitive catalog of **7 GRASP violations** (`V-01` through `V-07`) to be corrected in Phase 2 (`feature/grasp-refactor`):

### Violation `V-01` — Information Expert Between `Return` and `Sale` (Discount Ratio Calculation)

| Field | Content |
| :--- | :--- |
| **Consolidated ID** | `V-01` |
| **GRASP Pattern** | Information Expert (`Experto en Información`) |
| **Origin Reports** | `developer1-audit` `V-01` and `leader-audit` `V-01` (part 1) |
| **Evidence (`develop` `3f14c06`)** | `Return.getDiscountRatio()` (`Return.java`, lines 139–144):<br>```java
private double getDiscountRatio() {
    if (originalSale != null && originalSale.getSubtotal() > 0) {
        return originalSale.getDiscountAmount() / originalSale.getSubtotal();
    }
    return 0.0;
}
``` |
| **Explanation** | `Sale` owns both `subtotal` and `discountAmount`, making `Sale` the Information Expert on the discount ratio applied to its items. However, `Return` pulls both raw fields out of `originalSale` to compute `discountAmount / subtotal` on its own. |
| **Consequence** | Couples `Return` to `Sale`'s internal discount storage and formula. |
| **Agreed Solution** | 1. Add `public double getDiscountRatio()` to `Sale` returning `subtotal > 0 ? discountAmount / subtotal : 0.0`.<br>2. Refactor `Return.getDiscountRatio()` to delegate directly to `originalSale != null ? originalSale.getDiscountRatio() : 0.0`. |
| **Responsible for Correction** | **Technical Lead (`mlanderoc`)** in `Sale.java`; **Developer 1 (`craulgomez`)** in `Return.java`. |

---

### Violation `V-02` — Polymorphism in `CategoryDiscount` and `Product` Hierarchy

| Field | Content |
| :--- | :--- |
| **Consolidated ID** | `V-02` |
| **GRASP Pattern** | Polymorphism (`Polimorfismo`) *(also satisfies Protected Variations)* |
| **Origin Reports** | `developer1-audit` `V-02` |
| **Evidence (`develop` `3f14c06`)** | `CategoryDiscount.calculateDiscount(Sale sale)` (`CategoryDiscount.java`, lines 78–90):<br>```java
for(Product product : sale.getProducts()){
    if("VIDEOGAME".equalsIgnoreCase(categoryObjective) && product instanceof VideoGame){
        categoryTotal += product.getPrice(); 
    }
    if("CONSOLE".equalsIgnoreCase(categoryObjective) && product instanceof Console){
        categoryTotal += product.getPrice(); 
    }
    if("ACCESSORY".equalsIgnoreCase(categoryObjective) && product instanceof Accessory){
        categoryTotal += product.getPrice(); 
    }
}
``` |
| **Explanation** | `CategoryDiscount` checks concrete product types using `instanceof VideoGame`, `instanceof Console`, and `instanceof Accessory` instead of relying on polymorphic dispatch in the `Product` hierarchy. |
| **Consequence** | Adding any new product category requires modifying `CategoryDiscount.calculateDiscount` with additional `instanceof` branches. |
| **Agreed Solution** | 1. Declare `public abstract String getCategory();` and `public boolean belongsToCategory(String category)` in `Product`.<br>2. Override `getCategory()` in `VideoGame` (`"VIDEOGAME"`), `Console` (`"CONSOLE"`), and `Accessory` (`"ACCESSORY"`).<br>3. Refactor `CategoryDiscount.calculateDiscount(Sale)` to check `if (product.belongsToCategory(categoryObjective))` without `instanceof`. |
| **Responsible for Correction** | **Developer 1 (`craulgomez`)** in `Product.java`, `VideoGame.java`, `Console.java`, `Accessory.java`, and `CategoryDiscount.java`. |

---

### Violation `V-03` — Information Expert in `AccessoryService.findAcessoriesCOmpatibleWith`

| Field | Content |
| :--- | :--- |
| **Consolidated ID** | `V-03` |
| **GRASP Pattern** | Information Expert (`Experto en Información`) |
| **Origin Reports** | `developer2-audit` `V-01` |
| **Evidence (`develop` `3f14c06`)** | `AccessoryService.findAcessoriesCOmpatibleWith(String consoleId)` (`AccessoryService.java`, lines 72–78):<br>```java
public List<Accessory> findAcessoriesCOmpatibleWith(String consoleId) {
    return accessories.stream()
            .filter(accessory ->
                    accessory.getCompatibleConsoleIds() != null &&
                    accessory.getCompatibleConsoleIds().contains(consoleId))
            .collect(Collectors.toList());
}
``` |
| **Explanation** | `Accessory` already owns `compatibleConsoleIds` and defines the expert method `isCompatibleWith(String consoleId)` (`Accessory.java`, lines 56–63), yet `AccessoryService` bypasses it and inspects `getCompatibleConsoleIds().contains(consoleId)` directly. |
| **Consequence** | Duplicates console-compatibility logic in the service layer and couples `AccessoryService` to `Accessory`'s internal list. |
| **Agreed Solution** | 1. Ensure null-safe evaluation in `Accessory.isCompatibleWith(String consoleId)`.<br>2. Refactor `AccessoryService.findAcessoriesCOmpatibleWith(String consoleId)` to call `accessory.isCompatibleWith(consoleId)`. |
| **Responsible for Correction** | **Developer 1 (`craulgomez`)** in `Accessory.java`; **Developer 2 (`Hectorguevara22`)** in `AccessoryService.java`. |

---

### Violation `V-04` — Information Expert Between `ReturnService` and `Sale` (Product Lookup Within Sale)

| Field | Content |
| :--- | :--- |
| **Consolidated ID** | `V-04` |
| **GRASP Pattern** | Information Expert (`Experto en Información`) |
| **Origin Reports** | `developer2-audit` `V-02` and `leader-audit` `V-01` (part 2) |
| **Evidence (`develop` `3f14c06`)** | `ReturnService.findInSale(Sale sale, String productId)` (`ReturnService.java`, lines 210–217):<br>```java
private Product findInSale(Sale sale, String productId) {
    for (Product product : sale.getProducts()) {
        if (product.getId().equals(productId)) {
            return product;
        }
    }
    return null;
}
``` |
| **Explanation** | `Sale` owns `List<Product> products` and is the Information Expert on whether a product ID belongs to that sale, but `ReturnService` traverses `sale.getProducts()` externally. |
| **Consequence** | Couples `ReturnService` to the internal collection of `Sale` and duplicates product lookup logic. |
| **Agreed Solution** | 1. Add `public Product findProductById(String productId)` to `Sale`.<br>2. Refactor `ReturnService.findInSale(Sale, String)` to delegate to `sale.findProductById(productId)`. |
| **Responsible for Correction** | **Technical Lead (`mlanderoc`)** in `Sale.java`; **Developer 2 (`Hectorguevara22`)** in `ReturnService.java`. |

---

### Violation `V-05` — Pure Fabrication & High Cohesion in `ProductValidator`, `AccessoryService`, and `PromotionService`

| Field | Content |
| :--- | :--- |
| **Consolidated ID** | `V-05` |
| **GRASP Pattern** | Pure Fabrication (`Fabricación Pura`) and High Cohesion (`Alta Cohesión`) |
| **Origin Reports** | `developer1-audit` `V-03` and `developer2-audit` `V-04` |
| **Evidence (`develop` `3f14c06`)** | 1. `AccessoryService.registerCable` (lines 38–40) & `registerMemory` (lines 57–59 of `AccessoryService.java`).<br>2. `PromotionService.registerCategoryDiscount` (lines 53–58), `registerBulkPurchaseDiscount` (lines 66–68), & `validatePromotionBase` (lines 74–90 of `PromotionService.java`). |
| **Explanation** | Subtype validations for accessories (`lengthInMeters <= 0`, `storageCapacityGb <= 0`) and promotion registration validations (`validatePromotionBase`, `categoryObjective`, `minimumQuantity`) remained inline inside `AccessoryService` and `PromotionService` instead of being housed in the Pure Fabrication validation layer (`com.gamezone.validation`). |
| **Consequence** | Lowers the cohesion of `AccessoryService` and `PromotionService` by mixing input validation rules with business orchestration. |
| **Agreed Solution** | 1. Add `validateCableData(double, String)` and `validateMemoryData(int, String)` and promotion validation helpers (`validatePromotionBase`, `validateCategoryObjective`, `validateMinimumQuantity`) in `ProductValidator` (`com.gamezone.validation`).<br>2. Refactor `AccessoryService` and `PromotionService` to delegate those checks to `ProductValidator`. |
| **Responsible for Correction** | **Developer 1 (`craulgomez`)** in `ProductValidator.java`; **Developer 2 (`Hectorguevara22`)** in `AccessoryService.java` and `PromotionService.java`. |

---

### Violation `V-06` — Low Coupling Between `Main` and `AccessoryService` Constructor

| Field | Content |
| :--- | :--- |
| **Consolidated ID** | `V-06` |
| **GRASP Pattern** | Low Coupling (`Bajo Acoplamiento`) |
| **Origin Reports** | `developer2-audit` `V-03` and `leader-audit` `V-02` |
| **Evidence (`develop` `3f14c06`)** | `AccessoryService.java` (lines 19–22) and `Main.java` (line 40):<br>```java
AccessoryService accessoryService = new AccessoryService(accessoryRepository,accessoryRepository.loadAll());
``` |
| **Explanation** | `AccessoryService` requires a second `List<Accessory>` constructor argument that it ignores internally (`this.accessories = repository.loadAll()`), forcing `Main` to call `accessoryRepository.loadAll()` redundantly. |
| **Consequence** | Unnecessary coupling between `Main` and `AccessoryRepository.loadAll()` and duplicate file deserialization at startup. |
| **Agreed Solution** | 1. Add `public AccessoryService(AccessoryRepository repository)` to `AccessoryService` (keeping the 2-parameter constructor delegating to `this(repository)`).<br>2. Refactor `Main.main` to instantiate `new AccessoryService(accessoryRepository)`. |
| **Responsible for Correction** | **Developer 2 (`Hectorguevara22`)** in `AccessoryService.java`; **Technical Lead (`mlanderoc`)** in `Main.java`. |

---

### Violation `V-07` — High Cohesion & Indirection in `SaleService` and `ProductService.restoreStock`

| Field | Content |
| :--- | :--- |
| **Consolidated ID** | `V-07` |
| **GRASP Pattern** | High Cohesion (`Alta Cohesión`) |
| **Origin Reports** | `leader-audit` `V-03` |
| **Evidence (`develop` `3f14c06`)** | 1. `SaleService.registerSale` (lines 77–82, 116–122) and `toSale` (lines 229–233 of `SaleService.java`) repeating inline `ProductService` vs `AccessoryService` routing branches.<br>2. `ProductService.restoreStock(String productId, int quantity)` (`ProductService.java`, lines 142–144) lacking positive-quantity validation (`quantity <= 0`). |
| **Explanation** | `SaleService` duplicates catalog lookup and stock adjustment routing across multiple methods instead of encapsulating them in cohesive private helpers, and `ProductService.restoreStock` omits the positive-quantity guard present in `AccessoryService.restoreStock`. |
| **Consequence** | Lowers cohesion and readability in `SaleService` and leaves `ProductService.restoreStock` without symmetric input protection. |
| **Agreed Solution** | 1. Extract private helper methods `resolveCatalogItem(String productId)` and `adjustCatalogStock(Product item, int quantityDelta)` in `SaleService` and use them in `registerSale` and `toSale`.<br>2. Add `if (quantity <= 0) throw new InvalidDataException(...)` in `ProductService.restoreStock`. |
| **Responsible for Correction** | **Technical Lead (`mlanderoc`)** in `SaleService.java` and `ProductService.java` (`restoreStock`). |
