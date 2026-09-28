package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * 攻略图片关联实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "guide_images")
public class GuideImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 关联攻略
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guide_id", nullable = false)
    private Guide guide;
    
    /**
     * 图片URL
     */
    @Column(nullable = false, length = 255)
    private String imageUrl;
    
    /**
     * 排序顺序
     */
    @Column(nullable = false)
    private Integer sortOrder;
    
    /**
     * 创建时间
     */
    @CreationTimestamp
    private LocalDateTime createTime;
}