CREATE TABLE IF NOT EXISTS Residents (
	ID          VARCHAR(10)  NOT NULL,
	First_Name  VARCHAR(35)  NOT NULL, -- Arbitrary 35
	Last_Name   VARCHAR(35)  NOT NULL,
	Building_ID VARCHAR(15)  NOT NULL,
	Room_Num    VARCHAR(7)   NOT NULL, -- Arbitrary 7
	Email       VARCHAR(255) NOT NULL, -- Max VARCHAR length

	PRIMARY KEY (ID),
	FOREIGN KEY (Building_ID) REFERENCES Buildings(ID)
);