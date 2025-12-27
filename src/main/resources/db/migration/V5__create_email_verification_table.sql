CREATE TABLE fitness_app.email_verification (
                                                auth_id INTEGER PRIMARY KEY,
                                                verification_code VARCHAR(10),
                                                expiry_date TIMESTAMP,
                                                FOREIGN KEY (auth_id) REFERENCES fitness_app.auth(auth_id) ON DELETE CASCADE
);