/*
 Navicat Premium Data Transfer

 Source Server         : local
 Source Server Type    : MySQL
 Source Server Version : 50729
 Source Host           : localhost:3306
 Source Schema         : vueblog

 Target Server Type    : MySQL
 Target Server Version : 50729
 File Encoding         : 65001

 Date: 19/06/2022 10:48:27
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for m_blog
-- ----------------------------
DROP TABLE IF EXISTS `m_blog`;
CREATE TABLE `m_blog`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `sort_id` int(11) NOT NULL,
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `content` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `created` datetime(0) NOT NULL,
  `status` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 37 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of m_blog
-- ----------------------------

-- ----------------------------
-- Table structure for m_blogtag
-- ----------------------------
DROP TABLE IF EXISTS `m_blogtag`;
CREATE TABLE `m_blogtag`  (
  `blogId` bigint(20) NOT NULL,
  `tagId` bigint(20) NOT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of m_blogtag
-- ----------------------------

-- ----------------------------
-- Table structure for m_friend
-- ----------------------------
DROP TABLE IF EXISTS `m_friend`;
CREATE TABLE `m_friend`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '名称',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `href` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '链接地址',
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '头像地址',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of m_friend
-- ----------------------------

-- ----------------------------
-- Table structure for m_other
-- ----------------------------
DROP TABLE IF EXISTS `m_other`;
CREATE TABLE `m_other`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order` tinyint(4) NULL DEFAULT 0,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of m_other
-- ----------------------------
INSERT INTO `m_other` VALUES (1, 0, '关于我', '## 关于我\n00后, C、Rust、Java程序员, 现居北京。\n\n## 关于博客\n- 博客系统采用 [VueBlog v7.4](https://github.com/Clownsw/vueblog)\n- 这个网站平时自己工作学习的一个笔记,帮助自己查缺补漏\n\n## 免责声明\n- 本站所有文章为了记录工作、学习中遇到的问题，可能由于本人技术有限，有些不正确的地方，仅供参考\n- 本站文章引用或转载写明来源，感谢原作者的辛苦写作，如果有异议或侵权，及时联系我处理，谢谢！\n- 如他人引用本站中的文章或内容，请注明出处。但其文章或内容已不是本人原本的意思，请各位注意辨别！\n- 本站所有文章仅代表个人当时意见和想法\n- 欢迎指出有问题的地方，我会尽快修正，谢谢！\n\n## 联系方式\n- Email\n	- msmliexx1@gmail.com\n	- msmliexx1@skiff.com\n- Telegram: [https://t.me/smilex_plus](https://t.me/smilex_plus)');
INSERT INTO `m_other` VALUES (2, 1, '网页底部', '<div style=\"text-align: center;margin-top: 20px;margin-bottom:10px;\">\n	<img src=\"https://www.upyun.com/static/img/%E6%A0%B7%E5%BC%8F%E5%9B%BE.7cf927c.png\" style=\"width: 304px;\" />\n<p style=\"margin: 0;font-size: 13px;margin-bottom:2px;\">Copyright © 2021-2022 Smilex\'s Blog. <a href=\"https://beian.miit.gov.cn/#/Integrated/index\" target=\"_blank\" style=\"text-decoration: none;color: #333333;\"> 豫ICP备2021023470号</p>\n<p style=\"margin: 0;font-size: 13px;\">Powered by <a href=\"https://github.com/Clownsw/vueblog\" target=\"_blank\" style=\"color: #409eff;font-weight: bold;text-decoration: none;\">VueBlog</a></p>\n</div>');
INSERT INTO `m_other` VALUES (3, 3, '备份设置', '{\"username\":\" \",\"password\":\" \",\"operator\":\" \",\"operator_password\":\" \",\"bucket_name\":\" \"}');

-- ----------------------------
-- Table structure for m_sort
-- ----------------------------
DROP TABLE IF EXISTS `m_sort`;
CREATE TABLE `m_sort`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order` int(11) NOT NULL DEFAULT 0,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of m_sort
-- ----------------------------

-- ----------------------------
-- Table structure for m_system
-- ----------------------------
DROP TABLE IF EXISTS `m_system`;
CREATE TABLE `m_system`  (
  `id` int(11) NOT NULL,
  `welcome` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of m_system
-- ----------------------------
INSERT INTO `m_system` VALUES (1, '欢迎来到Smilex博客', 'Smile\' Blog', '一个神秘的博客');

-- ----------------------------
-- Table structure for m_tag
-- ----------------------------
DROP TABLE IF EXISTS `m_tag`;
CREATE TABLE `m_tag`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 59 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of m_tag
-- ----------------------------

-- ----------------------------
-- Table structure for m_user
-- ----------------------------
DROP TABLE IF EXISTS `m_user`;
CREATE TABLE `m_user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `email` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `password` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status` int(11) NOT NULL,
  `created` datetime(0) NOT NULL,
  `last_login` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of m_user
-- ----------------------------
INSERT INTO `m_user` VALUES (1, 'xiaoxiao', 'https://avatars.githubusercontent.com/u/28394742', 'msmliexx1@gmail.com', '123123', 1, '2022-03-29 21:23:55', '2022-06-19 01:03:44');

SET FOREIGN_KEY_CHECKS = 1;
