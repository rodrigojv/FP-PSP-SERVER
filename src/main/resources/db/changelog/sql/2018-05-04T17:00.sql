-- Delete unused columns is_partner, is_hub
ALTER TABLE ps_network.applications
    DROP COLUMN is_partner;
ALTER TABLE ps_network.applications
    DROP COLUMN is_hub;