--This script add a column to save the survey creation date time
ALTER TABLE data_collect.surveys
    ADD COLUMN created_at timestamp without time zone;