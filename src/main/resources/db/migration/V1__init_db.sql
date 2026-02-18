CREATE SCHEMA IF NOT EXISTS fitness_app;

CREATE TABLE fitness_app.users
(
    id            SERIAL PRIMARY KEY,
    email         VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name     VARCHAR(100),
    role          VARCHAR(20) DEFAULT 'ROLE_USER',
    enabled       BOOLEAN     DEFAULT FALSE,
    created_at    TIMESTAMP   DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE fitness_app.email_verification
(
    id                SERIAL PRIMARY KEY,
    user_id           INTEGER NOT NULL REFERENCES fitness_app.users (id) ON DELETE CASCADE,
    verification_code VARCHAR(10),
    email             VARCHAR(255),
    expiry_date       TIMESTAMP
);

CREATE TABLE fitness_app.exercise_definition
(
    id           SERIAL PRIMARY KEY,
    name         VARCHAR(100) NOT NULL UNIQUE,
    muscle_group VARCHAR(50)
);

CREATE TABLE fitness_app.workout_template
(
    id         SERIAL PRIMARY KEY,
    user_id    INTEGER       NOT NULL REFERENCES fitness_app.users (id) ON DELETE CASCADE,
    name       VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE fitness_app.template_exercise
(
    id          SERIAL PRIMARY KEY,
    template_id INTEGER  NOT NULL REFERENCES fitness_app.workout_template (id) ON DELETE CASCADE,
    exercise_id INTEGER  NOT NULL REFERENCES fitness_app.exercise_definition (id),
    order_index INTEGER NOT NULL -- Порядок упражнений в списке
);

CREATE TABLE fitness_app.workout_session
(
    id                 SERIAL PRIMARY KEY,
    user_id            INTEGER    NOT NULL REFERENCES fitness_app.users (id) ON DELETE CASCADE,
    source_template_id INTEGER    REFERENCES fitness_app.workout_template (id) ON DELETE SET NULL, -- Если шаблон удалят, история останется
    started_at         TIMESTAMP NOT NULL,
    ended_at           TIMESTAMP

);
CREATE TABLE fitness_app.session_exercise
(
    id          SERIAL PRIMARY KEY,
    session_id  INTEGER  NOT NULL REFERENCES fitness_app.workout_session (id) ON DELETE CASCADE,
    exercise_id INTEGER  NOT NULL REFERENCES fitness_app.exercise_definition (id),
    order_index INTEGER NOT NULL

);

CREATE TABLE fitness_app.exercise_set
(
    id                  SERIAL PRIMARY KEY,
    session_exercise_id INTEGER  NOT NULL REFERENCES fitness_app.session_exercise (id) ON DELETE CASCADE,
    set_number          INTEGER NOT NULL,
    weight              DECIMAL(6, 2),
    reps                INTEGER,
    rest_seconds        INTEGER,
    set_type            VARCHAR(20) DEFAULT 'NORMAL'
);

CREATE TABLE fitness_app.email_verification (
                                                auth_id SERIAL PRIMARY KEY,
                                                verification_code VARCHAR(10),
                                                email VARCHAR(255) UNIQUE ,
                                                expiry_date TIMESTAMP

);

CREATE TABLE fitness_app.deactivated_tokens
(
    id         UUID PRIMARY KEY,
    keep_until timestamp
);

CREATE INDEX idx_users_email ON fitness_app.users(email);
CREATE INDEX idx_session_user ON fitness_app.workout_session(user_id);
CREATE INDEX idx_template_user ON fitness_app.workout_template(user_id);
CREATE INDEX idx_session_exercise_session ON fitness_app.session_exercise(session_id);
CREATE INDEX idx_set_session_exercise ON fitness_app.exercise_set(session_exercise_id);