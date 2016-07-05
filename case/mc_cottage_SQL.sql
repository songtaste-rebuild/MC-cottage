-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        5.5.28 - MySQL Community Server (GPL)
-- 服务器操作系统:                      Win64
-- HeidiSQL 版本:                  9.2.0.4947
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- 导出 mc_cottage 的数据库结构
CREATE DATABASE IF NOT EXISTS `mc_cottage` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `mc_cottage`;


-- 导出  表 mc_cottage.album 结构
CREATE TABLE IF NOT EXISTS `album` (
  `id` bigint(64) NOT NULL AUTO_INCREMENT,
  `album_name` varchar(128) DEFAULT NULL,
  `album_status` int(8) DEFAULT NULL,
  `creator` bigint(64) DEFAULT NULL,
  `create_time` time DEFAULT NULL,
  `is_deleted` int(8) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。


-- 导出  表 mc_cottage.music 结构
CREATE TABLE IF NOT EXISTS `music` (
  `id` bigint(64) NOT NULL AUTO_INCREMENT,
  `music_name` varchar(128) DEFAULT NULL,
  `file_url` varchar(128) DEFAULT NULL,
  `file_name` varchar(128) DEFAULT NULL,
  `creator` bigint(128) DEFAULT NULL,
  `status` int(8) DEFAULT NULL,
  `create_time` time DEFAULT NULL,
  `is_deleted` int(8) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。


-- 导出  表 mc_cottage.music_album_relation 结构
CREATE TABLE IF NOT EXISTS `music_album_relation` (
  `id` bigint(64) NOT NULL AUTO_INCREMENT,
  `mus_id` bigint(64) DEFAULT NULL,
  `music_id` bigint(64) DEFAULT NULL,
  `album` bigint(64) DEFAULT NULL,
  `create_time` time DEFAULT NULL,
  `is_deleted` int(8) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。


-- 导出  表 mc_cottage.music_type 结构
CREATE TABLE IF NOT EXISTS `music_type` (
  `id` bigint(64) NOT NULL AUTO_INCREMENT,
  `type_name` varchar(128) DEFAULT NULL,
  `create_time` time DEFAULT NULL,
  `is_deleted` int(8) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。


-- 导出  表 mc_cottage.music_type_relation 结构
CREATE TABLE IF NOT EXISTS `music_type_relation` (
  `id` bigint(64) NOT NULL AUTO_INCREMENT,
  `music_id` bigint(64) DEFAULT NULL,
  `type_id` bigint(64) DEFAULT NULL,
  `is_deleted` int(8) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。


-- 导出  表 mc_cottage.permission 结构
CREATE TABLE IF NOT EXISTS `permission` (
  `id` bigint(64) NOT NULL AUTO_INCREMENT,
  `permission_name` varchar(128) DEFAULT NULL,
  `create_time` time DEFAULT NULL,
  `is_deleted` int(8) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。


-- 导出  表 mc_cottage.role 结构
CREATE TABLE IF NOT EXISTS `role` (
  `id` bigint(64) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(128) DEFAULT NULL,
  `create_time` time DEFAULT NULL,
  `is_deleted` int(8) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。


-- 导出  表 mc_cottage.role_permission_relation 结构
CREATE TABLE IF NOT EXISTS `role_permission_relation` (
  `id` bigint(128) NOT NULL AUTO_INCREMENT,
  `per_id` bigint(128) DEFAULT NULL,
  `permission_id` bigint(128) DEFAULT NULL,
  `role_id` bigint(128) DEFAULT NULL,
  `is_deleted` int(8) DEFAULT NULL,
  `create_time` time DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。


-- 导出  表 mc_cottage.user 结构
CREATE TABLE IF NOT EXISTS `user` (
  `id` bigint(64) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(64) DEFAULT '0',
  `create_time` date DEFAULT NULL,
  `is_deleted` int(8) DEFAULT '0',
  `user_status` int(8) DEFAULT '0',
  `user_name` varchar(128) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。


-- 导出  表 mc_cottage.user_group 结构
CREATE TABLE IF NOT EXISTS `user_group` (
  `id` bigint(64) NOT NULL AUTO_INCREMENT,
  `group_name` varchar(128) DEFAULT NULL,
  `creator` varchar(128) DEFAULT NULL,
  `create_time` time DEFAULT NULL,
  `is_deleted` int(8) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。


-- 导出  表 mc_cottage.user_group_relation 结构
CREATE TABLE IF NOT EXISTS `user_group_relation` (
  `id` bigint(128) NOT NULL AUTO_INCREMENT,
  `use_id` bigint(128) DEFAULT NULL,
  `user_id` bigint(128) DEFAULT NULL,
  `group_id` bigint(128) DEFAULT NULL,
  `is_deleted` int(8) DEFAULT NULL,
  `create_time` time DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 数据导出被取消选择。
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
