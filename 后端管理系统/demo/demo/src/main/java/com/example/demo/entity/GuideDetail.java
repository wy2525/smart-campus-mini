package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "guide_details")
public class GuideDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 关联的攻略
     */
    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guide_id", nullable = false)
    private Guide guide;

    /**
     * 详细攻略内容
     */
    @Column(columnDefinition = "LONGTEXT")
    private String detailedContent;

    /**
     * 旅行贴士
     */
    @Column(columnDefinition = "TEXT")
    private String tips;

    /**
     * 行程安排
     */
    @Column(columnDefinition = "TEXT")
    private String itinerary;

    /**
     * 费用信息
     */
    @Column(columnDefinition = "TEXT")
    private String costInfo;

    /**
     * 交通指南
     */
    @Column(columnDefinition = "TEXT")
    private String transportation;

    /**
     * 住宿推荐
     */
    @Column(columnDefinition = "TEXT")
    private String accommodation;

    /**
     * 美食推荐
     */
    @Column(columnDefinition = "TEXT")
    private String foodRecommendations;

    /**
     * 最佳旅行时间
     */
    @Column(length = 100)
    private String bestTime;

    /**
     * 建议游玩时长
     */
    @Column(length = 50)
    private String duration;

    /**
     * 难度等级：1-简单，2-中等，3-困难
     */
    @Column
    private Integer difficultyLevel = 1;

    /**
     * 适合人群
     */
    @Column(columnDefinition = "TEXT")
    private String suitableFor;

    /**
     * 紧急联系方式
     */
    @Column(columnDefinition = "TEXT")
    private String emergencyContact;

    /**
     * 天气信息
     */
    @Column(columnDefinition = "TEXT")
    private String weatherInfo;

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
}