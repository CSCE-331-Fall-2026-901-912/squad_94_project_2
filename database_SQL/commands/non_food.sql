SELECT * FROM non_food
ORDER BY nonFoodID;

-- QUERY 1: Shows items with the mininum amount value
SELECT
    nonFoodID,
    name,
    amount
    AS [lowest inventory item(s)]
FROM non_food
WHERE amount = (SELECT MIN(amount) FROM non_food);

-- QUERY 2: Shows table by amount in ascending order 
SELECT * FROM non_food
ORDER BY amount;

-- QUERY 3: Shows maximum number of drinks that can be made before having to buy new inventory
SELECT amount AS complete_drinks
FROM non_food
WHERE name IN ('Lids', 'Straws', 'Lid Covers', 'Cups')
  AND amount = (
      SELECT MIN(amount)
      FROM non_food
      WHERE name IN ('Lids', 'Straws', 'Lid Covers', 'Cups')
  );