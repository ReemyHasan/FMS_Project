CREATE DATABASE IF NOT EXISTS USER_APP;

USE USER_APP;

CREATE TABLE users(
      id int(7) NOT NULL,
      username VARCHAR(255) NOT NULL,
      fname VARCHAR(255) NOT NULL,
      lname VARCHAR(255) NOT NULL,
      gender VARCHAR(255) NOT NULL,
      password VARCHAR(255) NOT NULL,
      country VARCHAR(255) NOT NULL,
      email VARCHAR(255) NOT NULL,
      workingDate DATE DEFAULT NULL,
      role  VARCHAR(255) NOT NULL,
      PRIMARY KEY(userID)
);
ALTER TABLE users ADD CONSTRAINT unique_username UNIQUE (username);
ALTER TABLE users ADD CONSTRAINT unique_username UNIQUE (email);


-- password: reem
INSERT INTO users (id,username,fname,lname, gender, country, email, workingDate, password, role)
VALUES (1,'reem.hasan','John', 'Doe', 'female', 'USA', 'johndoe@example.com', '2023-07-24',
	'$2a$10$rC.uZ0ivWwiFJ7PHp8A8XOEFz7Al82SjHsvX27W7PcADX2GHdyu6q', 'admin');

-- password: reem 
INSERT INTO users (id,username,fname,lname, gender, country, email, workingDate, password, role)
VALUES (2,'bashar.hussain','John', 'Doe', 'male', 'USA', 'bashar@example.com', '2023-07-24',
	'$2a$10$4xbRR/mP2xrbSjN1tK8t/OBZLeTzLplQtG3VvB8mm4T8yA.Fti/AS', 'user');
