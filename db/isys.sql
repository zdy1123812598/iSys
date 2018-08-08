/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50016
Source Host           : 127.0.0.1:3306
Source Database       : isys

Target Server Type    : MYSQL
Target Server Version : 50016
File Encoding         : 65001

Date: 2017-06-05 16:49:01
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tblink
-- ----------------------------
DROP TABLE IF EXISTS `tblink`;
CREATE TABLE `tblink` (
  `linkid` int(11) NOT NULL auto_increment,
  `linkname` varchar(255) default NULL,
  `linktext` varchar(255) default NULL,
  `parentid` int(255) default NULL,
  `creatorid` int(11) default NULL,
  `createtime` timestamp NULL default NULL on update CURRENT_TIMESTAMP,
  PRIMARY KEY  (`linkid`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tblink
-- ----------------------------
INSERT INTO `tblink` VALUES ('1', '系统管理', '', '0', '1', '2017-06-05 16:43:40');
INSERT INTO `tblink` VALUES ('2', '组织管理', '../organization/organizationList.jsp', '1', '1', '2017-06-05 16:43:41');
INSERT INTO `tblink` VALUES ('3', '人员管理', '../user/userList.jsp', '1', '1', '2017-06-05 16:43:43');
INSERT INTO `tblink` VALUES ('4', '链接管理', '../link/linkList.jsp', '1', '1', '2017-06-05 16:43:44');
INSERT INTO `tblink` VALUES ('5', '角色管理', '../role/roleList.jsp', '1', '1', '2017-06-05 16:43:46');

-- ----------------------------
-- Table structure for tblog
-- ----------------------------
DROP TABLE IF EXISTS `tblog`;
CREATE TABLE `tblog` (
  `logid` varchar(255) default NULL,
  `logcontent` varchar(255) default NULL,
  `logtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `userid` int(11) default NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tblog
-- ----------------------------

-- ----------------------------
-- Table structure for tborganization
-- ----------------------------
DROP TABLE IF EXISTS `tborganization`;
CREATE TABLE `tborganization` (
  `organizationid` int(11) NOT NULL auto_increment,
  `organizationname` varchar(255) NOT NULL,
  `creatorid` int(11) default NULL,
  `createtime` timestamp NULL default NULL on update CURRENT_TIMESTAMP,
  PRIMARY KEY  (`organizationid`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tborganization
-- ----------------------------
INSERT INTO `tborganization` VALUES ('1', '信息部门', '1', '2017-06-05 16:45:09');

-- ----------------------------
-- Table structure for tbrole
-- ----------------------------
DROP TABLE IF EXISTS `tbrole`;
CREATE TABLE `tbrole` (
  `roleid` int(11) NOT NULL auto_increment,
  `rolename` varchar(255) NOT NULL,
  `creatorid` int(11) default NULL,
  `createtime` timestamp NULL default NULL on update CURRENT_TIMESTAMP,
  PRIMARY KEY  (`roleid`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tbrole
-- ----------------------------
INSERT INTO `tbrole` VALUES ('1', '管理角色', '1', '2017-06-05 16:45:14');

-- ----------------------------
-- Table structure for tbrolelink
-- ----------------------------
DROP TABLE IF EXISTS `tbrolelink`;
CREATE TABLE `tbrolelink` (
  `roleid` int(11) default NULL,
  `linkid` int(11) default NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tbrolelink
-- ----------------------------
INSERT INTO `tbrolelink` VALUES ('1', '5');
INSERT INTO `tbrolelink` VALUES ('1', '4');
INSERT INTO `tbrolelink` VALUES ('1', '3');
INSERT INTO `tbrolelink` VALUES ('1', '2');

-- ----------------------------
-- Table structure for tbroleuser
-- ----------------------------
DROP TABLE IF EXISTS `tbroleuser`;
CREATE TABLE `tbroleuser` (
  `roleid` int(11) NOT NULL,
  `userid` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tbroleuser
-- ----------------------------
INSERT INTO `tbroleuser` VALUES ('1', '1');
INSERT INTO `tbroleuser` VALUES ('1', '2');

-- ----------------------------
-- Table structure for tbuser
-- ----------------------------
DROP TABLE IF EXISTS `tbuser`;
CREATE TABLE `tbuser` (
  `userid` int(11) NOT NULL auto_increment,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) default NULL,
  `gender` int(255) default NULL,
  `telephone` varchar(255) default NULL,
  `status` int(11) default NULL,
  `organizationid` int(11) NOT NULL,
  `logonname` varchar(255) NOT NULL,
  `creatorid` int(11) default NULL,
  `createtime` timestamp NULL default NULL on update CURRENT_TIMESTAMP,
  PRIMARY KEY  (`userid`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tbuser
-- ----------------------------
INSERT INTO `tbuser` VALUES ('1', '超级管理员', '200ceb26807d6bf99fd6f4f0d1ca54d4', '1', '010-00000000', '2', '1', 'administrator', '1', '2017-06-05 16:45:19');
INSERT INTO `tbuser` VALUES ('2', '管理员', '21232f297a57a5a743894a0e4a801fc3', '1', '010-00000001', '1', '1', 'admin', '1', '2017-06-05 16:45:21');
