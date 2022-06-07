# The CRM Service

## Getting started
To start the local infrastructure for development, simply run:
```bash
make docker-start
```
Then, make sure that the Maven dependencies are installed and that the project
components are working properly by running:
```text
mvn clean test
```
Finally, just run **ApiApplication**. The server will be running at http://localhost:8080.

## Description
This service is responsible for providing the backend functionalities for our CRM!
Its main responsibilities includes:
- CRM User registration and login;
- Manage consumer personal information;
- List existing consumers.

## Technologies
Here are the underlying technologies that provides all the support needed for this service:
- Spring 2.7.0;
- Java 11;
- AWS DynamoDB database;
- Cognito as IDP;
- New Relic for APM.

## Documentation
The technical documentation for the APIs is available through Swagger, in _/swagger-ui.html_ path.