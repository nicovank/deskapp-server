UPDATE Rents_Access
SET Time_In = CURRENT_TIME, Employee_In = ?
WHERE ID = ?;