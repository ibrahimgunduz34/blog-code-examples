# Spring Batch Example Project (Without Spring Boot)

This project demonstrates how to set up a Spring Batch application without using Spring Boot. It includes a simple batch job that downloads and reads data from a CSV file, processes it, and writes the results to a database.

## Prequisites
- Java 11 or higher
- Maven
- docker (with compose plugin)

## Project Structure

```
/
├── mockserver/
└── src/
    └── org.example.productbatch/
        ├── config/
        ├── model/
        ├── processor/
        ├── reader/
        ├── runner/
        ├── tasklet/
        └── writer/
```        

### mockserver: 
This is a simple mock server that serves a CSV file for testing purposes. It is configured to run on port 3000 and returns the CSV content when accessed via http://localhost:3000/catalogs/download/

### config: 
This package holds the configuration classes that set up components used throughout the application, such as Hibernate and Spring Batch. 

### model: 
This package contains the data model classes used in the application. It includes AcmeProduct, which maps the structure of the CSV file, and Product, the domain model used for persistence and business logic.

### processor: 
This package contains the ItemProcessor implementation responsible for processing the data read from the CSV file. It handles necessary transformations and validations before the data is passed to the writer.

### reader: 
This package contains the ItemReader implementation responsible for reading CSV data and mapping it to the application's internal data model, based on the provider's structure.

### runner: 
This package contains the runner classes responsible for executing the batch processes. These classes serve as entry points and are triggered by the Spring Scheduler.

### tasklet: 
This package contains Tasklet implementations that handle single-step tasks such as downloading files, cleaning folders, and similar operations.

### writer: 
This package contains the ItemWriter implementation responsible for persisting data that has been processed by the ItemProcessor.


## How To Run The Project

We created a demo environment using Docker Compose. To run the project with all dependencies, just run the following command.

```bash
$ docker compose up
```

## How To Check The Results In Database
You can check the results in the database using the client in the Postgres container. To access the Postgres client and checking the result, run the following command:

```bash
$ docker compose \
exec postgres \
psql -U postgres -w -d catalog_db -c "SELECT * FROM catalog_products OFFSET 0 LIMIT 10;"
```