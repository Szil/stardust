CREATE TABLE contact (
  id SERIAL NOT NULL,
  firstname VARCHAR(100),
  lastname VARCHAR(100),
  phone VARCHAR(100),
  email VARCHAR(100),
  birthdate DATE,
  uuid UUID UNIQUE
);