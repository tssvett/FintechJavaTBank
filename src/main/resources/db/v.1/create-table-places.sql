--liquibase formatted sql

--changeset tssvett:create-table-places

CREATE TABLE IF NOT EXISTS places (
    id UUID NOT NULL PRIMARY KEY,
    slug TEXT NOT NULL,
    name TEXT NOT NULL
);
