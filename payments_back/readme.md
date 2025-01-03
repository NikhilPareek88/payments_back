# Payments Clearing Cost API

This is a Spring Boot-based API that provides the clearing cost of a payment card based on the card's number. The API uses path parameters for country-based lookups and validates the length of the card number using JSR-303/JSR-380 annotations for validation.

## Features

- **Get clearing cost** for a given country based on the card number.
- **Card number length validation**: The card number must be between 8 and 19 characters.
- **Validation**: Uses Spring's `@Valid` and `@Size` annotations to validate inputs.
- **Swagger UI**: Interactive API documentation generated with Springdoc OpenAPI.

## Endpoints

### 1. **Get All Clearing Costs**

#### Endpoint: `GET /payment-card-cost/`

This endpoint retrieves all the available clearing costs stored in the system.

**Response:**
- **200 OK**: Returns a collection of all clearing costs.

#### Example Request:
```bash
curl -X GET "http://localhost:8080/payment-card-cost/"
````

----------------------------

### 2. **Get Clearing Cost by Country**

#### Endpoint: `GET /payment-card-cost/country/{country}`

This endpoint fetches the clearing cost for a specific country based on the card number.

**Path parameter:**
- country – the country code for which the clearing cost is to be fetched (e.g., US for the United States).

**Response:**

- 200 OK: Successfully returns the clearing cost for the given country.
- 404 Not Found: If no clearing cost is found for the country.
- 500 Internal Server Error: If there is any unexpected error.

#### Example Request:
````bash
curl -X GET "http://localhost:8080/payment-card-cost/country/US"
````

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
Copy code
git clone <repository-url>
cd <repository-directory>
```

### 2. Build the Application
   You can build the application using Maven:

```
Copy code
mvn clean package
This will generate a jar file in the target/ directory.
```

### 3. Run the Application
   You can run the application using:

```
Copy code
mvn spring-boot:run
```

Alternatively, you can run the application by executing the generated JAR file:

```bash
Copy code
java -jar target/your-application-name.jar
```
The application will start on http://localhost:8080.

----------------------------

## API Documentation (Swagger UI)
Swagger UI will be available once the application is running:

**URL:** http://localhost:8080/swagger-ui.html

Here, you can interact with the API by sending requests and viewing the responses.

----------------------------

## Exception Handling

- **Validation errors:** If the ```card_number``` field fails the validation (i.e., it is not between 8 to 19 characters), the API will return a ``400 Bad Request`` response with a descriptive error message.

- **404 Not Found:** If the requested country does not have a clearing cost available, a ``404`` response will be returned.

- **500 Internal Server Error:** If there is an unexpected error, a ``500`` response will be returned.

----------------------------

## Testing
You can test the API by sending valid and invalid requests:

- **Valid Request Example:**

```
Copy code
{
"card_number": "1234567890"
}
```

- **Invalid Request Example (Card Number Too Short):**
  
```
Copy code
{
"card_number": "123"
}
```

- **Invalid Request Example (Card Number Too Long):**

```
Copy code
{
"card_number": "12345678901234567890123456"
}
```

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
docker pull nikhilpareek88/payments-back
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
- **Database Integration:** Use a database to store and retrieve clearing costs rather than keeping them in memory.
- **Unit Tests:** Add comprehensive unit tests for the controller and service layers.

----------------------------

## Conclusion
This API provides a simple yet efficient way to retrieve clearing costs for payment cards based on the card number. It supports validation, error handling, and interactive documentation via Swagger UI for seamless integration and testing.
