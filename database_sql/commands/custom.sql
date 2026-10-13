SELECT
    date_trunc('hour', time_created_at) AS sales_hour,
    COUNT(*) FILTER (WHERE status = 'completed') AS completed,
	COUNT(*) FILTER (WHERE status = 'void') AS void,
	COUNT(*) FILTER (WHERE status = 'discard') AS discard
FROM orders
WHERE time_created_at >= '2026-08-10' AND time_created_at < '2026-08-11'
GROUP BY sales_hour
ORDER BY sales_hour;