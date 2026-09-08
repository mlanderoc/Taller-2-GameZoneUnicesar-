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
        -purchaseHistory: List~Sale~
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
        -totalAmount: double
        +calculateTotal() double
        +generateReceipt() String
    }
    class promotion{
      -start_date:string
      -end_date: string
      -title:string
      -id:string
      +metodo isActive(localDate date):boolean
      +calculateDiscount(Sale sale):double
    }
    class  PercentageDiscount{
      discount_percentage:double
      +calculateDiscount():double
    }
     class CategoryDiscount{
      discount_percentage:double
      category:string
      object:string
      +calculateDiscount()
     }
     class BulkPurchaseDiscount{
      minimum_amount:int
      discount_percentage:double
      +calculateDiscount():double
     }

    Product <|-- VideoGame
    Product <|-- Console
    Person <|-- Customer
    Person <|-- Seller
    promotion<|-- PercentageDiscount
    promotion<|-- CategoryDiscount
    promotion<|-- BulkPurchaseDiscount
    Sale "1" --> "1" Customer
    Sale "1" --> "1" Seller
    Sale "1" o-- "1..*" Product
    Customer "1" o-- "0..*" Sale : purchaseHistory

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
    class SaleRecord {
        -saleId: String
        -date: LocalDate
        -customerId: String
        -sellerId: String
        -productIds: List~String~
        -totalAmount: double
    }
    class SaleRepository {
        +saveAll(records List~SaleRecord~) void
        +loadAll() List~SaleRecord~
    }
    class  PromotionRepository{
      +saveAll(List<Promotion>):List<Promotion>.
      +loadAll(): List<Promotion>.

    }
    ProductRepository ..> Product
    PersonRepository ..> Customer
    PersonRepository ..> Seller
    SaleRepository ..> SaleRecord
    PromotionRepository..>promotion
    %% ===== SERVICE LAYER =====
    class ProductService {
        -repository: ProductRepository
        +registerVideoGame(...) void
        +registerConsole(...) void
        +listAllProducts() List~Product~
        +updateStock(productId String, quantity int) void
        +findById(id String) Product
    }
    class PersonService {
        -repository: PersonRepository
        +registerCustomer(...) void
        +findCustomerById(id String) Customer
        +findSellerById(id String) Seller
        +listAllCustomers() List~Customer~
        +listAllSellers() List~Seller~
    }
    class SaleService {
        -saleRepository: SaleRepository
        -productService: ProductService
        -personService: PersonService
        +registerSale(customer Customer, seller Seller, products List~Product~) Sale
        +viewAllSales() List~Sale~
        +viewSalesByCustomer(customer Customer) List~Sale~
        +viewSalesBySeller(seller Seller) List~Sale~
    }
    class  PromotionService {
      -repository: promotionRepository
      + registerPercentageDiscount(...):void
      +registerCategoryDiscount(...):void
      +registerBulkPurchaseDiscount(...): void
      +listAllPromotions(): List<Promotion>:list<promotion>
      +listActivePromotions(): List<Promotion>
      +findBestPromotionFor(Sale sale): Promotion:
      +findById(String id): Promotion
    }

    ProductService "1" --> "1" ProductRepository
    ProductService ..> Product
    PersonService "1" --> "1" PersonRepository
    SaleService "1" --> "1" SaleRepository
    SaleService "1" --> "1" ProductService
    SaleService "1" --> "1" PersonService
    SaleService ..> Sale
    SaleService ..> SaleRecord
    PromotionService ..>PromotionRepository
     SaleService "1" --> "1" PromotionService

    %% ===== UI LAYER =====
    class ConsoleMenu {
        -productService: ProductService
        -personService: PersonService
        -saleService: SaleService
        +start() void
    }

    ConsoleMenu "1" --> "1" ProductService
    ConsoleMenu "1" --> "1" PersonService
    ConsoleMenu "1" --> "1" SaleService

    %% ===== ENTRY POINT =====
    class Main {
        +main(args String[]) void$
    }

    Main ..> ConsoleMenu
    Main ..> ProductRepository
    Main ..> PersonRepository
    Main ..> SaleRepository
    Main ..> ProductService
    Main ..> PersonService
    Main ..> SaleService
    Main ..>PromotionService
    Main ..>PromotionRepository

```

**Layering note:** `SaleRepository` only depends on `SaleRecord`, a plain data-transfer object holding raw ids (customerId, sellerId, productIds) as stored in `data/sales.csv`. The resolution of those ids into full domain objects (`Customer`, `Seller`, `Product`) is done in `SaleService`, which already depends on `ProductService` and `PersonService`. This keeps the dependency direction strictly as `ui → service → persistence → model`, with no reverse dependency from `persistence` to `service`.
