package com.example.demo.service;

import com.example.demo.entity.Attraction;
import com.example.demo.repository.AttractionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 景点Service
 */
@Service
@Transactional
public class AttractionService {

    @Autowired
    private AttractionRepository attractionRepository;

    /**
     * 分页查询景点列表
     */
    public Page<Attraction> getAttractions(String keyword, String category, Integer status, Pageable pageable) {
        if (keyword != null && !keyword.isEmpty()) {
            return attractionRepository.searchAttractions(keyword, pageable);
        } else if (category != null && status != null) {
            return attractionRepository.findByStatusAndCategory(status, category, pageable);
        } else if (category != null) {
            return attractionRepository.findByCategory(category, pageable);
        } else if (status != null) {
            return attractionRepository.findByStatus(status, pageable);
        }
        return attractionRepository.findAll(pageable);
    }

    /**
     * 根据ID获取景点详情
     */
    public Optional<Attraction> getAttractionById(Long id) {
        return attractionRepository.findById(id);
    }

    /**
     * 创建景点
     */
    public Attraction createAttraction(Attraction attraction) {
        return attractionRepository.save(attraction);
    }

    /**
     * 更新景点信息
     */
    @Transactional
    public Attraction updateAttraction(Long id, Attraction attraction) {
        // 获取现有景点信息
        Attraction existingAttraction = attractionRepository.findById(id).orElse(null);
        if (existingAttraction == null) {
            throw new RuntimeException("景点不存在");
        }
        
        // 保留现有关联的门票类型，只更新其他字段
        attraction.setTicketTypes(existingAttraction.getTicketTypes());
        attraction.setId(id);
        
        // 保存更新
        return attractionRepository.save(attraction);
    }

    /**
     * 更新景点状态
     */
    public Attraction updateAttractionStatus(Long id, Integer status) {
        Optional<Attraction> attractionOpt = attractionRepository.findById(id);
        if (attractionOpt.isPresent()) {
            Attraction attraction = attractionOpt.get();
            attraction.setStatus(status);
            return attractionRepository.save(attraction);
        }
        return null;
    }

    /**
     * 删除景点
     */
    public boolean deleteAttraction(Long id) {
        if (attractionRepository.existsById(id)) {
            attractionRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * 获取在线景点列表（按排序）
     */
    public List<Attraction> getOnlineAttractions() {
        return attractionRepository.findByStatusOrderBySortAsc(1);
    }

    /**
     * 获取热门景点（按浏览量）
     */
    public List<Attraction> getPopularAttractionsByView() {
        return attractionRepository.findTop10ByStatusOrderByViewCountDesc(1);
    }

    /**
     * 获取热门景点（按预订量）
     */
    public List<Attraction> getPopularAttractionsByBooking() {
        return attractionRepository.findTop10ByStatusOrderByBookingCountDesc(1);
    }

    /**
     * 统计在线景点数
     */
    public Long countByStatus(Integer status) {
        return attractionRepository.countByStatus(status);
    }

    /**
     * 统计所有景点数
     */
    public Long countAll() {
        return attractionRepository.count();
    }

    /**
     * 增加浏览量
     */
    public void incrementViewCount(Long id) {
        Optional<Attraction> attractionOpt = attractionRepository.findById(id);
        if (attractionOpt.isPresent()) {
            Attraction attraction = attractionOpt.get();
            attraction.setViewCount(attraction.getViewCount() + 1);
            attractionRepository.save(attraction);
        }
    }

    /**
     * 增加预订量
     */
    public void incrementBookingCount(Long id) {
        Optional<Attraction> attractionOpt = attractionRepository.findById(id);
        if (attractionOpt.isPresent()) {
            Attraction attraction = attractionOpt.get();
            attraction.setBookingCount(attraction.getBookingCount() + 1);
            attractionRepository.save(attraction);
        }
    }
}
