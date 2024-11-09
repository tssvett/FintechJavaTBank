--liquibase formatted sql

--changeset tssvett:create-table-api-user

CREATE TABLE IF NOT EXISTS api_user (
    user_id BIGSERIAL NOT NULL PRIMARY KEY,
    login TEXT NOT NULL,
    hash_password TEXT NOT NULL,
    user_role TEXT NOT NULL
);