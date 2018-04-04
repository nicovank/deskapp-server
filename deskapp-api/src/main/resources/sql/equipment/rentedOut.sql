SELECT
	Rents.ID             AS Rent_ID,
	Time_Out,
	Equipments.ID        AS Equipment_ID,
	Equipments.Name      AS Equipment_Name,
	Residents.ID         AS Resident_ID,
	Residents.First_Name AS Resident_First_Name,
	Residents.Last_Name  as Resident_Last_Name,
	Employees.ID         AS Employee_ID,
	Employees.First_Name AS Employee_First_Name,
	Employees.Last_Name  AS Employee_Last_Name

FROM Rents
	JOIN Employees ON Rents.Employee_Out = Employees.ID
	JOIN Residents ON Rents.Resident_ID = Residents.ID
	JOIN Equipments ON Rents.Equipment_ID = Equipments.ID

WHERE Time_In IS NULL
			AND Equipments.Building_ID = ?