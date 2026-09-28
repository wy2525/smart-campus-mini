package com.example.demo.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.entity.Guide;
import com.example.demo.service.GuideService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

/**
 * 攻略管理Controller
 */
@Tag(name = "攻略管理", description = "攻略管理相关接口")
@RestController
@RequestMapping("/api/admin/guides")
public class GuideController {

    @Autowired
    private GuideService guideService;

    @Operation(summary = "获取攻略列表", description = "分页获取攻略列表，支持搜索和筛选")
    @GetMapping
    public ApiResponse<Page<Guide>> getGuides(
            @Parameter(description = "搜索关键词（标题或作者）") @RequestParam(required = false) String keyword,
            @Parameter(description = "审核状态") @RequestParam(required = false) String auditStatus,
            @Parameter(description = "状态（0-下架，1-上架）") @RequestParam(required = false) Integer status,
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页条数") @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Guide> guides = guideService.getGuides(keyword, auditStatus, status, pageable);
        return ApiResponse.success(guides);
    }

    @Operation(summary = "获取攻略详情", description = "根据攻略ID获取攻略详情")
    @GetMapping("/{id}")
    public ApiResponse<Guide> getGuideById(
            @Parameter(description = "攻略ID") @PathVariable Long id) {
        return guideService.getGuideById(id)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error("攻略不存在"));
    }

    @Operation(summary = "审核通过", description = "审核通过攻略")
    @PutMapping("/{id}/approve")
    public ApiResponse<Guide> approveGuide(
            @Parameter(description = "攻略ID") @PathVariable Long id) {
        Guide guide = guideService.approveGuide(id);
        if (guide != null) {
            return ApiResponse.success(guide);
        }
        return ApiResponse.error("攻略不存在");
    }

    @Operation(summary = "审核拒绝", description = "审核拒绝攻略")
    @PutMapping("/{id}/reject")
    public ApiResponse<Guide> rejectGuide(
            @Parameter(description = "攻略ID") @PathVariable Long id,
            @Parameter(description = "拒绝原因") @RequestParam(required = false) String remark) {
        Guide guide = guideService.rejectGuide(id, remark);
        if (guide != null) {
            return ApiResponse.success(guide);
        }
        return ApiResponse.error("攻略不存在");
    }

    @Operation(summary = "更新攻略状态", description = "更新攻略状态")
    @PutMapping("/{id}/status")
    public ApiResponse<Guide> updateGuideStatus(
            @Parameter(description = "攻略ID") @PathVariable Long id,
            @Parameter(description = "状态（0-下架，1-上架）") @RequestParam Integer status) {
        Guide guide = guideService.updateGuideStatus(id, status);
        if (guide != null) {
            return ApiResponse.success(guide);
        }
        return ApiResponse.error("攻略不存在");
    }

    @Operation(summary = "删除攻略", description = "根据攻略ID删除攻略")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteGuide(
            @Parameter(description = "攻略ID") @PathVariable Long id) {
        boolean deleted = guideService.deleteGuide(id);
        if (deleted) {
            return ApiResponse.success();
        }
        return ApiResponse.error("攻略不存在");
    }

    @Operation(summary = "统计待审核攻略", description = "统计待审核攻略数量")
    @GetMapping("/statistics/pending")
    public ApiResponse<Long> countPendingGuides() {
        Long count = guideService.countPendingAudit();
        return ApiResponse.success(count);
    }
}
