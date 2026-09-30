DROP TABLE IF EXISTS inv_edible;
DROP TABLE IF EXISTS inv_nonedible;
DROP TABLE IF EXISTS menu_drink;
DROP TABLE IF EXISTS menu_toppings;
DROP TABLE IF EXISTS join_menu_drink_and_inv_edible;
DROP TABLE IF EXISTS join_menu_topping_and_inv_edible;
DROP TABLE IF EXISTS employees;
DROP TABLE IF EXISTS orders;

CREATE TABLE inv_edible(
    id_edible int PRIMARY KEY,
    name text,
    amount_servings int
);


CREATE TABLE inv_nonedible(
    id_nonedible int PRIMARY KEY,
    name text,
    amount int
);



CREATE TABLE menu_drinks(
    id_drink int PRIMARY KEY,
    name text,
    price numeric,
    hot_available boolean,
    is_non_caffeinated boolean
);

CREATE TABLE menu_toppings(
    id_topping int PRIMARY KEY,
    name text,
    price numeric
);


CREATE TABLE join_menu_drinks_and_inv_edible(
    id_join_menu_drinks_and_inv_edible int PRIMARY KEY,
    id_drink int,
    id_edible int
);

CREATE TABLE join_menu_toppings_and_inv_edible(
    id_join_menu_toppings_and_inv_edible int PRIMARY KEY,
    id_topping int,
    id_edible int
);

CREATE TABLE employees(
    id_employee int PRIMARY KEY,
    name text,
    position text,
    phone_number text,
    current_pay_rate numeric,
    hours__worked_for_week int
    
);

CREATE TABLE orders(
    id_order int PRIMARY KEY,
    completed boolean,
    time_created_at timestamptz,
    time_completed_at timestamptz,
    total_spent numeric,
	id_employee int,
    tip numeric,
    id_drink int,
    id_topping1 int,
    id_topping2 int,
    ice_level int,
    sugar_level int,
    hot_chosen boolean
    
);

\copy employees from '../csv_data/employees.csv' CSV HEADER
\copy inv_edible from '../csv_data/inv_edible.csv' CSV HEADER
\copy inv_nonedible from '../csv_data/inv_nonedible.csv' CSV HEADER
\copy join_menu_drinks_and_inv_edible from '../csv_data/join_menu_drinks_and_inv_edible.csv' CSV HEADER
\copy join_menu_toppings_and_inv_edible from '../csv_data/join_menu_toppings_and_inv_edible.csv' CSV HEADER
\copy menu_drinks from '../csv_data/menu_drinks.csv' CSV HEADER
\copy menu_toppings from '../csv_data/menu_toppings.csv' CSV HEADER
\copy orders from '../csv_data/orders.csv' CSV HEADER