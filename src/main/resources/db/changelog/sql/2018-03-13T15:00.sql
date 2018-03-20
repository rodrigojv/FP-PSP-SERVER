-- families
ALTER TABLE ps_families.family ADD COLUMN last_modified_at timestamptz;
ALTER TABLE ps_families.family ALTER COLUMN last_modified_at SET DEFAULT now();
UPDATE ps_families.family SET last_modified_at = now() WHERE last_modified_at is null;

-- surveys
ALTER TABLE data_collect.surveys ADD COLUMN last_modified_at timestamptz;
ALTER TABLE data_collect.surveys ALTER COLUMN last_modified_at SET DEFAULT now();
UPDATE data_collect.surveys SET last_modified_at = now() WHERE last_modified_at is null;