# Multiservice System

## Project Summary

This project is a microservices system developed with Spring Boot. It uses Apache Kafka for asynchronous communication and message processing. The service consumes messages from a Kafka topic, processes the information, and manages the delivery of notifications to users when an user is created, a product is created, a product is added to a cart, among others.

## Technologies Used

- Java 17
- Spring Boot 3.x
- Apache Kafka
- Maven
- MySQL

## Main Features

- Connects and consumes messages from Kafka
- Processes and validates incoming events
- Sends notifications to users (e.g., email, push notifications)
- Error handling and retry mechanisms

## Requirements

- JDK 17 or higher
- Apache Kafka running (locally or remotely)
- Maven 3.x

## How to Run

1. Clone the repository:
   ```bash
   git clone https://github.com/EstebanGC/notification-service.git
 
