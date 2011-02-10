--
-- Table structure for table tagcloud_portlet
--
DROP TABLE IF EXISTS tagcloud_portlet;
CREATE TABLE tagcloud_portlet (
  id_portlet int NOT NULL,
  id_cloud varchar(255) DEFAULT '' NOT NULL,
  PRIMARY KEY  (id_portlet)
);