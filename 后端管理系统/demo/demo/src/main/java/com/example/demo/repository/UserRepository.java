package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 用户Repository
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 根据手机号查找用户
     */
    Optional<User> findByPhone(String phone);

    /**
     * 搜索用户（手机号或昵称）
     */
    @Query("SELECT u FROM User u WHERE u.phone LIKE %:keyword% OR u.nickname LIKE %:keyword%")
    Page<User> searchUsers(@Param("keyword") String keyword, Pageable pageable);

    /**
     * 根据状态查找用户
     */
    Page<User> findByStatus(Integer status, Pageable pageable);

    /**
     * 统计注册时间范围内的用户数
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.registerTime BETWEEN :startTime AND :endTime")
    Long countByRegisterTimeBetween(@Param("startTime") LocalDateTime startTime,
                                     @Param("endTime") LocalDateTime endTime);

    /**
     * 统计总用户数
     */
    Long countByStatus(Integer status);
}
