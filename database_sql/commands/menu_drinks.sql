SELECT * FROM menu_drinks;

SELECT * FROM menu_drinks
ORDER BY price DESC; -- Gives drinks ordered by price, highest at the top

SELECT * FROM menu_drinks
WHERE hot_available is true; -- Gives drinks able to be hot

SELECT * FROM menu_drinks
WHERE name LIKE '%' || 'Jelly' || '%'; -- Contains Jelly

