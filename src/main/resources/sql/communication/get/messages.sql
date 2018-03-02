SELECT messages.ID, First_Name, Last_Name, Time, Message
FROM messages, employees
WHERE Building_ID = ?
ORDER BY ID DESC
LIMIT ?, ?