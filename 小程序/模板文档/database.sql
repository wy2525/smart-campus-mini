-- ============================================
-- 旅游小程序数据库表结构
-- 创建时间：2026-01-16
-- 说明：已移除支付和扫码核销相关表
-- ============================================

-- 1. 用户相关表
-- --------------------------------------------

-- 用户表
CREATE TABLE users (
  id INT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
  openid VARCHAR(100) UNIQUE COMMENT '微信openid',
  nickname VARCHAR(50) COMMENT '昵称',
  avatar VARCHAR(255) COMMENT '头像URL',
  gender TINYINT DEFAULT 0 COMMENT '性别：0未知/1男/2女',
  phone VARCHAR(20) UNIQUE COMMENT '手机号',
  password VARCHAR(255) COMMENT '密码(加密存储)',
  is_admin TINYINT DEFAULT 0 COMMENT '是否管理员：0否/1是',
  status TINYINT DEFAULT 1 COMMENT '状态：0禁用/1正常',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  INDEX idx_phone (phone),
  INDEX idx_openid (openid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 2. 景点相关表
-- --------------------------------------------

-- 景点表
CREATE TABLE attractions (
  id INT PRIMARY KEY AUTO_INCREMENT COMMENT '景点ID',
  name VARCHAR(100) NOT NULL COMMENT '景点名称',
  description TEXT COMMENT '景点介绍',
  cover_image VARCHAR(255) COMMENT '封面图URL',
  images JSON COMMENT '图片数组',
  address VARCHAR(255) COMMENT '地址',
  latitude DECIMAL(10,6) COMMENT '纬度',
  longitude DECIMAL(10,6) COMMENT '经度',
  min_price DECIMAL(10,2) DEFAULT 0 COMMENT '最低价格',
  average_rating DECIMAL(3,2) DEFAULT 0 COMMENT '平均评分',
  tags VARCHAR(255) COMMENT '标签，逗号分隔',
  opening_hours VARCHAR(255) COMMENT '开放时间',
  notices TEXT COMMENT '注意事项',
  phone VARCHAR(20) COMMENT '联系电话',
  category VARCHAR(50) COMMENT '分类：museum/park/hot/nearby/family',
  status TINYINT DEFAULT 1 COMMENT '状态：0下线/1上线',
  view_count INT DEFAULT 0 COMMENT '浏览量',
  sort_order INT DEFAULT 0 COMMENT '排序',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  INDEX idx_category (category),
  INDEX idx_status (status),
  INDEX idx_sort (sort_order)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='景点表';

-- 门票类型表
CREATE TABLE ticket_types (
  id INT PRIMARY KEY AUTO_INCREMENT COMMENT '门票类型ID',
  attraction_id INT NOT NULL COMMENT '景点ID',
  name VARCHAR(50) NOT NULL COMMENT '门票名称：成人票/学生票/老人票',
  description VARCHAR(255) COMMENT '门票描述',
  price DECIMAL(10,2) NOT NULL COMMENT '价格',
  stock INT DEFAULT 0 COMMENT '库存数量',
  status TINYINT DEFAULT 1 COMMENT '状态：0下架/1上架',
  sort_order INT DEFAULT 0 COMMENT '排序',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  FOREIGN KEY (attraction_id) REFERENCES attractions(id) ON DELETE CASCADE,
  INDEX idx_attraction_id (attraction_id),
  INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='门票类型表';

-- 3. 订单相关表
-- --------------------------------------------

-- 订单表
CREATE TABLE orders (
  id INT PRIMARY KEY AUTO_INCREMENT COMMENT '订单ID',
  order_no VARCHAR(50) UNIQUE NOT NULL COMMENT '订单编号',
  user_id INT NOT NULL COMMENT '用户ID',
  attraction_id INT NOT NULL COMMENT '景点ID',
  ticket_type_id INT NOT NULL COMMENT '门票类型ID',
  quantity INT NOT NULL COMMENT '数量',
  unit_price DECIMAL(10,2) NOT NULL COMMENT '单价',
  total_price DECIMAL(10,2) NOT NULL COMMENT '总价',
  visit_date DATE NOT NULL COMMENT '游玩日期',
  status ENUM('unpaid','toUse','used','completed','refunded','cancelled') DEFAULT 'unpaid' COMMENT '订单状态',
  passenger_name VARCHAR(50) COMMENT '游客姓名',
  passenger_id_card VARCHAR(20) COMMENT '身份证号',
  passenger_phone VARCHAR(20) COMMENT '手机号',
  refund_reason VARCHAR(255) COMMENT '退款原因',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (attraction_id) REFERENCES attractions(id) ON DELETE CASCADE,
  FOREIGN KEY (ticket_type_id) REFERENCES ticket_types(id) ON DELETE CASCADE,
  INDEX idx_user_id (user_id),
  INDEX idx_order_no (order_no),
  INDEX idx_status (status),
  INDEX idx_visit_date (visit_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 4. 社区相关表
-- --------------------------------------------

-- 攻略表
CREATE TABLE guides (
  id INT PRIMARY KEY AUTO_INCREMENT COMMENT '攻略ID',
  title VARCHAR(100) NOT NULL COMMENT '标题',
  summary VARCHAR(255) COMMENT '摘要',
  content LONGTEXT COMMENT '内容',
  cover_image VARCHAR(255) COMMENT '封面图URL',
  images JSON COMMENT '图片数组',
  user_id INT NOT NULL COMMENT '作者ID',
  attraction_id INT COMMENT '关联景点ID',
  view_count INT DEFAULT 0 COMMENT '浏览量',
  like_count INT DEFAULT 0 COMMENT '点赞数',
  comment_count INT DEFAULT 0 COMMENT '评论数',
  collect_count INT DEFAULT 0 COMMENT '收藏数',
  status TINYINT DEFAULT 0 COMMENT '审核状态：0待审核/1已通过/2已拒绝',
  reject_reason VARCHAR(255) COMMENT '拒绝原因',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  INDEX idx_user_id (user_id),
  INDEX idx_status (status),
  INDEX idx_attraction_id (attraction_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='攻略表';

-- 攻略点赞表
CREATE TABLE guide_likes (
  id INT PRIMARY KEY AUTO_INCREMENT COMMENT '点赞ID',
  guide_id INT NOT NULL COMMENT '攻略ID',
  user_id INT NOT NULL COMMENT '用户ID',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  FOREIGN KEY (guide_id) REFERENCES guides(id) ON DELETE CASCADE,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  UNIQUE KEY uk_guide_user (guide_id, user_id),
  INDEX idx_guide_id (guide_id),
  INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='攻略点赞表';

-- 攻略收藏表
CREATE TABLE guide_favorites (
  id INT PRIMARY KEY AUTO_INCREMENT COMMENT '收藏ID',
  guide_id INT NOT NULL COMMENT '攻略ID',
  user_id INT NOT NULL COMMENT '用户ID',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  FOREIGN KEY (guide_id) REFERENCES guides(id) ON DELETE CASCADE,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  UNIQUE KEY uk_guide_user (guide_id, user_id),
  INDEX idx_guide_id (guide_id),
  INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='攻略收藏表';

-- 评论表
CREATE TABLE comments (
  id INT PRIMARY KEY AUTO_INCREMENT COMMENT '评论ID',
  guide_id INT NOT NULL COMMENT '攻略ID',
  user_id INT NOT NULL COMMENT '用户ID',
  content TEXT NOT NULL COMMENT '评论内容',
  parent_id INT DEFAULT 0 COMMENT '父评论ID，0表示一级评论',
  like_count INT DEFAULT 0 COMMENT '点赞数',
  status TINYINT DEFAULT 1 COMMENT '状态：0删除/1正常',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  FOREIGN KEY (guide_id) REFERENCES guides(id) ON DELETE CASCADE,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  INDEX idx_guide_id (guide_id),
  INDEX idx_user_id (user_id),
  INDEX idx_parent_id (parent_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论表';

-- 评论点赞表
CREATE TABLE comment_likes (
  id INT PRIMARY KEY AUTO_INCREMENT COMMENT '点赞ID',
  comment_id INT NOT NULL COMMENT '评论ID',
  user_id INT NOT NULL COMMENT '用户ID',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  FOREIGN KEY (comment_id) REFERENCES comments(id) ON DELETE CASCADE,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  UNIQUE KEY uk_comment_user (comment_id, user_id),
  INDEX idx_comment_id (comment_id),
  INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论点赞表';

-- 5. 收藏相关表
-- --------------------------------------------

-- 景点收藏表
CREATE TABLE attraction_favorites (
  id INT PRIMARY KEY AUTO_INCREMENT COMMENT '收藏ID',
  user_id INT NOT NULL COMMENT '用户ID',
  attraction_id INT NOT NULL COMMENT '景点ID',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (attraction_id) REFERENCES attractions(id) ON DELETE CASCADE,
  UNIQUE KEY uk_user_attraction (user_id, attraction_id),
  INDEX idx_user_id (user_id),
  INDEX idx_attraction_id (attraction_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='景点收藏表';

-- 6. 内容管理表
-- --------------------------------------------

-- Banner表
CREATE TABLE banners (
  id INT PRIMARY KEY AUTO_INCREMENT COMMENT 'Banner ID',
  title VARCHAR(100) COMMENT '标题',
  image_url VARCHAR(255) NOT NULL COMMENT '图片URL',
  attraction_id INT COMMENT '关联景点ID',
  url VARCHAR(255) COMMENT '跳转链接',
  sort_order INT DEFAULT 0 COMMENT '排序',
  status TINYINT DEFAULT 1 COMMENT '状态：0下线/1上线',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  FOREIGN KEY (attraction_id) REFERENCES attractions(id) ON DELETE SET NULL,
  INDEX idx_status (status),
  INDEX idx_sort (sort_order)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Banner表';

-- 系统配置表
CREATE TABLE system_config (
  id INT PRIMARY KEY AUTO_INCREMENT COMMENT '配置ID',
  config_key VARCHAR(100) UNIQUE NOT NULL COMMENT '配置键',
  config_value TEXT COMMENT '配置值',
  description VARCHAR(255) COMMENT '描述',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  INDEX idx_config_key (config_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统配置表';

-- ============================================
-- 初始化数据
-- ============================================

-- 插入默认管理员账号（密码：123456，实际使用时需修改）
INSERT INTO users (openid, nickname, phone, password, is_admin, status) VALUES
('admin_openid', '系统管理员', '13800138000', '$2b$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 1, 1);

-- 插入系统配置
INSERT INTO system_config (config_key, config_value, description) VALUES
('site_name', '旅游分享平台', '网站名称'),
('site_logo', '', '网站Logo'),
('contact_phone', '400-123-4567', '联系电话'),
('refund_rule', '游玩日期前7天可全额退款，游玩前1-7天退款50%，游玩当天不可退款', '退款规则'),
('order_timeout', '30', '订单超时时间（分钟）');
