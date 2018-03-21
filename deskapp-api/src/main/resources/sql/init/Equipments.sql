CREATE TABLE IF NOT EXISTS Equipments (
	ID          VARCHAR(13)  NOT NULL, -- EAN-13 Barcode
	Building_ID VARCHAR(15)  NOT NULL,
	Name        VARCHAR(255) NOT NULL,
	Category    VARCHAR(30),

	PRIMARY KEY (ID),
	FOREIGN KEY (Building_ID) REFERENCES Buildings (ID)
);