# Read Me 

# Endpoints in this Cart Application
| Endpoint                  | Method |           Description           |
|---------------------------|:------:|:-------------------------------:|
| /product                  |  POST  |        create a product         |
| /product/{productId}      |  GET   |      fetch a product by Id      |
| /product/{productId}      |  PUT   |        Update a product         |
| /discount                 |  POST  | create a discount for a product |
| /discount/{discountId}    |  PUT   |        update a discount        |
| /basket                   |  POST  |         create a basket         |
| /basket/{basketId}        |  GET   |         fetch a basket          |
| /basket/{basketId}/add    |  POST  |       Add item to basket        |
| /basket/{basketId}/remove |  POST  |     Remove item from basket     |
| /basket/{basketId}/review |  POST  | Review the items in the basket  |

Test Endpoint url: http://localhost:8080/product

# Build Commands
## Running this spring boot application
mvn spring-boot:run

## Running the tests in this spring boot application
mvn test

