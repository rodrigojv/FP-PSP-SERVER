ALTER TABLE system.parameter ALTER COLUMN key_parameter TYPE character varying (100);

ALTER TABLE system.parameter
    ADD COLUMN value character varying (200);