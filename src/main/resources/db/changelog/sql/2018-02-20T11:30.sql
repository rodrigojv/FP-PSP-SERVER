--Add column to save if priority is attainment, is_attainment to NOT NULL

ALTER TABLE data_collect.snapshot_indicator_priorities
    ADD COLUMN is_attainment boolean;

UPDATE data_collect.snapshot_indicator_priorities SET is_attainment = false WHERE is_attainment IS NULL;

