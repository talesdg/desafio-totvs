CREATE TABLE account(
   id SERIAL PRIMARY KEY,
   dt_expiration DATE NOT NULL,
   dt_payment DATE,
   value DECIMAL(10, 2) NOT NULL,
   description VARCHAR(255) NOT NULL,
   status VARCHAR(50) NOT NULL
);
