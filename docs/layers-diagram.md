```mermaid
---
config:
  layout: elk
  theme: base
---
flowchart TB
 subgraph UI["UI"]
        ConsoleMenu["ConsoleMenu / MainApp"]
  end
 subgraph Service["Service"]
        PersonService["PersonService"]
        SaleService["SaleService"]
        ProductService["ProductService"]
        PromotionService["PromotionService"]
  end
 subgraph DAOLayer["DAO/Repository"]
        SaleRepository["SaleRepository"]
        PersonRepository["PersonRepository"]
        ProductRepository["ProductRepository"]
        PromotionRepository["PromotionRepository"]
  end
 subgraph ModVentas["Sale module"]
        Sale["Sale"]
        SaleRecord["SaleRecord"]
  end
 subgraph ModPersonas["People module"]
        Person["«abstract»Person"]
        Customer["Customer"]
        Seller["Seller"]
  end
 subgraph ModProductos["Products module"]
        Product["«abstract»Product"]
        VideoGame["VideoGame"]
        Console["Console"]
  end
 subgraph ModPromociones["Promotion module"]
        Promotion["«abstract»Promotion"]
        PercentageDiscount["PercentageDiscount"]
        CategoryDiscount["CategoryDiscount"]
        BulkPurchaseDiscount["BulkPurchaseDiscount"]
  end
 subgraph Model["Model"]
        ModVentas
        ModPersonas
        ModProductos
        ModPromociones
  end
    UI --> Service
    Service --> DAOLayer
    Service --> Model
    DAOLayer --> Model

