SELECT Messages.ID, First_Name, Last_Name, Time, Message
FROM Messages JOIN Employees ON Employee_ID = Employees.ID
WHERE Building_ID = ?
ORDER BY ID DESC
LIMIT ?, ?;