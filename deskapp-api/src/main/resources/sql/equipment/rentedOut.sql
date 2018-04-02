SELECT
	Rents.ID             AS Rent_ID,
	Time_Out,
	equipments.ID        AS Equipment_ID,
	equipments.Name      AS Equipment_Name,
	residents.ID         AS Resident_ID,
	residents.First_Name AS Resident_First_Name,
	residents.Last_Name  as Resident_Last_Name,
	employees.ID         AS Employee_ID,
	employees.First_Name AS Employee_First_Name,
	employees.Last_Name  AS Employee_Last_Name

FROM `rents`
	JOIN employees ON Employee_Out = employees.ID
	JOIN residents ON Resident_ID = residents.ID
	JOIN equipments ON equipments.ID = Equipment_ID

WHERE Time_In IS NULL
			AND equipments.Building_ID = ?