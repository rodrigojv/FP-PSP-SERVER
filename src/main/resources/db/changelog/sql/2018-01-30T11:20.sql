--Change columns: name, code, description, application_id, is_active to NOT NULL
UPDATE ps_network.organizations SET code = '' WHERE code IS NULL;
UPDATE ps_network.organizations SET name = '' WHERE name IS NULL;
UPDATE ps_network.organizations SET description = '' WHERE description IS NULL;
UPDATE ps_network.organizations SET is_active = false WHERE is_active IS NULL;


ALTER TABLE ps_network.organizations ALTER COLUMN name SET NOT NULL;
ALTER TABLE ps_network.organizations ALTER COLUMN code SET NOT NULL;
ALTER TABLE ps_network.organizations ALTER COLUMN description SET NOT NULL;
ALTER TABLE ps_network.organizations ALTER COLUMN is_active SET NOT NULL;
