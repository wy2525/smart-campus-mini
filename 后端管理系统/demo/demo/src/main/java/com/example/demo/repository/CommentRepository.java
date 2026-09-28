package com.example.demo.repository;

import com.example.demo.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * 评论Repository
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * 根据攻略ID查找评论，预加载用户和攻略数据
     */
    @Query("SELECT c FROM Comment c JOIN FETCH c.user JOIN FETCH c.guide WHERE c.guide.id = :guideId")
    Page<Comment> findByGuideId(@Param("guideId") Long guideId, Pageable pageable);

    /**
     * 根据用户ID查找评论，预加载用户和攻略数据
     */
    @Query("SELECT c FROM Comment c JOIN FETCH c.user JOIN FETCH c.guide WHERE c.user.id = :userId")
    Page<Comment> findByUserId(@Param("userId") Long userId, Pageable pageable);

    /**
     * 搜索评论（内容或用户昵称），预加载用户和攻略数据
     */
    @Query("SELECT c FROM Comment c JOIN FETCH c.user JOIN FETCH c.guide WHERE c.content LIKE %:keyword% OR c.user.nickname LIKE %:keyword%")
    Page<Comment> searchComments(@Param("keyword") String keyword, Pageable pageable);

    /**
     * 根据状态查找评论，预加载用户和攻略数据
     */
    @Query("SELECT c FROM Comment c JOIN FETCH c.user JOIN FETCH c.guide WHERE c.status = :status")
    Page<Comment> findByStatus(@Param("status") Integer status, Pageable pageable);

    /**
     * 根据ID查找评论，预加载用户和攻略数据
     */
    @Query("SELECT c FROM Comment c JOIN FETCH c.user JOIN FETCH c.guide WHERE c.id = :id")
    Comment findByIdWithDetails(@Param("id") Long id);

    /**
     * 统计评论数
     */
    Long countByStatus(Integer status);

    /**
     * 根据攻略ID和状态统计评论数
     */
    Long countByGuideIdAndStatus(Long guideId, Integer status);
}
