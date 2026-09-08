# AI Usage Log - Developer

## Student Information

**Full name:** Hector
**Role:** Developer
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
