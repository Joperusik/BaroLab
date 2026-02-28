-- liquibase formatted sql

-- changeset volosinzena:add-status-to-mod-guides
ALTER TABLE mod_guides ADD COLUMN status VARCHAR(20) DEFAULT 'ACTIVE' NOT NULL;
