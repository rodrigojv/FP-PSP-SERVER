--Add unique constraint in snapshot_indicator_priorities table

ALTER TABLE data_collect.snapshot_indicator_priorities ADD CONSTRAINT unq_snapshot_indicator_indicator UNIQUE (snapshot_indicator, indicator);
