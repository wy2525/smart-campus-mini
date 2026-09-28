-- ========================================
-- 旅游后台管理系统数据库初始化脚本
-- ========================================
-- 数据库名称: travel_system
-- 字符集: utf8mb4
-- 排序规则: utf8mb4_unicode_ci
-- ========================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS `travel_system` 
DEFAULT CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE `travel_system`;

-- ========================================
-- 1. 管理员表 (admins)
-- ========================================
DROP TABLE IF EXISTS `admins`;
CREATE TABLE `admins` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `password` VARCHAR(255) NOT NULL COMMENT '密码（加密后）',
  `nickname` VARCHAR(50) DEFAULT NULL COMMENT '昵称',
  `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
  `phone` VARCHAR(11) DEFAULT NULL COMMENT '手机号',
  `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
  `role` VARCHAR(20) NOT NULL DEFAULT 'admin' COMMENT '角色：super_admin-超级管理员，admin-管理员',
  `status` INT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='管理员表';

-- ========================================
-- 2. 用户表 (users)
-- ========================================
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `phone` VARCHAR(11) NOT NULL COMMENT '手机号',
  `nickname` VARCHAR(50) DEFAULT NULL COMMENT '昵称',
  `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
  `gender` INT DEFAULT NULL COMMENT '性别：0-未知，1-男，2-女',
  `birthday` VARCHAR(20) DEFAULT NULL COMMENT '生日',
  `status` INT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `register_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_phone` (`phone`),
  KEY `idx_status` (`status`),
  KEY `idx_register_time` (`register_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- ========================================
-- 3. 景点表 (attractions)
-- ========================================
DROP TABLE IF EXISTS `attractions`;
CREATE TABLE `attractions` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` VARCHAR(100) NOT NULL COMMENT '景点名称',
  `category` VARCHAR(50) NOT NULL COMMENT '分类：自然景观、历史古迹、海滨度假、主题公园等',
  `cover_image` VARCHAR(255) NOT NULL COMMENT '封面图片URL',
  `images` TEXT COMMENT '图片集（JSON格式）',
  `description` TEXT COMMENT '景点介绍',
  `address` VARCHAR(255) NOT NULL COMMENT '地址',
  `longitude` DECIMAL(10, 6) COMMENT '经度',
  `latitude` DECIMAL(10, 6) COMMENT '纬度',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
  `open_time` VARCHAR(50) DEFAULT NULL COMMENT '开放时间',
  `notes` TEXT COMMENT '注意事项',
  `tags` VARCHAR(255) DEFAULT NULL COMMENT '标签（逗号分隔）',
  `min_price` DECIMAL(10, 2) NOT NULL DEFAULT 0.00 COMMENT '最低价格',
  `rating` DECIMAL(2, 1) DEFAULT NULL COMMENT '评分',
  `view_count` INT NOT NULL DEFAULT 0 COMMENT '浏览量',
  `booking_count` INT NOT NULL DEFAULT 0 COMMENT '预订量',
  `status` INT NOT NULL DEFAULT 1 COMMENT '状态：0-下线，1-上线',
  `sort` INT NOT NULL DEFAULT 0 COMMENT '排序',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_category` (`category`),
  KEY `idx_status` (`status`),
  KEY `idx_sort` (`sort`),
  KEY `idx_rating` (`rating`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='景点表';

-- ========================================
-- 4. 门票类型表 (ticket_types)
-- ========================================
DROP TABLE IF EXISTS `ticket_types`;
CREATE TABLE `ticket_types` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `attraction_id` BIGINT NOT NULL COMMENT '景点ID',
  `name` VARCHAR(100) NOT NULL COMMENT '门票名称',
  `description` VARCHAR(255) DEFAULT NULL COMMENT '门票描述',
  `price` DECIMAL(10, 2) NOT NULL COMMENT '价格',
  `stock` INT NOT NULL DEFAULT 0 COMMENT '库存',
  `status` INT NOT NULL DEFAULT 1 COMMENT '状态：0-下架，1-上架',
  `sort` INT NOT NULL DEFAULT 0 COMMENT '排序',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_attraction_id` (`attraction_id`),
  KEY `idx_status` (`status`),
  CONSTRAINT `fk_ticket_attraction` FOREIGN KEY (`attraction_id`) REFERENCES `attractions` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='门票类型表';

-- ========================================
-- 5. 订单表 (orders)
-- ========================================
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_no` VARCHAR(32) NOT NULL COMMENT '订单号',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `attraction_id` BIGINT NOT NULL COMMENT '景点ID',
  `ticket_type_id` BIGINT NOT NULL COMMENT '门票类型ID',
  `quantity` INT NOT NULL COMMENT '数量',
  `total_amount` DECIMAL(10, 2) NOT NULL COMMENT '总价',
  `visit_date` DATE NOT NULL COMMENT '游玩日期',
  `visitor_name` VARCHAR(50) DEFAULT NULL COMMENT '游客姓名',
  `visitor_phone` VARCHAR(11) DEFAULT NULL COMMENT '游客手机号',
  `status` VARCHAR(20) NOT NULL DEFAULT 'unpaid' COMMENT '订单状态：unpaid-未支付，toUse-待使用，used-已使用，completed-已完成，refunded-已退款，cancelled-已取消',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_attraction_id` (`attraction_id`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_visit_date` (`visit_date`),
  CONSTRAINT `fk_order_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_order_attraction` FOREIGN KEY (`attraction_id`) REFERENCES `attractions` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_order_ticket` FOREIGN KEY (`ticket_type_id`) REFERENCES `ticket_types` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单表';

-- ========================================
-- 6. 攻略表 (guides)
-- ========================================
DROP TABLE IF EXISTS `guides`;
CREATE TABLE `guides` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `title` VARCHAR(200) NOT NULL COMMENT '攻略标题',
  `cover_image` VARCHAR(255) DEFAULT NULL COMMENT '封面图片URL',
  `images` TEXT COMMENT '图片集（JSON格式）',
  `content` TEXT NOT NULL COMMENT '攻略内容（富文本）',
  `audit_status` VARCHAR(20) NOT NULL DEFAULT 'pending' COMMENT '审核状态：pending-待审核，approved-已通过，rejected-已拒绝',
  `status` INT NOT NULL DEFAULT 1 COMMENT '状态：0-下架，1-上架',
  `is_recommended` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否推荐：0-否，1-是',
  `view_count` INT NOT NULL DEFAULT 0 COMMENT '浏览量',
  `like_count` INT NOT NULL DEFAULT 0 COMMENT '点赞数',
  `favorite_count` INT NOT NULL DEFAULT 0 COMMENT '收藏数',
  `comment_count` INT NOT NULL DEFAULT 0 COMMENT '评论数',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_audit_status` (`audit_status`),
  KEY `idx_status` (`status`),
  KEY `idx_is_recommended` (`is_recommended`),
  KEY `idx_create_time` (`create_time`),
  CONSTRAINT `fk_guide_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='攻略表';

-- ========================================
-- 7. 评论表 (comments)
-- ========================================
DROP TABLE IF EXISTS `comments`;
CREATE TABLE `comments` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `guide_id` BIGINT NOT NULL COMMENT '攻略ID',
  `content` TEXT NOT NULL COMMENT '评论内容',
  `like_count` INT NOT NULL DEFAULT 0 COMMENT '点赞数',
  `status` INT NOT NULL DEFAULT 1 COMMENT '状态：0-隐藏，1-显示',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_guide_id` (`guide_id`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`),
  CONSTRAINT `fk_comment_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_comment_guide` FOREIGN KEY (`guide_id`) REFERENCES `guides` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='评论表';

-- ========================================
-- 8. Banner表 (banners)
-- ========================================
DROP TABLE IF EXISTS `banners`;
CREATE TABLE `banners` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` VARCHAR(100) NOT NULL COMMENT 'Banner标题',
  `image_url` VARCHAR(255) NOT NULL COMMENT '图片URL',
  `attraction_id` BIGINT DEFAULT NULL COMMENT '关联景点ID',
  `link_url` VARCHAR(255) DEFAULT NULL COMMENT '跳转链接',
  `status` INT NOT NULL DEFAULT 1 COMMENT '状态：0-下线，1-上线',
  `sort` INT NOT NULL DEFAULT 0 COMMENT '排序',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_attraction_id` (`attraction_id`),
  KEY `idx_status` (`status`),
  KEY `idx_sort` (`sort`),
  CONSTRAINT `fk_banner_attraction` FOREIGN KEY (`attraction_id`) REFERENCES `attractions` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Banner表';

-- ========================================
-- 9. 系统配置表 (system_configs)
-- ========================================
DROP TABLE IF EXISTS `system_configs`;
CREATE TABLE `system_configs` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `config_key` VARCHAR(100) NOT NULL COMMENT '配置键',
  `config_value` TEXT COMMENT '配置值',
  `description` VARCHAR(255) DEFAULT NULL COMMENT '配置描述',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_config_key` (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统配置表';

-- ========================================
-- 插入初始数据
-- ========================================

-- 插入管理员账户
-- 密码: admin123 (BCrypt加密)
INSERT INTO `admins` (`username`, `password`, `nickname`, `phone`, `email`, `role`, `status`, `create_time`, `update_time`)
VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z2EHkHwGd5Y6Rq5vJG6MqG3e', '系统管理员', '13800138000', 'admin@example.com', 'super_admin', 1, NOW(), NOW()),
('test_admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z2EHkHwGd5Y6Rq5vJG6MqG3e', '测试管理员', '13800138001', 'test@example.com', 'admin', 1, NOW(), NOW());

-- 插入用户
INSERT INTO `users` (`phone`, `nickname`, `avatar`, `gender`, `birthday`, `status`, `register_time`, `update_time`)
VALUES
('13900000001', '张三', 'https://via.placeholder.com/150', 1, '1990-01-01', 1, NOW(), NOW()),
('13900000002', '李四', 'https://via.placeholder.com/150', 2, '1992-05-15', 1, NOW() - INTERVAL 1 DAY, NOW()),
('13900000003', '王五', 'https://via.placeholder.com/150', 1, '1988-09-20', 1, NOW() - INTERVAL 2 DAY, NOW()),
('13900000004', '赵六', 'https://via.placeholder.com/150', 2, '1995-03-10', 1, NOW() - INTERVAL 3 DAY, NOW()),
('13900000005', '钱七', 'https://via.placeholder.com/150', 1, '1993-07-25', 1, NOW() - INTERVAL 5 DAY, NOW()),
('13900000006', '孙八', 'https://via.placeholder.com/150', 2, '1991-11-30', 0, NOW() - INTERVAL 7 DAY, NOW()),
('13900000007', '周九', 'https://via.placeholder.com/150', 1, '1989-04-12', 1, NOW() - INTERVAL 10 DAY, NOW()),
('13900000008', '吴十', 'https://via.placeholder.com/150', 2, '1994-08-08', 1, NOW() - INTERVAL 15 DAY, NOW());

-- 插入景点
INSERT INTO `attractions` (`name`, `category`, `cover_image`, `images`, `description`, `address`, `longitude`, `latitude`, `phone`, `open_time`, `notes`, `tags`, `min_price`, `rating`, `view_count`, `booking_count`, `status`, `sort`, `create_time`, `update_time`)
VALUES
('杭州西湖', '自然景观', 'https://via.placeholder.com/400x300?text=WestLake',
'https://via.placeholder.com/400x300?text=WestLake1,https://via.placeholder.com/400x300?text=WestLake2,https://via.placeholder.com/400x300?text=WestLake3',
'西湖，位于中国浙江省杭州市西湖区，是中国著名的旅游景点之一。西湖以其秀丽的湖光山色和众多的名胜古迹而闻名于世，被誉为人间天堂。',
'浙江省杭州市西湖区龙井路1号', 120.148932, 30.259244, '0571-87179617', '全天开放',
'1. 请保护环境，不要乱扔垃圾；2. 注意安全，不要在湖边嬉戏；3. 旺季时请提前预订门票。',
'自然,湖泊,5A景区,免费', 0.00, 4.8, 15680, 2340, 1, 1, NOW() - INTERVAL 30 DAY, NOW()),
('北京故宫', '历史古迹', 'https://via.placeholder.com/400x300?text=ForbiddenCity',
'https://via.placeholder.com/400x300?text=ForbiddenCity1,https://via.placeholder.com/400x300?text=ForbiddenCity2',
'故宫，又称紫禁城，是中国明清两代的皇家宫殿，位于北京中轴线的中心，是世界上现存规模最大、保存最为完整的木质结构古建筑之一。',
'北京市东城区景山前街4号', 116.397455, 39.918058, '010-85007421', '08:30-17:00',
'1. 严禁携带易燃易爆物品；2. 请勿大声喧哗；3. 禁止在宫殿内拍照。',
'历史,宫殿,5A景区,世界遗产', 60.00, 4.9, 23450, 4560, 1, 2, NOW() - INTERVAL 25 DAY, NOW()),
('黄山风景区', '自然景观', 'https://via.placeholder.com/400x300?text=Huangshan',
'https://via.placeholder.com/400x300?text=Huangshan1,https://via.placeholder.com/400x300?text=Huangshan2',
'黄山，位于安徽省黄山市境内，是中国十大风景名胜唯一的山岳风光。黄山以奇松、怪石、云海、温泉、冬雪五绝著称于世。',
'安徽省黄山市黄山区汤口镇', 118.167590, 30.131940, '0559-5580244', '06:00-18:00',
'1. 山上气温较低，请携带保暖衣物；2. 雨天路滑，注意安全；3. 乘坐缆车时请遵守秩序。',
'自然,山岳,5A景区,避暑', 190.00, 4.8, 18920, 3210, 1, 3, NOW() - INTERVAL 20 DAY, NOW()),
('张家界国家森林公园', '自然景观', 'https://via.placeholder.com/400x300?text=Zhangjiajie',
'https://via.placeholder.com/400x300?text=Zhangjiajie1,https://via.placeholder.com/400x300?text=Zhangjiajie2',
'张家界国家森林公园，位于湖南省张家界市，是中国第一个国家森林公园，以独特的石英砂岩峰林地貌著称，是电影《阿凡达》的取景地。',
'湖南省张家界市武陵源区', 110.448720, 29.324820, '0744-5712189', '07:00-18:00',
'1. 山区气候多变，请备雨具；2. 攀登时请量力而行；3. 保护自然景观，不要攀爬树木。',
'自然,森林,5A景区,奇峰', 228.00, 4.7, 16780, 2890, 1, 4, NOW() - INTERVAL 18 DAY, NOW()),
('九寨沟风景区', '自然景观', 'https://via.placeholder.com/400x300?text=Jiuzhaigou',
'https://via.placeholder.com/400x300?text=Jiuzhaigou1,https://via.placeholder.com/400x300?text=Jiuzhaigou2',
'九寨沟，位于四川省阿坝藏族羌族自治州九寨沟县，以翠海、叠瀑、彩林、雪峰、藏情、蓝冰六绝著称于世，被誉为"童话世界"。',
'四川省阿坝州九寨沟县漳扎镇', 103.918110, 33.205590, '0837-7739753', '07:00-18:00',
'1. 高海拔地区，请注意高原反应；2. 严禁乱扔垃圾；3. 尊重当地风俗习惯。',
'自然,湖泊,瀑布,5A景区', 169.00, 4.8, 14230, 1980, 1, 5, NOW() - INTERVAL 15 DAY, NOW()),
('桂林山水', '自然景观', 'https://via.placeholder.com/400x300?text=Guilin',
'https://via.placeholder.com/400x300?text=Guilin1,https://via.placeholder.com/400x300?text=Guilin2',
'桂林山水，位于广西壮族自治区桂林市，是中国山水的代表。"桂林山水甲天下"这一千古名句，使得桂林山水名扬海内外。',
'广西壮族自治区桂林市秀峰区', 110.290100, 25.273530, '0773-2864600', '全天开放',
'1. 漓江游船请注意安全；2. 避免雨天出行；3. 保护漓江环境，不乱扔垃圾。',
'自然,山水,5A景区,漓江', 210.00, 4.7, 12560, 1780, 1, 6, NOW() - INTERVAL 12 DAY, NOW()),
('苏州园林', '历史古迹', 'https://via.placeholder.com/400x300?text=Suzhou',
'https://via.placeholder.com/400x300?text=Suzhou1,https://via.placeholder.com/400x300?text=Suzhou2',
'苏州园林，是中国古典园林的代表，以拙政园、留园、狮子林等最为著名。园林设计精巧，一步一景，体现了中国古典园林艺术的精髓。',
'江苏省苏州市姑苏区', 120.619585, 31.298960, '0512-67542442', '07:30-17:30',
'1. 保持安静，不要大声喧哗；2. 保护园林植被；3. 严禁在园林内吸烟。',
'历史,园林,5A景区,文化遗产', 90.00, 4.6, 10890, 1450, 1, 7, NOW() - INTERVAL 10 DAY, NOW()),
('丽江古城', '历史文化', 'https://via.placeholder.com/400x300?text=Lijiang',
'https://via.placeholder.com/400x300?text=Lijiang1,https://via.placeholder.com/400x300?text=Lijiang2',
'丽江古城，位于云南省丽江市，是中国历史文化名城之一。古城建筑风格独特，纳西族文化浓厚，是体验云南民族文化的绝佳去处。',
'云南省丽江市古城区', 100.233060, 26.872140, '0888-5191888', '全天开放',
'1. 古城内禁止车辆通行；2. 尊重当地纳西族文化；3. 夜间行走请注意安全。',
'历史,古城,5A景区,纳西族', 0.00, 4.5, 9760, 1230, 1, 8, NOW() - INTERVAL 8 DAY, NOW()),
('三亚亚龙湾', '海滨度假', 'https://via.placeholder.com/400x300?text=YalongBay',
'https://via.placeholder.com/400x300?text=YalongBay1,https://via.placeholder.com/400x300?text=YalongBay2',
'亚龙湾，位于海南省三亚市，被誉为"天下第一湾"。这里沙质洁白细腻，海水清澈见底，是热带海滨度假胜地。',
'海南省三亚市吉阳区亚龙湾国家旅游度假区', 109.669430, 18.196230, '0898-88568899', '全天开放',
'1. 注意防晒，避免晒伤；2. 海边戏水请注意安全；3. 保护海洋环境。',
'海滨,沙滩,度假,热带', 0.00, 4.7, 8650, 1020, 1, 9, NOW() - INTERVAL 6 DAY, NOW()),
('青海湖', '自然景观', 'https://via.placeholder.com/400x300?text=QinghaiLake',
'https://via.placeholder.com/400x300?text=QinghaiLake1,https://via.placeholder.com/400x300?text=QinghaiLake2',
'青海湖，位于青海省，是中国最大的内陆湖泊。湖水碧蓝，四周群山环抱，是高原旅游的绝佳目的地。每年夏季，湖畔油菜花盛开，美不胜收。',
'青海省海南藏族自治州共和县', 100.236560, 36.969420, '0974-8519680', '全天开放',
'1. 高原地区，注意防晒和高原反应；2. 早晚温差大，备好衣物；3. 保护高原生态环境。',
'自然,湖泊,高原,5A景区', 0.00, 4.6, 7580, 890, 1, 10, NOW() - INTERVAL 4 DAY, NOW());

-- 插入门票类型
INSERT INTO `ticket_types` (`attraction_id`, `name`, `description`, `price`, `stock`, `status`, `sort`, `create_time`, `update_time`)
VALUES
(2, '成人票', '18-60周岁成人门票', 60, 10000, 1, 1, NOW(), NOW()),
(2, '学生票', '学生凭学生证优惠门票', 20, 5000, 1, 2, NOW(), NOW()),
(2, '老人票', '60周岁以上老人优惠门票', 30, 3000, 1, 3, NOW(), NOW()),
(3, '成人票', '18-60周岁成人门票', 190, 8000, 1, 1, NOW(), NOW()),
(3, '学生票', '学生凭学生证优惠门票', 95, 4000, 1, 2, NOW(), NOW()),
(3, '老人票', '60周岁以上老人优惠门票', 115, 2000, 1, 3, NOW(), NOW()),
(3, '儿童票', '6-18周岁儿童优惠门票', 95, 3000, 1, 4, NOW(), NOW()),
(4, '门票+环保车', '包含大门票和环保车票', 228, 12000, 1, 1, NOW(), NOW()),
(4, '大门票', '仅包含大门票', 225, 10000, 1, 2, NOW(), NOW()),
(5, '门票+车票', '包含门票和观光车票', 169, 6000, 1, 1, NOW(), NOW()),
(5, '门票', '仅包含门票', 155, 8000, 1, 2, NOW(), NOW()),
(6, '游船票', '漓江游船票', 210, 5000, 1, 1, NOW(), NOW()),
(7, '成人票', '18-60周岁成人门票', 90, 7000, 1, 1, NOW(), NOW()),
(7, '学生票', '学生凭学生证优惠门票', 50, 3500, 1, 2, NOW(), NOW()),
(7, '老人票', '60周岁以上老人优惠门票', 70, 2000, 1, 3, NOW(), NOW());

-- 插入订单
INSERT INTO `orders` (`order_no`, `user_id`, `attraction_id`, `ticket_type_id`, `quantity`, `total_amount`, `visit_date`, `visitor_name`, `visitor_phone`, `status`, `create_time`, `update_time`)
VALUES
('ORD2025011800001', 1, 2, 1, 2, 120, '2025-01-20', '张三', '13900000001', 'toUse', NOW() - INTERVAL 1 HOUR, NOW()),
('ORD2025011800002', 2, 3, 1, 1, 190, '2025-01-21', '李四', '13900000002', 'toUse', NOW() - INTERVAL 2 HOUR, NOW()),
('ORD2025011800003', 3, 4, 1, 3, 684, '2025-01-19', '王五', '13900000003', 'completed', NOW() - INTERVAL 1 DAY, NOW()),
('ORD2025011800004', 4, 5, 1, 2, 338, '2025-01-18', '赵六', '13900000004', 'used', NOW() - INTERVAL 2 DAY, NOW()),
('ORD2025011800005', 5, 6, 1, 1, 210, '2025-01-22', '钱七', '13900000005', 'toUse', NOW() - INTERVAL 3 HOUR, NOW()),
('ORD2025011800006', 1, 7, 1, 2, 180, '2025-01-23', '张三', '13900000001', 'toUse', NOW() - INTERVAL 5 HOUR, NOW()),
('ORD2025011800007', 2, 2, 2, 1, 20, '2025-01-24', '李四', '13900000002', 'toUse', NOW() - INTERVAL 6 HOUR, NOW()),
('ORD2025011800008', 3, 3, 3, 2, 230, '2025-01-25', '王五', '13900000003', 'toUse', NOW() - INTERVAL 8 HOUR, NOW()),
('ORD2025011800009', 7, 5, 1, 1, 228, '2025-01-17', '周九', '13900000007', 'completed', NOW() - INTERVAL 3 DAY, NOW()),
('ORD2025011800010', 8, 6, 2, 1, 155, '2025-01-26', '吴十', '13900000008', 'toUse', NOW() - INTERVAL 10 HOUR, NOW());

-- 插入攻略
INSERT INTO `guides` (`user_id`, `title`, `cover_image`, `images`, `content`, `audit_status`, `status`, `is_recommended`, `view_count`, `like_count`, `favorite_count`, `comment_count`, `create_time`, `update_time`)
VALUES
(1, '杭州西湖一日游攻略', 'https://via.placeholder.com/400x300?text=Guide1',
'https://via.placeholder.com/400x300?text=Guide1-1,https://via.placeholder.com/400x300?text=Guide1-2',
'<p>西湖是杭州最著名的景点，以下是一日游攻略：</p><p>上午：从断桥出发，漫步白堤，游览孤山...</p><p>下午：游览雷峰塔，欣赏西湖全景...</p><p>晚上：在湖滨路欣赏音乐喷泉...</p>',
'approved', 1, 1, 1230, 256, 189, 45, NOW() - INTERVAL 5 DAY, NOW()),
(2, '北京故宫深度游指南', 'https://via.placeholder.com/400x300?text=Guide2',
'https://via.placeholder.com/400x300?text=Guide2-1,https://via.placeholder.com/400x300?text=Guide2-2',
'<p>故宫是中国历史文化的瑰宝，以下是我的深度游经验：</p><p>1. 最佳游览时间：春秋两季，避开节假日...</p><p>2. 推荐路线：午门-太和殿-中和殿-保和殿...</p>',
'approved', 1, 1, 1560, 328, 245, 67, NOW() - INTERVAL 7 DAY, NOW()),
(3, '黄山旅游全攻略', 'https://via.placeholder.com/400x300?text=Guide3',
'https://via.placeholder.com/400x300?text=Guide3-1,https://via.placeholder.com/400x300?text=Guide3-2',
'<p>黄山以其独特的自然风光闻名，以下是我的黄山旅游经验：</p><p>交通：从合肥或杭州出发，高铁约3小时...</p><p>住宿：山顶酒店需提前预订...</p>',
'approved', 1, 0, 980, 198, 156, 34, NOW() - INTERVAL 10 DAY, NOW()),
(4, '张家界三日游心得', 'https://via.placeholder.com/400x300?text=Guide4',
'https://via.placeholder.com/400x300?text=Guide4-1,https://via.placeholder.com/400x300?text=Guide4-2',
'<p>张家界是一个让人流连忘返的地方，以下是我的三日游心得：</p><p>第一天：张家界国家森林公园...</p><p>第二天：天门山国家森林公园...</p>',
'approved', 1, 0, 870, 156, 123, 28, NOW() - INTERVAL 12 DAY, NOW()),
(5, '九寨沟最佳游玩路线', 'https://via.placeholder.com/400x300?text=Guide5',
'https://via.placeholder.com/400x300?text=Guide5-1,https://via.placeholder.com/400x300?text=Guide5-2',
'<p>九寨沟的美丽让人难以忘怀，以下是我的推荐路线：</p><p>Y字型路线：先游览日则沟，再游览则查洼沟...</p>',
'approved', 1, 1, 1120, 245, 178, 52, NOW() - INTERVAL 14 DAY, NOW()),
(7, '苏州园林游览指南', 'https://via.placeholder.com/400x300?text=Guide6',
'https://via.placeholder.com/400x300?text=Guide6-1,https://via.placeholder.com/400x300?text=Guide6-2',
'<p>苏州园林代表了中国古典园林艺术的精华，以下是我的游览指南：</p><p>推荐园林：拙政园、留园、狮子林、沧浪亭...</p>',
'approved', 1, 0, 650, 112, 89, 19, NOW() - INTERVAL 16 DAY, NOW()),
(8, '丽江古城深度体验', 'https://via.placeholder.com/400x300?text=Guide7',
'https://via.placeholder.com/400x300?text=Guide7-1,https://via.placeholder.com/400x300?text=Guide7-2',
'<p>丽江古城是感受纳西族文化的最佳去处，以下是我的深度体验：</p><p>必去景点：四方街、木府、黑龙潭...</p>',
'approved', 1, 0, 780, 134, 105, 23, NOW() - INTERVAL 18 DAY, NOW()),
(1, '三亚亚龙湾度假攻略', 'https://via.placeholder.com/400x300?text=Guide8',
'https://via.placeholder.com/400x300?text=Guide8-1,https://via.placeholder.com/400x300?text=Guide8-2',
'<p>亚龙湾是度假天堂，以下是我的度假攻略：</p><p>海滩活动：游泳、潜水、沙滩排球...</p><p>美食推荐：海鲜、热带水果...</p>',
'approved', 1, 0, 560, 98, 76, 15, NOW() - INTERVAL 20 DAY, NOW());

-- 插入评论
INSERT INTO `comments` (`user_id`, `guide_id`, `content`, `like_count`, `status`, `create_time`, `update_time`)
VALUES
(1, 1, '攻略写得非常好，按照这个路线游玩很顺利！', 23, 1, NOW() - INTERVAL 4 DAY, NOW()),
(2, 1, '西湖确实很美，攻略中提到的音乐喷泉一定要看', 18, 1, NOW() - INTERVAL 3 DAY, NOW()),
(3, 2, '故宫的讲解很重要，下次会租个导览器', 15, 1, NOW() - INTERVAL 6 DAY, NOW()),
(4, 2, '建议上午早点去，避开人流高峰', 12, 1, NOW() - INTERVAL 5 DAY, NOW()),
(5, 3, '黄山的日出太美了，值得早起', 21, 1, NOW() - INTERVAL 9 DAY, NOW()),
(7, 3, '山上住宿条件一般，但景色值得', 19, 1, NOW() - INTERVAL 8 DAY, NOW()),
(8, 4, '张家界的三天行程安排得很合理', 16, 1, NOW() - INTERVAL 11 DAY, NOW()),
(1, 5, '九寨沟的水太清澈了，仿佛仙境', 25, 1, NOW() - INTERVAL 13 DAY, NOW()),
(2, 6, '苏州园林的精致让人惊叹', 14, 1, NOW() - INTERVAL 15 DAY, NOW()),
(3, 7, '丽江古城的夜景很美，适合慢游', 20, 1, NOW() - INTERVAL 17 DAY, NOW());

-- 插入Banner
INSERT INTO `banners` (`title`, `image_url`, `attraction_id`, `link_url`, `status`, `sort`, `create_time`, `update_time`)
VALUES
('杭州西湖', 'https://via.placeholder.com/800x400?text=Banner1', 1, '/attraction/1', 1, 1, NOW() - INTERVAL 5 DAY, NOW()),
('北京故宫', 'https://via.placeholder.com/800x400?text=Banner2', 2, '/attraction/2', 1, 2, NOW() - INTERVAL 4 DAY, NOW()),
('黄山风景区', 'https://via.placeholder.com/800x400?text=Banner3', 3, '/attraction/3', 1, 3, NOW() - INTERVAL 3 DAY, NOW()),
('张家界', 'https://via.placeholder.com/800x400?text=Banner4', 4, '/attraction/4', 1, 4, NOW() - INTERVAL 2 DAY, NOW()),
('九寨沟', 'https://via.placeholder.com/800x400?text=Banner5', 5, '/attraction/5', 1, 5, NOW() - INTERVAL 1 DAY, NOW());

-- 插入系统配置
INSERT INTO `system_configs` (`config_key`, `config_value`, `description`, `create_time`, `update_time`)
VALUES
('site_name', '旅游小程序后台管理系统', '网站名称', NOW(), NOW()),
('site_logo', '/logo.png', '网站Logo', NOW(), NOW()),
('contact_phone', '400-888-8888', '联系电话', NOW(), NOW()),
('refund_rule', '游玩日前一天可全额退款，游玩当日退款收取20%手续费', '退款规则', NOW(), NOW()),
('order_timeout', '30', '订单超时时间（分钟）', NOW(), NOW());

-- ========================================
-- 初始化完成
-- ========================================

-- 显示数据库信息
SELECT 
    'admins' AS table_name, COUNT(*) AS record_count FROM `admins`
UNION ALL
SELECT 
    'users', COUNT(*) FROM `users`
UNION ALL
SELECT 
    'attractions', COUNT(*) FROM `attractions`
UNION ALL
SELECT 
    'ticket_types', COUNT(*) FROM `ticket_types`
UNION ALL
SELECT 
    'orders', COUNT(*) FROM `orders`
UNION ALL
SELECT 
    'guides', COUNT(*) FROM `guides`
UNION ALL
SELECT 
    'comments', COUNT(*) FROM `comments`
UNION ALL
SELECT 
    'banners', COUNT(*) FROM `banners`
UNION ALL
SELECT 
    'system_configs', COUNT(*) FROM `system_configs`;

-- ========================================
-- 默认管理员账号信息
-- ========================================
-- 用户名: admin
-- 密码: admin123
-- 角色: super_admin
-- ========================================
