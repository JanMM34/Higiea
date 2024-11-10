DROP TABLE IF EXISTS sensor;
DROP TYPE IF EXISTS container_state;

CREATE TYPE container_state AS ENUM ('FULL', 'EMPTY');

CREATE TABLE sensor (
    id SERIAL PRIMARY KEY,
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    state container_state NOT NULL
);