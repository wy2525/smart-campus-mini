package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * 用户实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 手机号
     */
    @Column(nullable = false, unique = true, length = 11)
    private String phone;

    /**
     * 昵称
     */
    @Column(length = 50)
    private String nickname;

    /**
     * 头像URL
     */
    @Column(length = 255)
    private String avatar;

    /**
     * 密码（加密存储）
     */
    @Column(length = 255)
    private String password;

    /**
     * 性别：0-未知，1-男，2-女
     */
    @Column
    private Integer gender;

    /**
     * 生日
     */
    @Column
    private String birthday;

    /**
     * 状态：0-禁用，1-启用
     */
    @Column(nullable = false)
    private Integer status = 1;

    /**
     * 注册时间
     */
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime registerTime;

    /**
     * 更新时间
     */
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updateTime;
}
