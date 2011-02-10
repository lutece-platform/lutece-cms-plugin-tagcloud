--
-- Table structure for table tagcloud
--

DROP TABLE IF EXISTS tagcloud;
CREATE TABLE tagcloud (
  id_cloud int NOT NULL,
  cloud_description varchar(255) DEFAULT '' NOT NULL,
  PRIMARY KEY  (id_cloud)
);

--
-- Table structure for table tagcloud_tag
--

DROP TABLE IF EXISTS tagcloud_tag;
CREATE TABLE tagcloud_tag (
  id_cloud int NOT NULL,
  id_tag int NOT NULL,
  tag_name varchar(255) NOT NULL,
  tag_weight varchar(30) DEFAULT '' NOT NULL,
  tag_url varchar(255) DEFAULT '' NOT NULL,
  PRIMARY KEY  (id_cloud,id_tag)
);

