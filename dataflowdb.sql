/*
 Navicat Premium Dump SQL

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 80043 (8.0.43)
 Source Host           : localhost:3306
 Source Schema         : dataflowdb

 Target Server Type    : MySQL
 Target Server Version : 80043 (8.0.43)
 File Encoding         : 65001

 Date: 14/08/2025 11:01:17
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for dic_user
-- ----------------------------
DROP TABLE IF EXISTS `dic_user`;
CREATE TABLE `dic_user`  (
  `ID` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键ID',
  `USERKENNO` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户kenno',
  `ZGH` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '职工号',
  `XM` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '姓名',
  `PASSWORD` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '密码',
  `CREATE_TIME` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `ROLEID` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '角色id',
  `DEL_FLAG` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '删除标志',
  `CREATE_USER` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  `UPDATE_USER` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`ID`, `USERKENNO`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dic_user
-- ----------------------------
INSERT INTO `dic_user` VALUES ('10', '99999999', '9999', '管理员', '1', NULL, NULL, '483,499', '0', NULL, NULL);
INSERT INTO `dic_user` VALUES ('1859785384014864385', '002', '002', '测试新增', '1', '2024-11-22 10:26:03', '2024-11-22 10:44:35', NULL, '1', '99999999', '99999999');
INSERT INTO `dic_user` VALUES ('1859791347727925250', '003', '003', '张三', '1', '2024-11-22 10:49:45', '2024-11-23 13:35:08', '499,483', '1', '99999999', '99999999');
INSERT INTO `dic_user` VALUES ('1859792009962389506', '1', '1', '1', '1', '2024-11-22 10:52:23', '2024-11-24 17:35:09', '483,1860617984577069057', '1', '99999999', '99999999');
INSERT INTO `dic_user` VALUES ('1874973773554966530', '004', '004', '规则库操作', '1', '2025-01-03 08:19:17', NULL, NULL, '0', '无', NULL);
INSERT INTO `dic_user` VALUES ('1875025633355665410', '005', '005', '权限管理', '1', '2025-01-03 11:45:21', NULL, NULL, '0', '无', NULL);
INSERT INTO `dic_user` VALUES ('1909030583636500482', '006', '006', '工具', '1', '2025-04-07 07:48:53', '2025-04-21 10:50:41', NULL, '0', '无', '无');
INSERT INTO `dic_user` VALUES ('1914824093731778562', '007', '007', '数据采集', '1', '2025-04-23 07:30:14', NULL, NULL, '0', '无', NULL);
INSERT INTO `dic_user` VALUES ('1938470409624076290', '008', '008', '高压氧', '1', '2025-06-27 13:32:15', NULL, NULL, '0', '无', NULL);

-- ----------------------------
-- Table structure for dic_user_charge
-- ----------------------------
DROP TABLE IF EXISTS `dic_user_charge`;
CREATE TABLE `dic_user_charge`  (
  `ID` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'ID',
  `USER_ID` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户ID',
  `ROLE_ID` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '角色id',
  `PYM` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '拼音码',
  `SORT` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '排序',
  `DEL_FLAG` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '删除标志(0 未删除 1 删除)',
  `CREATE_TIME` datetime NULL DEFAULT NULL COMMENT '创建日期',
  `CREATE_USER` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  `UPDATE_TIME` datetime NULL DEFAULT NULL COMMENT '修改日期',
  `UPDATE_USER` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改人'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dic_user_charge
-- ----------------------------
INSERT INTO `dic_user_charge` VALUES ('1871382858798149634', '10', '483', NULL, NULL, '0', '2024-12-24 10:30:16', NULL, NULL, NULL);
INSERT INTO `dic_user_charge` VALUES ('1871385457261752322', '1859792009962389506', '483', NULL, NULL, '1', '2024-12-24 10:40:36', NULL, NULL, NULL);
INSERT INTO `dic_user_charge` VALUES ('1871385457261752323', '1859792009962389506', '1869631035523452929', NULL, NULL, '1', '2024-12-24 10:40:36', NULL, NULL, NULL);
INSERT INTO `dic_user_charge` VALUES ('1871385457261752324', '1859792009962389506', '1869630702944505858', NULL, NULL, '1', '2024-12-24 10:40:36', NULL, NULL, NULL);
INSERT INTO `dic_user_charge` VALUES ('1874973808082477058', '1874973773554966530', '1874973668412153857', NULL, NULL, '0', '2025-01-03 08:19:25', '无', NULL, NULL);
INSERT INTO `dic_user_charge` VALUES ('1875074041638428674', '1874973773554966530', '1869631035523452929', NULL, NULL, '1', '2025-01-03 14:57:43', '无', NULL, NULL);
INSERT INTO `dic_user_charge` VALUES ('1909076488016232449', '1909030583636500482', '1909076426263494658', NULL, NULL, '0', '2025-04-07 10:51:18', '无', NULL, NULL);
INSERT INTO `dic_user_charge` VALUES ('1914824264377036801', '1914824093731778562', '1914824191794606082', NULL, NULL, '1', '2025-04-23 07:30:55', '无', NULL, NULL);
INSERT INTO `dic_user_charge` VALUES ('1938469668004020226', NULL, '1938469539532488706', NULL, NULL, '0', '2025-06-27 13:29:18', '无', NULL, NULL);
INSERT INTO `dic_user_charge` VALUES ('1938470428053848065', '1938470409624076290', '1938469539532488706', NULL, NULL, '0', '2025-06-27 13:32:19', '无', NULL, NULL);
INSERT INTO `dic_user_charge` VALUES ('1871388827976437761', '1859792009962389506', '499', NULL, NULL, '1', '2024-12-24 10:53:59', NULL, NULL, NULL);
INSERT INTO `dic_user_charge` VALUES ('1871388828089683970', '1859792009962389506', '1869631035523452929', NULL, NULL, '1', '2024-12-24 10:53:59', NULL, NULL, NULL);
INSERT INTO `dic_user_charge` VALUES ('1871394780343828481', '1859792009962389506', '1860617984577069057', NULL, NULL, '1', '2024-12-24 11:17:39', NULL, NULL, NULL);
INSERT INTO `dic_user_charge` VALUES ('1875026201411194882', '1875025633355665410', '1869631035523452929', NULL, NULL, '0', '2025-01-03 11:47:37', '无', NULL, NULL);
INSERT INTO `dic_user_charge` VALUES ('1954862916011761665', '1914824093731778562', '1914824191794606082', NULL, NULL, '0', '2025-08-11 19:10:13', '无', NULL, NULL);

-- ----------------------------
-- Table structure for error_log
-- ----------------------------
DROP TABLE IF EXISTS `error_log`;
CREATE TABLE `error_log`  (
  `ERROR_ID` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '错误记录唯一标识',
  `ERROR_TYPE` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '错误类型分类',
  `ERROR_DATA` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '发生错误时涉及的数据',
  `ERROR_DESCRIPTION` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '错误问题的详细描述',
  `DEL_FLAG` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '删除标志(0 未删除 1 删除)',
  `CREATE_TIME` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  `UPDATE_TIME` datetime NULL DEFAULT NULL COMMENT '修改日期',
  `UPDATE_USER` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`ERROR_ID` DESC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '简化的错误日志表，记录错误ID、类型、相关数据和问题描述' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of error_log
-- ----------------------------
INSERT INTO `error_log` VALUES ('1955826861820321794', '成功', '{\"AAA\":\"1\",\"BBB\":\"定时器任务二\"}', '', NULL, '2025-08-14 11:00:36', '无', NULL, NULL);
INSERT INTO `error_log` VALUES ('1955826836612554754', '成功', '{\"AAA\":\"1\",\"BBB\":\"定时器任务二\"}', '', NULL, '2025-08-14 11:00:30', '无', NULL, NULL);
INSERT INTO `error_log` VALUES ('1955826811488673794', '成功', '{\"AAA\":\"1\",\"BBB\":\"定时器任务二\"}', '', NULL, '2025-08-14 11:00:24', '无', NULL, NULL);
INSERT INTO `error_log` VALUES ('1955826786264129538', '成功', '{\"AAA\":\"1\",\"BBB\":\"定时器任务二\"}', '', NULL, '2025-08-14 11:00:18', '无', NULL, NULL);
INSERT INTO `error_log` VALUES ('1955826761136054273', '成功', '{\"AAA\":\"1\",\"BBB\":\"定时器任务二\"}', '', NULL, '2025-08-14 11:00:12', '无', NULL, NULL);
INSERT INTO `error_log` VALUES ('1955826735928287234', '成功', '{\"AAA\":\"1\",\"BBB\":\"定时器任务二\"}', '', NULL, '2025-08-14 11:00:06', '无', NULL, NULL);
INSERT INTO `error_log` VALUES ('1955826710749880322', '成功', '{\"AAA\":\"1\",\"BBB\":\"定时器任务二\"}', '', NULL, '2025-08-14 11:00:00', '无', NULL, NULL);
INSERT INTO `error_log` VALUES ('1955826685663748097', '成功', '{\"AAA\":\"1\",\"BBB\":\"定时器任务二\"}', '', NULL, '2025-08-14 10:59:54', '无', NULL, NULL);
INSERT INTO `error_log` VALUES ('1955826660460175361', '成功', '{\"AAA\":\"1\",\"BBB\":\"定时器任务二\"}', '', NULL, '2025-08-14 10:59:48', '无', NULL, NULL);
INSERT INTO `error_log` VALUES ('1955826635302739970', '成功', '{\"AAA\":\"1\",\"BBB\":\"定时器任务二\"}', '', NULL, '2025-08-14 10:59:42', '无', NULL, NULL);
INSERT INTO `error_log` VALUES ('1955826610132721666', '成功', '{\"AAA\":\"1\",\"BBB\":\"定时器任务二\"}', '', NULL, '2025-08-14 10:59:36', '无', NULL, NULL);
INSERT INTO `error_log` VALUES ('1955826584987869186', '成功', '{\"AAA\":\"1\",\"BBB\":\"定时器任务二\"}', '', NULL, '2025-08-14 10:59:30', '无', NULL, NULL);
INSERT INTO `error_log` VALUES ('1955826559931097089', '成功', '{\"AAA\":\"1\",\"BBB\":\"定时器任务二\"}', '', NULL, '2025-08-14 10:59:24', '无', NULL, NULL);
INSERT INTO `error_log` VALUES ('1955826534652026881', '成功', '{\"AAA\":\"1\",\"BBB\":\"定时器任务二\"}', '', NULL, '2025-08-14 10:59:18', '无', NULL, NULL);
INSERT INTO `error_log` VALUES ('1955826509549117442', '成功', '{\"AAA\":\"1\",\"BBB\":\"定时器任务二\"}', '', NULL, '2025-08-14 10:59:12', '无', NULL, NULL);
INSERT INTO `error_log` VALUES ('1955826484303601666', '成功', '{\"AAA\":\"1\",\"BBB\":\"定时器任务二\"}', '', NULL, '2025-08-14 10:59:06', '无', NULL, NULL);
INSERT INTO `error_log` VALUES ('1955826459116806145', '成功', '{\"AAA\":\"1\",\"BBB\":\"定时器任务二\"}', '', NULL, '2025-08-14 10:59:00', '无', NULL, NULL);
INSERT INTO `error_log` VALUES ('1955826434030673921', '成功', '{\"AAA\":\"1\",\"BBB\":\"定时器任务二\"}', '', NULL, '2025-08-14 10:58:54', '无', NULL, NULL);
INSERT INTO `error_log` VALUES ('1955826408780963842', '成功', '{\"AAA\":\"1\",\"BBB\":\"定时器任务二\"}', '', NULL, '2025-08-14 10:58:48', '无', NULL, NULL);
INSERT INTO `error_log` VALUES ('1955826383598362626', '成功', '{\"AAA\":\"1\",\"BBB\":\"定时器任务二\"}', '', NULL, '2025-08-14 10:58:42', '无', NULL, NULL);
INSERT INTO `error_log` VALUES ('1955826358466093058', '成功', '{\"AAA\":\"1\",\"BBB\":\"定时器任务二\"}', '', NULL, '2025-08-14 10:58:36', '无', NULL, NULL);
INSERT INTO `error_log` VALUES ('1955826333308657665', '成功', '{\"AAA\":\"1\",\"BBB\":\"定时器任务二\"}', '', NULL, '2025-08-14 10:58:30', '无', NULL, NULL);
INSERT INTO `error_log` VALUES ('1955826308214136834', '成功', '{\"AAA\":\"1\",\"BBB\":\"定时器任务二\"}', '', NULL, '2025-08-14 10:58:24', '无', NULL, NULL);
INSERT INTO `error_log` VALUES ('1955826282951843841', '成功', '{\"AAA\":\"1\",\"BBB\":\"定时器任务二\"}', '', NULL, '2025-08-14 10:58:18', '无', NULL, NULL);
INSERT INTO `error_log` VALUES ('1955826257827962882', '成功', '{\"AAA\":\"1\",\"BBB\":\"定时器任务二\"}', '', NULL, '2025-08-14 10:58:12', '无', NULL, NULL);
INSERT INTO `error_log` VALUES ('1955826232683110401', '成功', '{\"AAA\":\"1\",\"BBB\":\"定时器任务二\"}', '', NULL, '2025-08-14 10:58:06', '无', NULL, NULL);
INSERT INTO `error_log` VALUES ('1955826207479537665', '成功', '{\"AAA\":\"1\",\"BBB\":\"定时器任务二\"}', '', NULL, '2025-08-14 10:58:00', '无', NULL, NULL);
INSERT INTO `error_log` VALUES ('1955826182385016833', '成功', '{\"AAA\":\"1\",\"BBB\":\"定时器任务二\"}', '', NULL, '2025-08-14 10:57:54', '无', NULL, NULL);
INSERT INTO `error_log` VALUES ('1955826157126918146', '成功', '{\"AAA\":\"1\",\"BBB\":\"定时器任务二\"}', '', NULL, '2025-08-14 10:57:48', '无', NULL, NULL);
INSERT INTO `error_log` VALUES ('1955826131931734017', '成功', '{\"AAA\":\"1\",\"BBB\":\"定时器任务二\"}', '', NULL, '2025-08-14 10:57:42', '无', NULL, NULL);
INSERT INTO `error_log` VALUES ('1955826106795270146', '成功', '{\"AAA\":\"1\",\"BBB\":\"定时器任务二\"}', '', NULL, '2025-08-14 10:57:36', '无', NULL, NULL);
INSERT INTO `error_log` VALUES ('1955826081629446145', '成功', '{\"AAA\":\"1\",\"BBB\":\"定时器任务二\"}', '', NULL, '2025-08-14 10:57:30', '无', NULL, NULL);
INSERT INTO `error_log` VALUES ('1955826056551702529', '成功', '{\"AAA\":\"1\",\"BBB\":\"定时器任务二\"}', '', NULL, '2025-08-14 10:57:24', '无', NULL, NULL);
INSERT INTO `error_log` VALUES ('1955826031301992450', '成功', '{\"AAA\":\"1\",\"BBB\":\"定时器任务二\"}', '', NULL, '2025-08-14 10:57:18', '无', NULL, NULL);
INSERT INTO `error_log` VALUES ('1955826006123585538', '成功', '{\"AAA\":\"1\",\"BBB\":\"定时器任务二\"}', '', NULL, '2025-08-14 10:57:12', '无', NULL, NULL);
INSERT INTO `error_log` VALUES ('1955825980974538754', '成功', '{\"AAA\":\"1\",\"BBB\":\"定时器任务二\"}', '', NULL, '2025-08-14 10:57:06', '无', NULL, NULL);
INSERT INTO `error_log` VALUES ('1955825955825491970', '成功', '{\"AAA\":\"1\",\"BBB\":\"定时器任务二\"}', '', NULL, '2025-08-14 10:57:00', '无', NULL, NULL);
INSERT INTO `error_log` VALUES ('1955825930621919234', '成功', '{\"AAA\":\"1\",\"BBB\":\"定时器任务二\"}', '', NULL, '2025-08-14 10:56:54', '无', NULL, NULL);
INSERT INTO `error_log` VALUES ('1955825905468678146', '成功', '{\"AAA\":\"1\",\"BBB\":\"定时器任务二\"}', '', NULL, '2025-08-14 10:56:48', '无', NULL, NULL);
INSERT INTO `error_log` VALUES ('1955825880323825666', '成功', '{\"AAA\":\"1\",\"BBB\":\"定时器任务二\"}', '', NULL, '2025-08-14 10:56:42', '无', NULL, NULL);
INSERT INTO `error_log` VALUES ('1955825855225110529', '成功', '{\"AAA\":\"1\",\"BBB\":\"定时器任务二\"}', '', NULL, '2025-08-14 10:56:36', '无', NULL, NULL);
INSERT INTO `error_log` VALUES ('1955825830067675137', '成功', '{\"AAA\":\"1\",\"BBB\":\"定时器任务二\"}', '', NULL, '2025-08-14 10:56:30', '无', NULL, NULL);
INSERT INTO `error_log` VALUES ('1955825804876685313', '成功', '{\"AAA\":\"1\",\"BBB\":\"定时器任务二\"}', '', NULL, '2025-08-14 10:56:24', '无', NULL, NULL);
INSERT INTO `error_log` VALUES ('1955825779622780930', '成功', '{\"AAA\":\"1\",\"BBB\":\"定时器任务二\"}', '', NULL, '2025-08-14 10:56:18', '无', NULL, NULL);
INSERT INTO `error_log` VALUES ('1955825754515677186', '成功', '{\"AAA\":\"1\",\"BBB\":\"定时器任务二\"}', '', NULL, '2025-08-14 10:56:12', '无', NULL, NULL);
INSERT INTO `error_log` VALUES ('1955825729354047490', '成功', '{\"AAA\":\"1\",\"BBB\":\"定时器任务二\"}', '', NULL, '2025-08-14 10:56:06', '无', NULL, NULL);
INSERT INTO `error_log` VALUES ('1955825704230166530', '成功', '{\"AAA\":\"1\",\"BBB\":\"定时器任务二\"}', '', NULL, '2025-08-14 10:56:00', '无', NULL, NULL);
INSERT INTO `error_log` VALUES ('1955825678997233666', '成功', '{\"AAA\":\"1\",\"BBB\":\"定时器任务二\"}', '', NULL, '2025-08-14 10:55:54', '无', NULL, NULL);
INSERT INTO `error_log` VALUES ('1955825653818826753', '成功', '{\"AAA\":\"1\",\"BBB\":\"定时器任务二\"}', '', NULL, '2025-08-14 10:55:48', '无', NULL, NULL);
INSERT INTO `error_log` VALUES ('1955825628707528705', '成功', '{\"AAA\":\"1\",\"BBB\":\"定时器任务二\"}', '', NULL, '2025-08-14 10:55:42', '无', NULL, NULL);
INSERT INTO `error_log` VALUES ('1955825603587842049', '成功', '{\"AAA\":\"1\",\"BBB\":\"定时器任务二\"}', '', NULL, '2025-08-14 10:55:36', '无', NULL, NULL);
INSERT INTO `error_log` VALUES ('1955825578291994625', '成功', '{\"AAA\":\"1\",\"BBB\":\"定时器任务二\"}', '', NULL, '2025-08-14 10:55:30', '无', NULL, NULL);
INSERT INTO `error_log` VALUES ('1955825553176502274', '成功', '{\"AAA\":\"1\",\"BBB\":\"定时器任务二\"}', '', NULL, '2025-08-14 10:55:24', '无', NULL, NULL);
INSERT INTO `error_log` VALUES ('1955825528014872578', '成功', '{\"AAA\":\"1\",\"BBB\":\"定时器任务二\"}', '', NULL, '2025-08-14 10:55:18', '无', NULL, NULL);
INSERT INTO `error_log` VALUES ('1955825502836465665', '成功', '{\"AAA\":\"1\",\"BBB\":\"定时器任务二\"}', '', NULL, '2025-08-14 10:55:12', '无', NULL, NULL);
INSERT INTO `error_log` VALUES ('1955825478018768897', '成功', '{\"AAA\":\"1\",\"BBB\":\"定时器任务二\"}', '', NULL, '2025-08-14 10:55:06', '无', NULL, NULL);

-- ----------------------------
-- Table structure for field_mapping
-- ----------------------------
DROP TABLE IF EXISTS `field_mapping`;
CREATE TABLE `field_mapping`  (
  `MAPPING_ID` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '映射记录的唯一标识符，自增主键',
  `SCHEDULED_ID` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '任务ID',
  `SOURCE_SYSTEM` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '源数据所在的系统名称',
  `SOURCE_TABLE` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '源数据所在的表名',
  `SOURCE_FIELD` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '源数据的字段名',
  `TARGET_SYSTEM` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '目标数据所在的系统名称',
  `TARGET_TABLE` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '目标数据所在的表名',
  `TARGET_FIELD` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '目标数据的字段名',
  `DATA_TYPE_MAPPING` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '源字段与目标字段的数据类型映射关系',
  `MAPPING_RULE` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '字段值的转换规则，可包含SQL函数或业务逻辑',
  `PYM` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `SORT` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '排序',
  `DEL_FLAG` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '删除标志(0 未删除 1 删除)',
  `CREATE_TIME` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `CREATE_USER` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  `UPDATE_TIME` datetime NULL DEFAULT NULL COMMENT '修改日期',
  `UPDATE_USER` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`MAPPING_ID` DESC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '字段映射关系表，用于记录不同系统或表之间的字段对应关系及转换规则' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of field_mapping
-- ----------------------------
INSERT INTO `field_mapping` VALUES ('1955500400404013059', '1', '测试', '', 'DEL_FLAG', '测试2', '', '', '', '', NULL, NULL, '0', '2025-08-13 13:23:22', '无', NULL, NULL);
INSERT INTO `field_mapping` VALUES ('1955500400404013058', '1', '测试', '', 'status', '测试2', '', '', '', '', NULL, NULL, '0', '2025-08-13 13:23:22', '无', NULL, NULL);
INSERT INTO `field_mapping` VALUES ('1955500400404013057', '1', '测试', '', 'cron', '测试2', '', '', '', '', NULL, NULL, '0', '2025-08-13 13:23:22', '无', NULL, NULL);
INSERT INTO `field_mapping` VALUES ('1955500400341098498', '1', '测试', '', 'cron_name', '测试2', '', 'BBB', '', '', NULL, NULL, '0', '2025-08-13 13:23:22', '无', '2025-08-14 10:30:01', '无');
INSERT INTO `field_mapping` VALUES ('1955500400341098497', '1', '测试', '', 'cron_id', '测试2', '', 'AAA', '', '', NULL, NULL, '0', '2025-08-13 13:23:22', '无', '2025-08-13 13:58:12', '无');

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu`  (
  `ID` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'ID',
  `LABEL` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '菜单名称',
  `ISCHILDREN` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '是否有子',
  `PATH` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '路由地址',
  `ICON` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '图标名称',
  `PARENTID` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '父地址ID',
  `DELFLAG` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '0' COMMENT '删除标志(1删除0使用)',
  `MODULE_ID` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '模块id',
  `MENU_LV` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '菜单等级',
  `CREATE_TIME` datetime NULL DEFAULT NULL,
  `CREATE_USER` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `UPDATE_TIME` datetime NULL DEFAULT NULL,
  `UPDATE_USER` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `SORT` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '排序号',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO `menu` VALUES ('10', '系统管理', 'true', '/', 'Menu', '0', '0', '1', '1', NULL, NULL, NULL, NULL, '1');
INSERT INTO `menu` VALUES ('11', '权限管理', 'false', '/', ' ', '10', '0', '1', '2', NULL, NULL, '2024-10-23 13:46:51', '99999999', '2');
INSERT INTO `menu` VALUES ('12', '基础信息维护', 'true', '/', ' ', '10', '0', '1', '2', NULL, NULL, '2024-10-26 15:18:29', '99999999', '1');
INSERT INTO `menu` VALUES ('16', '基础字典', 'false', '/dictionary', ' ', '12', '0', '1', '3', NULL, NULL, '2024-10-24 16:43:38', '99999999', '2');
INSERT INTO `menu` VALUES ('1871762163260674049', '测试新增', NULL, '/', NULL, '31', '1', '2', '2', '2024-12-25 11:37:30', NULL, NULL, NULL, '1');
INSERT INTO `menu` VALUES ('1871765083570024449', '测序新增', NULL, '/', NULL, '31', '1', '2', '2', '2024-12-25 11:49:06', NULL, NULL, NULL, '01');
INSERT INTO `menu` VALUES ('1871777702381072385', '测试', NULL, '/', NULL, NULL, '1', '1', '1', '2024-12-25 12:39:14', NULL, NULL, NULL, '2');
INSERT INTO `menu` VALUES ('1871778364263215105', '测试', NULL, '/', NULL, NULL, '0', '2', '1', '2024-12-25 12:41:52', NULL, NULL, NULL, '2');
INSERT INTO `menu` VALUES ('1874973465353314305', '规则库', NULL, '/', NULL, NULL, '0', '3', '1', '2025-01-03 08:18:04', '无', NULL, NULL, '4');
INSERT INTO `menu` VALUES ('1874973527278018561', '规则目录', NULL, '/', NULL, '1874973465353314305', '0', '3', '2', '2025-01-03 08:18:18', '无', NULL, NULL, '1');
INSERT INTO `menu` VALUES ('1874973569057480705', '规则维护', NULL, '/Ybgz', NULL, '1874973527278018561', '0', '3', '3', '2025-01-03 08:18:28', '无', '2025-01-03 14:56:33', '无', '1');
INSERT INTO `menu` VALUES ('1877283910789623810', '规则分析', NULL, '/Ybgzfx', NULL, '1874973465353314305', '0', '3', '2', '2025-01-09 17:18:57', '无', '2025-01-09 17:19:40', '无', '2');
INSERT INTO `menu` VALUES ('1896726385518133250', '规则分析_新', NULL, '/Ybgzfx_new', NULL, '1874973465353314305', '0', '3', '2', '2025-03-04 08:56:24', '无', '2025-03-04 08:56:38', '无', '3');
INSERT INTO `menu` VALUES ('1909030694978494465', '日志记录', NULL, '/', NULL, NULL, '1', '2', '1', '2025-04-07 07:49:20', '无', NULL, NULL, '5');
INSERT INTO `menu` VALUES ('1909030881373364225', '日志记录', NULL, '/logSave', NULL, '1909030694978494465', '1', '2', '2', '2025-04-07 07:50:04', '无', NULL, NULL, '01');
INSERT INTO `menu` VALUES ('1914149342088589313', '工具', NULL, '/', NULL, NULL, '0', '2', '1', '2025-04-21 10:49:00', '无', NULL, NULL, '6');
INSERT INTO `menu` VALUES ('1914149454294609922', '日志记录', NULL, '/logSave', NULL, '1914149342088589313', '1', '2', '2', '2025-04-21 10:49:27', '无', NULL, NULL, '01');
INSERT INTO `menu` VALUES ('1914150082433576962', '工具菜单', NULL, '/', NULL, '1914149342088589313', '0', '2', '2', '2025-04-21 10:51:57', '无', NULL, NULL, NULL);
INSERT INTO `menu` VALUES ('1914150227208368130', '日志记录', NULL, '/logSave', NULL, '1914150082433576962', '0', '2', '3', '2025-04-21 10:52:32', '无', NULL, NULL, '01');
INSERT INTO `menu` VALUES ('1914150317775974401', 'ftpHis处理', NULL, '/ftpHis', NULL, '1914150082433576962', '0', '2', '3', '2025-04-21 10:52:53', '无', NULL, NULL, '02');
INSERT INTO `menu` VALUES ('1914823876773015553', '接口', NULL, '/', NULL, NULL, '0', '2', '1', '2025-04-23 07:29:22', '无', NULL, NULL, '7');
INSERT INTO `menu` VALUES ('1914823949238005762', '数据采集', NULL, '/', NULL, '1914823876773015553', '0', '2', '2', '2025-04-23 07:29:39', '无', NULL, NULL, '1');
INSERT INTO `menu` VALUES ('1914824028279664641', '数据采集', NULL, '/dataCollection', NULL, '1914823949238005762', '0', '2', '3', '2025-04-23 07:29:58', '无', NULL, NULL, '1');
INSERT INTO `menu` VALUES ('1918140203529175042', 'pdf处理', NULL, '/pdf', NULL, '1914150082433576962', '0', '2', '3', '2025-05-02 11:07:16', '无', NULL, NULL, '3');
INSERT INTO `menu` VALUES ('1923971484141862913', 'HfMirror模型下载', NULL, '/HfMirrorMai', NULL, '1914150082433576962', '0', '2', '3', '2025-05-18 13:18:42', '无', '2025-05-18 13:19:56', '无', '4');
INSERT INTO `menu` VALUES ('1933120102038474754', '聊天', NULL, '/Chat', NULL, '1914150082433576962', '0', '2', '3', '2025-06-12 19:12:02', '无', NULL, NULL, '5');
INSERT INTO `menu` VALUES ('1938469241065816065', '高压氧', NULL, '/', NULL, NULL, '0', '2', '1', '2025-06-27 13:27:36', '无', NULL, NULL, '8');
INSERT INTO `menu` VALUES ('1938469360288907265', '氧舱维护', NULL, '/GyyCw', NULL, '1938469241065816065', '1', '2', '2', '2025-06-27 13:28:05', '无', NULL, NULL, '1');
INSERT INTO `menu` VALUES ('1938469411765600257', '高压氧安排', NULL, '/Gyy', NULL, '1938469241065816065', '1', '2', '2', '2025-06-27 13:28:17', '无', NULL, NULL, '2');
INSERT INTO `menu` VALUES ('1938984545222553601', '高压氧', NULL, '/', NULL, '1938469241065816065', '0', '2', '2', '2025-06-28 23:35:14', '无', NULL, NULL, '1');
INSERT INTO `menu` VALUES ('1938984605557616641', '氧舱维护', NULL, '/GyyCw', NULL, '1938984545222553601', '0', '2', '3', '2025-06-28 23:35:29', '无', NULL, NULL, '1');
INSERT INTO `menu` VALUES ('1938984677959692290', '高压氧安排', NULL, '/Gyy', NULL, '1938984545222553601', '0', '2', '3', '2025-06-28 23:35:46', '无', NULL, NULL, '2');
INSERT INTO `menu` VALUES ('1954862102983680002', '数据同步', NULL, '/', NULL, '1914823876773015553', '0', '2', '2', '2025-08-11 19:07:00', '无', NULL, NULL, '2');
INSERT INTO `menu` VALUES ('1954862786114166786', '数据源维护', NULL, '/DynamicDataSource', NULL, '1954862102983680002', '0', '2', '3', '2025-08-11 19:09:42', '无', NULL, NULL, '1');
INSERT INTO `menu` VALUES ('1955498076738625537', '任务数据维护', NULL, '/Scheduled', NULL, '1954862102983680002', '0', '2', '3', '2025-08-13 13:14:08', '无', NULL, NULL, '2');
INSERT INTO `menu` VALUES ('27', '菜单维护', 'false', '/menuAdd', ' ', '12', '0', '1', '3', NULL, NULL, '2024-10-28 17:21:47', '99999999', '1');
INSERT INTO `menu` VALUES ('28', '权限分配', 'true', '/menuMaintenance', ' ', '11', '0', '1', '3', NULL, NULL, '2024-10-23 11:55:54', '99999999', '1');
INSERT INTO `menu` VALUES ('29', '收费项目', 'true', '/', ' ', '10', '0', '1', '2', NULL, NULL, '2024-10-28 17:22:37', '99999999', '3');
INSERT INTO `menu` VALUES ('30', '收费项目维护', 'true', '/dictPaid/dictPaidItem', ' ', '29', '0', '1', '3', NULL, NULL, '2024-11-02 11:03:10', '99999999', '1');
INSERT INTO `menu` VALUES ('31', '人员管理', 'true', '/', ' ', '0', '1', '2', '1', NULL, NULL, '2024-12-25 11:48:53', NULL, '02');
INSERT INTO `menu` VALUES ('32', '收费类型维护', 'true', '/dictPaid/dictPaidSflx', ' ', '29', '0', '1', '3', NULL, NULL, '2024-11-03 12:06:37', '99999999', '2');
INSERT INTO `menu` VALUES ('33', '其他测试', 'true', '/', ' ', '0', '0', '2', '1', NULL, NULL, '2024-12-25 11:47:49', NULL, '01');
INSERT INTO `menu` VALUES ('34', '收费项目', 'true', '/', ' ', '33', '0', '2', '2', NULL, NULL, '2024-12-25 11:50:02', NULL, '1');
INSERT INTO `menu` VALUES ('35', '收费项目维护', 'true', '/dictPaid/dictPaidItem', ' ', '34', '0', '2', '3', NULL, NULL, '2024-12-25 11:50:06', NULL, '1');
INSERT INTO `menu` VALUES ('36', '员工信息维护', 'true', '/DictUser', ' ', '12', '0', '1', '3', NULL, NULL, '2024-11-14 13:45:57', '99999999', '3');
INSERT INTO `menu` VALUES ('37', '药品信息', 'true', '/', ' ', '10', '0', '1', '2', NULL, NULL, '2024-12-25 11:49:31', NULL, '4');
INSERT INTO `menu` VALUES ('38', '药品信息维护', 'true', '/DrugInfo', ' ', '37', '0', '1', '3', NULL, NULL, '2024-12-02 10:12:04', '99999999', '1');
INSERT INTO `menu` VALUES ('39', '药品分类维护', 'true', '/DrugInfoClass', ' ', '37', '0', '1', '3', NULL, NULL, '2024-12-25 11:49:49', NULL, '2');

-- ----------------------------
-- Table structure for scheduled
-- ----------------------------
DROP TABLE IF EXISTS `scheduled`;
CREATE TABLE `scheduled`  (
  `cron_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `cron_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '任务名称',
  `cron` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '表达式',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '状态',
  `DEL_FLAG` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '删除标志(0 未删除 1 删除)',
  PRIMARY KEY (`cron_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of scheduled
-- ----------------------------
INSERT INTO `scheduled` VALUES ('1', '定时器任务二', '0/6 * * * * ?', '0', '0');

-- ----------------------------
-- Table structure for scheduled_detatil
-- ----------------------------
DROP TABLE IF EXISTS `scheduled_detatil`;
CREATE TABLE `scheduled_detatil`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键',
  `zsj_mc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '主数据名称',
  `zsj_sql` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '主数据sql',
  `zsj_ks` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '主数据库标记',
  `to_mc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '对方表名称',
  `to_sql` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '对方sql',
  `to_ks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '对方数据库标记',
  `cz_lx` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '操作类型',
  `scheduled_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '任务ID',
  `DEL_FLAG` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '删除标志(0 未删除 1 删除)',
  PRIMARY KEY (`id` DESC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of scheduled_detatil
-- ----------------------------
INSERT INTO `scheduled_detatil` VALUES ('1', '测试', 'select * from scheduled', 'initMysql', '测试2', 'yin.test', 'bdhis', '', '1', '0');

-- ----------------------------
-- Table structure for sys_datasource
-- ----------------------------
DROP TABLE IF EXISTS `sys_datasource`;
CREATE TABLE `sys_datasource`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `ds_key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '数据源标识（如master、slave1、slave2）',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'JDBC连接地址',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `driver_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'com.mysql.cj.jdbc.Driver' COMMENT '驱动类',
  `is_default` tinyint NULL DEFAULT 0 COMMENT '是否默认数据源（1-是，0-否）',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态（1-启用，0-禁用）',
  `DEL_FLAG` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '删除标志(0 未删除 1 删除)',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_ds_key`(`ds_key` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '动态数据源配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_datasource
-- ----------------------------
INSERT INTO `sys_datasource` VALUES (1, 'initMysql', 'jdbc:mysql://localhost:3306/dataflowdb?useSSL=false&serverTimezone=UTC', 'root', '123456', 'com.mysql.cj.jdbc.Driver', 1, 1, '0');
INSERT INTO `sys_datasource` VALUES (2, 'bdhis', 'jdbc:oracle:thin:@127.0.0.1:1521/orcl', 'yin', '123456', 'oracle.jdbc.driver.OracleDriver', 0, 1, '0');
INSERT INTO `sys_datasource` VALUES (9, 'bdhis2', 'jdbc:oracle:thin:@127.0.0.1:1521/orcl', 'yin', '123456', 'oracle.jdbc.driver.OracleDriver', 0, 1, '0');

-- ----------------------------
-- Table structure for sys_module
-- ----------------------------
DROP TABLE IF EXISTS `sys_module`;
CREATE TABLE `sys_module`  (
  `MODULE_ID` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `MOUDLE_MC` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `MOUDLE_PATH` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `MOUDLE_SORT` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `MOUDLE_IMAGE` longblob NULL,
  `MOUDLE_IMAGE_TYPE` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `DEL_FLAG` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `CREATE_TIME` datetime NULL DEFAULT NULL,
  `CREATE_USER` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `UPDATE_TIME` datetime NULL DEFAULT NULL,
  `UPDATE_USER` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`MODULE_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_module
-- ----------------------------
INSERT INTO `sys_module` VALUES ('1', '基础信息', NULL, '1', NULL, NULL, '0', '2024-10-15 00:00:00', NULL, '2024-10-15 00:00:00', NULL);
INSERT INTO `sys_module` VALUES ('1847146007300374530', '测试新增模块11', NULL, '3', NULL, NULL, '1', '2024-10-18 13:21:41', NULL, '2024-10-18 13:25:50', NULL);
INSERT INTO `sys_module` VALUES ('2', '其他', NULL, '2', NULL, NULL, '0', '2024-10-15 00:00:00', NULL, '2024-10-15 00:00:00', NULL);
INSERT INTO `sys_module` VALUES ('3', '规则库', NULL, '4', NULL, NULL, '0', '2025-01-03 00:00:00', NULL, '2025-01-03 00:00:00', NULL);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `ID` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色ID',
  `MENU_ID` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '菜单id',
  `DEL_FLAG` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '删除标志',
  `CREATE_TIME` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `JSSM` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '角色说明',
  `CREATE_USER` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `UPDATE_USER` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `MODULE_ID` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '角色信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1846823222598426626', '0', '1', '2024-10-17 15:59:03', '2024-11-24 16:54:28', '测试修改', '99999999', '99999999', '1');
INSERT INTO `sys_role` VALUES ('1860616588842721281', '0', '1', '2024-11-24 17:28:58', '2024-11-24 17:34:18', '测试修改', '99999999', '99999999', '1');
INSERT INTO `sys_role` VALUES ('1860617984577069057', '0', '1', '2024-11-24 17:34:30', '2024-12-25 11:52:16', '其他测试', '99999999', NULL, '2');
INSERT INTO `sys_role` VALUES ('1869629317217767426', '0', '1', '2024-12-19 14:22:19', NULL, '其他新增', NULL, NULL, '2');
INSERT INTO `sys_role` VALUES ('1869630702944505858', '0', '0', '2024-12-19 14:27:50', NULL, '基础信息管理', NULL, NULL, '1');
INSERT INTO `sys_role` VALUES ('1869631035523452929', '0', '0', '2024-12-19 14:29:09', NULL, '权限信息管理', NULL, NULL, '1');
INSERT INTO `sys_role` VALUES ('1874973668412153857', '0', '0', '2025-01-03 08:18:52', NULL, '规则库操作', '无', NULL, '3');
INSERT INTO `sys_role` VALUES ('1909076426263494658', '0', '0', '2025-04-07 10:51:03', '2025-04-21 10:49:55', '工具角色', '无', '无', '2');
INSERT INTO `sys_role` VALUES ('1914824191794606082', '0', '0', '2025-04-23 07:30:37', NULL, '数据采集接口', '无', NULL, '2');
INSERT INTO `sys_role` VALUES ('1938469539532488706', '0', '0', '2025-06-27 13:28:47', NULL, '高压氧操作', '无', NULL, '2');
INSERT INTO `sys_role` VALUES ('483', '0', '0', '2024-08-29 10:12:01', '2024-08-29 10:12:01', '管理员', NULL, NULL, '1');
INSERT INTO `sys_role` VALUES ('499', '0', '1', '2024-09-01 10:44:28', '2024-09-01 10:44:28', '测试', NULL, NULL, '2');

-- ----------------------------
-- Table structure for sys_role_detail
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_detail`;
CREATE TABLE `sys_role_detail`  (
  `ID` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'id',
  `ROLE_ID` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色id',
  `DEL_FLAG` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '删除标志(0 未删除 1 删除)',
  `CREATE_TIME` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `UPDATE_TIME` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `JSSM` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '菜单说明',
  `MENU_ID` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '菜单id',
  `CREATE_USER` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
  `UPDATE_USER` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '修改人'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_detail
-- ----------------------------
INSERT INTO `sys_role_detail` VALUES ('1871394581651259394', '483', '0', '2024-12-24 11:16:51', NULL, '管理员', '39', NULL, NULL);
INSERT INTO `sys_role_detail` VALUES ('1871394581651259395', '483', '0', '2024-12-24 11:16:51', NULL, '管理员', '29', NULL, NULL);
INSERT INTO `sys_role_detail` VALUES ('1871394581651259396', '483', '0', '2024-12-24 11:16:51', NULL, '管理员', '30', NULL, NULL);
INSERT INTO `sys_role_detail` VALUES ('1871394581651259397', '483', '0', '2024-12-24 11:16:51', NULL, '管理员', '32', NULL, NULL);
INSERT INTO `sys_role_detail` VALUES ('1871394581651259398', '483', '0', '2024-12-24 11:16:51', NULL, '管理员', '11', NULL, NULL);
INSERT INTO `sys_role_detail` VALUES ('1871394581651259399', '483', '0', '2024-12-24 11:16:51', NULL, '管理员', '28', NULL, NULL);
INSERT INTO `sys_role_detail` VALUES ('1871394581651259400', '483', '0', '2024-12-24 11:16:51', NULL, '管理员', '12', NULL, NULL);
INSERT INTO `sys_role_detail` VALUES ('1871394581651259401', '483', '0', '2024-12-24 11:16:51', NULL, '管理员', '36', NULL, NULL);
INSERT INTO `sys_role_detail` VALUES ('1871394581714173953', '483', '0', '2024-12-24 11:16:51', NULL, '管理员', '27', NULL, NULL);
INSERT INTO `sys_role_detail` VALUES ('1871394581714173954', '483', '0', '2024-12-24 11:16:51', NULL, '管理员', '16', NULL, NULL);
INSERT INTO `sys_role_detail` VALUES ('1871394646176432129', '1869630702944505858', '0', '2024-12-24 11:17:07', NULL, '基础信息管理', '37', NULL, NULL);
INSERT INTO `sys_role_detail` VALUES ('1871394646176432130', '1869630702944505858', '0', '2024-12-24 11:17:07', NULL, '基础信息管理', '38', NULL, NULL);
INSERT INTO `sys_role_detail` VALUES ('1871394646176432131', '1869630702944505858', '0', '2024-12-24 11:17:07', NULL, '基础信息管理', '39', NULL, NULL);
INSERT INTO `sys_role_detail` VALUES ('1871394646176432132', '1869630702944505858', '0', '2024-12-24 11:17:07', NULL, '基础信息管理', '29', NULL, NULL);
INSERT INTO `sys_role_detail` VALUES ('1871394646176432133', '1869630702944505858', '0', '2024-12-24 11:17:07', NULL, '基础信息管理', '30', NULL, NULL);
INSERT INTO `sys_role_detail` VALUES ('1871394646176432134', '1869630702944505858', '0', '2024-12-24 11:17:07', NULL, '基础信息管理', '32', NULL, NULL);
INSERT INTO `sys_role_detail` VALUES ('1871394646176432135', '1869630702944505858', '0', '2024-12-24 11:17:07', NULL, '基础信息管理', '12', NULL, NULL);
INSERT INTO `sys_role_detail` VALUES ('1871394646176432136', '1869630702944505858', '0', '2024-12-24 11:17:07', NULL, '基础信息管理', '36', NULL, NULL);
INSERT INTO `sys_role_detail` VALUES ('1871394646176432137', '1869630702944505858', '0', '2024-12-24 11:17:07', NULL, '基础信息管理', '27', NULL, NULL);
INSERT INTO `sys_role_detail` VALUES ('1871394581475098626', '483', '0', '2024-12-24 11:16:51', NULL, '管理员', '10', NULL, NULL);
INSERT INTO `sys_role_detail` VALUES ('1871394581588344834', '483', '0', '2024-12-24 11:16:51', NULL, '管理员', '37', NULL, NULL);
INSERT INTO `sys_role_detail` VALUES ('1871394581588344835', '483', '0', '2024-12-24 11:16:51', NULL, '管理员', '38', NULL, NULL);
INSERT INTO `sys_role_detail` VALUES ('1871394646176432138', '1869630702944505858', '0', '2024-12-24 11:17:07', NULL, '基础信息管理', '16', NULL, NULL);
INSERT INTO `sys_role_detail` VALUES ('1871394646176432139', '1869630702944505858', '0', '2024-12-24 11:17:07', NULL, '基础信息管理', '10', NULL, NULL);
INSERT INTO `sys_role_detail` VALUES ('1875073998839750659', '1869631035523452929', '0', '2025-01-03 14:57:33', NULL, '权限信息管理', '12', '无', NULL);
INSERT INTO `sys_role_detail` VALUES ('1875073998839750660', '1869631035523452929', '0', '2025-01-03 14:57:33', NULL, '权限信息管理', '27', '无', NULL);
INSERT INTO `sys_role_detail` VALUES ('1875073998839750661', '1869631035523452929', '0', '2025-01-03 14:57:33', NULL, '权限信息管理', '36', '无', NULL);
INSERT INTO `sys_role_detail` VALUES ('1871765712396857348', '1860617984577069057', '0', '2024-12-25 11:51:36', NULL, '其他修改', '35', NULL, NULL);
INSERT INTO `sys_role_detail` VALUES ('1871765712396857349', '1860617984577069057', '0', '2024-12-25 11:51:36', NULL, '其他修改', '1871765083570024449', NULL, NULL);
INSERT INTO `sys_role_detail` VALUES ('1896732331543838723', '1874973668412153857', '0', '2025-03-04 09:20:02', NULL, '规则库操作', '1896726385518133250', '无', NULL);
INSERT INTO `sys_role_detail` VALUES ('1933120146716200963', '1909076426263494658', '0', '2025-06-12 19:12:13', NULL, '工具角色', '1914150317775974401', '无', NULL);
INSERT INTO `sys_role_detail` VALUES ('1871765712396857345', '1860617984577069057', '0', '2024-12-25 11:51:36', NULL, '其他修改', '31', NULL, NULL);
INSERT INTO `sys_role_detail` VALUES ('1871765712396857346', '1860617984577069057', '0', '2024-12-25 11:51:36', NULL, '其他修改', '33', NULL, NULL);
INSERT INTO `sys_role_detail` VALUES ('1871765712396857347', '1860617984577069057', '0', '2024-12-25 11:51:36', NULL, '其他修改', '34', NULL, NULL);
INSERT INTO `sys_role_detail` VALUES ('1896732331476729858', '1874973668412153857', '0', '2025-03-04 09:20:02', NULL, '规则库操作', '1874973465353314305', '无', NULL);
INSERT INTO `sys_role_detail` VALUES ('1875073998776836098', '1869631035523452929', '0', '2025-01-03 14:57:33', NULL, '权限信息管理', '10', '无', NULL);
INSERT INTO `sys_role_detail` VALUES ('1875073998776836099', '1869631035523452929', '0', '2025-01-03 14:57:33', NULL, '权限信息管理', '11', '无', NULL);
INSERT INTO `sys_role_detail` VALUES ('1875073998839750658', '1869631035523452929', '0', '2025-01-03 14:57:33', NULL, '权限信息管理', '28', '无', NULL);
INSERT INTO `sys_role_detail` VALUES ('1896732331543838721', '1874973668412153857', '0', '2025-03-04 09:20:02', NULL, '规则库操作', '1874973527278018561', '无', NULL);
INSERT INTO `sys_role_detail` VALUES ('1889104462651056129', '499', '0', '2025-02-11 08:09:36', NULL, '测试', '1871778364263215105', '无', NULL);
INSERT INTO `sys_role_detail` VALUES ('1933120146716200964', '1909076426263494658', '0', '2025-06-12 19:12:13', NULL, '工具角色', '1918140203529175042', '无', NULL);
INSERT INTO `sys_role_detail` VALUES ('1933120146716200965', '1909076426263494658', '0', '2025-06-12 19:12:13', NULL, '工具角色', '1923971484141862913', '无', NULL);
INSERT INTO `sys_role_detail` VALUES ('1933120146649092098', '1909076426263494658', '0', '2025-06-12 19:12:13', NULL, '工具角色', '1914149342088589313', '无', NULL);
INSERT INTO `sys_role_detail` VALUES ('1896732331543838722', '1874973668412153857', '0', '2025-03-04 09:20:02', NULL, '规则库操作', '1874973569057480705', '无', NULL);
INSERT INTO `sys_role_detail` VALUES ('1933120146649092099', '1909076426263494658', '0', '2025-06-12 19:12:13', NULL, '工具角色', '1914150082433576962', '无', NULL);
INSERT INTO `sys_role_detail` VALUES ('1933120146716200966', '1909076426263494658', '0', '2025-06-12 19:12:13', NULL, '工具角色', '1933120102038474754', '无', NULL);
INSERT INTO `sys_role_detail` VALUES ('1938984751083188229', '1938469539532488706', '0', '2025-06-28 23:36:03', NULL, '高压氧操作', '1938984677959692290', '无', NULL);
INSERT INTO `sys_role_detail` VALUES ('1933120146716200962', '1909076426263494658', '0', '2025-06-12 19:12:13', NULL, '工具角色', '1914150227208368130', '无', NULL);
INSERT INTO `sys_role_detail` VALUES ('1938984751083188226', '1938469539532488706', '0', '2025-06-28 23:36:03', NULL, '高压氧操作', '1938469241065816065', '无', NULL);
INSERT INTO `sys_role_detail` VALUES ('1938984751083188227', '1938469539532488706', '0', '2025-06-28 23:36:03', NULL, '高压氧操作', '1938984545222553601', '无', NULL);
INSERT INTO `sys_role_detail` VALUES ('1938984751083188228', '1938469539532488706', '0', '2025-06-28 23:36:03', NULL, '高压氧操作', '1938984605557616641', '无', NULL);
INSERT INTO `sys_role_detail` VALUES ('1955498115418497025', '1914824191794606082', '0', '2025-08-13 13:14:17', NULL, '数据采集接口', '1914823876773015553', '无', NULL);
INSERT INTO `sys_role_detail` VALUES ('1955498115481411586', '1914824191794606082', '0', '2025-08-13 13:14:17', NULL, '数据采集接口', '1914823949238005762', '无', NULL);
INSERT INTO `sys_role_detail` VALUES ('1955498115481411587', '1914824191794606082', '0', '2025-08-13 13:14:17', NULL, '数据采集接口', '1914824028279664641', '无', NULL);
INSERT INTO `sys_role_detail` VALUES ('1955498115481411588', '1914824191794606082', '0', '2025-08-13 13:14:17', NULL, '数据采集接口', '1954862102983680002', '无', NULL);
INSERT INTO `sys_role_detail` VALUES ('1955498115481411589', '1914824191794606082', '0', '2025-08-13 13:14:17', NULL, '数据采集接口', '1954862786114166786', '无', NULL);
INSERT INTO `sys_role_detail` VALUES ('1955498115481411590', '1914824191794606082', '0', '2025-08-13 13:14:17', NULL, '数据采集接口', '1955498076738625537', '无', NULL);

SET FOREIGN_KEY_CHECKS = 1;
