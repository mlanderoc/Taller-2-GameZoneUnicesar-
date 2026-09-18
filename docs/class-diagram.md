# Class Diagram — GameZone Unicesar

```mermaid

classDiagram
 
    %% ===== MODEL LAYER =====
    class Product {
        <<abstract>>
        -id: String
        -title: String
        -price: double
        -stock: int
        +getDescription() String*
        +updateStock(quantity int) void
    }
    class VideoGame {
        -platform: String
        -genre: String
        -ageRating: String
        +getDescription() String
    }
    class Console {
        -brand: String
        -model: String
        -generation: String
        +getDescription() String
    }
    class Accessory {
        <<abstract>>
        -compatibleConsoleIds: List~String~
        +isCompatibleWith(consoleId String) boolean
    }
    class Controller {
        -connectionType: String
        +getDescription() String
    }
    class Cable {
        -length: double
        -connectorType: String
        +getDescription() String
    }
    class Memory {
        -capacity: int
        -memoryType: String
        +getDescription() String
    }
 
    class Person {
        <<abstract>>
        -id: String
        -firstName: String
        -lastName: String
        -phone: String
        +getFullName() String
    }
    class Customer {
        -email: String
    }
    class Seller {
        -employeeCode: String
        -shift: String
    }
 
    class Sale {
        -id: String
        -date: LocalDate
        -customer: Customer
        -seller: Seller
        -products: List~Product~
        -subtotal: double
        -appliedPromotionName: String
        -discountAmount: double
        +calculateTotal() double
        +getTotalAmount() double
        +applyDiscount(promotionName String, discountAmount double) void
        +canBeReturned() boolean
        +generateReceipt() String
    }
 
    class Promotion {
        <<abstract>>
        -id: String
        -name: String
        -startDate: LocalDate
        -endDate: LocalDate
        +isActive(date LocalDate) boolean
        +calculateDiscount(sale Sale) double*
    }
    class PercentageDiscount {
        -discountPercentage: double
        +calculateDiscount(sale Sale) double
    }
    class CategoryDiscount {
        -discountPercentage: double
        -category: String
        +calculateDiscount(sale Sale) double
    }
    class BulkPurchaseDiscount {
        -minimumAmount: int
        -discountPercentage: double
        +calculateDiscount(sale Sale) double
    }
 
    class Return {
        -returnId: String
        -returnDate: LocalDate
        -originalSale: Sale
        -returnedProducts: List~Product~
        -reason: String
        -refundedAmount: double
        +calculateRefundAmount() double
        +generateReturnReceipt() String
    }
 
    class Warranty {
        <<abstract>>
        -warrantyId: String
        -product: Product
        -sale: Sale
        -startDate: LocalDate
        -endDate: LocalDate
        +getDurationInMonths() int*
        +getWarrantyType() String*
        +getAdditionalCost() double*
        +isActive(date LocalDate) boolean
        +generateWarrantyCertificate() String
    }
    class BasicWarranty {
        +getDurationInMonths() int
        +getWarrantyType() String
        +getAdditionalCost() double
    }
    class ExtendedWarranty {
        +getDurationInMonths() int
        +getWarrantyType() String
        +getAdditionalCost() double
    }
 
    Product <|-- VideoGame
    Product <|-- Console
    Product <|-- Accessory
    Accessory <|-- Controller
    Accessory <|-- Cable
    Accessory <|-- Memory
    Person <|-- Customer
    Person <|-- Seller
    Promotion <|-- PercentageDiscount
    Promotion <|-- CategoryDiscount
    Promotion <|-- BulkPurchaseDiscount
    Warranty <|-- BasicWarranty
    Warranty <|-- ExtendedWarranty
 
    Sale "1" --> "1" Customer
    Sale "1" --> "1" Seller
    Sale "1" o-- "1..*" Product
    Return "1" ..> "1" Sale
    Return "1" o-- "1..*" Product
    Warranty "1" ..> "1" Product
    Warranty "1" ..> "1" Sale
 
    %% ===== PERSISTENCE LAYER =====
    class ProductRepository {
        +saveAll(products List~Product~) void
        +loadAll() List~Product~
    }
    class PersonRepository {
        +saveAllCustomers(customers List~Customer~) void
        +loadAllCustomers() List~Customer~
        +saveAllSellers(sellers List~Seller~) void
        +loadAllSellers() List~Seller~
    }
    class AccessoryRepository {
        +saveAll(accessories List~Accessory~) void
        +loadAll() List~Accessory~
    }
    class SaleRecord {
        -saleId: String
        -date: LocalDate
        -customerId: String
        -sellerId: String
        -productIds: List~String~
        -subtotal: double
        -appliedPromotionName: String
        -discountAmount: double
    }
    class SaleRepository {
        +saveAll(records List~SaleRecord~) void
        +loadAll() List~SaleRecord~
    }
    class PromotionRepository {
        +saveAll(promotions List~Promotion~) void
        +loadAll() List~Promotion~
    }
    class ReturnRecord {
        -returnId: String
        -returnDate: LocalDate
        -saleId: String
        -productIds: List~String~
        -reason: String
        -refundedAmount: double
    }
    class ReturnRepository {
        +saveAll(records List~ReturnRecord~) void
        +loadAll() List~ReturnRecord~
    }
    class WarrantyRecord {
        -warrantyId: String
        -productId: String
        -saleId: String
        -warrantyType: String
        -startDate: LocalDate
        -endDate: LocalDate
    }
    class WarrantyRepository {
        +saveAll(records List~WarrantyRecord~) void
        +loadAll() List~WarrantyRecord~
    }
 
    ProductRepository ..> Product
    PersonRepository ..> Customer
    PersonRepository ..> Seller
    AccessoryRepository ..> Accessory
    SaleRepository ..> SaleRecord
    PromotionRepository ..> Promotion
    ReturnRepository ..> ReturnRecord
    WarrantyRepository ..> WarrantyRecord
 
    %% ===== SERVICE LAYER =====
    class ProductService {
        -repository: ProductRepository
        +registerVideoGame(...) void
        +registerConsole(...) void
        +listAllProducts() List~Product~
        +updateStock(productId String, quantity int) void
        +restoreStock(productId String, quantity int) void
        +findById(id String) Product
    }
    class PersonService {
        -repository: PersonRepository
        +registerCustomer(...) void
        +registerSeller(...) void
        +findCustomerById(id String) Customer
        +findSellerById(id String) Seller
        +listAllCustomers() List~Customer~
        +listAllSellers() List~Seller~
    }
    class AccessoryService {
        -repository: AccessoryRepository
        +registerController(...) void
        +registerCable(...) void
        +registerMemory(...) void
        +listAllAccessories() List~Accessory~
        +listAccessoriesByType(type String) List~Accessory~
        +findAccessoriesCompatibleWith(consoleId String) List~Accessory~
        +updateStock(accessoryId String, quantity int) void
        +findById(id String) Accessory
    }
    class PromotionService {
        -repository: PromotionRepository
        +registerPercentageDiscount(...) void
        +registerCategoryDiscount(...) void
        +registerBulkPurchaseDiscount(...) void
        +listAllPromotions() List~Promotion~
        +listActivePromotions() List~Promotion~
        +findBestPromotionFor(sale Sale) Promotion
        +findById(id String) Promotion
    }
    class WarrantyService {
        -repository: WarrantyRepository
        +assignBasicWarranty(product Product, sale Sale, startDate LocalDate) BasicWarranty
        +assignExtendedWarranty(product Product, sale Sale, startDate LocalDate) ExtendedWarranty
        +findWarrantyByProduct(productId String, saleId String) Warranty
        +listAllWarranties() List~Warranty~
        +listActiveWarranties() List~Warranty~
        +listWarrantiesExpiringSoon(daysAhead int) List~Warranty~
    }
    class SaleService {
        -saleRepository: SaleRepository
        -productService: ProductService
        -personService: PersonService
        -accessoryService: AccessoryService
        -promotionService: PromotionService
        -warrantyService: WarrantyService
        +registerSale(customer Customer, seller Seller, items List~Product~, productIdsWithExtendedWarranty List~String~) Sale
        +viewAllSales() List~Sale~
        +viewSalesByCustomer(customer Customer) List~Sale~
        +viewSalesBySeller(seller Seller) List~Sale~
    }
    class ReturnService {
        -returnRepository: ReturnRepository
        -saleService: SaleService
        -productService: ProductService
        +registerReturn(saleId String, productIds List~String~, reason String) Return
        +viewAllReturns() List~Return~
        +viewReturnsByCustomer(customerId String) List~Return~
        +viewReturnsBySale(saleId String) List~Return~
        +generateMonthlyBalance(month int, year int) double
    }
 
    ProductService "1" --> "1" ProductRepository
    ProductService ..> Product
    PersonService "1" --> "1" PersonRepository
    AccessoryService "1" --> "1" AccessoryRepository
    AccessoryService ..> Accessory
    PromotionService "1" --> "1" PromotionRepository
    WarrantyService "1" --> "1" WarrantyRepository
    WarrantyService ..> Warranty
 
    SaleService "1" --> "1" SaleRepository
    SaleService "1" --> "1" ProductService
    SaleService "1" --> "1" PersonService
    SaleService "1" --> "1" AccessoryService
    SaleService "1" --> "1" PromotionService
    SaleService "1" --> "1" WarrantyService
    SaleService ..> Sale
    SaleService ..> SaleRecord
 
    ReturnService "1" --> "1" ReturnRepository
    ReturnService "1" --> "1" SaleService
    ReturnService "1" --> "1" ProductService
    ReturnService ..> Return
    ReturnService ..> ReturnRecord
 
    %% ===== UI LAYER =====
    class ConsoleMenu {
        -productService: ProductService
        -personService: PersonService
        -saleService: SaleService
        -accessoryService: AccessoryService
        -promotionService: PromotionService
        -returnService: ReturnService
        -warrantyService: WarrantyService
        +start() void
    }
 
    ConsoleMenu "1" --> "1" ProductService
    ConsoleMenu "1" --> "1" PersonService
    ConsoleMenu "1" --> "1" SaleService
    ConsoleMenu "1" --> "1" AccessoryService
    ConsoleMenu "1" --> "1" PromotionService
    ConsoleMenu "1" --> "1" ReturnService
    ConsoleMenu "1" --> "1" WarrantyService
 
    %% ===== ENTRY POINT =====
    class Main {
        +main(args String[]) void$
    }
 
    Main ..> ConsoleMenu
    Main ..> ProductRepository
    Main ..> PersonRepository
    Main ..> SaleRepository
    Main ..> AccessoryRepository
    Main ..> PromotionRepository
    Main ..> ReturnRepository
    Main ..> WarrantyRepository
    Main ..> ProductService
    Main ..> PersonService
    Main ..> SaleService
    Main ..> AccessoryService
    Main ..> PromotionService
    Main ..> ReturnService
    Main ..> WarrantyService

```

**Layering note:** `SaleRepository` only depends on `SaleRecord`, a plain data-transfer object holding raw ids (customerId, sellerId, productIds) as stored in `sales.dat`. The resolution of those ids into full domain objects (`Customer`, `Seller`, `Product`) is done in `SaleService`, which already depends on `ProductService` and `PersonService`. This keeps the dependency direction strictly as `ui → service → persistence → model`, with no reverse dependency from `persistence` to `service`.
