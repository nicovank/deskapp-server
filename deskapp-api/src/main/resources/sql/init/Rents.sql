CREATE TABLE IF NOT EXISTS Rents (
	ID           INT AUTO_INCREMENT NOT NULL,
	Equipment_ID VARCHAR(13)        NOT NULL,
	Resident_ID  VARCHAR(10)        NOT NULL,
	Time_Out     DATETIME           NOT NULL,
	Employee_Out VARCHAR(10)        NOT NULL, -- Employee that logged the item out.
	Time_In      DATETIME,
	Employee_In  VARCHAR(10),

	PRIMARY KEY (ID),
	FOREIGN KEY (Equipment_ID) REFERENCES Equipments (ID),
	FOREIGN KEY (Resident_ID)  REFERENCES Residents  (ID),
	FOREIGN KEY (Employee_Out) REFERENCES Employees  (ID),
	FOREIGN KEY (Employee_In)  REFERENCES Employees  (ID)
);
