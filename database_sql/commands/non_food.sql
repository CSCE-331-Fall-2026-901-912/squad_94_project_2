SELECT * FROM inv_nonedible
ORDER BY id_nonedible;

-- QUERY 1: Shows items with the mininum amount value
SELECT
    id_nonedible,
    name,
    amount
    AS [lowest inventory item(s)]
FROM inv_nonedible
WHERE amount = (SELECT MIN(amount) FROM non_food);

-- QUERY 2: Shows table by amount in ascending order 
SELECT * FROM inv_nonedible
ORDER BY amount;

-- QUERY 3: Shows maximum number of drinks that can be made before having to buy new inventory
SELECT amount AS complete_drinks
FROM inv_nonedible
WHERE name IN ('Lids', 'Straws', 'Lid Covers', 'Cups')
  AND amount = (
      SELECT MIN(amount)
      FROM inv_nonedible
      WHERE name IN ('Lids', 'Straws', 'Lid Covers', 'Cups')
  );