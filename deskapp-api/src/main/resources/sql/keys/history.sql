SELECT
	Rents.ID             AS Rent_ID,
	Time_Out, Time_In,
	Access.ID            AS Access_ID,
	Access.Type          AS Access_Type,
	Residents.ID         AS Resident_ID,
	Residents.First_Name AS Resident_First_Name,
	Residents.Last_Name  as Resident_Last_Name,
	EO.ID                AS Employee_Out_ID,
	EO.First_Name        AS Employee_Out_First_Name,
	EO.Last_Name         AS Employee_Out_Last_Name,
	EI.ID                AS Employee_In_ID,
	EI.First_Name        AS Employee_In_First_Name,
	EI.Last_Name         AS Employee_In_Last_Name

FROM Rents
	JOIN Employees AS EO ON Rents.Employee_Out = EO.ID
	JOIN Employees AS EI ON Rents.Employee_In = EI.ID
	JOIN Residents ON Rents.Resident_ID = Residents.ID
	JOIN Access ON Rents.Equipment_ID = Access.ID

WHERE Access.ID = ?

ORDER BY Time_Out DESC;