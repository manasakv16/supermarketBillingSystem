create database supermarketdb;

use supermarketdb;

create table product(
product_id int primary key auto_increment,
product_name varchar(30),
product_cost double,
product_unit int,
total_product_cost double,
product_company varchar(30),
product_type varchar(20),
product_description varchar(100)
);

create table customer(
customer_name varchar(30),
customer_mobile varchar(10) primary key,
customer_email varchar(50)
);

create table sales(
sales_id int primary key auto_increment,
customer_id varchar(10),
total double,
sales_date date,
foreign key(customer_id) references customer(customer_mobile)
);

create table cart(
sales_id int ,
product_id int,
product_count int,
foreign key(sales_id) references sales(sales_id),
foreign key(product_id) references product(product_id),
primary key(sales_id, product_id)
);

