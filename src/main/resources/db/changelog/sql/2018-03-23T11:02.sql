DELETE FROM security.users_roles where user_id in 
(select id from security.users where username in ('hub_admin', 'app_admin', 'user', 'survey_user', 'partner_admin'))
and user_id not in ( select distinct user_id FROM data_collect.snapshot_draft) 
and user_id not in (select distinct user_id FROM data_collect.snapshots_economics) 
and user_id not in (select distinct user_id FROM security.password_reset_token)
and user_id not in (select distinct user_id from ps_network.users_applications 
	where organization_id in (select id from ps_network.organizations where code not in ('farorg', 'tacorg', 'mejororg', 'irisorg', 'crediorg'))
	or application_id in (select id from ps_network.applications where code not in ('fcolapp', 'fargapp', 'fsudapp', 'fmexapp', 'fukapp'))); 

DELETE FROM ps_network.users_applications where user_id 
in (select id from security.users where username in ('hub_admin', 'app_admin', 'user', 'survey_user', 'partner_admin'))
and user_id not in ( select distinct user_id FROM data_collect.snapshot_draft) 
and user_id not in (select distinct user_id FROM data_collect.snapshots_economics) 
and user_id not in (select distinct user_id FROM security.password_reset_token)
and application_id in (select id from ps_network.applications where code in ('fcolapp', 'fargapp', 'fsudapp', 'fmexapp', 'fukapp'))
and organization_id in (select id from ps_network.organizations where code in ('farorg', 'tacorg', 'mejororg', 'irisorg', 'crediorg'));

DELETE FROM security.users where username in ('hub_admin', 'app_admin', 'user', 'survey_user', 'partner_admin') 
and id not in ( select distinct user_id FROM data_collect.snapshot_draft) 
and id not in (select distinct user_id FROM data_collect.snapshots_economics) 
and id not in (select distinct user_id FROM security.password_reset_token)
and id not in (select distinct user_id from ps_network.users_applications); 

DELETE FROM ps_network.organizations where code in ('farorg', 'tacorg', 'mejororg', 'irisorg', 'crediorg') 
and id not in (select distinct organization_id from ps_families.family) 
and id not in (select distinct organization_id from ps_network.surveys_organizations where organization_id is not null)
and id in (select distinct organization_id from ps_network.users_applications where user_id in (
select id from security.users where username in ('hub_admin', 'app_admin', 'user', 'survey_user', 'partner_admin')));

DELETE FROM ps_network.applications where code in ('fcolapp', 'fargapp', 'fsudapp', 'fmexapp', 'fukapp') 
and id not in (select distinct application_id from ps_families.family)
and id not in (select distinct application_id from ps_network.organizations)
and id not in (select distinct application_id from ps_network.surveys_organizations where application_id is not null)
and id in (select distinct application_id from ps_network.users_applications where user_id in (
select id from security.users where username in ('hub_admin', 'app_admin', 'user', 'survey_user', 'partner_admin')));
