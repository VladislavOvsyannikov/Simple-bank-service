# Simple bank service

## Intro
A simple bank service providing APIs for:
- Create bank users with the ability to get the entire list of them.
- Create and retrieve bank accounts, which are composed of user, number, four-digit PIN code and balance.
Account number is generated automatically as a unique 16-digit sequence. A single user may have multiple accounts.
- Perform bank operations such as deposit, withdraw or transfer money between accounts. 
Any operation which deducts funds from the account needs to include the correct PIN code.
- Record all balance changes to each of the accounts with the ability to retrieve all operations for a certain account.

Application is based on Kotlin and Spring Boot, and uses H2 as an in-memory database.

## Build and run

### Build .jar
```
mvn clean install
```

### Build Docker image
```
docker build -t bank .
```

### Run via Docker
```
docker run -d -p 8080:8080 bank
```

## Usage

### Swagger
```
http://localhost:8080/swagger-ui.html
```

### OpenAPI
```
http://localhost:8080/v3/api-docs.yaml
```