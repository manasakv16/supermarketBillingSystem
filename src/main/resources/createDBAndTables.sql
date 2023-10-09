create database supermarketdb;

use supermarketdb;

create table product(
product_id int primary key auto_increment,
product_name varchar(30),
product_cost double,
product_company varchar(30),
product_type varchar(20),
product_description varchar(100)
);

create table sales(
sales_id int primary key auto_increment,
customer_id varchar(10),
customer_name varchar(30),
customer_mobile varchar(10),
customer_email varchar(50),
total double,
sales_date date
);


