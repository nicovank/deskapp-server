SELECT
	Rents_Access.ID      AS Rent_ID,
	Time_Out,
	Access.ID            AS Access_ID,
	Access.Type          AS Access_Type,
	Residents.ID         AS Resident_ID,
	Residents.First_Name AS Resident_First_Name,
	Residents.Last_Name  as Resident_Last_Name,
	Employees.ID         AS Employee_ID,
	Employees.First_Name AS Employee_First_Name,
	Employees.Last_Name  AS Employee_Last_Name

FROM Rents_Access
	JOIN Employees ON Rents_Access.Employee_Out = Employees.ID
	JOIN Residents ON Rents_Access.Resident_ID = Residents.ID
	JOIN Access ON Rents_Access.Access_ID = Access.ID

WHERE Time_In IS NULL
			AND Access.Building_ID = ?

ORDER BY Time_Out DESC;