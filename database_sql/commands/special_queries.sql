-- Weekly Sales History
SELECT
    to_char(time_created_at, 'YYYY-WW') AS sales_week,
    COUNT(DISTINCT id_order) AS order_count
FROM orders
GROUP BY to_char(time_created_at, 'YYYY-WW')
ORDER BY sales_week;

-- Peak Sales Days v1 (calculate the top 10 sales days)
SELECT
    date(time_created_at) AS sales_day,
    SUM(total_spent) AS total_sales
FROM orders
GROUP BY sales_day
ORDER BY total_sales DESC
LIMIT 10;

-- Peak Sales Days v2 (add the top ten sales given a specific day)
SELECT SUM(total_spent) AS top_10_sales
FROM (
    SELECT total_spent
    FROM orders
    WHERE date(time_created_at) = '2026-08-30' --subject to change
    ORDER BY total_spent DESC
    LIMIT 10
) AS top_10_orders;

-- Realistic Sales History
SELECT
    EXTRACT(HOUR FROM time_completed_at) AS sale_hour, 
    COUNT(DISTINCT id_order) AS order_count,
    SUM(total_spent) AS total_sales
FROM orders
WHERE EXTRACT(HOUR FROM time_completed_at) = 12
GROUP BY sale_hour;

-- Menu Item Inventory
SELECT
    dm.id_drink,
    dm.name AS menu_item,
    COUNT(DISTINCT mij.id_edible) AS inventory_item_count
FROM menu_drinks AS dm
JOIN join_menu_drinks_and_inv_edible AS mij
    ON dm.id_drink = mij.id_drink
JOIN inv_edible AS ei
    ON mij.id_edible = ei.id_edible
GROUP BY
    dm.id_drink,
    dm.name
ORDER BY
    dm.name;