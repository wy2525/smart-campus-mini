package com.example.demo.repository;

import com.example.demo.entity.GuideDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GuideDetailRepository extends JpaRepository<GuideDetail, Long> {

    /**
     * 根据攻略ID查找攻略详情
     */
    Optional<GuideDetail> findByGuideId(Long guideId);

    /**
     * 检查攻略详情是否存在
     */
    boolean existsByGuideId(Long guideId);

    /**
     * 根据攻略ID删除攻略详情
     */
    void deleteByGuideId(Long guideId);
}