package com.example.demo.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.entity.Attraction;
import com.example.demo.entity.TicketType;
import com.example.demo.service.AttractionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 景点管理Controller
 */
@Tag(name = "景点管理", description = "景点管理相关接口")
@RestController
@RequestMapping("/api/admin/attractions")
public class AttractionController {

    @Autowired
    private AttractionService attractionService;

    @Operation(summary = "获取景点列表", description = "分页获取景点列表，支持搜索和筛选")
    @GetMapping
    public ApiResponse<Page<Attraction>> getAttractions(
            @Parameter(description = "搜索关键词（名称或分类）") @RequestParam(required = false) String keyword,
            @Parameter(description = "分类") @RequestParam(required = false) String category,
            @Parameter(description = "状态（0-下线，1-上线）") @RequestParam(required = false) Integer status,
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页条数") @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Attraction> attractions = attractionService.getAttractions(keyword, category, status, pageable);
        return ApiResponse.success(attractions);
    }

    @Operation(summary = "获取景点详情", description = "根据景点ID获取景点详情")
    @GetMapping("/{id}")
    public ApiResponse<Attraction> getAttractionById(
            @Parameter(description = "景点ID") @PathVariable Long id) {
        return attractionService.getAttractionById(id)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error("景点不存在"));
    }

    @Operation(summary = "创建景点", description = "创建新景点")
    @PostMapping
    public ApiResponse<Attraction> createAttraction(@RequestBody Attraction attraction) {
        Attraction created = attractionService.createAttraction(attraction);
        return ApiResponse.success(created);
    }

    @Operation(summary = "更新景点", description = "更新景点信息")
    @PutMapping("/{id}")
    public ApiResponse<Attraction> updateAttraction(
            @Parameter(description = "景点ID") @PathVariable Long id,
            @RequestBody Attraction attraction) {
        Attraction updated = attractionService.updateAttraction(id, attraction);
        return ApiResponse.success(updated);
    }

    @Operation(summary = "更新景点状态", description = "上线或下线景点")
    @PutMapping("/{id}/status")
    public ApiResponse<Attraction> updateAttractionStatus(
            @Parameter(description = "景点ID") @PathVariable Long id,
            @Parameter(description = "状态（0-下线，1-上线）") @RequestParam Integer status) {
        Attraction attraction = attractionService.updateAttractionStatus(id, status);
        if (attraction != null) {
            return ApiResponse.success(attraction);
        }
        return ApiResponse.error("景点不存在");
    }

    @Operation(summary = "更新景点排序", description = "更新景点排序")
    @PutMapping("/{id}/sort")
    public ApiResponse<Attraction> updateAttractionSort(
            @Parameter(description = "景点ID") @PathVariable Long id,
            @Parameter(description = "排序") @RequestParam Integer sort) {
        Attraction attraction = attractionService.getAttractionById(id).orElse(null);
        if (attraction != null) {
            attraction.setSort(sort);
            attractionService.updateAttraction(id, attraction);
            return ApiResponse.success(attraction);
        }
        return ApiResponse.error("景点不存在");
    }

    @Operation(summary = "删除景点", description = "根据景点ID删除景点")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteAttraction(
            @Parameter(description = "景点ID") @PathVariable Long id) {
        boolean deleted = attractionService.deleteAttraction(id);
        if (deleted) {
            return ApiResponse.success();
        }
        return ApiResponse.error("景点不存在");
    }

    @Operation(summary = "获取景点门票类型", description = "获取指定景点的门票类型列表")
    @GetMapping("/{id}/tickets")
    public ApiResponse<List<TicketType>> getAttractionTickets(
            @Parameter(description = "景点ID") @PathVariable Long id) {
        List<TicketType> tickets = attractionService.getAttractionById(id).map(
                attraction -> attraction.getTicketTypes()
        ).orElse(null);
        return ApiResponse.success(tickets);
    }
}
