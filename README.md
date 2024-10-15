# PackageSelfService Backend

This project is a Spring Boot web service that serves as the backend for the PackageSelfService. It provides REST APIs to list available receivers, submit package details, list all packages for a sender, and retrieve the details of an individual package.

## Prerequisites

Before running the application, ensure that you have the following installed on your local machine:

- Java 17
- Maven 3.x
- Spring Boot 3.3.4

## Technologies Used

- **Java 17**: The application is developed using Java 17.
- **Spring Boot**: The core framework for building the backend service.
- **Maven**: Build and dependency management tool.
- **Spring Cloud OpenFeign**: For making HTTP requests to external services.
- **OpenAPI (Springdoc)**: Automatically generates API documentation and provides a Swagger UI.
- **Lombok**: Used to reduce boilerplate code.

## Dependencies

The following major dependencies are used in the project:

1. **Spring Boot Web**: Provides the foundation for creating web applications, including REST APIs.
2. **Spring Cloud OpenFeign**: This project uses OpenFeign for making HTTP requests to external services. For this assignment, instead of integrating with the PackageShippingService using Feign, the service details are hardcoded in the application. When integrating with real external services, Feign clients will simplify service-to-service communication in a declarative way.
3. **Springdoc OpenAPI**: For generating Swagger UI documentation.
4. **Lombok**: Reduces the amount of boilerplate code like getters/setters.
5. **JUnit**: For unit testing the service.

## Functional Requirements

The application provides the following functionalities via REST APIs. Currently, due to the unavailability of the ProductOrderService, hardcoded data has been used to simulate the service interactions in a specific way. Once the external service becomes available, the application will use a FeignClient to make real-time HTTP requests to the ProductOrderService.

List Available Receivers
API: /api/v1/packages/listReceivers
Description: Retrieves a hardcoded list of available receivers with masked address details.

Submit a Package for Delivery
API: /api/v1/packages/submitPackage
Description: Submit details of a package, including the package name, weight, receiver's employee ID, and sender's employee ID.
Future Integration: This API will call the ProductOrderService using FeignClient to submit the package for delivery.
```java
    feignClient.submitPackage(request);
 ```

List Packages for a Sender
API: /api/v1/packages/listPackages/{senderId}
Description: Retrieves a list of packages submitted by the specified sender. Optional filtering by package status is supported.
Future Integration: This API will call the ProductOrderService using FeignClient to retrieve a list of orders placed by the sender.
```java
    feignClient.listOrders(status, 0, 10);
```


Get Details of an Individual Package
API: /api/v1/packages/getPackageDetails/{packageId}
Description: Retrieves the details of a specific package, including the registration date, status, and the expected delivery date (if applicable).
Future Integration: This API will call the ProductOrderService using FeignClient to fetch the details of the package from the external service.
```java
    feignClient.getOrderDetails(packageId);
```

## Steps to Run

Clone the repository to your local machine:
git clone <repository-url>
cd PackageSelfServiceBackend

Build the project using Maven:
mvn clean install

Run the Spring Boot application:
mvn spring-boot:run
The application will be available at http://localhost:8080.

To explore the API, navigate to the Swagger UI at:
http://localhost:8080/swagger-ui.html

Testing:
Unit tests can be created in the src/test/java directory using JUnit. Use the following command to run the tests:
mvn test


This README.md provides a complete guide to running and using the PackageSelfService backend locally, including detailed API documentation, integration examples, and configuration steps. The project is designed to be easily extendable, with future integration with external services like PackageShippingService through FeignClient.





