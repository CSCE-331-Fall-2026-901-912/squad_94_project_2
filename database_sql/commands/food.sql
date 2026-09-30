-- shows ingredients that have an amount <=500 servings, ordered by the amount currently in stock from highest to lowest
SELECT * FROM inv_edible 
WHERE amount_servings <= 500
ORDER BY amount_servings DESC;


-- shows ingredients with specific keyword (boba) in their name
SELECT * FROM inv_edible
WHERE name LIKE '%boba%';


-- shows the top five ingredients that have the lowest amount in stock
SELECT * FROM inv_edible
ORDER BY amount_servings LIMIT 5;


