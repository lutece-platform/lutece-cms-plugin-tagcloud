-- liquibase formatted sql
-- changeset tagcloud:update_db_core_tagcloud-4.1.1-6.0.0.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = database() AND table_name = 'core_portlet' AND column_name = 'id_template'
--
-- The XSL based rendering has been removed : every tag cloud portlet is now rendered with a FreeMarker template
-- chosen per portlet among the templates registered in the core for the portlet type (core_portlet_template,
-- core_portlet.id_template, Section Template Management feature). The old XSL style is mapped to the default template.
--
-- The plugin upgrade scripts run BEFORE the core upgrade script in the same liquibase run (sql/plugins/* sorts before sql/upgrade/*) :
-- the core structures are created here when they do not exist yet, with the very same statements as the core script, which is then skipped.
--
ALTER TABLE core_portlet ADD COLUMN id_template int default 0 NOT NULL;

-- changeset tagcloud:update_db_core_tagcloud-4.1.1-6.0.0.sql-rev1.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = database() AND table_name = 'core_portlet_template'
CREATE TABLE IF NOT EXISTS core_portlet_template (
	id_template int AUTO_INCREMENT NOT NULL,
	id_portlet_type varchar(50) default NULL,
	description varchar(255) default NULL,
	template_path varchar(255) default NULL,
	PRIMARY KEY (id_template)
);

--
-- Templates available for the tag cloud portlets
--
-- changeset tagcloud:update_db_core_tagcloud-4.1.1-6.0.0.sql-rev2.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM core_portlet_template WHERE id_portlet_type = 'TAG_CLOUD_PORTLET'
INSERT INTO core_portlet_template (id_portlet_type, description, template_path) VALUES ('TAG_CLOUD_PORTLET', 'Défaut', 'skin/plugins/tagcloud/portlet/tagcloud_portlet.html');
INSERT INTO core_portlet_template (id_portlet_type, description, template_path) VALUES ('TAG_CLOUD_PORTLET', 'Barre latérale', 'skin/plugins/tagcloud/portlet/tagcloud_portlet_sidebar.html');

--
-- Template chosen for each portlet : the only XSL style (800 "Défaut") -> default template (0)
--
-- changeset tagcloud:update_db_core_tagcloud-4.1.1-6.0.0.sql-rev3.sql
-- preconditions onFail:MARK_RAN onError:WARN
UPDATE core_portlet SET id_template = 0, id_style = 0 WHERE id_portlet_type = 'TAG_CLOUD_PORTLET';

-- changeset tagcloud:update_db_core_tagcloud-4.1.1-6.0.0.sql-rev4.sql
-- preconditions onFail:MARK_RAN onError:WARN
-- comment Legacy XSL style tables left the core for plugin-xmltransformer and are absent from many databases: skip instead of failing the whole update
-- precondition-sql-check expectedResult:3 SELECT COUNT(1) from INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA=database() AND TABLE_NAME IN ('core_style_mode_stylesheet','core_stylesheet','core_style');
DELETE FROM core_style_mode_stylesheet WHERE id_style = 800;
DELETE FROM core_style WHERE id_style = 800;
DELETE FROM core_stylesheet WHERE id_stylesheet = 9003;
