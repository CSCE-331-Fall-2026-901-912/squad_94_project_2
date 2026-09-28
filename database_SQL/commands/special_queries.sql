-- Weekly Sales History
SELECT
    strftime('%Y-%W', created_at) AS sales_week,
    COUNT(DISTINCT order_id) AS order_count
FROM orders
WHERE strftime sales_week = '2026-01'
GROUP BY sales_week;

-- Peak Sales Days v1 (calculate the top 10 sales days)
SELECT
    date(created_at) AS sales_day,
    SUM(total_spent) AS total_sales
FROM orders
GROUP BY sales_day
ORDER BY daily_sales DESC
LIMIT 10;

-- Peak Sales Days v2 (add the top ten sales given a specific day)
SELECT SUM(total_spent) AS top_10_sales
FROM (
    SELECT total_spent
    FROM orders
    WHERE date(created_at) = '2026-08-30' --subject to change
    ORDER BY total_spent DESC
    LIMIT 10
) AS top_10_orders;

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
    dm.id_drink,
    dm.name AS menu_item,
    COUNT(DISTINCT mij.ingredient_id) AS inventory_item_count
FROM drink_menu AS dm
JOIN join_menu_drink_and_inv_edible AS mij
    ON dm.id_drink = mij.id_drink
JOIN inv_edible AS ei
    ON mij.id_edible = ei.id_edible
GROUP BY
    dm.itemID,
    dm.name
ORDER BY
    dm.name;