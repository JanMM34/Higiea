DROP TABLE IF EXISTS sensor;

CREATE TABLE sensor (
    id SERIAL,
    location VARCHAR(255),
    CONSTRAINT sensor_pk PRIMARY KEY (id)
);