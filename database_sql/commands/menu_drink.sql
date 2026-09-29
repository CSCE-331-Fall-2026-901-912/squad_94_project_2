SELECT * FROM menu_drink;

SELECT * FROM menu_drink
ORDER BY price DESC; -- Gives drinks ordered by price, highest at the top

SELECT * FROM menu_drink
WHERE hot_available is true; -- Gives drinks able to be hot

SELECT * FROM menu_drink
WHERE name LIKE '%' || 'Jelly' || '%'; -- Contains Jelly

