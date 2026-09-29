-- shows ingredients by the amount currently in stock from highest to lowest
SELECT * FROM inv_edible 
ORDER BY amount DESC;


-- shows ingredients with specific keyword (boba) in their name
SELECT * FROM inv_edible
WHERE name LIKE '%boba%';


-- shows the top five ingredients that have the lowest amount in stock
SELECT * FROM inv_edible
ORDER BY amount LIMIT 5;