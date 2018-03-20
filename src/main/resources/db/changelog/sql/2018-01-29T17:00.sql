--Delete innecesaries columns
ALTER TABLE data_collect.snapshot_draft DROP COLUMN economic_response;
ALTER TABLE data_collect.snapshot_draft DROP COLUMN indicator_response;
ALTER TABLE data_collect.snapshot_draft DROP COLUMN personal_response;
