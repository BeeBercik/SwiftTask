### 🏦 Swift Codes Management Application

This project provides a RESTful API to manage SWIFT codes data (headquarters and branches) for different countries. 

The application automatically initialize the database with predefined data when the application starts. This ensures that the service is ready to use from the moment it is deployed.

The system is built with validation mechanisms and includes a comprehensive test suite (31 tests total) to guarantee reliability.

---

#### 🔥 Core Functionalities
#### 1. REST API for Managing SWIFT Codes
- Add new SWIFT codes
- Retrieve details of a specific SWIFT code
- List all SWIFT codes for a given country
- Delete SWIFT codes
#### 2. Automatic Database Population
- Upon startup, Spring Boot automatically inserts SWIFT codes into the database.
- No need to manually upload or parse an external file.
#### 3. Strict Validation Rules
- Ensures correct SWIFT code format (e.g., HQ codes must end in XXX).
- Validates ISO2 country codes (must be exactly 2 characters).
- Prevents insertion of invalid or inconsistent data.

---

#### 🛠️  Technology Stack
- **Java 17** + **Spring Boot**
- **Spring Data JPA** for persistence
- **Maven** for build automation
- **PostgreSQL** for storing data
- **Docker & Docker Compose** for containerization
- **JUnit 5, Mockito** for unit and integration tests

---

### 📌 API Endpoints

#### 1. Get SWIFT Code Details

GET: `/v1/swift-codes/{swift-code}`
- For a headquarter SWIFT code (ends in XXX): returns an object containing the bank data plus a list of branches.
- For a branch SWIFT code: empty branches array is returned.

#### 2. Get All SWIFT Codes for a Country

GET: `/v1/swift-codes/country/{countryISO2code}`

- Returns all SWIFT codes (headquarters and branches) for the given country code

#### 3. Add a New SWIFT Code

POST: `/v1/swift-codes`

- Adds a new valid SWIFT code to the database.

#### 4. Delete a SWIFT Code

DELETE:` /v1/swift-codes/{swift-code}`

- Removes an existing SWIFT code from the system.

---

### 🚀 Getting Started

#### Running with Docker (you need to have Docker installed)
1. Run via Docker Compose:<br>
   `docker-compose up -d --build`<br>
- This command will build the application and start both the database and the app containers in detach mode.

2. Verify It’s Running
- The API should be available at http://localhost:8080.
- Use Postman or curl to test endpoints.

---

### ✅ Testing

Project includes 31 tests.

1.	**Unit Tests:** Verifying service and controller logic.
2.	**Integration Tests:** Validating end-to-end application behavior using real database.


💡 All tests are run using JUnit 5, with MockMvc for controller testing and a test database for integration tests.

#### 🔹Running Tests Inside Docker
1.	**Ensure the application is running (docker-compose up).**
2.	**Open a shell inside the application container:**<br>
`docker exec -it <swift-app-container-name> /bin/shz`
3. **Run all tests:**<br>
`mvn test`

💡 The Docker image has Maven built-in, so when you are inside the container, you don’t need to have Maven installed on your machine.
