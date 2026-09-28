package com.example.demo.repository;

import com.example.demo.entity.Guide;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 攻略Repository
 */
@Repository
public interface GuideRepository extends JpaRepository<Guide, Long> {

    /**
     * 根据用户ID查找攻略
     */
    Page<Guide> findByUserId(Long userId, Pageable pageable);

    /**
     * 根据审核状态查找攻略
     */
    Page<Guide> findByAuditStatus(String auditStatus, Pageable pageable);

    /**
     * 搜索攻略（标题或作者昵称）
     */
    @Query("SELECT g FROM Guide g WHERE g.title LIKE %:keyword% OR g.user.nickname LIKE %:keyword%")
    Page<Guide> searchGuides(@Param("keyword") String keyword, Pageable pageable);

    /**
     * 根据状态查找攻略
     */
    Page<Guide> findByStatus(Integer status, Pageable pageable);

    /**
     * 查找推荐的攻略
     */
    Page<Guide> findByIsRecommendedTrue(Integer status, Pageable pageable);

    /**
     * 根据浏览量排序查找攻略
     */
    List<Guide> findTop10ByStatusOrderByViewCountDesc(Integer status);

    /**
     * 根据点赞数排序查找攻略
     */
    List<Guide> findTop10ByStatusOrderByLikeCountDesc(Integer status);

    /**
     * 统计攻略数
     */
    Long countByAuditStatus(String auditStatus);

    /**
     * 统计待审核攻略数
     */
    @Query("SELECT COUNT(g) FROM Guide g WHERE g.auditStatus = 'pending'")
    Long countPendingAudit();
    
    /**
     * 根据ID获取攻略详情，同时加载用户信息，排除已删除的攻略
     */
    @Query("SELECT g FROM Guide g JOIN FETCH g.user WHERE g.id = :id AND g.isDeleted = false")
    Optional<Guide> findByIdWithUser(@Param("id") Long id);

    /**
     * 根据ID查找未删除的攻略
     */
    Optional<Guide> findByIdAndIsDeletedFalse(Long id);
}
