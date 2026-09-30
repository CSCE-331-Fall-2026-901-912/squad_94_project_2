-- Gives all orders that tipped at least [half] the cost of their order--
SELECT * FROM orders
WHERE tip > (0.5); --total_spend/2--


-- Given an order ID, gives the total amount spent for the order, including tip --	
SELECT *
FROM (
	SELECT
		EXTRACT(HOUR FROM time_completed_at) AS sale_hour, 
		COUNT(DISTINCT id_order) AS order_count,
		COUNT(*) FILTER (WHERE hot_chosen IS TRUE) AS num_hot,
		COUNT(*) FILTER (WHERE hot_chosen IS NOT TRUE) AS num_cold
	FROM orders
	GROUP BY 1
) t
WHERE sale_hour = 20;