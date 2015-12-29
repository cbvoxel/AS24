# Using the API

## Routes and their usage
GET     /adverts/            gets a list of adverts in json format (see below)
GET     /adverts/<id>        get one single advert with id, 404 if advert does not exist (see below)
POST    /adverts/<id>        update one single advert by id. Any data sent in the body will be modified. 
                             If id is not known, 404 status code is returned, else json format (see below).
PUT     /adverts/            add one single advert, the advert gets a new uuid as id, and data is added as provided
                             by a json body. Defaults are applied if not specified. Returns json data formatted as below.
DELETE  /adverts/            delete advert by id, if it not exists 404 status code is returned.

## Data Format
Default json data
```json
{
  "id":"<generated uuid>",
  "title":"",
  "fuel":"gasoline",
  "isNew":0,
  "mileage":"0,
  "price":0,
  "firstRegistration":"<current date/time in format yyyy-MM-dd (*)>"
}
```
Time format for input can be "yyyy-MM-dd" or in ISO-8601. It will, however, always be returned as "yyyy-MM-dd" (requirement).


Example json data
```json
{
  "id":"1d4c551c-c3e1-4fff-97a3-de630a89ac35",
  "title":"Audi A4 Avant - modified",
  "fuel":"gasoline",
  "isNew":0,
  "mileage":"45233,
  "price":8644,
  "firstRegistration":"2010-08-30"
}
```
 

## Installation
You must supply your aws credentials in your home directory:
~/.aws/credentials
The application uses the region eu-central-1 (hard coded). If this needs to be changed, you must change the code in 
logic.DynamoDbRepositoryImpl. 
The application expects a table named `car_adverts`. If this must be changed, you must change the code in 
models.CarAdvertDynamoDb.
