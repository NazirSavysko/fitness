INSERT INTO fitness_app.users (email, role, password_hash, full_name, created_at,enable)
VALUES (
           'john@test.com',
           'ROLE_USER',
           '$2a$10$swcqpuCXGPqumemam2t34uPXn15aQKVzFEqSEahUmNvLvLmwvc4xO',
           'John Doe',
           NOW(),
            true
       );
