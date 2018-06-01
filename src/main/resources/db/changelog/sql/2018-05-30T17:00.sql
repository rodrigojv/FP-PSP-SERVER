ALTER TABLE security.termcondpol DROP COLUMN language;

ALTER TABLE security.termcondpol
    ADD COLUMN id_application bigint NOT NULL DEFAULT 0;