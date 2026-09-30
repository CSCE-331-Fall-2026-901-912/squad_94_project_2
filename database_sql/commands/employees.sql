-- Shows number of hours worked by employees from least to greatest
SELECT * FROM employees
ORDER BY hours_worked_for_week;


--Shows weekly pay for each employee ordered greatest to least
SELECT id_employee, name, current_pay_rate*hours_worked_for_week AS pay FROM employees
ORDER BY pay DESC;

--Displays all the managers
SELECT * FROM employees
WHERE position = 'Manager'
