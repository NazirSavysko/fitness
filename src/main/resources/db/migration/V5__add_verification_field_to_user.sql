ALTER TABLE fitness_app.auth ADD COLUMN enabled BOOLEAN DEFAULT FALSE;

UPDATE fitness_app.auth SET enabled = TRUE WHERE email = 'john@test.com';