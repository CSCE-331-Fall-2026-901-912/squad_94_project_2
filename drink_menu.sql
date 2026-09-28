SELECT * FROM drink_menu;

SELECT * FROM drink_menu
ORDER BY price DESC; -- Gives drinks ordered by price, highest at the top

SELECT * FROM drink_menu
WHERE temperature is true; -- Gives drinks able to be hot

SELECT * FROM drink_menu
WHERE name LIKE '%' || 'Jelly' || '%';
-- Contains Jelly