# Past Papers Service
<!-- Badges -->
[![Java 21](https://img.shields.io/badge/Java-21-007396?logo=openjdk&logoColor=white)](https://openjdk.org/projects/jdk/21/)
![Spring Boot 3.x](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?logo=spring-boot)
[![Build Status](https://github.com/MufidGu/pastpapers-service/actions/workflows/build-deploy.yml/badge.svg)](https://github.com/MufidGu/pastpapers-service/actions/workflows/build-deploy.yml)

This is the backend service for the Past Papers application, a platform designed to streamline the management and distribution of educational materials for students and educators.

**Live API Documentation (Swagger UI):** [139.185.58.82:8444/swagger-ui/index.html](http://139.185.58.82:8444/swagger-ui/index.html)

**Companion Android Application (Figma Design):** [View on Figma](https://www.figma.com/design/KfHVSo1iniYjBlhp3ZTGVV/Past-Papers-v1?node-id=123-705&t=YAI4BeEBi5sHIeSA-1)

## 🎯 Project Purpose

The Past Papers application solves a common problem for students: the fragmented and inefficient management of past exam papers. Currently, these valuable resources are scattered across various cloud storage and local storage devices, making them difficult to find and use. This project provides a centralized, searchable, and secure platform to solve this problem.

## ✨ Features

- **RESTful API:** A comprehensive set of endpoints for managing institutions, degrees, courses, instructors, and papers.
- **Hexagonal Architecture:** A clean and decoupled architecture that separates the core business logic from external concerns, making the application easier to maintain and test.
- **Secure by Design:** Secured with OAuth2 and role-based access control (RBAC) using Google as the authentication provider.
- **OpenAPI Documentation:** Automatically generated and interactive API documentation with Swagger UI.
- **CI/CD Pipeline:** Automated build, test, and deployment pipeline using GitHub Actions and Docker.
- **Environment-based Configuration:** Separate configurations for development, testing, and production environments.
- **Robust Error Handling:** A global exception handler that provides meaningful error messages.
- **Input Validation:** Comprehensive validation of all incoming requests.

## 🏛️ Architectural Overview

This project is built using the principles of **Hexagonal Architecture** (also known as Ports and Adapters). This architectural style isolates the core business logic from external dependencies, promoting maintainability and testability.

```
+-------------------+      +------------------+      +-------------------+
|  Infrastructure   |      |      Domain      |      |  Infrastructure   |
| (e.g., REST API)  | <--> | (Business Logic) | <--> |  (e.g., Database) |
+-------------------+      +------------------+      +-------------------+
       (Port)                     (Core)                     (Port)
```

- **Domain (`src/main/java/com/mufidgu/pastpapers/domain`)**: This is the heart of the application. It contains all business logic, entities, and domain-specific exceptions. It is completely independent of any external frameworks or technologies.

- **Infrastructure (`src/main/java/com/mufidgu/pastpapers/infrastructure`)**: This layer acts as the bridge between the domain and the outside world. It contains adapters for external concerns, such as:
    - **REST Controllers:** Exposing the domain's capabilities as a RESTful API.
    - **Configuration:** Setting up the application, including security and wiring the domain services.
    - **Exception Handling:** Translating domain exceptions into appropriate HTTP responses.

This clean separation ensures the core logic is robust and can be adapted to different technologies (e.g., swapping from REST to gRPC) with minimal changes to the domain.

## 🛠️ Technology Stack

### Core Backend
- **Java 21:** Utilizes the latest long-term support (LTS) version of Java for modern language features and performance improvements.
- **Spring Boot 3:** Provides a robust, convention-over-configuration framework for building stand-alone, production-grade applications.
- **Spring Web:** The foundation for building the RESTful API with embedded Tomcat server.

### Database & Persistence
- **PostgreSQL:** Production-grade relational database for persistent data storage.
- **Spring Data JPA:** Simplifies database access with the Java Persistence API and Hibernate ORM.
- **Flyway:** Database migration tool for versioned schema management.
- **Two-User Security Model:** Separate DDL and DML database users for enhanced security.

### Security
- **Spring Security:** Implements comprehensive security measures, configured to act as an OAuth2 Resource Server.
- **JWT Authentication:** Secures endpoints by validating JSON Web Tokens, ensuring that all requests are properly authenticated.

### API & Documentation
- **Springdoc OpenAPI:** Automatically generates interactive API documentation from the source code, providing a live Swagger UI for easy exploration and testing of endpoints.
- **Spring Boot Actuator:** (planned) Provides production-ready features like health checks and metrics.

### Development Tools & Build
- **Gradle:** A powerful build automation tool for dependency management and running application tasks.
- **Lombok:** Reduces boilerplate code like getters, setters, and constructors through annotations, leading to cleaner and more readable domain objects.

### Testing
- **JUnit 5:** The primary framework for writing and running unit and integration tests.
- **Spring Boot Test:** Provides utilities and annotations to simplify the testing of Spring applications.
- **Spring Security Test:** Offers support for testing secured endpoints and method-level security.

### Deployment
- **Docker:** Containerizes the application and its environment, ensuring consistency from development to production.
- **GitHub Actions:** Orchestrates a full CI/CD pipeline for automated testing, building, and deploying the application as a Docker container.

## 🚀 Getting Started

### Prerequisites

- Java 21
- Gradle
- PostgreSQL 12+ (for database persistence)

### Running the Application

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/MufidGu/pastpapers-service.git
    ```
2.  **Navigate to the project directory:**
    ```bash
    cd pastpapers-service
    ```
3.  **Set up the database:**
    ```bash
    # See DATABASE_INTEGRATION.md for detailed setup instructions
    cd database-setup
    psql -U postgres -f 01-create-database-and-users.sql
    ```
4.  **Configure environment variables:**
    ```bash
    export DB_URL=jdbc:postgresql://localhost:5432/pastpapers_db
    export DB_DDL_USERNAME=pastpapers_ddl_user
    export DB_DDL_PASSWORD=ddl_password
    export DB_DML_USERNAME=pastpapers_dml_user
    export DB_DML_PASSWORD=dml_password
    ```
5.  **Build the application:**
    ```bash
    ./gradlew clean bootJar
    ```
6.  **Run the application:**
    ```bash
    java -jar build/libs/*.jar
    ```
7.  **Grant DML permissions (first run only):**
    ```bash
    psql -U postgres -d pastpapers_db -f database-setup/02-grant-dml-permissions.sql
    ```

The application will be available at `http://localhost:8080`.

For detailed database setup, configuration, and troubleshooting, see [DATABASE_INTEGRATION.md](DATABASE_INTEGRATION.md).

## 🔒 Security

The application is secured using Spring Security's OAuth2 Resource Server. All endpoints require a valid JWT token issued by Google.

- **Authentication:** The service validates JWTs from Google, ensuring that only authenticated users can access the API.
- **Authorization:** Role-based access control (RBAC) is implemented to restrict access to certain endpoints based on user roles. Admin-level access is managed via a configurable list of Google User IDs.

## 🧪 Testing

The project includes a suite of unit and integration tests to ensure the quality and correctness of the code.

- **Run tests:**
  ```bash
  ./gradlew test
  ```

## 🔄 CI/CD

The project is configured with a CI/CD pipeline using GitHub Actions. The pipeline is defined in `.github/workflows/build-deploy.yml` and includes the following stages:

1.  **Run Tests:** Automatically runs all tests on every push to the `main` and `dev` branches.
2.  **Build JAR:** Builds the application into a JAR file.
3.  **Build and Deploy:**
    - Builds a Docker image of the application.
    - Pushes the image to the GitHub Container Registry.
    - Deploys the container to the server.

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a pull request or open an issue to report a bug or suggest a feature.

## 📊 Database

The application uses PostgreSQL with a comprehensive security model:

- **Persistent Storage:** All data is stored in PostgreSQL with proper relationships and constraints
- **Audit Trail:** All tables include audit fields (createdAt, updatedAt, createdBy, updatedBy)
- **Two-User Security:** Separate DDL and DML users prevent schema modifications at runtime
- **Cascading Rules:** Properly configured foreign key relationships with appropriate cascade behaviors
- **Migration Management:** Flyway handles all schema versioning and migrations

See [DATABASE_INTEGRATION.md](DATABASE_INTEGRATION.md) for complete documentation on:
- Database architecture and schema
- Security model and user roles
- Setup and configuration
- Migration management
- Troubleshooting and best practices

## 🔮 Future Work

- **Performance Optimization:** Implement caching strategies and query optimization
- **Read Replicas:** Set up database read replicas for horizontal scaling
- **Full-text Search:** Add Elasticsearch for advanced paper search capabilities
- **File Storage:** Consider moving large files to object storage (S3, MinIO) for better scalability