SELECT SUM(total_spent) AS sum_total
FROM (
	SELECT total_spent
	FROM orders
	WHERE completed = TRUE
);

SELECT SUM(total_spent) AS sum_total
FROM (
	SELECT total_spent
	FROM orders
	WHERE completed = TRUE
		AND date(time_completed_at) = '2026-09-30'
)