-- 为门票表添加景点字段（如果还没有的话）- 实际上已存在
-- ALTER TABLE ticket_types ADD COLUMN IF NOT EXISTS attraction_id BIGINT COMMENT '所属景点ID';

-- 检查攻略表结构并扩展（如果需要）
-- 如果没有攻略详情表，则创建攻略详情表
-- 这里假设需要一个专门的攻略详情表来存储更详细的信息

-- 创建攻略详情表
CREATE TABLE IF NOT EXISTS guide_details (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    guide_id BIGINT NOT NULL COMMENT '攻略ID',
    detailed_content LONGTEXT COMMENT '详细攻略内容',
    tips TEXT COMMENT '旅行贴士',
    itinerary TEXT COMMENT '行程安排',
    cost_info TEXT COMMENT '费用信息',
    transportation TEXT COMMENT '交通指南',
    accommodation TEXT COMMENT '住宿推荐',
    food_recommendations TEXT COMMENT '美食推荐',
    best_time VARCHAR(100) COMMENT '最佳旅行时间',
    duration VARCHAR(50) COMMENT '建议游玩时长',
    difficulty_level INT DEFAULT 1 COMMENT '难度等级：1-简单，2-中等，3-困难',
    suitable_for TEXT COMMENT '适合人群',
    emergency_contact TEXT COMMENT '紧急联系方式',
    weather_info TEXT COMMENT '天气信息',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_guide_id (guide_id),
    KEY idx_guide_id (guide_id),
    CONSTRAINT fk_guide_detail_guide FOREIGN KEY (guide_id) REFERENCES guides (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='攻略详情表';

-- 添加索引优化查询性能
ALTER TABLE ticket_types ADD INDEX IF NOT EXISTS idx_attraction_id_status (attraction_id, status);

-- 更新门票表，确保景点字段存在且有约束
ALTER TABLE ticket_types 
ADD CONSTRAINT IF NOT EXISTS fk_ticket_attraction 
FOREIGN KEY (attraction_id) REFERENCES attractions (id) ON DELETE CASCADE;

-- 更新订单表，确保门票类型字段有约束
ALTER TABLE orders 
ADD CONSTRAINT IF NOT EXISTS fk_order_ticket 
FOREIGN KEY (ticket_type_id) REFERENCES ticket_types (id) ON DELETE CASCADE;

-- 更新订单表，确保景点字段有约束
ALTER TABLE orders 
ADD CONSTRAINT IF NOT EXISTS fk_order_attraction 
FOREIGN KEY (attraction_id) REFERENCES attractions (id) ON DELETE CASCADE;

-- 为订单表添加索引
ALTER TABLE orders ADD INDEX IF NOT EXISTS idx_attraction_id_status (attraction_id, status);
ALTER TABLE orders ADD INDEX IF NOT EXISTS idx_ticket_type_id (ticket_type_id);

-- 更新攻略表，增加一些字段（如果还没有的话）
ALTER TABLE guides 
ADD COLUMN IF NOT EXISTS min_price DECIMAL(10,2) DEFAULT 0.00 COMMENT '预估最低消费',
ADD COLUMN IF NOT EXISTS max_price DECIMAL(10,2) DEFAULT 0.00 COMMENT '预估最高消费',
ADD COLUMN IF NOT EXISTS duration_days INT DEFAULT 1 COMMENT '建议游玩天数',
ADD COLUMN IF NOT EXISTS difficulty_level INT DEFAULT 1 COMMENT '游玩难度等级：1-简单，2-中等，3-困难';

-- 更新景点表，增加更多字段（如果还没有的话）
ALTER TABLE attractions 
ADD COLUMN IF NOT EXISTS season_recommendations TEXT COMMENT '季节推荐',
ADD COLUMN IF NOT EXISTS best_time_to_visit VARCHAR(100) COMMENT '最佳游览时间',
ADD COLUMN IF NOT EXISTS estimated_duration VARCHAR(50) COMMENT '建议游玩时长',
ADD COLUMN IF NOT EXISTS entrance_fee_info TEXT COMMENT '门票费用信息',
ADD COLUMN IF NOT EXISTS facilities TEXT COMMENT '设施服务',
ADD COLUMN IF NOT EXISTS accessibility TEXT COMMENT '无障碍设施',
ADD COLUMN IF NOT EXISTS parking_info TEXT COMMENT '停车信息';