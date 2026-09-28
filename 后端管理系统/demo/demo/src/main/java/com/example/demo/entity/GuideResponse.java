package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 攻略详情响应类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GuideResponse {

    private Long id; // 攻略ID
    
    private Long userId; // 用户ID
    
    private Long attractionId; // 景点ID
    
    private String title; // 攻略标题
    
    private String coverImage; // 封面图片
    
    private String content; // 攻略内容
    
    private LocalDateTime createTime; // 创建时间
    
    private Integer viewCount; // 浏览数
    
    private Integer likeCount; // 点赞数
    
    private Integer favoriteCount; // 收藏数
    
    private Integer commentCount; // 评论数
    
    private String userName; // 用户名
    
    private String userAvatar; // 用户头像
    
    private String attractionName; // 景点名称
    
    private String attractionAddress; // 景点地址
    
    private String auditStatus; // 审核状态
    
    private Integer status; // 状态
}