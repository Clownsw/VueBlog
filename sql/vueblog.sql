/*
 Navicat Premium Data Transfer

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 50729
 Source Host           : localhost:3306
 Source Schema         : vueblog

 Target Server Type    : MySQL
 Target Server Version : 50729
 File Encoding         : 65001

<<<<<<< HEAD
 Date: 30/05/2022 23:49:10
=======
 Date: 02/06/2022 16:34:17
>>>>>>> dev
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
  `created` datetime NOT NULL,
  `status` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of m_blog
-- ----------------------------
INSERT INTO `m_blog` VALUES (15, 1, 3, 'Hello,World', 'Hello,World', 'Hello,World', '2022-04-17 10:55:09', 0);
<<<<<<< HEAD
INSERT INTO `m_blog` VALUES (17, 1, 7, '[2022/3/11更新]某联盟换肤工具汉化版-持续更新中...', '某联盟换肤工具汉化版-持续更新中...', '> 本软件仅用于学习，请勿用于非法用途\n> 解释权归本网站所有\n\n# 公告\n## 官方已支持中国服务器\n[R3nzTheCodeGOD](https://github.com/R3nzTheCodeGOD/R3nzSkin/releases/)\n\n## 使用说明\n1. 请勿使用WeGame启动\n2. 进入泉水后注入\n3. 只支持Win7-Win10 2004(及其以下版本)\n\n## 下载地址\n[注入器](https://smlie.lanzoui.com/ilrRsilrtkf)', '2022-05-30 15:30:07', 0);
INSERT INTO `m_blog` VALUES (18, 1, 1, 'Java快速、小巧、简单易用网络请求库', 'Java快速、小巧、简单易用网络请求库', '# 一个Java快速、小巧、简单易用网络请求库 - req_java\n\n## 优点\n- 快速的\n- 小巧的\n- 简单易用的\n\n# 下载\n#### 开源地址: https://github.com/Clownsw/req_java/\n#### 下载地址: https://github.com/Clownsw/req_java/releases', '2022-05-30 15:33:09', 0);
INSERT INTO `m_blog` VALUES (19, 1, 8, '使用JetBrains全家通在Linux的问题', '使用JetBrains全家通在Linux的问题', '# 起因\n这两天因把我的工作环境迁移到了Linux平台， 刚开始使用没有任何问题, 使用一段时间后会JetBrains全家通皆无法使用中文输入法, 我尝试从Ubuntu -> Centos 但无果\n# 临时解决方案\n1. 点击菜单 \"Help | Edit Custom VM options...\"\n2. 添加 -Drecreate.x11.input.method=true 到最后一行\n3. 重启IDEA', '2022-05-30 15:35:17', 0);
INSERT INTO `m_blog` VALUES (20, 1, 1, '开源：企业微信及时通讯api！server酱的替代品！', '企业微信及时通讯api！server酱的替代品！', '# 简述\n由于server酱被举报后，只能通过卡片消息条转显示内容，**内容呈现方式不直观，程序员都是懒惰的，多点一下我都不愿意**。\n\n以下是调用的企业微信官方api开发的，所以无需担心安全问题。\n\n# 用到的技术及实现过程\n### 技术\n1. Java\n2. libhv\n3. jackson\n4. lombok\n\n### 实现过程\n利用企业微信自建应用，调用官方api，绑定普通微信，实现即时通讯！\n1. 获取access_token: [https://work.weixin.qq.com/api/doc/90000/90135/91039](https://work.weixin.qq.com/api/doc/90000/90135/91039)\n2. 发布应用消息: [https://work.weixin.qq.com/api/doc/90000/90135/90236](https://work.weixin.qq.com/api/doc/90000/90135/90236)\n\n### 特点\n每个人一个秘钥，无需担心被举报。即时通讯，和好友给你发消息一样。\n\n# 开发过程\n### 项目结构\nApplication.java\n```Java\npackage cn.smilex;\n\nimport cn.smilex.libhv.jni.http.HttpRequest;\nimport cn.smilex.libhv.jni.http.HttpResponse;\nimport cn.smilex.libhv.jni.http.Requests;\nimport com.fasterxml.jackson.core.JsonProcessingException;\nimport com.fasterxml.jackson.databind.JsonNode;\nimport com.fasterxml.jackson.databind.ObjectMapper;\nimport com.fasterxml.jackson.databind.node.ObjectNode;\n\nimport java.io.FileOutputStream;\nimport java.io.IOException;\nimport java.io.InputStream;\nimport java.text.SimpleDateFormat;\nimport java.util.*;\n\n/**\n * @author smilex\n */\npublic class Application {\n\n    /**\n     * 加载配置文件\n     * @return config\n     */\n    public static Properties loadConfig() {\n        InputStream configStream = null;\n        try {\n            configStream = Application.class.getResourceAsStream(\"/config.properties\");\n            Properties config = new Properties();\n            config.load(configStream);\n            return config;\n        } catch (Exception e) {\n            throw new RuntimeException(e);\n        } finally {\n            if (configStream != null) {\n                try {\n                    configStream.close();\n                } catch (IOException e) {\n                    e.printStackTrace();\n                }\n            }\n        }\n    }\n\n    /**\n     * 验证token是否有效\n     * @param token token\n     * @return true = 有效, false = 无效\n     */\n    public static boolean isEffective(String token) {\n        HttpResponse response = Requests.getRequests().get(\"https://open.work.weixin.qq.com/devtool/getInfoByAccessToken?access_token=\" + token);\n        if (response == null) {\n            return false;\n        } else {\n            ObjectMapper objectMapper = new ObjectMapper();\n            try {\n                JsonNode root = objectMapper.readTree(response.getBody());\n                JsonNode jsonNode = root.get(\"result\");\n                return jsonNode == null;\n            } catch (JsonProcessingException e) {\n                throw new RuntimeException(e);\n            }\n        }\n    }\n\n    /**\n     * 获取token\n     * @return token\n     */\n    public static String getToken() throws JsonProcessingException {\n        String corpid = \"\";			// 请参照access_token文档\n        String corpsecret = \"\";			// 请参照access_token文档\n        String api1 = \"https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=\" + corpid + \"&corpsecret=\" + corpsecret;\n        HttpResponse response = Requests.getRequests().get(api1);\n\n        if (response != null) {\n            ObjectMapper mapper = new ObjectMapper();\n            JsonNode root = mapper.readTree(response.getBody());\n            return root.get(\"access_token\").asText();\n        }\n        return null;\n    }\n\n    /**\n     * 创建一个卡片模版的json数据\n     * @param agentid 企业id\n     * @param msg 信息对象\n     */\n    public static String createCardText(Integer agentid, TextCard msg, Map<Integer, String> values) throws JsonProcessingException {\n        ObjectMapper objectMapper = new ObjectMapper();\n        ObjectNode objectNode = objectMapper.createObjectNode();\n\n        int i = 1;\n        Set<Integer> keys = values.keySet();\n        Iterator<Integer> it = keys.iterator();\n        while (it.hasNext()) {\n            msg.setDescription(msg.getDescription().replace(\"{\" + i++ + \"}\", values.get(it.next())));\n        }\n\n        objectNode.put(\"touser\", \"@all\");\n        objectNode.put(\"msgtype\", \"markdown\");\n        objectNode.put(\"agentid\", agentid);\n\n        ObjectNode textcard = objectNode.putObject(\"markdown\");\n        textcard.put(\"content\", msg.getDescription());\n        textcard.put(\"url\", msg.getUrl());\n\n        objectNode.set(\"markdown\", textcard);\n\n        objectNode.put(\"enable_duplicate_check\", 0);\n        objectNode.put(\"duplicate_check_interval\", 1800);\n\n        return objectMapper.writeValueAsString(objectNode);\n    }\n\n    public static void main(String[] args) throws IOException {\n\n        Properties config = loadConfig();\n\n        Integer agentid = 0;		// 企业ID\n        String token = (String) config.get(\"token\");\n\n        if (!isEffective(token)) {\n            token = getToken();\n            config.setProperty(\"token\", token);\n            config.store(new FileOutputStream(\"config.properties\"), \"config\");\n        }\n\n        Date date = new Date(System.currentTimeMillis());\n\n        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(\"yyyy:MM:dd hh:mm:ss\");\n\n        TextCard textCard = new TextCard();\n\n        /**\n         * \"您的会议室已经预定，稍后会同步到`邮箱` \\n\" +\n         *                 \"                                >**事项详情** \\n\" +\n         *                 \"                                >事　项：<font color=\\\\\\\"info\\\\\\\">开会</font> \\n\" +\n         *                 \"                                >组织者：@miglioguan \\n\" +\n         *                 \"                                >参与者：@miglioguan、@kunliu、@jamdeezhou、@kanexiong、@kisonwang \\n\" +\n         *                 \"                                > \\n\" +\n         *                 \"                                >会议室：<font color=\\\\\\\"info\\\\\\\">广州TIT 1楼 301</font> \\n\" +\n         *                 \"                                >日　期：<font color=\\\\\\\"warning\\\\\\\">2018年5月18日</font> \\n\" +\n         *                 \"                                >时　间：<font color=\\\\\\\"comment\\\\\\\">上午9:00-11:00</font> \\n\" +\n         *                 \"                                > \\n\" +\n         *                 \"                                >请准时参加会议。 \\n\" +\n         *                 \"                                > \\n\" +\n         *                 \"                                >如需修改会议信息，请点击：[修改会议信息](https://work.weixin.qq.com)\"\n         */\n        Map<Integer, String> values = new HashMap<>();\n        values.put(1, \"天气通知\");\n        values.put(2, \"天气情况\");\n        values.put(3, \"天气报告\");\n        values.put(4, \"北京\");\n\n//        values.put(3, simpleDateFormat.format(date));\n        textCard.setDescription(\"`{1}`\\n\" +\n                \"                                >**{2}** \\n\" +\n                \"                                >事　项：<font color=\\\\\\\"info\\\\\\\">{3}</font> \\n\" +\n                \"                                >区  域：<font color=\\\\\\\"info\\\\\\\"> {4}</font> \\n\" +\n                \"                                >日　期：<font color=\\\\\\\"warning\\\\\\\">2018年5月18日</font> \\n\" +\n                \"                                >时　间：<font color=\\\\\\\"comment\\\\\\\">上午9:00-11:00</font> \\n\" +\n                \"                                > \\n\" +\n                \"                                >请准时参加会议。 \\n\" +\n                \"                                > \\n\");\n        textCard.setUrl(\"https://www.smilex.cn/\");\n\n        String msg = createCardText(agentid, textCard, values);\n\n        HttpRequest request = HttpRequest\n                .build()\n                .setUrl(\"https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=\" + token)\n                .setMethod(HttpRequest.HTTP_METHOD.HTTP_METHOD_POST.id)\n                .setBody(msg);\n\n        HttpResponse response = Requests.getRequests().request(request);\n        if (response != null) {\n            System.out.println(response.getBody());\n        }\n    }\n}\n\n```\n\nTextCard.java\n```Java\npackage cn.smilex;\n\nimport lombok.AllArgsConstructor;\nimport lombok.Data;\n\n/**\n * @author smilex\n */\n@Data\n@AllArgsConstructor\npublic class TextCard {\n\n    private String description;\n    private String url;\n\n    public TextCard() { }\n}\n```\n\nconfig.properties\n```Java\ntoken=你的access_token\n```\n\n# 参考文献\n1. https://htm.fun/archives/43.html\n2. https://work.weixin.qq.com/api/doc/90000/90135/91039\n3. https://work.weixin.qq.com/api/doc/90000/90135/90236', '2022-05-30 15:37:04', 0);
=======
INSERT INTO `m_blog` VALUES (17, 1, 10, '[2022/3/11更新]某联盟换肤工具汉化版-持续更新中...', '某联盟换肤工具汉化版-持续更新中...', '> 本软件仅用于学习，请勿用于非法用途\n> 解释权归本网站所有\n\n# 公告\n## 官方已支持中国服务器\n[R3nzTheCodeGOD](https://github.com/R3nzTheCodeGOD/R3nzSkin/releases/)\n\n## 使用说明\n1. 请勿使用WeGame启动\n2. 进入泉水后注入\n3. 只支持Win7-Win10 2004(及其以下版本)\n\n## 下载地址\n[注入器](https://smlie.lanzoui.com/ilrRsilrtkf)', '2022-05-30 15:30:07', 0);
INSERT INTO `m_blog` VALUES (18, 1, 1, 'Java快速、小巧、简单易用网络请求库', 'Java快速、小巧、简单易用网络请求库', '# 一个Java快速、小巧、简单易用网络请求库 - req_java\n\n## 优点\n- 快速的\n- 小巧的\n- 简单易用的\n\n# 下载\n#### 开源地址: https://github.com/Clownsw/req_java/\n#### 下载地址: https://github.com/Clownsw/req_java/releases', '2022-05-30 15:33:09', 0);
INSERT INTO `m_blog` VALUES (19, 1, 8, '使用JetBrains全家通在Linux的问题', '使用JetBrains全家通在Linux的问题', '# 起因\n这两天因把我的工作环境迁移到了Linux平台， 刚开始使用没有任何问题, 使用一段时间后会JetBrains全家通皆无法使用中文输入法, 我尝试从Ubuntu -> Centos 但无果\n# 临时解决方案\n1. 点击菜单 \"Help | Edit Custom VM options...\"\n2. 添加 -Drecreate.x11.input.method=true 到最后一行\n3. 重启IDEA', '2022-05-30 15:35:17', 0);
INSERT INTO `m_blog` VALUES (20, 1, 9, '开源：企业微信及时通讯api！server酱的替代品！', '企业微信及时通讯api！server酱的替代品！', '# 简述\n由于server酱被举报后，只能通过卡片消息条转显示内容，**内容呈现方式不直观，程序员都是懒惰的，多点一下我都不愿意**。\n\n以下是调用的企业微信官方api开发的，所以无需担心安全问题。\n\n# 用到的技术及实现过程\n### 技术\n1. Java\n2. libhv\n3. jackson\n4. lombok\n\n### 实现过程\n利用企业微信自建应用，调用官方api，绑定普通微信，实现即时通讯！\n1. 获取access_token: [https://work.weixin.qq.com/api/doc/90000/90135/91039](https://work.weixin.qq.com/api/doc/90000/90135/91039)\n2. 发布应用消息: [https://work.weixin.qq.com/api/doc/90000/90135/90236](https://work.weixin.qq.com/api/doc/90000/90135/90236)\n\n### 特点\n每个人一个秘钥，无需担心被举报。即时通讯，和好友给你发消息一样。\n\n# 开发过程\n### 项目结构\nApplication.java\n```Java\npackage cn.smilex;\n\nimport cn.smilex.libhv.jni.http.HttpRequest;\nimport cn.smilex.libhv.jni.http.HttpResponse;\nimport cn.smilex.libhv.jni.http.Requests;\nimport com.fasterxml.jackson.core.JsonProcessingException;\nimport com.fasterxml.jackson.databind.JsonNode;\nimport com.fasterxml.jackson.databind.ObjectMapper;\nimport com.fasterxml.jackson.databind.node.ObjectNode;\n\nimport java.io.FileOutputStream;\nimport java.io.IOException;\nimport java.io.InputStream;\nimport java.text.SimpleDateFormat;\nimport java.util.*;\n\n/**\n * @author smilex\n */\npublic class Application {\n\n    /**\n     * 加载配置文件\n     * @return config\n     */\n    public static Properties loadConfig() {\n        InputStream configStream = null;\n        try {\n            configStream = Application.class.getResourceAsStream(\"/config.properties\");\n            Properties config = new Properties();\n            config.load(configStream);\n            return config;\n        } catch (Exception e) {\n            throw new RuntimeException(e);\n        } finally {\n            if (configStream != null) {\n                try {\n                    configStream.close();\n                } catch (IOException e) {\n                    e.printStackTrace();\n                }\n            }\n        }\n    }\n\n    /**\n     * 验证token是否有效\n     * @param token token\n     * @return true = 有效, false = 无效\n     */\n    public static boolean isEffective(String token) {\n        HttpResponse response = Requests.getRequests().get(\"https://open.work.weixin.qq.com/devtool/getInfoByAccessToken?access_token=\" + token);\n        if (response == null) {\n            return false;\n        } else {\n            ObjectMapper objectMapper = new ObjectMapper();\n            try {\n                JsonNode root = objectMapper.readTree(response.getBody());\n                JsonNode jsonNode = root.get(\"result\");\n                return jsonNode == null;\n            } catch (JsonProcessingException e) {\n                throw new RuntimeException(e);\n            }\n        }\n    }\n\n    /**\n     * 获取token\n     * @return token\n     */\n    public static String getToken() throws JsonProcessingException {\n        String corpid = \"\";			// 请参照access_token文档\n        String corpsecret = \"\";			// 请参照access_token文档\n        String api1 = \"https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=\" + corpid + \"&corpsecret=\" + corpsecret;\n        HttpResponse response = Requests.getRequests().get(api1);\n\n        if (response != null) {\n            ObjectMapper mapper = new ObjectMapper();\n            JsonNode root = mapper.readTree(response.getBody());\n            return root.get(\"access_token\").asText();\n        }\n        return null;\n    }\n\n    /**\n     * 创建一个卡片模版的json数据\n     * @param agentid 企业id\n     * @param msg 信息对象\n     */\n    public static String createCardText(Integer agentid, TextCard msg, Map<Integer, String> values) throws JsonProcessingException {\n        ObjectMapper objectMapper = new ObjectMapper();\n        ObjectNode objectNode = objectMapper.createObjectNode();\n\n        int i = 1;\n        Set<Integer> keys = values.keySet();\n        Iterator<Integer> it = keys.iterator();\n        while (it.hasNext()) {\n            msg.setDescription(msg.getDescription().replace(\"{\" + i++ + \"}\", values.get(it.next())));\n        }\n\n        objectNode.put(\"touser\", \"@all\");\n        objectNode.put(\"msgtype\", \"markdown\");\n        objectNode.put(\"agentid\", agentid);\n\n        ObjectNode textcard = objectNode.putObject(\"markdown\");\n        textcard.put(\"content\", msg.getDescription());\n        textcard.put(\"url\", msg.getUrl());\n\n        objectNode.set(\"markdown\", textcard);\n\n        objectNode.put(\"enable_duplicate_check\", 0);\n        objectNode.put(\"duplicate_check_interval\", 1800);\n\n        return objectMapper.writeValueAsString(objectNode);\n    }\n\n    public static void main(String[] args) throws IOException {\n\n        Properties config = loadConfig();\n\n        Integer agentid = 0;		// 企业ID\n        String token = (String) config.get(\"token\");\n\n        if (!isEffective(token)) {\n            token = getToken();\n            config.setProperty(\"token\", token);\n            config.store(new FileOutputStream(\"config.properties\"), \"config\");\n        }\n\n        Date date = new Date(System.currentTimeMillis());\n\n        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(\"yyyy:MM:dd hh:mm:ss\");\n\n        TextCard textCard = new TextCard();\n\n        /**\n         * \"您的会议室已经预定，稍后会同步到`邮箱` \\n\" +\n         *                 \"                                >**事项详情** \\n\" +\n         *                 \"                                >事　项：<font color=\\\\\\\"info\\\\\\\">开会</font> \\n\" +\n         *                 \"                                >组织者：@miglioguan \\n\" +\n         *                 \"                                >参与者：@miglioguan、@kunliu、@jamdeezhou、@kanexiong、@kisonwang \\n\" +\n         *                 \"                                > \\n\" +\n         *                 \"                                >会议室：<font color=\\\\\\\"info\\\\\\\">广州TIT 1楼 301</font> \\n\" +\n         *                 \"                                >日　期：<font color=\\\\\\\"warning\\\\\\\">2018年5月18日</font> \\n\" +\n         *                 \"                                >时　间：<font color=\\\\\\\"comment\\\\\\\">上午9:00-11:00</font> \\n\" +\n         *                 \"                                > \\n\" +\n         *                 \"                                >请准时参加会议。 \\n\" +\n         *                 \"                                > \\n\" +\n         *                 \"                                >如需修改会议信息，请点击：[修改会议信息](https://work.weixin.qq.com)\"\n         */\n        Map<Integer, String> values = new HashMap<>();\n        values.put(1, \"天气通知\");\n        values.put(2, \"天气情况\");\n        values.put(3, \"天气报告\");\n        values.put(4, \"北京\");\n\n//        values.put(3, simpleDateFormat.format(date));\n        textCard.setDescription(\"`{1}`\\n\" +\n                \"                                >**{2}** \\n\" +\n                \"                                >事　项：<font color=\\\\\\\"info\\\\\\\">{3}</font> \\n\" +\n                \"                                >区  域：<font color=\\\\\\\"info\\\\\\\"> {4}</font> \\n\" +\n                \"                                >日　期：<font color=\\\\\\\"warning\\\\\\\">2018年5月18日</font> \\n\" +\n                \"                                >时　间：<font color=\\\\\\\"comment\\\\\\\">上午9:00-11:00</font> \\n\" +\n                \"                                > \\n\" +\n                \"                                >请准时参加会议。 \\n\" +\n                \"                                > \\n\");\n        textCard.setUrl(\"https://www.smilex.cn/\");\n\n        String msg = createCardText(agentid, textCard, values);\n\n        HttpRequest request = HttpRequest\n                .build()\n                .setUrl(\"https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=\" + token)\n                .setMethod(HttpRequest.HTTP_METHOD.HTTP_METHOD_POST.id)\n                .setBody(msg);\n\n        HttpResponse response = Requests.getRequests().request(request);\n        if (response != null) {\n            System.out.println(response.getBody());\n        }\n    }\n}\n\n```\n\nTextCard.java\n```Java\npackage cn.smilex;\n\nimport lombok.AllArgsConstructor;\nimport lombok.Data;\n\n/**\n * @author smilex\n */\n@Data\n@AllArgsConstructor\npublic class TextCard {\n\n    private String description;\n    private String url;\n\n    public TextCard() { }\n}\n```\n\nconfig.properties\n```Java\ntoken=你的access_token\n```\n\n# 参考文献\n1. https://htm.fun/archives/43.html\n2. https://work.weixin.qq.com/api/doc/90000/90135/91039\n3. https://work.weixin.qq.com/api/doc/90000/90135/90236', '2022-05-30 15:37:04', 0);
>>>>>>> dev
INSERT INTO `m_blog` VALUES (22, 1, 1, '搭建Github镜像站', '搭建Github镜像站', '> 每次使用Github的时候，尤其clone项目的时候github的速度总是非常感人，我有个香港的轻型服务器，而nginx可以反向代理，为什么我不使用nginx反向代理一个Github呢？\n\n## 操作\n- 申请域名证书\n- 安装NGINX\n- 创建一个配置文件\n\n```\nupstream github {\n    server github.com:443;\n    keepalive 16;\n}\n\nserver {\n    listen 80;\n    server_name github.guiyun.plus;\n    rewrite ^ https://$http_host$request_uri? permanent;\n}\n\nserver\n{\n    listen 443;\n    server_name github.guiyun.plus;\n    ssl_certificate   ssl/github.guiyun.plus.crt;\n    ssl_certificate_key  ssl/github.guiyun.plus.key;\n    ssl_session_timeout 5m;\n    ssl_ciphers ECDHE-RSA-AES128-GCM-SHA256:ECDHE:ECDH:AES:HIGH:!NULL:!aNULL:!MD5:!ADH:!RC4;\n    ssl_protocols TLSv1 TLSv1.1 TLSv1.2;\n    ssl_prefer_server_ciphers on;\n    if ($http_user_agent ~* \"qihoobot|Baiduspider|Googlebot|Googlebot-Mobile|Googlebot-Image|Mediapartners-Google|Adsbot-Google|Feedfetcher-Google|Yahoo! Slurp|Yahoo! Slurp China|YoudaoBot|Sosospider|Sogou spider|Sogou web spider|MSNBot|ia_archiver|Tomato Bot\") #防止搜索引擎收录\n    {\n        return 403;\n    }\n    location / {\n        proxy_set_header Accept-Encoding \"\";\n        proxy_set_header Connection \"\";\n        proxy_http_version 1.1;\n        proxy_connect_timeout    10s;\n        proxy_read_timeout       10s;\n        proxy_set_header Host github.com;\n\n        proxy_hide_header Strict-Transport-Security; #隐藏协议头，避免因为反向代理开启hsts\n\n        proxy_pass https://github;\n    }\n}\n```\n- 重启Nginx\n\n这样Github就搭建好了\n## 效果\n访问了一下感觉速度还是可以的\n![](https://image.guiyunweb.com/2021/%E5%B1%8F%E5%B9%95%E6%88%AA%E5%9B%BE%202021-08-25%20001332_1629821639595.png?imageMogr2/format/webp/interlace/1/quality/090)\n\n## 而clone的服务就非常让人惊喜了\n![](https://image.guiyunweb.com/2021/%E5%B1%8F%E5%B9%95%E6%88%AA%E5%9B%BE%202021-08-25%20000309_1629821638320.png?imageMogr2/format/webp/interlace/1/quality/090)\n\n唯一比较遗憾的就是没办法登录了，如果有什么登录的办法，还请小伙伴们告诉我\n\n[转自归云博客](https://guiyunweb.com/archives/%E6%90%AD%E5%BB%BAgithub%E9%95%9C%E5%83%8F%E7%AB%99)', '2022-05-30 15:40:43', 0);

-- ----------------------------
-- Table structure for m_blogtag
-- ----------------------------
DROP TABLE IF EXISTS `m_blogtag`;
CREATE TABLE `m_blogtag`  (
  `blogId` bigint(20) NOT NULL,
  `tagId` bigint(20) NOT NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of m_blogtag
-- ----------------------------
INSERT INTO `m_blogtag` VALUES (17, 24);
INSERT INTO `m_blogtag` VALUES (17, 25);
INSERT INTO `m_blogtag` VALUES (17, 26);
INSERT INTO `m_blogtag` VALUES (17, 27);
INSERT INTO `m_blogtag` VALUES (18, 22);
INSERT INTO `m_blogtag` VALUES (18, 28);
INSERT INTO `m_blogtag` VALUES (18, 29);
INSERT INTO `m_blogtag` VALUES (19, 30);
INSERT INTO `m_blogtag` VALUES (19, 31);
INSERT INTO `m_blogtag` VALUES (20, 22);
INSERT INTO `m_blogtag` VALUES (20, 23);
INSERT INTO `m_blogtag` VALUES (20, 32);
INSERT INTO `m_blogtag` VALUES (20, 33);
INSERT INTO `m_blogtag` VALUES (20, 34);
INSERT INTO `m_blogtag` VALUES (20, 35);
INSERT INTO `m_blogtag` VALUES (20, 22);
INSERT INTO `m_blogtag` VALUES (20, 23);
INSERT INTO `m_blogtag` VALUES (20, 32);
INSERT INTO `m_blogtag` VALUES (20, 33);
INSERT INTO `m_blogtag` VALUES (20, 34);
INSERT INTO `m_blogtag` VALUES (20, 35);
INSERT INTO `m_blogtag` VALUES (22, 36);
INSERT INTO `m_blogtag` VALUES (22, 37);

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
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of m_friend
-- ----------------------------
INSERT INTO `m_friend` VALUES (1, 'Smile\' Blog', '一个神秘的博客', 'https://blog.areyou.ml/', 'https://avatars.githubusercontent.com/u/28394742');

-- ----------------------------
-- Table structure for m_other
-- ----------------------------
DROP TABLE IF EXISTS `m_other`;
CREATE TABLE `m_other`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of m_other
-- ----------------------------
INSERT INTO `m_other` VALUES (1, '关于我', '::: hljs-center\n\n# 关于我\n\n:::\n\n\naaa\naaa\naaa\naaa\naaa\naaa\naaa\naaa\naaa\naaa\naaa');

-- ----------------------------
-- Table structure for m_sort
-- ----------------------------
DROP TABLE IF EXISTS `m_sort`;
CREATE TABLE `m_sort`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order` int(11) NOT NULL DEFAULT 0,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of m_sort
-- ----------------------------
INSERT INTO `m_sort` VALUES (1, 1, '综合');
INSERT INTO `m_sort` VALUES (3, 0, '其他');
INSERT INTO `m_sort` VALUES (7, 0, '游戏');
INSERT INTO `m_sort` VALUES (8, 0, 'Linux');
INSERT INTO `m_sort` VALUES (9, 0, 'Java');
INSERT INTO `m_sort` VALUES (10, 100, '置顶');

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
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

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
) ENGINE = InnoDB AUTO_INCREMENT = 38 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of m_tag
-- ----------------------------
INSERT INTO `m_tag` VALUES (15, '测试文章');
INSERT INTO `m_tag` VALUES (22, '原创');
INSERT INTO `m_tag` VALUES (23, '转载');
INSERT INTO `m_tag` VALUES (24, 'LOL');
INSERT INTO `m_tag` VALUES (25, '英雄联盟');
INSERT INTO `m_tag` VALUES (26, '换肤');
INSERT INTO `m_tag` VALUES (27, '动态换肤');
INSERT INTO `m_tag` VALUES (28, 'Java');
INSERT INTO `m_tag` VALUES (29, '网络请求库');
INSERT INTO `m_tag` VALUES (30, 'JetBrains');
INSERT INTO `m_tag` VALUES (31, 'Linux');
INSERT INTO `m_tag` VALUES (32, '开源');
INSERT INTO `m_tag` VALUES (33, '企业微信');
INSERT INTO `m_tag` VALUES (34, 'Server酱');
INSERT INTO `m_tag` VALUES (35, 'Api');
INSERT INTO `m_tag` VALUES (36, 'Github');
INSERT INTO `m_tag` VALUES (37, '镜像站');

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
  `status` int(5) NOT NULL,
  `created` datetime NOT NULL,
  `last_login` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of m_user
-- ----------------------------
INSERT INTO `m_user` VALUES (1, 'xiaoxiao', 'https://avatars.githubusercontent.com/u/28394742', 'msmliexx1@gmail.com', '123123', 1, '2022-03-29 21:23:55', '2022-06-01 15:09:56');
INSERT INTO `m_user` VALUES (2, 'x', 'https://avatars.githubusercontent.com/u/28394742', 'msmliexx1@gmail.com', '123123', -1, '2022-03-29 21:23:55', '2022-03-29 21:24:00');

SET FOREIGN_KEY_CHECKS = 1;
