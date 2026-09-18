# Class hierarchu-diagram— GameZone Unicesar

```mermaid
---
config:
  layout: dagre
---
classDiagram
direction TB
    class person {
    }

    class customer {
    }

    class seller {
    }

    class product {
    }

    class game {
    }

    class console {
    }

    class accessories {
    }

    class Controller {
    }

    class cable {
    }

    class Memory {
    }

    class promotion {
    }

    class percentagediscount {
    }

    class categorydiscount {
    }

    class BulkPurchaseDiscount {
    }

    class Return {
    }

    class Warranty {
    }

    class BasicWarranty {
    }

    class ExtendedWarranty {
    }

    person <|-- customer
    person <|-- seller
    product <|-- game
    product <|-- console
    product <|-- accessories
    accessories <|-- Controller
    accessories <|-- cable
    accessories <|-- Memory
    promotion <|-- percentagediscount
    promotion <|-- categorydiscount
    promotion <|-- BulkPurchaseDiscount
    Warranty <|-- BasicWarranty
    Warranty <|-- ExtendedWarranty
