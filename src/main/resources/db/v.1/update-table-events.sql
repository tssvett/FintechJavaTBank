--liquibase formatted sql

--changeset tssvett:update-table-events
ALTER TABLE events
ADD COLUMN price DECIMAL(15, 2);