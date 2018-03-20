--Change columns: name, code, description, application_id, is_active to NOT NULL
UPDATE security.users SET email = '' WHERE email IS NULL;
UPDATE security.users SET pass = '' WHERE pass IS NULL;
UPDATE security.users SET active = false WHERE active IS NULL;

ALTER TABLE security.users ALTER COLUMN email SET NOT NULL;
ALTER TABLE security.users ALTER COLUMN pass SET NOT NULL;
ALTER TABLE security.users ALTER COLUMN active SET NOT NULL;
