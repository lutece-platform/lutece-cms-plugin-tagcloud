-- liquibase formatted sql
-- changeset tagcloud:update_db_tagcloud-4.1.1-6.0.0.sql
-- preconditions onFail:MARK_RAN onError:WARN
UPDATE core_admin_right SET icon_url = 'ti ti-tags' WHERE id_right = 'TAGCLOUD_MANAGEMENT';
