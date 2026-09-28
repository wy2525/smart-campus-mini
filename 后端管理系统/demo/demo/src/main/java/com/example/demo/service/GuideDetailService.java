package com.example.demo.service;

import com.example.demo.entity.GuideDetail;
import com.example.demo.repository.GuideDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class GuideDetailService {

    @Autowired
    private GuideDetailRepository guideDetailRepository;

    /**
     * 根据攻略ID获取攻略详情
     */
    public Optional<GuideDetail> getGuideDetailByGuideId(Long guideId) {
        return guideDetailRepository.findByGuideId(guideId);
    }

    /**
     * 创建或更新攻略详情
     */
    public GuideDetail saveGuideDetail(GuideDetail guideDetail) {
        return guideDetailRepository.save(guideDetail);
    }

    /**
     * 检查攻略详情是否存在
     */
    public boolean existsByGuideId(Long guideId) {
        return guideDetailRepository.existsByGuideId(guideId);
    }

    /**
     * 删除攻略详情
     */
    @Transactional
    public boolean deleteByGuideId(Long guideId) {
        if (existsByGuideId(guideId)) {
            guideDetailRepository.deleteByGuideId(guideId);
            return true;
        }
        return false;
    }
}