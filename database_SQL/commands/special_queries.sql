-- Weekly Sales History
SELECT
    strftime('%Y-%W', created_at) AS sales_week,
    COUNT(DISTINCT order_id) AS order_count
FROM orders
WHERE strftime sales_week = '2026-01'
GROUP BY sales_week;

-- Peak Sales Days
SELECT
    date(created_at) AS sales_day,
    SUM(total_spent) AS total_sales
FROM orders
GROUP BY sales_day
ORDER BY daily_sales DESC
LIMIT 10;

-- Realistic Sales History
SELECT
    strftime('%H', created_at) AS sale_hour, 
    COUNT(DISTINCT order_id) AS order_count,
    SUM(total_spent) AS total_sales
FROM orders
WHERE sale_hour = '12'
GROUP BY sale_hour

-- Menu Item Inventory
SELECT
    dm.itemID,
    dm.name AS menu_item,
    COUNT(DISTINCT mij.ingredient_id) AS inventory_item_count
FROM drink_menu AS dm
JOIN menu_item_ingredients_joined AS mij
    ON dm.itemID = mij.menu_item_id
JOIN edible_ingredients AS ei
    ON mij.ingredient_id = ei.IngredientID
GROUP BY
    dm.itemID,
    dm.name
ORDER BY
    dm.name;