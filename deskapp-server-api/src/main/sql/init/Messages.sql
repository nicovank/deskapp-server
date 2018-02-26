CREATE TABLE Messages (
	ID          INT AUTO_INCREMENT NOT NULL,
	Building_ID VARCHAR(15)        NOT NULL,
	Employee_ID VARCHAR(10)        NOT NULL,
	Time        DATETIME           NOT NULL,
	Message     TEXT               NOT NULL,

	PRIMARY KEY (ID),
	FOREIGN KEY (Building_ID) REFERENCES Buildings (ID),
	FOREIGN KEY (Employee_ID) REFERENCES Employees (ID)
);