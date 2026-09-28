package com.example.demo.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.entity.Banner;
import com.example.demo.service.BannerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Banner管理Controller
 */
@Tag(name = "Banner管理", description = "Banner管理相关接口")
@RestController
@RequestMapping("/api/admin/banners")
public class BannerController {

    @Autowired
    private BannerService bannerService;

    @Operation(summary = "获取Banner列表", description = "获取所有Banner列表")
    @GetMapping
    public ApiResponse<List<Banner>> getAllBanners() {
        return ApiResponse.success(bannerService.getAllBanners());
    }

    @Operation(summary = "获取Banner详情", description = "根据Banner ID获取详情")
    @GetMapping("/{id}")
    public ApiResponse<Banner> getBannerById(
            @Parameter(description = "Banner ID") @PathVariable Long id) {
        return bannerService.getBannerById(id)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error("Banner不存在"));
    }

    @Operation(summary = "创建Banner", description = "创建新Banner")
    @PostMapping
    public ApiResponse<Banner> createBanner(@RequestBody Banner banner) {
        Banner created = bannerService.createBanner(banner);
        return ApiResponse.success(created);
    }

    @Operation(summary = "更新Banner", description = "更新Banner信息")
    @PutMapping("/{id}")
    public ApiResponse<Banner> updateBanner(
            @Parameter(description = "Banner ID") @PathVariable Long id,
            @RequestBody Banner banner) {
        Banner updated = bannerService.updateBanner(id, banner);
        return ApiResponse.success(updated);
    }

    @Operation(summary = "更新Banner状态", description = "上线或下线Banner")
    @PutMapping("/{id}/status")
    public ApiResponse<Banner> updateBannerStatus(
            @Parameter(description = "Banner ID") @PathVariable Long id,
            @Parameter(description = "状态（0-下线，1-上线）") @RequestParam Integer status) {
        Banner banner = bannerService.updateBannerStatus(id, status);
        if (banner != null) {
            return ApiResponse.success(banner);
        }
        return ApiResponse.error("Banner不存在");
    }

    @Operation(summary = "更新Banner排序", description = "更新Banner排序")
    @PutMapping("/{id}/sort")
    public ApiResponse<Banner> updateBannerSort(
            @Parameter(description = "Banner ID") @PathVariable Long id,
            @Parameter(description = "排序") @RequestParam Integer sort) {
        Banner banner = bannerService.updateBannerSort(id, sort);
        if (banner != null) {
            return ApiResponse.success(banner);
        }
        return ApiResponse.error("Banner不存在");
    }

    @Operation(summary = "删除Banner", description = "根据Banner ID删除")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteBanner(
            @Parameter(description = "Banner ID") @PathVariable Long id) {
        boolean deleted = bannerService.deleteBanner(id);
        if (deleted) {
            return ApiResponse.success();
        }
        return ApiResponse.error("Banner不存在");
    }
}
