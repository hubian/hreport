DROP TABLE IF EXISTS `report_table_schema`;
-- report record
CREATE TABLE `report_table_schema` (
  `table_id` INT NOT NULL AUTO_INCREMENT,
  `table_name` VARCHAR(128) NOT NULL COMMENT '表名和类名一致',
  `sign_code` VARCHAR(128) NOT NULL COMMENT '表结构签名',
  `create_date` DATE NOT NULL COMMENT '创建时间',
  `create_ip` VARCHAR(32) NOT NULL COMMENT '创建IP',
  `update_date` DATE NOT NULL COMMENT '更新时间',
  `update_ip` VARCHAR(32) NOT NULL COMMENT '更新IP',
  `lasted_version` VARCHAR(32) NOT NULL COMMENT '最新版本号',
  PRIMARY  KEY (`table_id`),
  UNIQUE KEY (`table_name`)
) ENGINE = INNODB DEFAULT CHARSET = utf8;


DROP TABLE IF EXISTS `report_field_schema`;
CREATE TABLE `report_field_schema` (
  `field_id` INT NOT NULL AUTO_INCREMENT ,
  `table_id` INT NOT NULL,
  `field_name` VARCHAR(32) NOT NULL COMMENT '属性名称',
  `field_type` INT NOT NULL COMMENT 'field类型：数字/字符串',
  `data_field` VARCHAR(6) NOT NULL COMMENT 'report_data中的字段',
  `primary_key` BOOL NOT NULL DEFAULT FALSE COMMENT '当前属性是否是主键',
  PRIMARY KEY(`field_id`),
  UNIQUE KEY (`table_id`, `field_name`)
) ENGINE = INNODB DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `report_data`;
CREATE TABLE `report_data` (
  `data_id` BIGINT NOT NULL AUTO_INCREMENT,
  `table_id` INT  NOT NULL COMMENT '表ID',
  `data_version` varchar(32) NOT NULL,
  `lf1` BIGINT COMMENT '数字型字段1',
  `lf2` BIGINT COMMENT '数字型字段2',
  `lf3` BIGINT COMMENT '数字型字段3',
  `lf4` BIGINT COMMENT '数字型字段4',
  `lf5` BIGINT COMMENT '数字型字段5',
  `lf6` BIGINT COMMENT '数字型字段6',
  `lf7` BIGINT COMMENT '数字型字段7',
  `lf8` BIGINT COMMENT '数字型字段8',
  `lf9` BIGINT COMMENT '数字型字段9',
  `lf10` BIGINT COMMENT '数字型字段10',
  `sf1` VARCHAR(256) COMMENT '字符串字段1',
  `sf2` VARCHAR(256) COMMENT '字符串字段2',
  `sf3` VARCHAR(256) COMMENT '字符串字段3',
  `sf4` VARCHAR(256) COMMENT '字符串字段4',
  `sf5` VARCHAR(256) COMMENT '字符串字段5',
  `sf6` VARCHAR(256) COMMENT '字符串字段6',
  `sf7` VARCHAR(256) COMMENT '字符串字段7',
  `sf8` VARCHAR(256) COMMENT '字符串字段8',
  `sf9` VARCHAR(256) COMMENT '字符串字段9',
  `sf10` VARCHAR(256) COMMENT '字符串字段10',
  `jf1` VARCHAR(2048) COMMENT 'JSON字段1',
  `jf2` VARCHAR(2048) COMMENT 'JSON字段2',
  PRIMARY  KEY (`data_id`),
  KEY `table_id` (`table_id`,`data_version`) USING BTREE,
  KEY `lf1` (`lf1`) USING BTREE
) ENGINE = InnoDB DEFAULT CHARSET = utf8;