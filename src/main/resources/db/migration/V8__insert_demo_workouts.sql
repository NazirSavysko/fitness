-- Demo workout templates for John Doe (3x per week)
INSERT INTO fitness_app.workout_template (user_id, name, created_at, scheduled_day)
SELECT u.id, v.name, v.created_at, v.scheduled_day
FROM fitness_app.users u
JOIN (VALUES
    ('Push (Mon)', TIMESTAMP '2026-04-25 09:00:00', 'MONDAY'),
    ('Pull (Wed)', TIMESTAMP '2026-04-25 09:10:00', 'WEDNESDAY'),
    ('Legs + Core (Fri)', TIMESTAMP '2026-04-25 09:20:00', 'FRIDAY')
) AS v(name, created_at, scheduled_day) ON true
WHERE u.email = 'john@test.com';

-- Demo template exercises with normal/failure sets
INSERT INTO fitness_app.template_exercise (template_id, exercise_id, order_index, normal_sets, failure_sets)
SELECT t.id, ed.id, v.order_index, v.normal_sets, v.failure_sets
FROM (VALUES
    ('Push (Mon)', 'Barbell Bench Press', 1, 3, 0),
    ('Push (Mon)', 'Incline Barbell Bench Press', 2, 3, 0),
    ('Push (Mon)', 'Overhead Press', 3, 3, 0),
    ('Push (Mon)', 'Lateral Raise', 4, 3, 1),
    ('Push (Mon)', 'Tricep Pushdown', 5, 3, 1),
    ('Push (Mon)', 'Dips', 6, 2, 1),

    ('Pull (Wed)', 'Deadlift', 1, 3, 0),
    ('Pull (Wed)', 'Pull Up', 2, 3, 1),
    ('Pull (Wed)', 'Lat Pulldown', 3, 3, 0),
    ('Pull (Wed)', 'Seated Cable Row', 4, 3, 0),
    ('Pull (Wed)', 'Barbell Curl', 5, 3, 1),
    ('Pull (Wed)', 'Hammer Curl', 6, 3, 1),

    ('Legs + Core (Fri)', 'Barbell Squat', 1, 3, 0),
    ('Legs + Core (Fri)', 'Leg Press', 2, 3, 0),
    ('Legs + Core (Fri)', 'Romanian Deadlift', 3, 3, 0),
    ('Legs + Core (Fri)', 'Leg Extension', 4, 3, 1),
    ('Legs + Core (Fri)', 'Leg Curl', 5, 3, 1),
    ('Legs + Core (Fri)', 'Plank', 6, 3, 0)
) AS v(template_name, exercise_name, order_index, normal_sets, failure_sets)
JOIN fitness_app.users u ON u.email = 'john@test.com'
JOIN fitness_app.workout_template t ON t.user_id = u.id AND t.name = v.template_name
JOIN fitness_app.exercise_definition ed ON ed.name = v.exercise_name;

-- Demo sessions for May 2026 (3x per week)
INSERT INTO fitness_app.workout_session (user_id, source_template_id, started_at, ended_at)
SELECT u.id, t.id, v.started_at, v.ended_at
FROM fitness_app.users u
JOIN (VALUES
    ('Legs + Core (Fri)', TIMESTAMP '2026-05-01 17:30:00', TIMESTAMP '2026-05-01 18:50:00'),
    ('Push (Mon)', TIMESTAMP '2026-05-04 18:00:00', TIMESTAMP '2026-05-04 19:10:00'),
    ('Pull (Wed)', TIMESTAMP '2026-05-06 18:15:00', TIMESTAMP '2026-05-06 19:25:00'),
    ('Legs + Core (Fri)', TIMESTAMP '2026-05-08 17:30:00', TIMESTAMP '2026-05-08 18:50:00'),
    ('Push (Mon)', TIMESTAMP '2026-05-11 18:00:00', TIMESTAMP '2026-05-11 19:10:00'),
    ('Pull (Wed)', TIMESTAMP '2026-05-13 18:15:00', TIMESTAMP '2026-05-13 19:25:00'),
    ('Legs + Core (Fri)', TIMESTAMP '2026-05-15 17:30:00', TIMESTAMP '2026-05-15 18:50:00'),
    ('Push (Mon)', TIMESTAMP '2026-05-18 18:00:00', TIMESTAMP '2026-05-18 19:10:00'),
    ('Pull (Wed)', TIMESTAMP '2026-05-20 18:15:00', TIMESTAMP '2026-05-20 19:25:00'),
    ('Legs + Core (Fri)', TIMESTAMP '2026-05-22 17:30:00', TIMESTAMP '2026-05-22 18:50:00'),
    ('Push (Mon)', TIMESTAMP '2026-05-25 18:00:00', TIMESTAMP '2026-05-25 19:10:00'),
    ('Pull (Wed)', TIMESTAMP '2026-05-27 18:15:00', TIMESTAMP '2026-05-27 19:25:00'),
    ('Legs + Core (Fri)', TIMESTAMP '2026-05-29 17:30:00', TIMESTAMP '2026-05-29 18:50:00')
) AS v(template_name, started_at, ended_at) ON true
JOIN fitness_app.workout_template t ON t.user_id = u.id AND t.name = v.template_name
WHERE u.email = 'john@test.com';

-- Derive session exercises from templates
INSERT INTO fitness_app.session_exercise (session_id, exercise_id, order_index)
SELECT ws.id, te.exercise_id, te.order_index
FROM fitness_app.workout_session ws
JOIN fitness_app.template_exercise te ON te.template_id = ws.source_template_id
WHERE ws.user_id = (SELECT id FROM fitness_app.users WHERE email = 'john@test.com')
  AND ws.started_at >= TIMESTAMP '2026-05-01 00:00:00'
  AND ws.started_at < TIMESTAMP '2026-06-01 00:00:00';

-- Derive preset sets using template config
INSERT INTO fitness_app.exercise_set (session_exercise_id, set_number, weight, reps, rest_seconds, set_type)
SELECT se.id,
       set_rows.set_number,
       CASE
           WHEN ed.name IN ('Pull Up', 'Dips', 'Plank') THEN NULL
           WHEN ed.name = 'Barbell Squat' THEN 100
           WHEN ed.name = 'Leg Press' THEN 160
           WHEN ed.name = 'Romanian Deadlift' THEN 90
           WHEN ed.name = 'Deadlift' THEN 110
           WHEN ed.name = 'Barbell Bench Press' THEN 80
           WHEN ed.name = 'Incline Barbell Bench Press' THEN 70
           WHEN ed.name = 'Overhead Press' THEN 45
           WHEN ed.name = 'Seated Cable Row' THEN 55
           WHEN ed.name = 'Lat Pulldown' THEN 50
           WHEN ed.name = 'Lateral Raise' THEN 12.5
           WHEN ed.name = 'Tricep Pushdown' THEN 25
           WHEN ed.name = 'Barbell Curl' THEN 25
           WHEN ed.name = 'Hammer Curl' THEN 20
           WHEN ed.name = 'Leg Extension' THEN 45
           WHEN ed.name = 'Leg Curl' THEN 40
           ELSE 40
       END AS weight,
       CASE
           WHEN ed.name = 'Plank' THEN 45
           WHEN ed.name IN ('Pull Up', 'Dips') THEN 8
           WHEN ed.name IN ('Barbell Squat', 'Deadlift', 'Romanian Deadlift', 'Barbell Bench Press',
                            'Incline Barbell Bench Press', 'Overhead Press', 'Leg Press') THEN 6
           WHEN ed.name IN ('Seated Cable Row', 'Lat Pulldown') THEN 8
           ELSE 10
       END AS reps,
       CASE
           WHEN ed.name IN ('Barbell Squat', 'Deadlift', 'Romanian Deadlift',
                            'Barbell Bench Press', 'Incline Barbell Bench Press', 'Leg Press') THEN 120
           WHEN ed.name IN ('Overhead Press', 'Pull Up', 'Lat Pulldown', 'Seated Cable Row') THEN 90
           ELSE 60
       END AS rest_seconds,
       set_rows.set_type
FROM fitness_app.session_exercise se
JOIN fitness_app.workout_session ws ON ws.id = se.session_id
JOIN fitness_app.template_exercise te ON te.template_id = ws.source_template_id AND te.exercise_id = se.exercise_id
JOIN fitness_app.exercise_definition ed ON ed.id = se.exercise_id
JOIN LATERAL (
    SELECT gs AS set_number, CAST('NORMAL' AS VARCHAR(20)) AS set_type
    FROM generate_series(1, te.normal_sets) gs
    UNION ALL
    SELECT te.normal_sets + gs, CAST('FAILURE' AS VARCHAR(20))
    FROM generate_series(1, te.failure_sets) gs
) AS set_rows ON true
WHERE ws.user_id = (SELECT id FROM fitness_app.users WHERE email = 'john@test.com')
  AND ws.started_at >= TIMESTAMP '2026-05-01 00:00:00'
  AND ws.started_at < TIMESTAMP '2026-06-01 00:00:00';

