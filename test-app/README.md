# test-app

## Description

It's the backend portion of the Test App.

## How to build

To build the runnable .jar file, run the following command from the console, from its base folder (prerequisite: having the Java 17+ and latest version of Maven installed):

        mvn clean package

## How to run

To run the project, run the following command from the console, from the generate jar's folder (prerequisite: having Java 17 installed):

        java -jar test-app-0.0.1-SNAPSHOT.jar


## Application endpoints

The project contains the following endpoints (all of them run from the base URL http://localhost:8080/):

#### Get all purchases

Returns all available transaction purchases registered in the application with no added conversion exchange rate data.

        GET /purchase/get-all

Parameters: none

#### Add a purchase

Adds a new purchase to the application.

        POST /purchase/add

Example of a required payload in JSON format:

        { "description": "Example Purchase 1", "transactionDate": "2023-12-17", "purchaseAmount": 10.50 }

#### Get a purchase

Returns a specific transaction purchase from the application with added conversion exchange rate data if a valid currency is given.

        GET /purchase/get

Parameters: id (number) -> mandatory, it's the id of a purchase already registered in the application; currency (string) -> optional, it's the currency used to add data about exchange rates.

#### Get all purchases converted to a given currency

Returns all available transaction purchases registered in the application with added conversion exchange rate data for each transaction, if available.

        GET /purchase/get-all-converted

Parameters: none

#### Get all available currencies

Returns a list of all valid and available currencies to be used with the endpoints that accept this field.

        GET /exchange-rate/get-available-currencies

Parameters: none

#### Get the current exchange retes

Returns a list ordered by currency name of all available exchange rates from the application. Only the most recent rate for each currency is returned.

        GET /exchange-rate/get-current-exchange-rates

Parameters: none

## Other info

A tool like [Postman](https://www.postman.com) can be used to add to and retrieve data from the application. Or, additionally, a web interface can be used for that, which is the second portion of the Test App.

## About testing

The application source code contains a suite of tests in the following path: \src\main\java\com\test\alexandre\wex\testapp