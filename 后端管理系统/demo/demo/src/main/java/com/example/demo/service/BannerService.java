package com.example.demo.service;

import com.example.demo.entity.Banner;
import com.example.demo.repository.BannerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Banner Service
 */
@Service
@Transactional
public class BannerService {

    @Autowired
    private BannerRepository bannerRepository;

    /**
     * 获取所有Banner
     */
    public List<Banner> getAllBanners() {
        return bannerRepository.findAll();
    }

    /**
     * 获取在线Banner（按排序）
     */
    public List<Banner> getOnlineBanners() {
        return bannerRepository.findByStatusOrderBySortAsc(1);
    }

    /**
     * 根据ID获取Banner详情
     */
    public Optional<Banner> getBannerById(Long id) {
        return bannerRepository.findById(id);
    }

    /**
     * 创建Banner
     */
    public Banner createBanner(Banner banner) {
        return bannerRepository.save(banner);
    }

    /**
     * 更新Banner
     */
    public Banner updateBanner(Long id, Banner banner) {
        banner.setId(id);
        return bannerRepository.save(banner);
    }

    /**
     * 更新Banner状态
     */
    public Banner updateBannerStatus(Long id, Integer status) {
        Optional<Banner> bannerOpt = bannerRepository.findById(id);
        if (bannerOpt.isPresent()) {
            Banner banner = bannerOpt.get();
            banner.setStatus(status);
            return bannerRepository.save(banner);
        }
        return null;
    }

    /**
     * 更新Banner排序
     */
    public Banner updateBannerSort(Long id, Integer sort) {
        Optional<Banner> bannerOpt = bannerRepository.findById(id);
        if (bannerOpt.isPresent()) {
            Banner banner = bannerOpt.get();
            banner.setSort(sort);
            return bannerRepository.save(banner);
        }
        return null;
    }

    /**
     * 删除Banner
     */
    public boolean deleteBanner(Long id) {
        if (bannerRepository.existsById(id)) {
            bannerRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
