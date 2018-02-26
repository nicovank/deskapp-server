CREATE TABLE IF NOT EXISTS Warns (
	ID          INT AUTO_INCREMENT NOT NULL,
	Employee_ID VARCHAR(10)        NOT NULL,
	Resident_ID VARCHAR(10)        NOT NULL,
	Time        DATETIME           NOT NULL,
	Comment     TEXT               NOT NULL,

	PRIMARY KEY (ID),
	FOREIGN KEY (Employee_ID) REFERENCES Employees (ID),
	FOREIGN KEY (Resident_ID) REFERENCES Residents (ID)
);