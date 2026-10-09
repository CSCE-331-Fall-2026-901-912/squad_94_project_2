-- Gives all orders that tipped at least [half] the cost of their order--
SELECT * FROM orders
WHERE tip > (0.5); --total_spend/2--


-- Given an order ID, gives the total amount spent for the order, including tip --	
SELECT *
FROM (
	SELECT
		EXTRACT(HOUR FROM time_created_at) AS sale_hour, 
		COUNT(DISTINCT id_order) AS order_count,
		COUNT(*) FILTER (WHERE hot_chosen IS TRUE) AS num_hot,
		COUNT(*) FILTER (WHERE hot_chosen IS NOT TRUE) AS num_cold
	FROM orders
	GROUP BY 1
) t
WHERE sale_hour = 20;

--Display the top 10 most ordered drinks from most to least
SELECT
    md.id_drink,
    md.name AS drink_name,
    COUNT(o.id_order) AS times_ordered
FROM orders AS o
JOIN menu_drinks AS md
    ON o.id_drink = md.id_drink
GROUP BY
    md.id_drink,
    md.name
ORDER BY
    times_ordered DESC
LIMIT 10;