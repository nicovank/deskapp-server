CREATE TABLE IF NOT EXISTS Rents_Access (
	ID            INT AUTO_INCREMENT NOT NULL,
	Access_ID     VARCHAR(10)        NOT NULL,
	Resident_ID   VARCHAR(10)        NOT NULL,
	Time_Out      DATETIME           NOT NULL,
	Employee_Out  VARCHAR(10)        NOT NULL,
	Time_In       DATETIME,
	Employee_In   VARCHAR(10),

	PRIMARY KEY (ID),
	FOREIGN KEY (Access_ID)     REFERENCES Access    (ID),
	FOREIGN KEY (Resident_ID)   REFERENCES Residents (ID),
	FOREIGN KEY (Employee_Out)  REFERENCES Employees (ID),
	FOREIGN KEY (Employee_In)   REFERENCES Employees (ID)
);