-- liquibase formatted sql
-- changeset tagcloud:init_core_tagcloud.sql
-- preconditions onFail:MARK_RAN onError:WARN

--
-- Dumping data for table core_admin_right
--

DELETE FROM core_admin_right WHERE id_right = 'TAGCLOUD_MANAGEMENT';
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url) VALUES 
('TAGCLOUD_MANAGEMENT','tagcloud.adminFeature.tagcloud_management.name',3,'jsp/admin/plugins/tagcloud/ManageTagClouds.jsp','tagcloud.adminFeature.tagcloud_management.description',0,'tagcloud','CONTENT','ti ti-tags', NULL);

--
-- Dumping data for table core_user_right
--
DELETE FROM core_user_right WHERE id_right = 'TAGCLOUD_MANAGEMENT';
INSERT INTO core_user_right (id_right,id_user) VALUES ('TAGCLOUD_MANAGEMENT',1);
INSERT INTO core_user_right (id_right,id_user) VALUES ('TAGCLOUD_MANAGEMENT',2);
