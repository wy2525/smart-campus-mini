package com.example.demo.repository;

import com.example.demo.entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Banner Repository
 */
@Repository
public interface BannerRepository extends JpaRepository<Banner, Long> {

    /**
     * 根据状态查找Banner
     */
    List<Banner> findByStatus(Integer status);

    /**
     * 根据状态和排序查找Banner
     */
    List<Banner> findByStatusOrderBySortAsc(Integer status);
}
