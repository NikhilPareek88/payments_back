# Payments Clearing Cost API

This is a Spring Boot-based API that provides the clearing cost of a payment card based on the card's number. The API uses path parameters for country-based lookups and validates the length of the card number. It has some card clearing cost management APIs as well, which does add, updated, delete or get for card clearing costs.


----------------------------

## Validation
- The ``card_number`` field must be between **8 to 19** characters long.
- **Invalid requests** (card numbers with lengths outside the 8–19 range) will return a ``400 Bad Request`` with the corresponding validation message.

----------------------------

## Technologies Used

- **Spring Boot:** Framework for creating the REST API.
- **Springdoc OpenAPI:** To automatically generate Swagger UI documentation.
- **Maven:** Build tool for managing dependencies and packaging the application.
- **Spring Validation:** To validate the input parameters with annotations such as ``@Size, @NotNull``, etc.

----------------------------

## Setup and Installation
### Prerequisites
- **Java 21** or higher.
- **Maven** installed (if using Maven for building the application).
- **Docker** (optional, if you prefer to containerize the application).
### 1. Clone the Repository
   Clone the repository to your local machine:

```
git clone <repository-url>
cd <repository-directory>
```

### 2. Build the Application
   You can build the application using Maven:

```
mvn clean package
```

This will generate a jar file in the target/ directory.

### 3. Run the Application
   You can run the application using:

```
mvn spring-boot:run
```

The application will start on http://localhost:8080/payments_back.

----------------------------

## API Documentation (Swagger UI)
Swagger UI will be available once the application is running:

**URL:** http://localhost:8080/payments_back/swagger-ui.html

Here, you can interact with the API by sending requests and viewing the responses.

----------------------------

## Running with Docker (Optional)
To run the application in Docker, you can use the following ``Dockerfile`` to build a Docker image:

### 1. Build the Docker Image:

```
docker build -t payments-back .
```

### 2. Run the Docker Container:

```
docker run -p 8080:8080 payments-back
```

This will run the application inside a Docker container, accessible on http://localhost:8080/payments_back/swagger-ui/index.html.

----------------------------

## Pull Docker Image from DockerHub 
To run the application locally as dockerimage, pull it from dockerhub.

### 1. Pull the docker image:

```
docker pull nikhilpareek88/payments-back:latest
```

### 2. Run the docker container:

```
docker run -p 8080:8080 nikhilpareek88/payments-back:latest
```

One spring application started, follow further:

### 3. Use Swagger UI page to try the APIs locally:

```
http://localhost:8080/payments_back/swagger-ui/index.html
```


----------------------------

## Future Enhancements
- **Authentication:** Add authentication mechanisms like OAuth2 or Basic Authentication.
- **Cache Integration:** Use External Cache (Redis or Memcache) to store and retrieve clearing costs rather in-memory cache.
- **Unit Tests:** Add comprehensive unit tests for the controller and service layers.

----------------------------

## Conclusion
This API provides a simple yet efficient way to retrieve clearing costs for payment cards based on the card number. It supports validation, error handling, and interactive documentation via Swagger UI for seamless integration and testing.
