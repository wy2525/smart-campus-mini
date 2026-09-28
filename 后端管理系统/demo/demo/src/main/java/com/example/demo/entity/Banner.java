package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Banner实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "banners")
public class Banner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 标题
     */
    @Column(nullable = false, length = 100)
    private String title;

    /**
     * 图片URL
     */
    @Column(nullable = false, length = 255)
    private String imageUrl;

    /**
     * 关联景点
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attraction_id")
    @JsonIgnore
    private Attraction attraction;

    /**
     * 跳转链接
     */
    @Column(length = 255)
    private String linkUrl;

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
}
