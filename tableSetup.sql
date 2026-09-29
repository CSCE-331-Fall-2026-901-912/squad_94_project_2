DROP TABLE IF EXISTS inv_edible;
DROP TABLE IF EXISTS inv_nonedible;
DROP TABLE IF EXISTS menu_drink;
DROP TABLE IF EXISTS menu_toppings;
DROP TABLE IF EXISTS join_menu_drink_and_inv_edible;
DROP TABLE IF EXISTS join_menu_topping_and_inv_edible;
DROP TABLE IF EXISTS employees;
DROP TABLE IF EXISTS orders;

CREATE TABLE inv_edible(
    id_edible serial PRIMARY KEY,
    name text,
    amount_servings int
);


CREATE TABLE inv_nonedible(
    id_nonedible serial PRIMARY KEY,
    name text,
    amount int
);



CREATE TABLE menu_drink(
    id_drink serial PRIMARY KEY,
    name text,
    price float,
    hot_available boolean,
    non_caffeinated_available boolean,
    alternative_milk_available boolean
);

CREATE TABLE menu_toppings(
    id_topping serial PRIMARY KEY,
    name text,
    price float
);


CREATE TABLE join_menu_drink_and_inv_edible(
    id_join_menu_drink_and_inv_edible serial PRIMARY KEY,
    id_drink int,
    id_edible int
);

CREATE TABLE join_menu_topping_and_inv_edible(
    id_join_menu_topping_and_inv_edible serial PRIMARY KEY,
    id_topping int,
    id_edible int
);

CREATE TABLE employees(
    id_employee serial PRIMARY KEY,
    name text,
    position text,
    phone_number text,
    pay_rate_hourly float,
    hours_scheduled int
    
);

CREATE TABLE orders(
    id_order serial PRIMARY KEY,
    completed boolean,
    time_created_at timestamptz,
    time_completed_at timestamptz,
    total_spent float,
    tip float,
    id_drink int,
    id_topping1 int,
    id_topping2 int,
    ice_level int,
    sugar_level int,
    hot_choosen boolean,
    non_caffeinated_chosen boolean,
    alternative_milk_chosen boolean

);






