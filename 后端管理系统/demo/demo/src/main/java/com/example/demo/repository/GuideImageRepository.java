package com.example.demo.repository;

import com.example.demo.entity.GuideImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 攻略图片关联Repository
 */
@Repository
public interface GuideImageRepository extends JpaRepository<GuideImage, Long> {
    /**
     * 根据攻略ID删除关联的图片
     */
    void deleteByGuideId(Long guideId);
}
