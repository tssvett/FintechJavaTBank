--liquibase formatted sql

--changeset tssvett:create-table-events

CREATE TABLE IF NOT EXISTS events(
    id BIGINT NOT NULL PRIMARY KEY,
    place_id UUID NOT NULL,
    name TEXT NOT NULL,
    date DATE NOT NULL,
    FOREIGN KEY (place_id) REFERENCES places(id) ON DELETE CASCADE
);