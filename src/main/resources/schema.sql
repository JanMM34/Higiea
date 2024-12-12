
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS sensor (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    state VARCHAR(255) NOT NULL,
    assigned_to_route BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS truck (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    plate VARCHAR(15),
    route VARCHAR(255),
    max_load_capacity INT,
    depot_latitude DOUBLE PRECISION NOT NULL,
    depot_longitude DOUBLE PRECISION NOT NULL
);
