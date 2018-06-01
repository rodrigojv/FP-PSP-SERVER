--Add column to save the user in family
ALTER TABLE ps_families.family ADD COLUMN user_id bigint;

ALTER TABLE ps_families.family ADD CONSTRAINT fk_family_user FOREIGN KEY (user_id)
      REFERENCES security.users (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
