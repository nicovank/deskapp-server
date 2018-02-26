CREATE TABLE IF NOT EXISTS Access (
	ID          VARCHAR(10)         NOT NULL,
	Building_ID VARCHAR(10),
	Type        ENUM ('key', 'fob') NOT NULL,

	PRIMARY KEY (ID),
	FOREIGN KEY (Building_ID) REFERENCES Buildings (ID)
);