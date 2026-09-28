package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 景点实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "attractions")
public class Attraction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 景点名称
     */
    @Column(nullable = false, length = 100)
    private String name;

    /**
     * 分类
     */
    @Column(length = 50)
    private String category;

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
     * 介绍
     */
    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * 地址
     */
    @Column(length = 255)
    private String address;

    /**
     * 经度
     */
    @Column(precision = 10, scale = 7)
    private BigDecimal longitude;

    /**
     * 纬度
     */
    @Column(precision = 10, scale = 7)
    private BigDecimal latitude;

    /**
     * 联系电话
     */
    @Column(length = 20)
    private String phone;

    /**
     * 开放时间
     */
    @Column(length = 100)
    private String openTime;

    /**
     * 注意事项
     */
    @Column(columnDefinition = "TEXT")
    private String notes;

    /**
     * 标签（多个标签，逗号分隔）
     */
    @Column(length = 255)
    private String tags;

    /**
     * 最低价格
     */
    @Column(precision = 10, scale = 2)
    private BigDecimal minPrice;

    /**
     * 评分（0-5）
     */
    @Column(precision = 2, scale = 1)
    private BigDecimal rating;

    /**
     * 浏览量
     */
    @Column
    private Integer viewCount = 0;

    /**
     * 预订量
     */
    @Column
    private Integer bookingCount = 0;

    /**
     * 状态：0-下线，1-上线
     */
    @Column(nullable = false)
    private Integer status = 0;

    /**
     * 排序
     */
    @Column
    private Integer sort = 0;

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
     * 门票类型（一对多关系）
     */
    @OneToMany(mappedBy = "attraction", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TicketType> ticketTypes;
}
