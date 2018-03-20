CREATE TABLE ps_network.surveys_organizations
(
  id bigserial,
  survey_id bigint,
  organization_id bigint,
  application_id bigint,
  CONSTRAINT surveys_organizations_pkey PRIMARY KEY (id),

  CONSTRAINT fk_surveys_x_organizations_organization_organization_id FOREIGN KEY (organization_id)
      REFERENCES ps_network.organizations (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,

  CONSTRAINT fk_surveys_x_organizations_survey_survey_id FOREIGN KEY (survey_id)
      REFERENCES data_collect.surveys (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,

  CONSTRAINT fk_surveys_x_organizations_application_application_id FOREIGN KEY (application_id)
      REFERENCES ps_network.applications (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);