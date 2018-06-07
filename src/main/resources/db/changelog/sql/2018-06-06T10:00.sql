
CREATE TABLE ps_network.organizations_labels (
	id bigint NOT NULL,
	organization_id bigint NOT NULL,
	label_id bigint NOT NULL,
	PRIMARY KEY (id)
);


ALTER TABLE ps_network.organizations_labels
ADD CONSTRAINT fk_organizations_labels_organizations_id FOREIGN KEY (organization_id) REFERENCES ps_network.organizations (id),
ADD CONSTRAINT fk_organizations_labels_label_id FOREIGN KEY (label_id) REFERENCES ps_network.labels (id);

CREATE SEQUENCE ps_network.organizations_labels_id_seq;
ALTER TABLE ps_network.organizations_labels ALTER COLUMN id SET DEFAULT nextval('ps_network.organizations_labels_id_seq');