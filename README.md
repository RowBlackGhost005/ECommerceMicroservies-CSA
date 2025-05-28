
# Microservice E-Commerce

An E-Commerce app built with Spring Boot following Microservices Architecture for managing Orders, Products, Payments logs and Users.
(This repo holds one stable version, each project has is unique repo, see #Projects)

## Technologies
- Java 21
- Spring Boot 3.5
- MySQL

## Features
- Microservice Architecture.
- API Gateway for single entry point.
- Dynamic management/discovery using Eureka
- Dynamic configuration management using Cloud Config Server.
- Secured using JWT.
- Management of role base Users.
- Management of Products.
- Management of Orders.
- Management of Payments.

# HOW TO RUN

**1. Download this repo (or Download each individual project, see #Projects)**

You can use GIT or download it as a ZIP in the *`<>Code`* button
```
git clone https://github.com/RowBlackGhost005/ECommerceMicroservies-CSA.git
```

---


**2. Setup your enviroment variables [Windows]**

*If you cant setup env variables in your machine, check step 3*

On your CMD type each:
```
set DB_USER=MySQLUser
set DB_PASSWORD=MySQLUserPassword
set USERSERVICE_JWT_SECRET=YourSecretCustomJWTKey
```
**You may be required to restart your system to be able to read those env variables**

*Note 1: Your JWT secret must be at least 24 characters long.*

*Note 2: Set your correct MySQL User and Password.*

*Note 3: You can skip this step but you'll need extra steps shown down below.*

---

**3. Manually setting up the .properties files (Optional)**

If you cant set env variables or you want to set up different ports for the services, you can avoid the config server by setting up the .properties files manually. To achieve this do:

```
Go to the Config Files Repo located at: 
https://github.com/RowBlackGhost005/ECommerceCSA-ConfigFiles
And download each application-dev.properties, they are each in a folder named after the service where they belong to.
```

```
Match each .properties file with each service and place it under /src/main/resources/ along side the application.properties
```
```
Modify the entries you require like removing env references
spring.datasource.username=${DB_USER}
with
spring.datasource.username=root
```
```
Now modify each project application.properties to remove the reference to the config server, to achieve this simply remove or comment the following line:
spring.config.import=configserver:http://localhost:8888/
Either remove or comment with # at the beginning
#spring.config.import=configserver:http://localhost:8888/
```
*Note that you now bypassed the config server and it wont be required to run because no service will query its config from it (Unless you only set manually some .properties files)*

---

**4. Setup the Databases in MySQL**

Open MySQL Workbench or CLI to create the databases (Tables are automatically generated)
```
CREATE DATABASE UsersCSA;
CREATE DATABASE ProductsCSA;
CREATE DATABASE OrdersCSA;
CREATE DATABASE PaymentsCSA;
```
*Note: You *can* create your own databases name but only if you skip step 2 and follow the linked steps*

---

**5. Execute the services**

Now you can execute each service by calling the `MAIN` class located in `/src/main/java/com.marin.{AppName}.{AppName}Application.java` of each service.

For this ECommerce to work you'll need to execute the services in a specific order and wait for it to fully initialize before starting up the next one shown as follows:
```
1. Execute ECommerceServiceRegistry (ECommerce-Eureka Folder)
2. Execute ECommerceConfigServer (SpringCloudConfigServer Folder)
3. Execute ECommerce-API-Gateway
4. Execute User Service
5. Execute Orders Service
6. Execute Products Service
7. Execute Payments Service
```
*Note: The ports used by these services are [8761 , 8888 , 8080 , 8084 , 8082 , 8081 , 8083] respectively*

---

**6. Verify the execution**

Once your services are up and running you can monitor them by going into the Eureka endpoint located at: `http://localhost:8761/`, there you should be able to see each service as well as the API Gateway.

If you can see all 4 services and the API Gateway up and running you can now start using the app through the API Gateway located at `http://localhost:8080/`

*Note: Services MAY take up to two minutes to be able to respond to request due to the time it takes to discover them through Eureka*

## API Reference

Now that the ECommerce is up and running you can perform various actions in the system through the API Gateway, you can still call each microservice individually because there is no implemented system to avoid it but it is not recomended.

The section below will show you how to interact with the system through the API Gateway divided into the different categories

### Users

#### Register

```http
POST http://localhost:8080/auth/register
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- | 
| `Credentials` | `JSON` | {"username":"admin" , "password":"password"} | 

Response: (String)
```
User registered sucessfully
```

---

#### Login

```http
POST http://localhost:8080/auth/login
```

---

| Parameter | Type     | Description                | Response                |
| :-------- | :------- | :------------------------- | :------------------------- |
| `Credentials` | `JSON` | {"username":"admin" , "password":"password"} | JWT Token|

Response: (JWT Token)
```
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlkIjoxLCJyb2xlcyI6WyJST0xASDFETUlOIl0sImlhdCI6MTc0ODM5Mzk1NiwiZXhwIjoxNzQ4NDgwMzU2fQ.wZiTCpWuKeEHnfX9_GoE3_RplZOy7eVcUph0_19HGYo
```

---

#### Profile

```http
GET http://localhost:8080/users/profile
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- | 
| `Authorization` | `HEADER` | Bearer {JWT TOKEN} | 

Response: Profile of the Authenticated user (JSON Object)
```JSON
    {
    "id": 1,
    "username": "admin"
    }
```

---

#### Profile by User

Requires ADMIN JWT Token
```http
GET http://localhost:8080/users/profile/{Username}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- | 
| `Authorization` | `HEADER` | Bearer {ADMIN JWT TOKEN} | 
| `{username}` | `String` | Username to fetch its profile | 

Response: Profile of the User with username {username} (JSON Object)
```JSON
    {
    "id": 1,
    "username": "admin"
    }
```

---

#### All users

Requires ADMIN JWT Token
```http
GET http://localhost:8080/users
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- | 
| `Authorization` | `HEADER` | Bearer {ADMIN JWT TOKEN} | 

Response: Array of all registered users (JSON Array)
```JSON
[
    {
        "id": 1,
        "username": "admin"
    },
    {
        "id": 2,
        "username": "luis"
    }
]
```

---

#### Delete user

Requires ADMIN JWT Token
```http
DELETE http://localhost:8080/users/{id}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- | 
| `Authorization` | `HEADER` | Bearer {ADMIN JWT TOKEN} | 
| `{id}` | `INT` | Id of the user to delete | 

Response: 200 (OK)
```JSON
1
```

---

### Products

#### Create Product

Requires ADMIN JWT Token
```http
POST http://localhost:8080/products
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- | 
| `Authorization` | `HEADER` | Bearer {ADMIN JWT TOKEN} | 
| `Product` | `JSON` | {"name":"Cupcake" , "description":"Chocolate cupcake" , "price": 1.50, "stock": 25} | 

Response: Registered product (JSON Object)
```JSON
{
    "id": 1,
    "name": "Cupcake",
    "description": "Small cupcake with chocolate",
    "price": 1.5,
    "stock": 25
}
```
---

#### Fetch all products

```http
GET http://localhost:8080/products
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- | 
| `Authorization` | `HEADER` | Bearer {JWT TOKEN} | 

Response: All registered products (JSON Array)
```JSON
[
    {
        "id": 1,
        "name": "Cookies",
        "description": "Chocolate cookies",
        "price": 1.99,
        "stock": 32
    },
    {
        "id": 2,
        "name": "Vanilla Ice Cream",
        "description": "Delicious vanilla ice cream",
        "price": 7.5,
        "stock": 14
    },
    {
        "id": 3,
        "name": "Cupcake",
        "description": "Small cupcake with chocolate",
        "price": 1.5,
        "stock": 9
    }
]
```

---

#### Fetch product by ID

```http
GET http://localhost:8080/products/{id}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- | 
| `Authorization` | `HEADER` | Bearer {JWT TOKEN} | 
| `{id}` | `INT` | Id of the product | 

Response: Product with ID {id} (JSON Object)
```JSON
    {
        "id": 2,
        "name": "Vanilla Ice Cream",
        "description": "Delicious vanilla ice cream",
        "price": 7.5,
        "stock": 14
    }
```

---

#### Update product

Updates one or more fields of a product if present in the Body.
```http
PUT http://localhost:8080/products/{id}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- | 
| `Authorization` | `HEADER` | Bearer {ADMIN JWT TOKEN} | 
| `{id}` | `INT` | Id of the product to update| 
| `Product` | `JSON` | {"description":"Delicious vanilla ice cream 250g"}| 

Response: Product with ID {id} updated (JSON Object)
```JSON
{
    "id": 2,
    "name": "Vanilla Ice Cream",
    "description": "Delicious vanilla ice cream 250g",
    "price": 7.5,
    "stock": 14
}
```

---

#### Add stock to a product

```http
PUT http://localhost:8080/products/stock/add
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- | 
| `Authorization` | `HEADER` | Bearer {ADMIN JWT TOKEN} | 
| `Product Stock` | `JSON` | {"productId":2 , "stock": 10} | 

Response: New stock of the product (JSON)
```JSON
{
    "productId": 2,
    "quantity": 15
}
```

---

#### Remove stock of a product

```http
PUT http://localhost:8080/products/stock/add
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- | 
| `Authorization` | `HEADER` | Bearer {ADMIN JWT TOKEN} | 
| `Product Stock` | `JSON` | {"productId":2 , "stock": 5} | 

Response: New stock of the product (JSON)
```JSON
{
    "productId": 2,
    "quantity": 10
}
```

---

#### Delete product

```http
DELETE http://localhost:8080/products/{id}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- | 
| `Authorization` | `HEADER` | Bearer {ADMIN JWT TOKEN} | 
| `{id}` | `INT` | Id of the product to delete| 

Response: 200 (OK)
```HTTP
1
```

---

### Orders

#### Create an Order

```http
POST http://localhost:8080/orders
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- | 
| `Authorization` | `HEADER` | Bearer {JWT TOKEN} | 
| `Order` | `JSON` | { "userId": 1, "products": [ { "productId": 1, "quantity": 1 }, { "productId": 2, "quantity" : 1 } ] }
 | 

Response: Registered Order (JSON Object)
```JSON
{
    "id": 3,
    "userId": 1,
    "orderDate": "2025-05-27T18:31:43.7347969",
    "status": "ORDERED",
    "total": 9.49,
    "orderDetails": [
        {
            "id": 7,
            "productId": 1,
            "quantity": 1,
            "unitPrice": 1.99
        },
        {
            "id": 8,
            "productId": 2,
            "quantity": 1,
            "unitPrice": 7.5
        }
    ]
}
```

---

#### Get orders of Auth User

```http
GET http://localhost:8080/orders/user
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- | 
| `Authorization` | `HEADER` | Bearer {JWT TOKEN} | 

Response: Registered Orders from authenticated user (JSON Object)
```JSON
[
    {
        "id": 1,
        "userId": 1,
        "orderDate": "2025-05-26T23:30:14",
        "status": "CANCELLED",
        "total": 12.49,
        "orderDetails": [
            {
                "id": 1,
                "productId": 1,
                "quantity": 1,
                "unitPrice": 1.99
            },
            {
                "id": 2,
                "productId": 2,
                "quantity": 1,
                "unitPrice": 7.5
            },
            {
                "id": 3,
                "productId": 3,
                "quantity": 2,
                "unitPrice": 1.5
            }
        ]
    },
    {
        "id": 2,
        "userId": 1,
        "orderDate": "2025-05-26T23:30:46",
        "status": "PROCESSING",
        "total": 12.49,
        "orderDetails": [
            {
                "id": 4,
                "productId": 1,
                "quantity": 1,
                "unitPrice": 1.99
            },
            {
                "id": 5,
                "productId": 2,
                "quantity": 1,
                "unitPrice": 7.5
            },
            {
                "id": 6,
                "productId": 3,
                "quantity": 2,
                "unitPrice": 1.5
            }
        ]
    },
]
```
---

#### Cancel an order

```http
POST http://localhost:8080/orders/cancel/{id}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- | 
| `Authorization` | `HEADER` | Bearer {JWT TOKEN} | 
| `{id}` | `INT` | ID of the order to cancel | 

Response: Order cancelled (JSON Object)
```JSON
{
    "id": 1,
    "userId": 1,
    "orderDate": "2025-05-26T23:30:14",
    "status": "CANCELLED",
    "total": 12.49,
    "orderDetails": [
        {
            "id": 1,
            "productId": 1,
            "quantity": 1,
            "unitPrice": 1.99
        },
        {
            "id": 2,
            "productId": 2,
            "quantity": 1,
            "unitPrice": 7.5
        },
        {
            "id": 3,
            "productId": 3,
            "quantity": 2,
            "unitPrice": 1.5
        }
    ]
}
```

---

#### Process an Order

Advances the status of an order Ordered -> Processing -> Shipped -> Delivered

```http
POST http://localhost:8080/orders/process/{id}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- | 
| `Authorization` | `HEADER` | Bearer {JWT TOKEN} | 
| `{id}` | `INT` | ID of the order to process | 

Response: Order with new status (JSON Object)
```JSON
{
    "id": 2,
    "userId": 1,
    "orderDate": "2025-05-26T23:30:46",
    "status": "SHIPPED",
    "total": 12.49,
    "orderDetails": [
        {
            "id": 4,
            "productId": 1,
            "quantity": 1,
            "unitPrice": 1.99
        },
        {
            "id": 5,
            "productId": 2,
            "quantity": 1,
            "unitPrice": 7.5
        },
        {
            "id": 6,
            "productId": 3,
            "quantity": 2,
            "unitPrice": 1.5
        }
    ]
}
```

---

#### Fetch all orders


```http
GET http://localhost:8080/orders
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- | 
| `Authorization` | `HEADER` | Bearer {ADMIN JWT TOKEN} | 

Response: All registered orders (JSON Array)
```JSON
[
    {
        "id": 1,
        "userId": 1,
        "orderDate": "2025-05-26T23:30:14",
        "status": "CANCELLED",
        "total": 12.49,
        "orderDetails": [
            {
                "id": 1,
                "productId": 1,
                "quantity": 1,
                "unitPrice": 1.99
            },
            {
                "id": 2,
                "productId": 2,
                "quantity": 1,
                "unitPrice": 7.5
            },
            {
                "id": 3,
                "productId": 3,
                "quantity": 2,
                "unitPrice": 1.5
            }
        ]
    },
    {
        "id": 2,
        "userId": 1,
        "orderDate": "2025-05-26T23:30:46",
        "status": "SHIPPED",
        "total": 12.49,
        "orderDetails": [
            {
                "id": 4,
                "productId": 1,
                "quantity": 1,
                "unitPrice": 1.99
            },
            {
                "id": 5,
                "productId": 2,
                "quantity": 1,
                "unitPrice": 7.5
            },
            {
                "id": 6,
                "productId": 3,
                "quantity": 2,
                "unitPrice": 1.5
            }
        ]
    },
    {
        "id": 3,
        "userId": 1,
        "orderDate": "2025-05-27T18:31:44",
        "status": "ORDERED",
        "total": 9.49,
        "orderDetails": [
            {
                "id": 7,
                "productId": 1,
                "quantity": 1,
                "unitPrice": 1.99
            },
            {
                "id": 8,
                "productId": 2,
                "quantity": 1,
                "unitPrice": 7.5
            }
        ]
    }
]
```

---

#### Fetch order by ID


```http
GET http://localhost:8080/orders/{id}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- | 
| `Authorization` | `HEADER` | Bearer {ADMIN JWT TOKEN} | }
| `{id}` | `INT` | ID of the order to fetch | 

Response: Order with ID {id} (JSON Object)
```JSON
{
    "id": 1,
    "userId": 1,
    "orderDate": "2025-05-26T23:30:14",
    "status": "CANCELLED",
    "total": 12.49,
    "orderDetails": [
        {
            "id": 1,
            "productId": 1,
            "quantity": 1,
            "unitPrice": 1.99
        },
        {
            "id": 2,
            "productId": 2,
            "quantity": 1,
            "unitPrice": 7.5
        },
        {
            "id": 3,
            "productId": 3,
            "quantity": 2,
            "unitPrice": 1.5
        }
    ]
}
```

---

#### Fetch orders by auth user

Fetch all orders of the authenticated user
```http
GET http://localhost:8080/orders/user
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- | 
| `Authorization` | `HEADER` | Bearer {JWT TOKEN} | 

Response: All orders of the authenticated user (JSON Array)
```JSON
[
    {
        "id": 1,
        "userId": 1,
        "orderDate": "2025-05-26T23:30:14",
        "status": "CANCELLED",
        "total": 12.49,
        "orderDetails": [
            {
                "id": 1,
                "productId": 1,
                "quantity": 1,
                "unitPrice": 1.99
            },
            {
                "id": 2,
                "productId": 2,
                "quantity": 1,
                "unitPrice": 7.5
            },
            {
                "id": 3,
                "productId": 3,
                "quantity": 2,
                "unitPrice": 1.5
            }
        ]
    },
    {
        "id": 2,
        "userId": 1,
        "orderDate": "2025-05-26T23:30:46",
        "status": "SHIPPED",
        "total": 12.49,
        "orderDetails": [
            {
                "id": 4,
                "productId": 1,
                "quantity": 1,
                "unitPrice": 1.99
            },
            {
                "id": 5,
                "productId": 2,
                "quantity": 1,
                "unitPrice": 7.5
            },
            {
                "id": 6,
                "productId": 3,
                "quantity": 2,
                "unitPrice": 1.5
            }
        ]
    },
    {
        "id": 3,
        "userId": 1,
        "orderDate": "2025-05-27T18:31:44",
        "status": "ORDERED",
        "total": 9.49,
        "orderDetails": [
            {
                "id": 7,
                "productId": 1,
                "quantity": 1,
                "unitPrice": 1.99
            },
            {
                "id": 8,
                "productId": 2,
                "quantity": 1,
                "unitPrice": 7.5
            }
        ]
    }
]
```

---

#### Fetch orders by user id

Fetch all orders of the authenticated user
```http
GET http://localhost:8080/orders/user/{id}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- | 
| `Authorization` | `HEADER` | Bearer {JWT TOKEN} | 
| `{id}` | `INT` | Id of the user to fetch its orders | 

Response: All orders from the user with ID {id} (JSON Array)
```JSON
[
    {
        "id": 1,
        "userId": 1,
        "orderDate": "2025-05-26T23:30:14",
        "status": "CANCELLED",
        "total": 12.49,
        "orderDetails": [
            {
                "id": 1,
                "productId": 1,
                "quantity": 1,
                "unitPrice": 1.99
            },
            {
                "id": 2,
                "productId": 2,
                "quantity": 1,
                "unitPrice": 7.5
            },
            {
                "id": 3,
                "productId": 3,
                "quantity": 2,
                "unitPrice": 1.5
            }
        ]
    },
    {
        "id": 2,
        "userId": 1,
        "orderDate": "2025-05-26T23:30:46",
        "status": "SHIPPED",
        "total": 12.49,
        "orderDetails": [
            {
                "id": 4,
                "productId": 1,
                "quantity": 1,
                "unitPrice": 1.99
            },
            {
                "id": 5,
                "productId": 2,
                "quantity": 1,
                "unitPrice": 7.5
            },
            {
                "id": 6,
                "productId": 3,
                "quantity": 2,
                "unitPrice": 1.5
            }
        ]
    },
    {
        "id": 3,
        "userId": 1,
        "orderDate": "2025-05-27T18:31:44",
        "status": "ORDERED",
        "total": 9.49,
        "orderDetails": [
            {
                "id": 7,
                "productId": 1,
                "quantity": 1,
                "unitPrice": 1.99
            },
            {
                "id": 8,
                "productId": 2,
                "quantity": 1,
                "unitPrice": 7.5
            }
        ]
    }
]
```

---

### Payments

#### Create a payment order of an order

Creates a Payment Order used to link a paymet to an order, this is usually called only
by the Orders Service after creating an order.
```http
POST http://localhost:8080/payments/orders
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- | 
| `Authorization` | `HEADER` | Bearer {JWT TOKEN} | 
| `Order` | `JSON` | { "orderId": 1 } | 

Response: Registered Payment (JSON Object)
```JSON
{
    "id": 4,
    "orderId": 1,
    "paymentDate": "2025-05-27T18:45:11.650142",
    "status": "PROCESSING"
}
```

#### Process a Payment

Process the payment to 'pay' (Simulated, will always return status 'CONFIRMED' unless previosuly cancelled)
```http
POST http://localhost:8080/payments/process
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- | 
| `Authorization` | `HEADER` | Bearer {JWT TOKEN} | 
| `Payment` | `JSON` | { "id": 1 } | 

Response: Processed Payment (JSON Object)
```JSON
{
    "id": 2,
    "orderId": 1,
    "paymentDate": "2025-05-27T00:09:02",
    "status": "CONFIRMED"
}
```

---


#### Fetch payments

Fetch all registerd payments
```http
GET http://localhost:8080/payments
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- | 
| `Authorization` | `HEADER` | Bearer {ADMIN JWT TOKEN} | 

Response: Registered payments (JSON Array)
```JSON
[
    {
        "id": 1,
        "orderId": 2,
        "paymentDate": "2025-05-26T23:30:46",
        "status": "CANCELLED"
    },
    {
        "id": 2,
        "orderId": 1,
        "paymentDate": "2025-05-27T00:09:02",
        "status": "CONFIRMED"
    },
    {
        "id": 3,
        "orderId": 3,
        "paymentDate": "2025-05-27T18:31:44",
        "status": "PROCESSING"
    },
    {
        "id": 4,
        "orderId": 1,
        "paymentDate": "2025-05-27T18:45:12",
        "status": "PROCESSING"
    }
]
```

---


#### Update payment status

Sets the status of an order directly like 'CANCELLED', 'CONFIRMED' , 'PROCESSING
```http
PUT http://localhost:8080/payments/{id}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- | 
| `Authorization` | `HEADER` | Bearer {ADMIN JWT TOKEN} | 
| `{id}` | `INT` | Id of the order to change status | 
| `Status` | `JSON` | {"status":"CANCELLED"} | 

Response: Payment with status updated (JSON Object)
```JSON
{
    "id": 1,
    "orderId": 2,
    "paymentDate": "2025-05-26T23:30:46",
    "status": "CANCELLED"
}
```

---


# Projects
This repo only holds one stable version of the service for demonstration purposes, each service has its unique repo as shown below:

#### User service
```
https://github.com/RowBlackGhost005/UserService
```

#### Orders service
```
https://github.com/RowBlackGhost005/OrdersService-CSA
```

#### Products service
```
https://github.com/RowBlackGhost005/ProductsService-CSA
```

#### Payments service
```
https://github.com/RowBlackGhost005/PaymentsService-CSA
```

#### Configuration Server
```
https://github.com/RowBlackGhost005/SpringCloudConfigServer-CSA
```

#### Discovery Server
```
https://github.com/RowBlackGhost005/ECommerce-Eureka-CSA
```

#### API Gateway
```
https://github.com/RowBlackGhost005/ECommerce-APIGateway-CSA
```

#### Configuration Files Repo
```
https://github.com/RowBlackGhost005/ECommerceCSA-ConfigFiles
```

---
Developed by: Luis Marin
