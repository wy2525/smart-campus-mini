package com.example.demo.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.entity.GuideDetail;
import com.example.demo.service.GuideDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "攻略详情管理", description = "攻略详情管理相关接口")
@RestController
@RequestMapping("/api/admin/guide-details")
public class GuideDetailController {

    @Autowired
    private GuideDetailService guideDetailService;

    @Operation(summary = "获取攻略详情", description = "根据攻略ID获取攻略详情")
    @GetMapping("/{guideId}")
    public ApiResponse<GuideDetail> getGuideDetail(@Parameter(description = "攻略ID") @PathVariable Long guideId) {
        return guideDetailService.getGuideDetailByGuideId(guideId)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error("攻略详情不存在"));
    }

    @Operation(summary = "创建或更新攻略详情", description = "创建或更新攻略详情信息")
    @PostMapping
    public ApiResponse<GuideDetail> saveGuideDetail(@RequestBody GuideDetail guideDetail) {
        GuideDetail saved = guideDetailService.saveGuideDetail(guideDetail);
        return ApiResponse.success(saved);
    }

    @Operation(summary = "更新攻略详情", description = "更新攻略详情信息")
    @PutMapping("/{guideId}")
    public ApiResponse<GuideDetail> updateGuideDetail(
            @Parameter(description = "攻略ID") @PathVariable Long guideId,
            @RequestBody GuideDetail guideDetail) {
        // 检查攻略详情是否存在
        if (!guideDetailService.existsByGuideId(guideId)) {
            return ApiResponse.error("攻略详情不存在");
        }
        // 设置攻略ID
        guideDetail.setGuide(new com.example.demo.entity.Guide());
        guideDetail.getGuide().setId(guideId);

        GuideDetail saved = guideDetailService.saveGuideDetail(guideDetail);
        return ApiResponse.success(saved);
    }

    @Operation(summary = "删除攻略详情", description = "根据攻略ID删除攻略详情")
    @DeleteMapping("/{guideId}")
    public ApiResponse<Void> deleteGuideDetail(@Parameter(description = "攻略ID") @PathVariable Long guideId) {
        boolean deleted = guideDetailService.deleteByGuideId(guideId);
        if (deleted) {
            return ApiResponse.success();
        }
        return ApiResponse.error("攻略详情不存在");
    }
}