--Add email column in security.users 
ALTER TABLE security.users
    ADD COLUMN email text;
    
--Create table to store reset password user
CREATE TABLE security.password_reset_token
(
  id bigint,
  token text NOT NULL,
  user_id bigint NOT NULL,
  expiry_date date NOT NULL,
  
  CONSTRAINT password_reset_token_pkey PRIMARY KEY (id),
  CONSTRAINT fk_password_reset_token_users FOREIGN KEY (user_id)
        REFERENCES security.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE SEQUENCE security.password_reset_token_id_seq;