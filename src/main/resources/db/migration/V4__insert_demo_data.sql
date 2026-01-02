INSERT INTO fitness_app.auth (auth_id, email, role, password_hash, created_at)
VALUES (100,
        'john@test.com',
        'ROLE_USER',
        '$2a$10$swcqpuCXGPqumemam2t34uPXn15aQKVzFEqSEahUmNvLvLmwvc4xO',
        NOW());

INSERT INTO fitness_app.fitness_user (user_id, name, surname, auth_id)
VALUES (100, 'John', 'Doe', 100);

INSERT INTO fitness_app.workout_template (template_id, user_id, name)
VALUES (1, 100, 'Upper Body Power'),
       (2, 100, 'Leg Day Hypertrophy');


INSERT INTO fitness_app.template_exercise (template_id, exercise_def_id)
VALUES (1, 1),
       (1, 7),
       (1, 4),
       (1, 31),
       (1, 14);


INSERT INTO fitness_app.template_exercise (template_id, exercise_def_id)
VALUES (2, 2),
       (2, 25),
       (2, 23),
       (2, 20),
       (2, 22);

INSERT INTO fitness_app.workout_session (session_id, user_id, template_id, session_date)
VALUES (1, 100, 1, NOW() - INTERVAL '3' DAY);

INSERT INTO fitness_app.workout_session (session_id, user_id, template_id, session_date)
VALUES (2, 100, 2, NOW() - INTERVAL '1' DAY);


INSERT INTO fitness_app.exercise_log (session_id, exercise_def_id, set_number, reps, weight, rest_seconds)
VALUES (1, 1, 1, 10, 60.0, 90),
       (1, 1, 2, 8, 65.0, 120),
       (1, 1, 3, 6, 70.0, 120),
       (1, 7, 1, 12, 50.0, 90),
       (1, 7, 2, 10, 55.0, 90),
       (1, 7, 3, 10, 55.0, 90);

INSERT INTO fitness_app.exercise_log (session_id, exercise_def_id, set_number, reps, weight, rest_seconds)
VALUES (2, 2, 1, 10, 80.0, 120),
       (2, 2, 2, 8, 90.0, 180),
       (2, 2, 3, 5, 100.0, 180),
       (2, 25, 1, 12, 70.0, 90),
       (2, 25, 2, 12, 70.0, 90);