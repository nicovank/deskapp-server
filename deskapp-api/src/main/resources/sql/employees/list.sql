SELECT
	ID,
	First_Name,
	Last_Name,
	Position,
	Email,
	Phone_Num

FROM Employees
WHERE Building_ID = ?
AND Active = 'yes';