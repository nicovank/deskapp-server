SELECT *
FROM Employees
WHERE (Email = ? OR ID = ?)
AND Active = 'yes';