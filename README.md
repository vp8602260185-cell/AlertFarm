# AlertFarm

AlertFarm is a Spring Boot-based web application designed to fetch, store, and serve agricultural mandi (market) price data and manage user registrations for farmers and stakeholders.

## Features

- Fetches mandi price data from the Indian government API and stores it in a MySQL database.
- RESTful API endpoints for:
	- Fetching all mandi prices
	- Fetching total records
	- User registration with validation and duplicate email checks
	- Listing all users
- Input validation and global exception handling for clean API responses.
- Modular code structure with service, repository, controller, and DTO layers.

## Tech Stack

- Java 21
- Spring Boot 4.x
- Spring Data JPA
- MySQL
- Hibernate Validator
- Maven

## Getting Started

### Prerequisites

- Java 21+
- Maven
- MySQL

### Configuration

Edit `src/main/resources/application.properties` to set your database credentials and API keys:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/testdb
spring.datasource.username=root
spring.datasource.password=root123

mandi.api.key=YOUR_API_KEY
mandi.api.url=https://api.data.gov.in/resource/9ef84268-d588-465a-a308-a864a43d0070?api-key=
mandi.api.format=json
mandi.api.limit=1000
```

### Build & Run

```bash
cd alertkisan
mvn clean install
mvn spring-boot:run
```

### API Endpoints

#### Mandi Data

- `POST /api/v1/mandi/`  
	Fetch and store mandi data from the API.

- `GET /api/v1/mandi/`  
	Get all mandi prices.

- `GET /api/v1/mandi/total_records`  
	Get the total number of mandi price records.

#### User Management

- `POST /api/v1/users`  
	Register a new user.  
	Request body:  
	```json
	{
		"name": "Farmer Name",
		"email": "farmer@example.com",
		"password": "yourpassword",
		"role": "USER",
		"phoneNumber": "9876543210"
	}
	```

- `GET /api/v1/users`  
	List all registered users.

### Project Structure

- `controller/` - REST controllers for API endpoints
- `service/` - Business logic and data processing
- `repository/` - Spring Data JPA repositories
- `models/` - JPA entities
- `dto/` - Data Transfer Objects
- `exception/` - Global exception handling

### Contributing

Pull requests are welcome! For major changes, please open an issue first to discuss what you would like to change.

### License

This project is licensed under the MIT License.

---
