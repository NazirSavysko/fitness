CREATE TABLE fitness_app.email_verification (
                                                auth_id SERIAL PRIMARY KEY,
                                                verification_code VARCHAR(10),
                                                email VARCHAR(255) UNIQUE ,
                                                expiry_date TIMESTAMP

);