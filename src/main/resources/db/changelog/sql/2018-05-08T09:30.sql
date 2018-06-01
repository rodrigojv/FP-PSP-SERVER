-- Set columns length for applications fields
ALTER TABLE ps_network.applications
    ALTER COLUMN name TYPE varchar(50),
    ALTER COLUMN code TYPE varchar(50),
    ALTER COLUMN description TYPE varchar(256),
    ALTER COLUMN information TYPE varchar(256);

UPDATE ps_network.applications SET name = '' WHERE name IS NULL;
UPDATE ps_network.applications SET code = '' WHERE code IS NULL;
UPDATE ps_network.applications SET description = '' WHERE description IS NULL;

-- Set columns to NOT NULL for applications required fields
ALTER TABLE ps_network.applications ALTER COLUMN name SET NOT NULL;
ALTER TABLE ps_network.applications ALTER COLUMN code SET NOT NULL;
ALTER TABLE ps_network.applications ALTER COLUMN description SET NOT NULL;

-- Set columns length for organizations fields
ALTER TABLE ps_network.organizations
    ALTER COLUMN name TYPE varchar(50),
    ALTER COLUMN code TYPE varchar(50),
    ALTER COLUMN description TYPE varchar(256),
    ALTER COLUMN information TYPE varchar(256);
