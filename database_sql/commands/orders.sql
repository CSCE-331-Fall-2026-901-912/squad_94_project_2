-- Gives all orders that tipped at least [half] the cost of their order--
SELECT * FROM orders
WHERE tip > (total_cost/2);


-- Given an order ID, gives the total amount spent for the order, including tip --
SELECT 
    strftime('%H', created_at) AS sale_hour, 
    COUNT(DISTINCT order_id) AS order_count,
    SUM(hot_chosen is 1) AS num_hot,
    SUM(hot_chosen is 0) AS num_cold
FROM orders
WHERE sale_hour = '10'
GROUP BY sale_hour;