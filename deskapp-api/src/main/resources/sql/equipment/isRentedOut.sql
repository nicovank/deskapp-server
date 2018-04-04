SELECT ID
FROM Rents
WHERE Time_In IS NULL AND Equipment_ID = ?;