SELECT SUM(total_spent) AS sum_total
FROM (
	SELECT total_spent
	FROM orders
	WHERE completed = TRUE
) AS total_sales;

SELECT SUM(total_spent) AS sum_total
FROM (
	SELECT total_spent
	FROM orders
	WHERE completed = TRUE
		AND date(time_completed_at) = '2026-08-30'
) AS total_sales;