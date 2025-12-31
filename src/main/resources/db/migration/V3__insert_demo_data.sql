INSERT INTO fitness_app.auth (auth_id, email, role, password_hash, created_at)
VALUES (100,
        'john@test.com',
        'ROLE_USER',
        '$2a$10$swcqpuCXGPqumemam2t34uPXn15aQKVzFEqSEahUmNvLvLmwvc4xO',
        NOW());

INSERT INTO fitness_app.fitness_user (user_id, name, surname, auth_id)
VALUES (100, 'John', 'Doe', 100);