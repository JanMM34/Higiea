DROP TABLE IF EXISTS sensor;
DROP TABLE IF EXISTS truck;

CREATE TABLE sensor (
    id UUID PRIMARY KEY,
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    state VARCHAR(255) NOT NULL,
    assigned_to_route BOOLEAN DEFAULT FALSE
);

CREATE TABLE truck (
    id SERIAL PRIMARY KEY,
    route VARCHAR(255),
    max_load_capacity INT,
    depot_latitude DOUBLE PRECISION NOT NULL,
    depot_longitude DOUBLE PRECISION NOT NULL
);
