DROP TABLE IF EXISTS sensor;

CREATE TYPE container_state AS ENUM ('FULL', 'EMPTY');

CREATE TABLE sensor (
    id BIGINT PRIMARY KEY,
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    state container_state NOT NULL
);