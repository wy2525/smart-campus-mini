-- ========================================
-- 数据库迁移脚本：添加攻略与景区关联功能
-- 执行日期: 2026-01-20
-- ========================================

USE `travel_system`;

-- ========================================
-- 修改 guides 表，添加 attraction_id 列
-- ========================================
ALTER TABLE `guides`
ADD COLUMN `attraction_id` BIGINT DEFAULT NULL COMMENT '关联景区ID' AFTER `user_id`;

-- 添加索引以优化按景区查询攻略的性能
ALTER TABLE `guides`
ADD KEY `idx_attraction_id` (`attraction_id`);

-- 添加外键约束确保数据完整性
-- 当景区被删除时，将攻略的 attraction_id 设为 NULL
ALTER TABLE `guides`
ADD CONSTRAINT `fk_guide_attraction`
  FOREIGN KEY (`attraction_id`) REFERENCES `attractions` (`id`) ON DELETE SET NULL;

-- ========================================
-- 验证修改
-- ========================================
-- 查看表结构
SHOW CREATE TABLE `guides`;

-- 查看索引
SHOW INDEX FROM `guides` WHERE Key_name = 'idx_attraction_id';

-- 查看外键约束
SELECT
    CONSTRAINT_NAME,
    TABLE_NAME,
    COLUMN_NAME,
    REFERENCED_TABLE_NAME,
    REFERENCED_COLUMN_NAME
FROM
    information_schema.KEY_COLUMN_USAGE
WHERE
    TABLE_SCHEMA = 'travel_system'
    AND TABLE_NAME = 'guides'
    AND REFERENCED_TABLE_NAME = 'attractions';

-- ========================================
-- 迁移完成
-- ========================================
