# Requirement 1: Accessories Module — Analysis

### 1. Should accessories be integrated into the existing product hierarchy by extending Product, or should they form an independent hierarchy? Justify your decision considering code reuse and model coherence.

Accessories should be integrated into the existing product hierarchy by extending `Product`. They share common attributes such as identifier, title, price, and stock, so extending `Product` avoids duplicating code. An abstract class called `Accessory` can manage the common accessory behavior—especially console compatibility—while `Controller`, `Cable`, and `Memory` extend it. This design is coherent because accessories are still products that can be sold in the store system.

### 2. What attributes are common to the three types of accessories, and which ones are specific to each type? How is this distinction reflected in the module class hierarchy?

The common attributes are identifier, title, price, and stock (inherited from `Product`), as well as a `List<String>` called `compatibleConsoleIds`, which stores the IDs of the consoles they are compatible with. Specific attributes include connection type for `Controller`, length in meters and connector type for `Cable`, and storage capacity in gigabytes along with memory type for `Memory`. This is reflected by having `Accessory` extend `Product`, while `Controller`, `Cable`, and `Memory` extend `Accessory`.

### 3. Compatibility between an accessory and a console is a relationship between two system entities. How is this relationship represented in the design and persistence? Is compatibility an attribute of the accessory, the console, or both?

Compatibility is mainly represented as an attribute of the accessory. Each `Accessory` stores a `List<String>` named `compatibleConsoleIds`, containing the IDs of compatible consoles. `AccessoryService` can use this list to find accessories compatible with a selected console. Since the project uses Java serialization, this list is saved automatically as part of the serialized `Accessory` object without needing manual text conversions.

### 4. What modifications are necessary in the sales service class, SaleService, so that sales can include accessories without breaking the existing behavior for video games and consoles?

`SaleService` must allow a sale to contain regular products and accessories within the same `List<Product>`. Since `Accessory` extends `Product`, no separate list is necessary in `Sale`. However, `SaleService` must use `instanceof Accessory` to determine which service manages each item: `AccessoryService` for accessories and `ProductService` for video games and consoles. When reconstructing a sale from a `SaleRecord`, it must look up each item ID in the correct service repository.

### 5. In which layer of the system architecture should the new accessory module classes be located? Justify your decision based on the responsibilities of each layer.

`Accessory`, `Controller`, `Cable`, and `Memory` belong to the `model` layer because they represent entity data and compatibility behavior. `AccessoryRepository` belongs to `persistence` because it saves and loads serialized accessory data. `AccessoryService` belongs to `service` because it manages business rules for registering, querying, and updating stock for accessories.
