```mermaid
graph TD

    %% Capa de Interfaz de Usuario
    subgraph UI ["Capa de Interfaz de Usuario (ui)"]
        ConsoleMenu["ConsoleMenu / MainApp<br/><i>(Líder Técnico)</i>"]
    end

    %% Capa de Servicios
    subgraph Service ["Capa de Servicios (service)"]
        PersonService["PersonService<br/><i>(Desarrollador 2)</i>"]
        SaleService["SaleService<br/><i>(Líder Técnico)</i>"]
        ProductService["ProductService<br/><i>(Desarrollador 1)</i>"]
        PromotionService["PromotionService<br/><i>(Desarrollador 2)</i>"]
    end

    %% Capa de Persistencia
    subgraph Persistence ["Capa de Persistencia (persistence)"]
        SaleRepository["SaleRepository<br/><i>(Líder Técnico)</i>"]
        PersonRepository["PersonRepository<br/><i>(Desarrollador 2)</i>"]
        ProductRepository["ProductRepository<br/><i>(Desarrollador 1)</i>"]
        PromotionRepository["PromotionRepository<br/><i>(desarollador 2)</1>"]
    end

    %% Capa de Modelo
    subgraph Model ["Capa de Modelo (model)"]
        
        subgraph ModVentas ["Módulo de Ventas (Líder Técnico)"]
            Sale["Sale"]
            SaleRecord["SaleRecord"]
        end
        
        subgraph ModPersonas ["Módulo de Personas (Desarrollador 2)"]
            Person["«abstract»<br/>Person"]
            Customer["Customer"]
            Seller["Seller"]
        end
        
        subgraph ModProductos ["Módulo de Productos (Desarrollador 1)"]
            Product["«abstract»<br/>Product"]
            VideoGame["VideoGame"]
            Console["Console"]
        end

        subgraph ModPromociones ["Módulo de Promociones (desarollador 1)"]
            Promotion["«abstract»<br/>Promotion"]
            PercentageDiscount["PercentageDiscount"]
            CategoryDiscount["CategoryDiscount"]
            BulkPurchaseDiscount["BulkPurchaseDiscount"]
        end
        
    end

    %% Relaciones desde UI
    ConsoleMenu --> PersonService
    ConsoleMenu --> SaleService
    ConsoleMenu --> ProductService

    %% Relaciones desde Service
    PersonService --> PersonRepository
    PersonService --> Person

    SaleService --> SaleRepository
    SaleService -.-> ProductService
    SaleService -.-> PersonService
    SaleService -.-> PromotionService
    SaleService --> Sale
    SaleService --> Person
    SaleService --> Customer
    SaleService --> Seller
    SaleService --> Product
    SaleService --> SaleRecord

    ProductService --> ProductRepository
    ProductService --> Product

    PromotionService --> PromotionRepository
    PromotionService --> Promotion

    %% Relaciones desde Persistence
    SaleRepository --> SaleRecord
    PersonRepository --> Person
    ProductRepository --> Product
    PromotionRepository --> Promotion
