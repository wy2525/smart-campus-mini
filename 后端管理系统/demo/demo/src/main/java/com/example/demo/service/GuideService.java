package com.example.demo.service;

import com.example.demo.entity.Guide;
import com.example.demo.entity.GuideDetail;
import com.example.demo.entity.GuideImage;
import com.example.demo.repository.GuideDetailRepository;
import com.example.demo.repository.GuideImageRepository;
import com.example.demo.repository.GuideRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.request.GuideCreateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 攻略Service
 */
@Service
@Transactional
public class GuideService {

    @Autowired
    private GuideRepository guideRepository;

    @Autowired
    private GuideImageRepository guideImageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageService imageService;

    @Autowired
    private GuideDetailRepository guideDetailRepository;

    /**
     * 分页查询攻略列表
     */
    public Page<Guide> getGuides(String keyword, String auditStatus, Integer status, Pageable pageable) {
        if (keyword != null && !keyword.isEmpty()) {
            return guideRepository.searchGuides(keyword, pageable);
        } else if (auditStatus != null) {
            return guideRepository.findByAuditStatus(auditStatus, pageable);
        } else if (status != null) {
            return guideRepository.findByStatus(status, pageable);
        }
        return guideRepository.findAll(pageable);
    }

    /**
     * 根据ID获取攻略详情（排除已删除的攻略）
     */
    public Optional<Guide> getGuideById(Long id) {
        return guideRepository.findByIdAndIsDeletedFalse(id);
    }

    /**
     * 根据ID获取攻略详情，同时加载用户信息（排除已删除的攻略）
     */
    public Optional<Guide> getGuideByIdWithUser(Long id) {
        return guideRepository.findByIdWithUser(id);
    }

    /**
     * 根据用户ID获取攻略列表
     */
    public Page<Guide> getGuidesByUserId(Long userId, Pageable pageable) {
        return guideRepository.findByUserId(userId, pageable);
    }

    /**
     * 创建攻略
     */
    public Guide createGuide(Guide guide) {
        return guideRepository.save(guide);
    }

    /**
     * 创建攻略（从请求创建）
     */
    public Guide createGuide(GuideCreateRequest request) {
        // 验证用户是否存在
        userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        // 创建攻略实体
        Guide guide = new Guide();
        guide.setUserId(request.getUserId());
        guide.setTitle(request.getTitle());
        guide.setContent(request.getContent());
        guide.setCoverImage(request.getCoverImage());
        guide.setAttractionId(request.getAttractionId());
        guide.setAuditStatus("pending");
        guide.setStatus(0);
        guide.setIsDeleted(false);

        // 保存攻略
        Guide savedGuide = guideRepository.save(guide);

        // 处理图片URL列表
        if (request.getImageUrls() != null && !request.getImageUrls().isEmpty()) {
            // 验证所有图片URL
            List<String> validImageUrls = request.getImageUrls().stream()
                    .filter(imageService::validateImageUrl)
                    .collect(Collectors.toList());

            // 保存图片关联
            for (int i = 0; i < validImageUrls.size(); i++) {
                GuideImage guideImage = new GuideImage();
                guideImage.setGuide(savedGuide);
                guideImage.setImageUrl(validImageUrls.get(i));
                guideImage.setSortOrder(i);
                guideImageRepository.save(guideImage);
            }

            // 如果有封面图但未在图片列表中，添加到列表
            if (savedGuide.getCoverImage() != null && !validImageUrls.contains(savedGuide.getCoverImage())) {
                validImageUrls.add(savedGuide.getCoverImage());
            }

            // 更新攻略的图片字段（逗号分隔）
            savedGuide.setImages(String.join(",", validImageUrls));
            guideRepository.save(savedGuide);
        }

        return savedGuide;
    }

    /**
     * 更新攻略信息
     */
    public Guide updateGuide(Long id, Guide guide) {
        guide.setId(id);
        return guideRepository.save(guide);
    }

    /**
     * 审核通过
     */
    public Guide approveGuide(Long id) {
        Optional<Guide> guideOpt = guideRepository.findById(id);
        if (guideOpt.isPresent()) {
            Guide guide = guideOpt.get();
            guide.setAuditStatus("approved");
            guide.setStatus(1);
            return guideRepository.save(guide);
        }
        return null;
    }

    /**
     * 审核拒绝
     */
    public Guide rejectGuide(Long id, String remark) {
        Optional<Guide> guideOpt = guideRepository.findById(id);
        if (guideOpt.isPresent()) {
            Guide guide = guideOpt.get();
            guide.setAuditStatus("rejected");
            guide.setAuditRemark(remark);
            return guideRepository.save(guide);
        }
        return null;
    }

    /**
     * 更新攻略状态
     */
    public Guide updateGuideStatus(Long id, Integer status) {
        Optional<Guide> guideOpt = guideRepository.findById(id);
        if (guideOpt.isPresent()) {
            Guide guide = guideOpt.get();
            guide.setStatus(status);
            return guideRepository.save(guide);
        }
        return null;
    }

    /**
     * 设置推荐
     */
    public Guide setRecommended(Long id, Boolean recommended) {
        Optional<Guide> guideOpt = guideRepository.findById(id);
        if (guideOpt.isPresent()) {
            Guide guide = guideOpt.get();
            guide.setIsRecommended(recommended);
            return guideRepository.save(guide);
        }
        return null;
    }

    /**
     * 删除攻略
     */
    public boolean deleteGuide(Long id) {
        if (guideRepository.existsById(id)) {
            // 1. 删除关联的攻略详情
            guideDetailRepository.findByGuideId(id).ifPresent(guideDetailRepository::delete);

            // 2. 删除关联的攻略图片
            guideImageRepository.deleteByGuideId(id);

            // 3. 删除攻略本身
            guideRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * 统计攻略数
     */
    public Long countByAuditStatus(String auditStatus) {
        return guideRepository.countByAuditStatus(auditStatus);
    }

    /**
     * 统计待审核攻略数
     */
    public Long countPendingAudit() {
        return guideRepository.countPendingAudit();
    }

    /**
     * 获取所有攻略
     */
    public List<Guide> getAllGuides() {
        return guideRepository.findAll();
    }

    /**
     * 获取热门攻略（按浏览量）
     */
    public List<Guide> getPopularGuidesByView() {
        return guideRepository.findTop10ByStatusOrderByViewCountDesc(1);
    }

    /**
     * 获取热门攻略（按点赞数）
     */
    public List<Guide> getPopularGuidesByLike() {
        return guideRepository.findTop10ByStatusOrderByLikeCountDesc(1);
    }

    /**
     * 增加浏览量
     */
    public void incrementViewCount(Long id) {
        Optional<Guide> guideOpt = guideRepository.findById(id);
        if (guideOpt.isPresent()) {
            Guide guide = guideOpt.get();
            guide.setViewCount((guide.getViewCount() != null ? guide.getViewCount() : 0) + 1);
            guideRepository.save(guide);
        }
    }

    /**
     * 增加点赞数
     */
    public void incrementLikeCount(Long id) {
        Optional<Guide> guideOpt = guideRepository.findById(id);
        if (guideOpt.isPresent()) {
            Guide guide = guideOpt.get();
            guide.setLikeCount(guide.getLikeCount() + 1);
            guideRepository.save(guide);
        }
    }
}
