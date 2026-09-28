

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admins
-- ----------------------------
DROP TABLE IF EXISTS `admins`;
CREATE TABLE `admins`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码（加密后）',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像URL',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `role` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'admin' COMMENT '角色：super_admin-超级管理员，admin-管理员',
  `status` int NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_username`(`username` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '管理员表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of admins
-- ----------------------------
INSERT INTO `admins` VALUES (1, 'admin', '123456', '系统管理员', NULL, '13800138000', 'admin@example.com', 'super_admin', 1, '2026-01-18 15:14:31', '2026-01-18 15:52:58');
INSERT INTO `admins` VALUES (2, 'test_admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z2EHkHwGd5Y6Rq5vJG6MqG3e', '测试管理员', NULL, '13800138001', 'test@example.com', 'admin', 1, '2026-01-18 15:14:31', '2026-01-18 15:14:31');

-- ----------------------------
-- Table structure for attractions
-- ----------------------------
DROP TABLE IF EXISTS `attractions`;
CREATE TABLE `attractions`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '景点名称',
  `category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '分类：自然景观、历史古迹、海滨度假、主题公园等',
  `cover_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '封面图片URL',
  `images` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '图片集（JSON格式）',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '景点介绍',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '地址',
  `longitude` decimal(10, 7) NULL DEFAULT NULL,
  `latitude` decimal(10, 7) NULL DEFAULT NULL,
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '联系电话',
  `open_time` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `notes` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '注意事项',
  `tags` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '标签（逗号分隔）',
  `min_price` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '最低价格',
  `rating` decimal(2, 1) NULL DEFAULT NULL COMMENT '评分',
  `view_count` int NOT NULL DEFAULT 0 COMMENT '浏览量',
  `booking_count` int NOT NULL DEFAULT 0 COMMENT '预订量',
  `status` int NOT NULL DEFAULT 1 COMMENT '状态：0-下线，1-上线',
  `sort` int NOT NULL DEFAULT 0 COMMENT '排序',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_category`(`category` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_sort`(`sort` ASC) USING BTREE,
  INDEX `idx_rating`(`rating` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '景点表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of attractions
-- ----------------------------
INSERT INTO `attractions` VALUES (1, '杭州西湖', '自然景观', 'https://ts4.tc.mm.bing.net/th/id/OIP-C.VC96IQ53w1EhF5yR442lGAHaNK?rs=1&pid=ImgDetMain&o=7&rm=3', 'https://via.placeholder.com/400x300?text=WestLake1,https://via.placeholder.com/400x300?text=WestLake2,https://via.placeholder.com/400x300?text=WestLake3', '西湖，位于中国浙江省杭州市西湖区，是中国著名的旅游景点之一。西湖以其秀丽的湖光山色和众多的名胜古迹而闻名于世，被誉为人间天堂。', '浙江省杭州市西湖区龙井路1号', 120.1489320, 30.2592440, '0571-87179617', '全天开放', '1. 请保护环境，不要乱扔垃圾；2. 注意安全，不要在湖边嬉戏；3. 旺季时请提前预订门票。', '自然,湖泊,5A景区,免费', 20.00, 4.8, 15697, 2340, 1, 1, '2025-12-19 15:14:31', '2026-01-23 20:24:59');
INSERT INTO `attractions` VALUES (2, '北京故宫', '历史古迹', 'https://www.shuomingshu.cn/wp-content/uploads/images/2022/12/02/a382daee878049f2969575e60d9f2464_vgf1x4cfjcj.jpg', 'https://via.placeholder.com/400x300?text=ForbiddenCity1,https://via.placeholder.com/400x300?text=ForbiddenCity2', '故宫，又称紫禁城，是中国明清两代的皇家宫殿，位于北京中轴线的中心，是世界上现存规模最大、保存最为完整的木质结构古建筑之一。', '北京市东城区景山前街4号', 116.3974550, 39.9180580, '010-85007421', '08:30-17:00', '1. 严禁携带易燃易爆物品；2. 请勿大声喧哗；3. 禁止在宫殿内拍照。', '历史,宫殿,5A景区,世界遗产', 60.00, 4.9, 23475, 4560, 1, 2, '2025-12-24 15:14:31', '2026-01-23 19:52:54');
INSERT INTO `attractions` VALUES (3, '黄山风景区', '自然景观', 'https://via.placeholder.com/400x300?text=Huangshan', 'https://via.placeholder.com/400x300?text=Huangshan1,https://via.placeholder.com/400x300?text=Huangshan2', '黄山，位于安徽省黄山市境内，是中国十大风景名胜唯一的山岳风光。黄山以奇松、怪石、云海、温泉、冬雪五绝著称于世。', '安徽省黄山市黄山区汤口镇', 118.1675900, 30.1319400, '0559-5580244', '06:00-18:00', '1. 山上气温较低，请携带保暖衣物；2. 雨天路滑，注意安全；3. 乘坐缆车时请遵守秩序。', '自然,山岳,5A景区,避暑', 190.00, 4.8, 18923, 3210, 1, 3, '2025-12-29 15:14:31', '2026-01-23 18:38:45');
INSERT INTO `attractions` VALUES (4, '张家界国家森林公园', '自然景观', 'https://via.placeholder.com/400x300?text=Zhangjiajie', 'https://via.placeholder.com/400x300?text=Zhangjiajie1,https://via.placeholder.com/400x300?text=Zhangjiajie2', '张家界国家森林公园，位于湖南省张家界市，是中国第一个国家森林公园，以独特的石英砂岩峰林地貌著称，是电影《阿凡达》的取景地。', '湖南省张家界市武陵源区', 110.4487200, 29.3248200, '0744-5712189', '07:00-18:00', '1. 山区气候多变，请备雨具；2. 攀登时请量力而行；3. 保护自然景观，不要攀爬树木。', '自然,森林,5A景区,奇峰', 228.00, 4.7, 16784, 2890, 1, 4, '2025-12-31 15:14:31', '2026-01-23 19:55:49');
INSERT INTO `attractions` VALUES (5, '九寨沟风景区', '自然景观', 'https://via.placeholder.com/400x300?text=Jiuzhaigou', 'https://via.placeholder.com/400x300?text=Jiuzhaigou1,https://via.placeholder.com/400x300?text=Jiuzhaigou2', '九寨沟，位于四川省阿坝藏族羌族自治州九寨沟县，以翠海、叠瀑、彩林、雪峰、藏情、蓝冰六绝著称于世，被誉为\"童话世界\"。', '四川省阿坝州九寨沟县漳扎镇', 103.9181100, 33.2055900, '0837-7739753', '07:00-18:00', '1. 高海拔地区，请注意高原反应；2. 严禁乱扔垃圾；3. 尊重当地风俗习惯。', '自然,湖泊,瀑布,5A景区', 169.00, 4.8, 14230, 1980, 1, 5, '2026-01-03 15:14:31', '2026-01-18 15:14:31');
INSERT INTO `attractions` VALUES (6, '桂林山水', '自然景观', 'https://via.placeholder.com/400x300?text=Guilin', 'https://via.placeholder.com/400x300?text=Guilin1,https://via.placeholder.com/400x300?text=Guilin2', '桂林山水，位于广西壮族自治区桂林市，是中国山水的代表。\"桂林山水甲天下\"这一千古名句，使得桂林山水名扬海内外。', '广西壮族自治区桂林市秀峰区', 110.2901000, 25.2735300, '0773-2864600', '全天开放', '1. 漓江游船请注意安全；2. 避免雨天出行；3. 保护漓江环境，不乱扔垃圾。', '自然,山水,5A景区,漓江', 210.00, 4.7, 12560, 1780, 1, 6, '2026-01-06 15:14:31', '2026-01-18 15:14:31');
INSERT INTO `attractions` VALUES (7, '苏州园林', '历史古迹', 'https://via.placeholder.com/400x300?text=Suzhou', 'https://via.placeholder.com/400x300?text=Suzhou1,https://via.placeholder.com/400x300?text=Suzhou2', '苏州园林，是中国古典园林的代表，以拙政园、留园、狮子林等最为著名。园林设计精巧，一步一景，体现了中国古典园林艺术的精髓。', '江苏省苏州市姑苏区', 120.6195850, 31.2989600, '0512-67542442', '07:30-17:30', '1. 保持安静，不要大声喧哗；2. 保护园林植被；3. 严禁在园林内吸烟。', '历史,园林,5A景区,文化遗产', 90.00, 4.6, 10892, 1450, 1, 7, '2026-01-08 15:14:31', '2026-01-23 19:55:18');
INSERT INTO `attractions` VALUES (8, '丽江古城', '历史文化', 'https://via.placeholder.com/400x300?text=Lijiang', 'https://via.placeholder.com/400x300?text=Lijiang1,https://via.placeholder.com/400x300?text=Lijiang2', '丽江古城，位于云南省丽江市，是中国历史文化名城之一。古城建筑风格独特，纳西族文化浓厚，是体验云南民族文化的绝佳去处。', '云南省丽江市古城区', 100.2330600, 26.8721400, '0888-5191888', '全天开放', '1. 古城内禁止车辆通行；2. 尊重当地纳西族文化；3. 夜间行走请注意安全。', '历史,古城,5A景区,纳西族', 0.00, 4.5, 9760, 1230, 1, 8, '2026-01-10 15:14:31', '2026-01-18 15:14:31');
INSERT INTO `attractions` VALUES (9, '三亚亚龙湾', '海滨度假', 'https://via.placeholder.com/400x300?text=YalongBay', 'https://via.placeholder.com/400x300?text=YalongBay1,https://via.placeholder.com/400x300?text=YalongBay2', '亚龙湾，位于海南省三亚市，被誉为\"天下第一湾\"。这里沙质洁白细腻，海水清澈见底，是热带海滨度假胜地。', '海南省三亚市吉阳区亚龙湾国家旅游度假区', 109.6694300, 18.1962300, '0898-88568899', '全天开放', '1. 注意防晒，避免晒伤；2. 海边戏水请注意安全；3. 保护海洋环境。', '海滨,沙滩,度假,热带', 0.00, 4.7, 8651, 1020, 1, 9, '2026-01-12 15:14:31', '2026-01-20 21:04:15');
INSERT INTO `attractions` VALUES (10, '青海湖', '自然景观', 'https://via.placeholder.com/400x300?text=QinghaiLake', 'https://via.placeholder.com/400x300?text=QinghaiLake1,https://via.placeholder.com/400x300?text=QinghaiLake2', '青海湖，位于青海省，是中国最大的内陆湖泊。湖水碧蓝，四周群山环抱，是高原旅游的绝佳目的地。每年夏季，湖畔油菜花盛开，美不胜收。', '青海省海南藏族自治州共和县', 100.2365600, 36.9694200, '0974-8519680', '全天开放', '1. 高原地区，注意防晒和高原反应；2. 早晚温差大，备好衣物；3. 保护高原生态环境。', '自然,湖泊,高原,5A景区', 10.00, 4.6, 7581, 890, 1, 10, '2026-01-14 15:14:31', '2026-01-22 20:07:30');
INSERT INTO `attractions` VALUES (11, '11', '1', '1', '1', '1', '1', 1.0000000, 1.0000000, '1', '1', '1', '1', 1.00, 1.0, 0, 0, 1, 1, '2026-01-18 18:06:27', '2026-01-18 18:06:27');

-- ----------------------------
-- Table structure for banners
-- ----------------------------
DROP TABLE IF EXISTS `banners`;
CREATE TABLE `banners`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Banner标题',
  `image_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '图片URL',
  `attraction_id` bigint NULL DEFAULT NULL COMMENT '关联景点ID',
  `link_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '跳转链接',
  `status` int NOT NULL DEFAULT 1 COMMENT '状态：0-下线，1-上线',
  `sort` int NOT NULL DEFAULT 0 COMMENT '排序',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_attraction_id`(`attraction_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_sort`(`sort` ASC) USING BTREE,
  CONSTRAINT `fk_banner_attraction` FOREIGN KEY (`attraction_id`) REFERENCES `attractions` (`id`) ON DELETE SET NULL ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'Banner表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of banners
-- ----------------------------
INSERT INTO `banners` VALUES (1, '杭州西湖', 'https://via.placeholder.com/800x400?text=Banner1', 1, '/attraction/1', 1, 1, '2026-01-13 15:14:31', '2026-01-18 15:14:31');
INSERT INTO `banners` VALUES (2, '北京故宫', 'https://via.placeholder.com/800x400?text=Banner2', 2, '/attraction/2', 1, 2, '2026-01-14 15:14:31', '2026-01-18 15:14:31');
INSERT INTO `banners` VALUES (3, '黄山风景区', 'https://via.placeholder.com/800x400?text=Banner3', 3, '/attraction/3', 1, 3, '2026-01-15 15:14:31', '2026-01-18 15:14:31');
INSERT INTO `banners` VALUES (4, '张家界', 'https://via.placeholder.com/800x400?text=Banner4', 4, '/attraction/4', 1, 4, '2026-01-16 15:14:31', '2026-01-18 15:14:31');
INSERT INTO `banners` VALUES (5, '九寨沟', 'https://via.placeholder.com/800x400?text=Banner5', 5, '/attraction/5', 1, 5, '2026-01-17 15:14:31', '2026-01-18 15:14:31');

-- ----------------------------
-- Table structure for comments
-- ----------------------------
DROP TABLE IF EXISTS `comments`;
CREATE TABLE `comments`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `guide_id` bigint NOT NULL COMMENT '攻略ID',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '评论内容',
  `like_count` int NOT NULL DEFAULT 0 COMMENT '点赞数',
  `status` int NOT NULL DEFAULT 1 COMMENT '状态：0-隐藏，1-显示',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `parent_id` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_guide_id`(`guide_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE,
  INDEX `FKlri30okf66phtcgbe5pok7cc0`(`parent_id` ASC) USING BTREE,
  CONSTRAINT `fk_comment_guide` FOREIGN KEY (`guide_id`) REFERENCES `guides` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_comment_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `FKlri30okf66phtcgbe5pok7cc0` FOREIGN KEY (`parent_id`) REFERENCES `comments` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 91 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '评论表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of comments
-- ----------------------------
INSERT INTO `comments` VALUES (2, 2, 1, '西湖确实很美，攻略中提到的音乐喷泉一定要看', 18, 1, '2026-01-15 15:14:31', '2026-01-18 15:14:31', NULL);
INSERT INTO `comments` VALUES (3, 3, 2, '故宫的讲解很重要，下次会租个导览器', 15, 1, '2026-01-12 15:14:31', '2026-01-18 15:14:31', NULL);
INSERT INTO `comments` VALUES (4, 4, 2, '建议上午早点去，避开人流高峰', 12, 1, '2026-01-13 15:14:31', '2026-01-18 15:14:31', NULL);
INSERT INTO `comments` VALUES (5, 5, 3, '黄山的日出太美了，值得早起', 21, 1, '2026-01-09 15:14:31', '2026-01-18 15:14:31', NULL);
INSERT INTO `comments` VALUES (6, 7, 3, '山上住宿条件一般，但景色值得', 19, 1, '2026-01-10 15:14:31', '2026-01-18 15:14:31', NULL);
INSERT INTO `comments` VALUES (7, 8, 4, '张家界的三天行程安排得很合理', 16, 1, '2026-01-07 15:14:31', '2026-01-18 15:14:31', NULL);
INSERT INTO `comments` VALUES (8, 1, 5, '九寨沟的水太清澈了，仿佛仙境', 25, 1, '2026-01-05 15:14:31', '2026-01-18 15:14:31', NULL);
INSERT INTO `comments` VALUES (9, 2, 6, '苏州园林的精致让人惊叹', 14, 1, '2026-01-03 15:14:31', '2026-01-18 15:14:31', NULL);
INSERT INTO `comments` VALUES (10, 3, 7, '丽江古城的夜景很美，适合慢游', 20, 1, '2026-01-01 15:14:31', '2026-01-18 15:14:31', NULL);
INSERT INTO `comments` VALUES (11, 9, 1, 'I有偶', 0, 1, '2026-01-23 20:30:38', '2026-01-23 20:30:38', NULL);
INSERT INTO `comments` VALUES (90, 9, 1, '你好', 0, 1, '2026-01-23 21:15:07', '2026-01-23 21:15:06', NULL);

-- ----------------------------
-- Table structure for guide_details
-- ----------------------------
DROP TABLE IF EXISTS `guide_details`;
CREATE TABLE `guide_details`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `guide_id` bigint NOT NULL COMMENT '攻略ID',
  `detailed_content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '详细攻略内容',
  `tips` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '旅行贴士',
  `itinerary` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '行程安排',
  `cost_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '费用信息',
  `transportation` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '交通指南',
  `accommodation` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '住宿推荐',
  `food_recommendations` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '美食推荐',
  `best_time` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '最佳旅行时间',
  `duration` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '建议游玩时长',
  `difficulty_level` int NULL DEFAULT 1 COMMENT '难度等级：1-简单，2-中等，3-困难',
  `suitable_for` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '适合人群',
  `emergency_contact` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '紧急联系方式',
  `weather_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '天气信息',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_guide_id`(`guide_id` ASC) USING BTREE,
  INDEX `idx_guide_id`(`guide_id` ASC) USING BTREE,
  CONSTRAINT `fk_guide_detail_guide` FOREIGN KEY (`guide_id`) REFERENCES `guides` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '攻略详情表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of guide_details
-- ----------------------------

-- ----------------------------
-- Table structure for guide_images
-- ----------------------------
DROP TABLE IF EXISTS `guide_images`;
CREATE TABLE `guide_images`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `create_time` datetime(6) NULL DEFAULT NULL,
  `image_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `sort_order` int NOT NULL,
  `guide_id` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKdxnscl65k952yrjiwpp8n52q0`(`guide_id` ASC) USING BTREE,
  CONSTRAINT `FKdxnscl65k952yrjiwpp8n52q0` FOREIGN KEY (`guide_id`) REFERENCES `guides` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of guide_images
-- ----------------------------

-- ----------------------------
-- Table structure for guides
-- ----------------------------
DROP TABLE IF EXISTS `guides`;
CREATE TABLE `guides`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `attraction_id` bigint NULL DEFAULT NULL COMMENT '关联景区ID',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '攻略标题',
  `cover_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '封面图片URL',
  `images` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '图片集（JSON格式）',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '攻略内容（富文本）',
  `audit_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'pending' COMMENT '审核状态：pending-待审核，approved-已通过，rejected-已拒绝',
  `status` int NOT NULL DEFAULT 1 COMMENT '状态：0-下架，1-上架',
  `is_recommended` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否推荐：0-否，1-是',
  `view_count` int NOT NULL DEFAULT 0 COMMENT '浏览量',
  `like_count` int NOT NULL DEFAULT 0 COMMENT '点赞数',
  `favorite_count` int NOT NULL DEFAULT 0 COMMENT '收藏数',
  `comment_count` int NOT NULL DEFAULT 0 COMMENT '评论数',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `audit_remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `deleted_at` datetime(6) NULL DEFAULT NULL,
  `is_deleted` bit(1) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_audit_status`(`audit_status` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_is_recommended`(`is_recommended` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE,
  INDEX `idx_attraction_id`(`attraction_id` ASC) USING BTREE,
  CONSTRAINT `fk_guide_attraction` FOREIGN KEY (`attraction_id`) REFERENCES `attractions` (`id`) ON DELETE SET NULL ON UPDATE RESTRICT,
  CONSTRAINT `fk_guide_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '攻略表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of guides
-- ----------------------------
INSERT INTO `guides` VALUES (1, 1, NULL, '杭州西湖一日游攻略', 'https://via.placeholder.com/400x300?text=Guide1', 'https://via.placeholder.com/400x300?text=Guide1-1,https://via.placeholder.com/400x300?text=Guide1-2', '<p>西湖是杭州最著名的景点，以下是一日游攻略：</p><p>上午：从断桥出发，漫步白堤，游览孤山...</p><p>下午：游览雷峰塔，欣赏西湖全景...</p><p>晚上：在湖滨路欣赏音乐喷泉...</p>', 'approved', 1, 1, 1274, 259, 189, 3, '2026-01-13 15:14:31', '2026-01-23 21:15:25', NULL, NULL, b'0');
INSERT INTO `guides` VALUES (2, 2, NULL, '北京故宫深度游指南', 'https://via.placeholder.com/400x300?text=Guide2', 'https://via.placeholder.com/400x300?text=Guide2-1,https://via.placeholder.com/400x300?text=Guide2-2', '<p>故宫是中国历史文化的瑰宝，以下是我的深度游经验：</p><p>1. 最佳游览时间：春秋两季，避开节假日...</p><p>2. 推荐路线：午门-太和殿-中和殿-保和殿...</p>', 'approved', 1, 1, 1564, 332, 247, 2, '2026-01-11 15:14:31', '2026-01-23 21:12:10', NULL, NULL, b'0');
INSERT INTO `guides` VALUES (3, 3, NULL, '黄山旅游全攻略', 'https://via.placeholder.com/400x300?text=Guide3', 'https://via.placeholder.com/400x300?text=Guide3-1,https://via.placeholder.com/400x300?text=Guide3-2', '<p>黄山以其独特的自然风光闻名，以下是我的黄山旅游经验：</p><p>交通：从合肥或杭州出发，高铁约3小时...</p><p>住宿：山顶酒店需提前预订...</p>', 'approved', 1, 0, 987, 198, 156, 2, '2026-01-08 15:14:31', '2026-01-23 21:11:38', NULL, NULL, b'0');
INSERT INTO `guides` VALUES (4, 4, NULL, '张家界三日游心得', 'https://via.placeholder.com/400x300?text=Guide4', 'https://via.placeholder.com/400x300?text=Guide4-1,https://via.placeholder.com/400x300?text=Guide4-2', '<p>张家界是一个让人流连忘返的地方，以下是我的三日游心得：</p><p>第一天：张家界国家森林公园...</p><p>第二天：天门山国家森林公园...</p>', 'approved', 1, 0, 872, 156, 123, 28, '2026-01-06 15:14:31', '2026-01-23 19:00:54', NULL, NULL, b'0');
INSERT INTO `guides` VALUES (5, 5, NULL, '九寨沟最佳游玩路线', 'https://via.placeholder.com/400x300?text=Guide5', 'https://via.placeholder.com/400x300?text=Guide5-1,https://via.placeholder.com/400x300?text=Guide5-2', '<p>九寨沟的美丽让人难以忘怀，以下是我的推荐路线：</p><p>Y字型路线：先游览日则沟，再游览则查洼沟...</p>', 'approved', 1, 1, 1123, 245, 178, 52, '2026-01-04 15:14:31', '2026-01-23 20:25:45', NULL, NULL, b'0');
INSERT INTO `guides` VALUES (6, 7, NULL, '苏州园林游览指南', 'https://via.placeholder.com/400x300?text=Guide6', 'https://via.placeholder.com/400x300?text=Guide6-1,https://via.placeholder.com/400x300?text=Guide6-2', '<p>苏州园林代表了中国古典园林艺术的精华，以下是我的游览指南：</p><p>推荐园林：拙政园、留园、狮子林、沧浪亭...</p>', 'approved', 1, 0, 657, 112, 89, 1, '2026-01-02 15:14:31', '2026-01-23 21:12:27', NULL, NULL, b'0');
INSERT INTO `guides` VALUES (7, 8, NULL, '丽江古城深度体验', 'https://via.placeholder.com/400x300?text=Guide7', 'https://via.placeholder.com/400x300?text=Guide7-1,https://via.placeholder.com/400x300?text=Guide7-2', '<p>丽江古城是感受纳西族文化的最佳去处，以下是我的深度体验：</p><p>必去景点：四方街、木府、黑龙潭...</p>', 'approved', 1, 0, 780, 134, 105, 23, '2025-12-31 15:14:31', '2026-01-18 15:14:31', NULL, NULL, b'0');
INSERT INTO `guides` VALUES (8, 1, NULL, '三亚亚龙湾度假攻略', 'https://via.placeholder.com/400x300?text=Guide8', 'https://via.placeholder.com/400x300?text=Guide8-1,https://via.placeholder.com/400x300?text=Guide8-2', '<p>亚龙湾是度假天堂，以下是我的度假攻略：</p><p>海滩活动：游泳、潜水、沙滩排球...</p><p>美食推荐：海鲜、热带水果...</p>', 'approved', 1, 0, 561, 98, 76, 15, '2025-12-29 15:14:31', '2026-01-23 20:21:48', NULL, NULL, b'0');
INSERT INTO `guides` VALUES (9, 1, NULL, '11111111111111', NULL, NULL, '111111111111111111111111111', 'approved', 1, 0, 3, 0, 0, 0, '2026-01-20 22:02:22', '2026-01-23 20:26:28', NULL, NULL, b'0');
INSERT INTO `guides` VALUES (10, 9, NULL, 'd/。安静的垃圾的拉进来的骄傲；对接；爱哭的卡阿卡出路扫平dial的', NULL, NULL, '拉进来的骄傲来得及垃圾堆里的卡阿卡Dowd【去啊啊狂顶外婆', 'approved', 1, 0, 5, 0, 0, 0, '2026-01-23 19:01:28', '2026-01-23 21:12:35', NULL, NULL, b'0');

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '订单号',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `attraction_id` bigint NOT NULL COMMENT '景点ID',
  `ticket_type_id` bigint NOT NULL COMMENT '门票类型ID',
  `quantity` int NOT NULL COMMENT '数量',
  `total_amount` decimal(10, 2) NOT NULL COMMENT '总价',
  `visit_date` date NOT NULL COMMENT '游玩日期',
  `visitor_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '游客姓名',
  `visitor_phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '游客手机号',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'unpaid' COMMENT '订单状态：unpaid-未支付，toUse-待使用，used-已使用，completed-已完成，refunded-已退款，cancelled-已取消',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_order_no`(`order_no` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_attraction_id`(`attraction_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE,
  INDEX `idx_visit_date`(`visit_date` ASC) USING BTREE,
  INDEX `fk_order_ticket`(`ticket_type_id` ASC) USING BTREE,
  CONSTRAINT `fk_order_attraction` FOREIGN KEY (`attraction_id`) REFERENCES `attractions` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_order_ticket` FOREIGN KEY (`ticket_type_id`) REFERENCES `ticket_types` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_order_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of orders
-- ----------------------------
INSERT INTO `orders` VALUES (1, 'ORD2025011800001', 1, 2, 1, 2, 120.00, '2025-01-20', '张三', '13900000001', 'toUse', '2026-01-18 14:14:31', '2026-01-18 15:14:31');
INSERT INTO `orders` VALUES (2, 'ORD2025011800002', 2, 3, 1, 1, 190.00, '2025-01-21', '李四', '13900000002', 'toUse', '2026-01-18 13:14:31', '2026-01-18 15:14:31');
INSERT INTO `orders` VALUES (3, 'ORD2025011800003', 3, 4, 1, 3, 684.00, '2025-01-19', '王五', '13900000003', 'completed', '2026-01-17 15:14:31', '2026-01-18 15:14:31');
INSERT INTO `orders` VALUES (4, 'ORD2025011800004', 4, 5, 1, 2, 338.00, '2025-01-18', '赵六', '13900000004', 'used', '2026-01-16 15:14:31', '2026-01-18 15:14:31');
INSERT INTO `orders` VALUES (5, 'ORD2025011800005', 5, 6, 1, 1, 210.00, '2025-01-22', '钱七', '13900000005', 'toUse', '2026-01-18 12:14:31', '2026-01-18 15:14:31');
INSERT INTO `orders` VALUES (6, 'ORD2025011800006', 1, 7, 1, 2, 180.00, '2025-01-23', '张三', '13900000001', 'toUse', '2026-01-18 10:14:31', '2026-01-18 15:14:31');
INSERT INTO `orders` VALUES (7, 'ORD2025011800007', 2, 2, 2, 1, 20.00, '2025-01-24', '李四', '13900000002', 'toUse', '2026-01-18 09:14:31', '2026-01-18 15:14:31');
INSERT INTO `orders` VALUES (8, 'ORD2025011800008', 3, 3, 3, 2, 230.00, '2025-01-25', '王五', '13900000003', 'toUse', '2026-01-18 07:14:31', '2026-01-18 15:14:31');
INSERT INTO `orders` VALUES (9, 'ORD2025011800009', 7, 5, 1, 1, 228.00, '2025-01-17', '周九', '13900000007', 'completed', '2026-01-15 15:14:31', '2026-01-18 15:14:31');
INSERT INTO `orders` VALUES (10, 'ORD2025011800010', 8, 6, 2, 1, 155.00, '2025-01-26', '吴十', '13900000008', 'toUse', '2026-01-18 05:14:31', '2026-01-18 15:14:31');
INSERT INTO `orders` VALUES (11, 'ORD1769165525037F0A25CE9', 9, 2, 1, 1, 60.00, '2026-01-24', '问问', '18352184541', 'unpaid', '2026-01-23 18:52:05', '2026-01-23 18:52:05');
INSERT INTO `orders` VALUES (12, 'ORD1769169358000BA06B6A6', 9, 4, 8, 1, 228.00, '2026-01-24', '呜呜呜', '18352184541', 'unpaid', '2026-01-23 19:55:58', '2026-01-23 19:55:58');

-- ----------------------------
-- Table structure for system_configs
-- ----------------------------
DROP TABLE IF EXISTS `system_configs`;
CREATE TABLE `system_configs`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `config_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '配置键',
  `config_value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '配置值',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '配置描述',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_config_key`(`config_key` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '系统配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of system_configs
-- ----------------------------
INSERT INTO `system_configs` VALUES (1, 'site_name', '旅游小程序后台管理系统', '网站名称', '2026-01-18 15:14:31', '2026-01-18 15:14:31');
INSERT INTO `system_configs` VALUES (2, 'site_logo', '/logo.png', '网站Logo', '2026-01-18 15:14:31', '2026-01-18 15:14:31');
INSERT INTO `system_configs` VALUES (3, 'contact_phone', '400-888-8888', '联系电话', '2026-01-18 15:14:31', '2026-01-18 15:14:31');
INSERT INTO `system_configs` VALUES (4, 'refund_rule', '游玩日前一天可全额退款，游玩当日退款收取20%手续费', '退款规则', '2026-01-18 15:14:31', '2026-01-18 15:14:31');
INSERT INTO `system_configs` VALUES (5, 'order_timeout', '30', '订单超时时间（分钟）', '2026-01-18 15:14:31', '2026-01-18 15:14:31');

-- ----------------------------
-- Table structure for ticket_types
-- ----------------------------
DROP TABLE IF EXISTS `ticket_types`;
CREATE TABLE `ticket_types`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `attraction_id` bigint NOT NULL COMMENT '景点ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '门票名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '门票描述',
  `price` decimal(10, 2) NOT NULL COMMENT '价格',
  `stock` int NOT NULL DEFAULT 0 COMMENT '库存',
  `status` int NOT NULL DEFAULT 1 COMMENT '状态：0-下架，1-上架',
  `sort` int NOT NULL DEFAULT 0 COMMENT '排序',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_attraction_id`(`attraction_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  CONSTRAINT `fk_ticket_attraction` FOREIGN KEY (`attraction_id`) REFERENCES `attractions` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '门票类型表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ticket_types
-- ----------------------------
INSERT INTO `ticket_types` VALUES (1, 2, '成人票', '18-60周岁成人门票', 60.00, 10000, 1, 1, '2026-01-18 15:14:31', '2026-01-18 15:14:31');
INSERT INTO `ticket_types` VALUES (2, 2, '学生票', '学生凭学生证优惠门票', 20.00, 5000, 1, 2, '2026-01-18 15:14:31', '2026-01-18 15:14:31');
INSERT INTO `ticket_types` VALUES (3, 2, '老人票', '60周岁以上老人优惠门票', 30.00, 3000, 1, 3, '2026-01-18 15:14:31', '2026-01-18 15:14:31');
INSERT INTO `ticket_types` VALUES (4, 3, '成人票', '18-60周岁成人门票', 190.00, 8000, 1, 1, '2026-01-18 15:14:31', '2026-01-18 15:14:31');
INSERT INTO `ticket_types` VALUES (5, 3, '学生票', '学生凭学生证优惠门票', 95.00, 4000, 1, 2, '2026-01-18 15:14:31', '2026-01-18 15:14:31');
INSERT INTO `ticket_types` VALUES (6, 3, '老人票', '60周岁以上老人优惠门票', 115.00, 2000, 1, 3, '2026-01-18 15:14:31', '2026-01-18 15:14:31');
INSERT INTO `ticket_types` VALUES (7, 3, '儿童票', '6-18周岁儿童优惠门票', 95.00, 3000, 1, 4, '2026-01-18 15:14:31', '2026-01-18 15:14:31');
INSERT INTO `ticket_types` VALUES (8, 4, '门票+环保车', '包含大门票和环保车票', 228.00, 12000, 1, 1, '2026-01-18 15:14:31', '2026-01-18 15:14:31');
INSERT INTO `ticket_types` VALUES (9, 4, '大门票', '仅包含大门票', 225.00, 10000, 1, 2, '2026-01-18 15:14:31', '2026-01-18 15:14:31');
INSERT INTO `ticket_types` VALUES (10, 5, '门票+车票', '包含门票和观光车票', 169.00, 6000, 1, 1, '2026-01-18 15:14:31', '2026-01-18 15:14:31');
INSERT INTO `ticket_types` VALUES (11, 5, '门票', '仅包含门票', 155.00, 8000, 1, 2, '2026-01-18 15:14:31', '2026-01-18 15:14:31');
INSERT INTO `ticket_types` VALUES (12, 6, '游船票', '漓江游船票', 210.00, 5000, 1, 1, '2026-01-18 15:14:31', '2026-01-18 15:14:31');
INSERT INTO `ticket_types` VALUES (13, 7, '成人票', '18-60周岁成人门票', 90.00, 7000, 1, 1, '2026-01-18 15:14:31', '2026-01-18 15:14:31');
INSERT INTO `ticket_types` VALUES (14, 7, '学生票', '学生凭学生证优惠门票', 50.00, 3500, 1, 2, '2026-01-18 15:14:31', '2026-01-18 15:14:31');
INSERT INTO `ticket_types` VALUES (15, 7, '老人票', '60周岁以上老人优惠门票', 70.00, 2000, 1, 3, '2026-01-18 15:14:31', '2026-01-18 15:14:31');

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '手机号',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像URL',
  `gender` int NULL DEFAULT NULL COMMENT '性别：0-未知，1-男，2-女',
  `birthday` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `status` int NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `register_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_phone`(`phone` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_register_time`(`register_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (1, '13900000001', '张三', 'https://via.placeholder.com/150', 1, '1990-01-01', 1, '2026-01-18 15:14:31', '2026-01-18 15:14:31');
INSERT INTO `users` VALUES (2, '13900000002', '李四', 'https://via.placeholder.com/150', 2, '1992-05-15', 1, '2026-01-17 15:14:31', '2026-01-18 15:14:31');
INSERT INTO `users` VALUES (3, '13900000003', '王五', 'https://via.placeholder.com/150', 1, '1988-09-20', 1, '2026-01-16 15:14:31', '2026-01-18 15:14:31');
INSERT INTO `users` VALUES (4, '13900000004', '赵六', 'https://via.placeholder.com/150', 2, '1995-03-10', 1, '2026-01-15 15:14:31', '2026-01-18 15:14:31');
INSERT INTO `users` VALUES (5, '13900000005', '钱七', 'https://via.placeholder.com/150', 1, '1993-07-25', 1, '2026-01-13 15:14:31', '2026-01-18 15:14:31');
INSERT INTO `users` VALUES (6, '13900000006', '孙八', 'https://via.placeholder.com/150', 2, '1991-11-30', 0, '2026-01-11 15:14:31', '2026-01-18 15:14:31');
INSERT INTO `users` VALUES (7, '13900000007', '周九', 'https://via.placeholder.com/150', 1, '1989-04-12', 1, '2026-01-08 15:14:31', '2026-01-18 15:14:31');
INSERT INTO `users` VALUES (8, '13900000008', '吴十', 'https://via.placeholder.com/150', 2, '1994-08-08', 1, '2026-01-03 15:14:31', '2026-01-18 15:14:31');
INSERT INTO `users` VALUES (9, '18352184541', '18352184541', NULL, NULL, NULL, 1, '2026-01-22 15:41:02', '2026-01-22 15:41:02');

SET FOREIGN_KEY_CHECKS = 1;
