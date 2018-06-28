CREATE TABLE ps_network.labels
(
    id bigint NOT NULL,
    code character varying(20) NOT NULL,
    description character varying(50) NOT NULL,
    is_active boolean,
    created_date date NOT NULL,
    PRIMARY KEY (id)
);

CREATE SEQUENCE ps_network.label_id_seq;
ALTER TABLE ps_network.labels ALTER COLUMN id SET DEFAULT nextval('ps_network.label_id_seq');
