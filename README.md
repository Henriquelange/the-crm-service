# The CRM Service

## Getting started
Since our service uses Cognito as the IDP, you will need to configure AWS in your environment.
####If you are familiarized with AWS configuration:
- Set up a new profile using the credentials that
were provided for you, and set it as the default in ~/.aws/credentials.
####If you are new to AWS configuration and still don't have the client installed:
1) Install the [aws-cli](https://docs.aws.amazon.com/cli/latest/userguide/getting-started-install.html) accordingly to your Operational System;
2) Run `aws configure`, and use the Access Key ID and Secret Access Key that were provided for you;
3) Leave the rest of the configurations as default (`us-east-1` and `None`).

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
- Manage customers personal information;
- List existing customers.

## Technologies
Here are the underlying technologies that provides all the support needed for this service:
- Spring 2.7.0;
- Java 11;
- AWS DynamoDB database;
- Cognito as IDP;
- New Relic for APM.

## Documentation
The technical documentation for the APIs is available through Swagger, in _/swagger-ui.html_ path.