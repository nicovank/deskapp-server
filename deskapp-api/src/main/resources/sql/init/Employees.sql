CREATE TABLE IF NOT EXISTS Employees (
	ID          VARCHAR(10)  NOT NULL,
	Building_ID VARCHAR(15)  NOT NULL,
	First_Name  VARCHAR(35)  NOT NULL, -- Arbitrary 35
	Last_Name   VARCHAR(35)  NOT NULL,
	Position    VARCHAR(50),
	Email       VARCHAR(255) NOT NULL,
	Password    VARCHAR(255) NOT NULL,
	Phone_Num   VARCHAR(20),

	PRIMARY KEY (ID),
	UNIQUE (Email),
	FOREIGN KEY (Building_ID) REFERENCES Buildings (ID)
);