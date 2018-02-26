CREATE TABLE Buildings (
	ID        VARCHAR(15) NOT NULL, -- Ex: "Hart", "Waterbury", "MASH", ...
	Address   VARCHAR(100),         -- We only need to store the number and street name
	Phone_Num VARCHAR(20),

	PRIMARY KEY (ID)
);