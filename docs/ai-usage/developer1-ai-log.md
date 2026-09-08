# AI Usage Log - Developer 1

## Student Information

 **Full name:** Carlos Gomez
 **Role:** Developer 1
 **Assigned module:** Product management
 **Project:** GameZone Unicesar

## Purpose of AI Use

I used Artificial Intelligence as a support tool during the development of the product module. The tool was used to clarify object-oriented programming concepts, review my code decisions, understand Java features, and identify possible improvements. I reviewed all suggestions and implemented the final code myself.

## AI Tool Used

 **Tool:** Perplexity AI
 **Use:** Concept clarification and code review.

## Consultation Log

###  Product hierarchy design

 **Topic:** Inheritance in the product module.
 **Consultation:** I asked for clarification about how to organize common product attributes and specific attributes for video games and consoles.
 **What I learned:** Common data such as ID, title, price, and stock should belong to the abstract `Product` class. Attributes that only apply to video games or consoles should remain in their respective subclasses.
 **How I applied it:** I used this clarification to understand and review the inheritance relationship between `Product`, `VideoGame`, and `Console`.

###  Abstract description method

 **Topic:** Polymorphism and abstract methods.
 **Consultation:** I asked how each product type could display its own detailed description while sharing a common base class.
 **What I learned:** A method whose behavior varies by product type can be declared as abstract in the base class and implemented with `@Override` in each subclass.
 **How I applied it:** I used this concept to understand the purpose of the product description method and verify that each concrete product type provides its own implementation.

###  Stock update validation

 **Topic:** Product inventory management.
 **Consultation:** I asked about validating stock updates and how to prevent inventory values from becoming invalid.
 **What I learned:** Stock must be handled through a method that validates the new quantity or requested change. Business rules should be placed in the service layer when they involve application operations.
 **How I applied it:** I reviewed the stock-related behavior in my product module and made sure that the product service is responsible for coordinating inventory updates and persistence.

 ### Product persistence

 **Topic:** File-based persistence in the product module.
 **Consultation:** I asked about the responsibilities of a repository class when saving and loading products from files.
 **What I learned:** The repository is responsible for file access, while the model classes should not contain file handling logic. The service layer calls the repository and applies business rules.
 **How I applied it:** I used this guidance to keep `ProductRepository` focused on saving and loading data and to keep file operations outside the product model classes.

### Code review and naming

 **Topic:** Code quality and Java naming conventions.
 **Consultation:** I asked for a review of naming and structure in my product module.
 **What I learned:** Classes should use PascalCase, while attributes and methods should use camelCase. Names should be descriptive and written in English.
 **How I applied it:** I reviewed identifiers in the product module and kept names consistent with Java conventions and the project requirements.


