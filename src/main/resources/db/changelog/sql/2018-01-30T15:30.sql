--Insert default email to default users
UPDATE security.users SET email = 'user@povertystoplight.org' WHERE username = 'admin';
UPDATE security.users SET email = 'user@povertystoplight.org' WHERE username = 'app_admin';
UPDATE security.users SET email = 'user@povertystoplight.org' WHERE username = 'hub_admin';
UPDATE security.users SET email = 'user@povertystoplight.org' WHERE username = 'user';
UPDATE security.users SET email = 'user@povertystoplight.org' WHERE username = 'partner_admin';
UPDATE security.users SET email = 'user@povertystoplight.org' WHERE username = 'survey_user';
