-- Creation SQL command for drink table --

CREATE TABLE menu_drink(
    id_drink INT PRIMARY KEY,
    name VARCHAR(255),
    price DECIMAL,
    hot_available BOOLEAN,
    is_non_caffeinated BOOLEAN
);

-- #################################### --