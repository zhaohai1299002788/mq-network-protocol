/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50505
Source Host           : localhost:3306
Source Database       : mq-http

Target Server Type    : MYSQL
Target Server Version : 50505
File Encoding         : 65001

Date: 2022-04-02 12:00:43
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for message_data
-- ----------------------------
DROP TABLE IF EXISTS `message_data`;
CREATE TABLE `message_data` (
  `id` bigint(20) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `create_date` datetime NOT NULL,
  `update_date` datetime NOT NULL,
  `consume_site_date` datetime NOT NULL,
  `is_deleted` int(10) unsigned zerofill NOT NULL,
  `name_srv_addr` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `instance_id` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `topic` varchar(255) NOT NULL DEFAULT '',
  `group_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci NOT NULL DEFAULT '',
  `messag_id` varchar(255) NOT NULL DEFAULT '',
  `tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_german2_ci NOT NULL DEFAULT '',
  `key` varchar(255) NOT NULL DEFAULT '',
  `message` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;
