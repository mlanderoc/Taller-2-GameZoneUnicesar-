# Class hierarchu-diagram— GameZone Unicesar

```mermaid
classDiagram
    person <|-- customer
    person <|-- seller
    product <|-- game
    product <|-- console
    promotion <|--percentagediscount
    promotion <|--categorydiscount
    promotion <|--BulkPurchaseDiscount

