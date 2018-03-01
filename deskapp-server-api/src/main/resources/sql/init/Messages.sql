CREATE TABLE IF NOT EXISTS Messages (
	ID          INT AUTO_INCREMENT NOT NULL,
	Employee_ID VARCHAR(10)        NOT NULL,
	Time        DATETIME           NOT NULL,
	Message     TEXT               NOT NULL,

	PRIMARY KEY (ID),
	FOREIGN KEY (Employee_ID) REFERENCES Employees (ID)
);