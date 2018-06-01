--Adds activity message
ALTER TABLE system.activity
  ADD COLUMN activity_key character varying(200) NOT NULL,
  ADD COLUMN activity_params character varying(200) NOT NULL,
  ADD COLUMN activity_role character varying(200) NOT NULL,
  ADD COLUMN family_id bigint
;

--Adds relationship with families
ALTER TABLE system.activity
  ADD CONSTRAINT fk_activity_family_family_id FOREIGN KEY (family_id)
  REFERENCES ps_families.family (family_id) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION;

--Deletes unused columns of activity
ALTER TABLE system.activity
  DROP COLUMN description CASCADE;

--Renames create_at column to created_at
ALTER TABLE system.activity
  RENAME COLUMN create_at TO created_at;


