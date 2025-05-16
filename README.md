# tracking
# Parcel Tracking Number Generator API

## Overview

This API generates unique tracking numbers for parcels.  It's designed to be scalable and efficient, suitable for use in a high-traffic e-commerce or logistics environment.  The API is built using Spring Boot.

## Features

* **Unique Tracking Number Generation:** Generates highly unique tracking numbers using UUIDs.
* **RESTful Interface:** Provides a simple RESTful API endpoint for generating tracking numbers.
* **Scalable Design:** The API is stateless, allowing it to be easily scaled horizontally.
* **Efficient Generation:** Generates tracking numbers quickly.
* **Configurable Prefix:** Tracking numbers are prefixed with "TN-" for easy identification.
* **Reasonable Length:** Tracking numbers are limited to 19 characters (including prefix) for readability.

## Technologies Used

* Spring Boot
* Java
* UUID

## Getting Started

### Prerequisites

* Java Development Kit (JDK) 8 or later
* Maven

### Installation

1.  Clone the repository:

    ```bash
    git clone <your_repository_url>
    ```

2.  Navigate to the project directory:

    ```bash
    cd tracking-number-generator
    ```

3.  Build the application using Maven:

    ```bash
    mvn clean install
    ```

### Running the Application

1.  Run the Spring Boot application:

    ```bash
    mvn spring-boot:run
    ```

    The API will be accessible at `http://localhost:8080`.

## API Usage

### Generate Tracking Number

* **Endpoint:** `/api/tracking/generate`
* **Method:** `GET`
* **Request:** No request body is required.
* **Response:**

    * **Status Code:** 200 OK
    * **Content Type:** `text/plain;charset=UTF-8`
    * **Body:** A string containing the generated tracking number.

#### Example Request

```bash
curl http://localhost:8080/api/tracking/generate

Example Response
TN-a1b2c3d4e5f6g7h8i

Configuration
The application can be configured using Spring Boot's standard configuration mechanisms (e.g., application.properties or application.yml).  Currently, the tracking number format is hardcoded, but you could add properties to configure:

Tracking number prefix.

Tracking number length.

Scalability
The API is designed to be stateless. This means that it can be easily scaled horizontally by running multiple instances of the application behind a load balancer.  Each instance can generate tracking numbers independently without needing to coordinate with other instances.  The use of UUIDs ensures that the generated tracking numbers will be unique across all instances.

Error Handling
The API currently returns a 200 OK status code for successful tracking number generation.  Error handling could be improved to include more specific status codes and error messages for different scenarios (e.g., server errors).

Future Enhancements
Add configuration options for the tracking number format (prefix, length, etc.).

Implement more robust error handling.

Consider adding a database to persist tracking numbers (if needed for tracking history or other business requirements).
