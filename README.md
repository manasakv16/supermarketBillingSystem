# supermarketBillingSystem
 

About: 
A basic java spring boot application that can be used by employees of a shop to bill thier customers in a simple way

Branches:
-> Refer to branch main for a basic app
1. Basic frontend using HTML
2. Basic CRUD allowed backend

-> Refer to branch "backend" for a complete back end application with below features
1. Completely backend, test endpoints via REST (CRUD + other features)
2. Security - Token based (RSA + JWT) - login/ logout/ extend session
3. Security - disable user / lock user/ share temp password over mail to assist in password reset etc
4. Logging - Every time you run application a log file is created and logs are written to it
5. Integrate Gmail to share BILL via mail to users
6. Encrypt the Bill with a password

Guide: 
-> Below are the endpoints given in "backend" branch

1. Auth Endpoints → ("/auth")
POST-> ("/signup”) : sign up user
POST-> ("/signin") : sign in user
POST-> ("/refresh") : refresh user token
POST-> ("/logout”) : Logout an user
POST-> ("/forgot/{id}") : sends a temporary password to user with id = {id} 
POST-> ("/reset/{id}") : allows to reset password for user with id = {id} (use the previously sent temp password to set new password)

2. Customer Endpoints → ("/customer")
Get -> ("/”) : show all customers
Get -> ("/{id}") : Get customer with id = {id}
PUT -> ("/{id}") : Update a customer with id = {id}

3. Product Endpoints → ("/product")
GET-> ("/”) : show all products
POST -> ("/") : Add a product
GET-> ("/{id}") : Get product with id = {id}
PUT -> ("/{id}") : Update a product with id = {id}
DELETE -> ("/{id}") : Delete a product with id = {id}

4. Sale Endpoints → ("/sale")
GET-> ("/”) : show all Sale
POST -> ("/") : Add a Sale
GET-> ("/{id}") : Get Sale with id = {id}
GET-> ("/{id}/{pid}") : Get user cart, Sale with id = {id} & product with id = {pid}
PUT-> ("/{id}/{pid}") : Update user cart, Sale with id = {id} & product with id = {pid}
DELETE-> ("/{id}/{pid}") : DELETE user cart, Sale with id = {id} & product with id = {pid}
DELETE -> ("/{id}") : Delete a Sale with id = {id}
GET -> ("/Bill{id}") : Get bill for Sale with id = {id}
GET -> ("generate/{id}") : Generate Bill for id = {id} as PDF and allow download option

5. User Endpoints → ("/user") // sales team
GET-> ("/”) : show all users
POST -> ("/") : Add a user
GET-> ("/{id}") : Get user with id = {id}
PUT -> ("/{id}") : Update a user with id = {id}
DELETE -> ("/{id}") : Delete a user with id = {id}

Mail Endpoints -> ("mail")
POST -> ("/") : Send mail with predefined data
