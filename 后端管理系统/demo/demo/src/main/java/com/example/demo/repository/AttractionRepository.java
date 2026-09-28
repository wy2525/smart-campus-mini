package com.example.demo.repository;

import com.example.demo.entity.Attraction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 景点Repository
 */
@Repository
public interface AttractionRepository extends JpaRepository<Attraction, Long> {

    /**
     * 搜索景点（名称或分类）
     */
    @Query("SELECT a FROM Attraction a WHERE a.name LIKE %:keyword% OR a.category LIKE %:keyword%")
    Page<Attraction> searchAttractions(@Param("keyword") String keyword, Pageable pageable);

    /**
     * 根据分类查找景点
     */
    Page<Attraction> findByCategory(String category, Pageable pageable);

    /**
     * 根据状态查找景点
     */
    Page<Attraction> findByStatus(Integer status, Pageable pageable);

    /**
     * 根据状态和分类查找景点
     */
    Page<Attraction> findByStatusAndCategory(Integer status, String category, Pageable pageable);

    /**
     * 根据排序字段查找景点
     */
    List<Attraction> findByStatusOrderBySortAsc(Integer status);

    /**
     * 根据浏览量排序查找景点
     */
    List<Attraction> findTop10ByStatusOrderByViewCountDesc(Integer status);

    /**
     * 根据预订量排序查找景点
     */
    List<Attraction> findTop10ByStatusOrderByBookingCountDesc(Integer status);

    /**
     * 统计在线景点数
     */
    Long countByStatus(Integer status);
}
