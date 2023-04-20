# Hardware Store
This project is a REST API built with Gradle/Spring Boot with an in memory h2 database.

The product admin functionality can be accessed with basic auth using the credentials `admin/password`

Example data is seeded at start up, check `data.sql` in the resources folder for details.

The following endpoints are supplied wth examples:
### Products
* GET /products - lists all available products in the store
  ```json
  [
    {
      "id": 1,
      "name": "Sledgehammer",
      "price": 124.95
    },
    {
      "id": 2,
      "name": "Axe",
      "price": 199.0
    }
  ]
  ```
* POST /products/admin - add a product to the store
  ```json
  {
    "id": 6,
    "name": "Shiny Axe",
    "price": 250.0
  }
  ```
* PUT /products/admin/{id} - updates an existing product if it exists
  ```json
  {
    "id": 6,
    "name": "Even Shinier Axe",
    "price": 500.0
  }
  ```
* DELETE /products/admin/{id} - deletes an existing product if it exists
### Orders
* GET /orders - retrieves all orders
  ```json
  [
    {
      "id": 1,
      "customerId": "5a679c00-f98e-4b74-992c-704a4dbc5835",
      "products": [
        {
          "productId": 3,
          "quantity": 200
        },
        {
          "productId": 4,
          "quantity": 20
        }
      ]
    }
  ]
  ```
* POST /orders - adds an order of products if those products exist. A customer id can be optionally supplied otherwise one will be generated
  ```json
  [
    {
      "id": 1,
      "products": [
        {
          "productId": 3,
          "quantity": 200
        },
        {
          "productId": 4,
          "quantity": 20
        }
      ]
    }
  ]
  ```

## Design choices
* Spring Boot. I chose to use Spring Boot as it is  a widely accepted industry standard which has been around a long time, has a lot of support/development still being done and doesn't seem to be at risk of being discontinued any time soon. Another option I have used before is Quarkus but I chose not to use it as it's advantages e.g. (live coding, start up speed) are not worth the speed of development/support that Spring brings.
* H2 in-memory database. In basically any other case I would not use an in memory database and instead opt for something like MSSQL which gives better reliability/stability (plus it's easier to dig into an explore). Considering this Hardware Store demo is very lightweight I decided that having an API which could be checked out and run with minimal config to be more ideal. It also has the added advantage that writing the SQL for creating the tables is not necessary as it can be driven entirely off entity objects. The obvious disadvantages of this is that the database will not persist when we close down the app and storing any large amount of data would be a waste of memory.
* REST. I chose to use REST because it seemed to fit the specifications best compared to alternatives such as GraphQL. For how simple the current structure of the Product/Order schemas are there doesn't seem to be any need (yet) for fine tuning the details we return from the current endpoints

## Shortcomings/Improvements
* HATEOAS - Normally I'd build REST APIs with HATEOAS links included so the REST API could be navigated from the client side. This would mean we could potentially update how we structure endpoints without any needed change from the client. It would also make the order list a little less clunky as we could prodive links to products rather than supplying raw product ids.
* Security - I've chosen a pretty simple authentication strategy for this project which I wouldn't use in a production environment (I'd likely use Auth0 or something similar instead). The main issues to note are:
  * Hardcoded admin credentials which can easily be picked up by anyone viewing the code
  * Admin is authenticated through plain basic auth, ideally you'd want to drive this through tokens which need regular refreshing
  * Any user can place any order against any customer id
* Performance
  * There's no notion of pagination or batching for large orders
  * Creating orders could run into an issue if lots of products are included. The following query will break if the set of product ids becomes too large:
    ```java
    @Query("select count(*) from Product p where p.id in (?1)")
    int countMatchingProductIds(Set<Integer> productIds);
    ```
* Design
  * Currently, the store has an infinite number of products. I considered adding a quantity field to the Products table which would then be updated when orders were made but decided against it due to time constraints
  * Customer identification is pretty rudimentary. Ideally I'd like to have some kind of authentication to properly determine which customer is which but I instead opted for a simple UUID to identify orders belonging to a particular customer