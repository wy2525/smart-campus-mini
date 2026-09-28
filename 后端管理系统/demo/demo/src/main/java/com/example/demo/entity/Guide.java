package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * 攻略实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "guides")
public class Guide {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * 关联景区ID
     */
    @Column(name = "attraction_id")
    private Long attractionId;

    /**
     * 标题
     */
    @Column(nullable = false, length = 200)
    private String title;

    /**
     * 封面图URL
     */
    @Column(length = 255)
    private String coverImage;

    /**
     * 图片集（多个URL，逗号分隔）
     */
    @Column(columnDefinition = "TEXT")
    private String images;

    /**
     * 内容（富文本）
     */
    @Column(columnDefinition = "TEXT")
    private String content;

    /**
     * 审核状态：pending-待审核，approved-已通过，rejected-已拒绝
     */
    @Column(nullable = false, length = 20)
    private String auditStatus = "pending";

    /**
     * 审核意见
     */
    @Column(columnDefinition = "TEXT")
    private String auditRemark;

    /**
     * 状态：0-下架，1-上架
     */
    @Column(nullable = false)
    private Integer status = 0;

    /**
     * 是否推荐
     */
    @Column
    private Boolean isRecommended = false;

    /**
     * 浏览量
     */
    @Column
    private Integer viewCount = 0;

    /**
     * 点赞数
     */
    @Column
    private Integer likeCount = 0;

    /**
     * 收藏数
     */
    @Column
    private Integer favoriteCount = 0;

    /**
     * 评论数
     */
    @Column
    private Integer commentCount = 0;

    /**
     * 创建时间
     */
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updateTime;

    /**
     * 删除时间（软删除标记）
     */
    @Column
    private LocalDateTime deletedAt;

    /**
     * 是否已删除（软删除标记）
     */
    @Column(nullable = false)
    private Boolean isDeleted = false;

    public void setUserId(@NotNull(message = "用户ID不能为空") Long userId) {
    }
}
