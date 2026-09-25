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

--
-- The FreeMarker templates of the tag cloud portlets are registered in the core (core_portlet_template, Section Template Management feature).
-- The first one is the default template of the portlet type.
--
-- changeset tagcloud:init_core_tagcloud.sql-rev1.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM core_portlet_template WHERE id_portlet_type = 'TAG_CLOUD_PORTLET'
INSERT INTO core_portlet_template (id_portlet_type, description, template_path) VALUES ('TAG_CLOUD_PORTLET', 'Défaut', 'skin/plugins/tagcloud/portlet/tagcloud_portlet.html');
INSERT INTO core_portlet_template (id_portlet_type, description, template_path) VALUES ('TAG_CLOUD_PORTLET', 'Barre latérale', 'skin/plugins/tagcloud/portlet/tagcloud_portlet_sidebar.html');
