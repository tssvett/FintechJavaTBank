--liquibase formatted sql

--changeset tssvett:create-table-jwt-tokens

CREATE TABLE IF NOT EXISTS jwt_tokens (
    id BIGSERIAL NOT NULL PRIMARY KEY,
    token TEXT NOT NULL,
    revoked BOOLEAN NOT NULL,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES api_user(user_id) ON delete CASCADE
);