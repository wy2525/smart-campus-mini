package com.example.demo.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.entity.TicketType;
import com.example.demo.service.TicketTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 门票管理Controller
 */
@Tag(name = "门票管理", description = "门票类型管理相关接口")
@RestController
@RequestMapping("/api/admin/ticket-types")
public class TicketTypeController {

    @Autowired
    private TicketTypeService ticketTypeService;

    @Operation(summary = "获取所有门票类型", description = "获取所有门票类型列表")
    @GetMapping
    public ApiResponse<List<TicketType>> getAllTicketTypes() {
        return ApiResponse.success(ticketTypeService.getAllTicketTypes());
    }

    @Operation(summary = "获取景点门票类型", description = "获取指定景点的门票类型")
    @GetMapping("/attraction/{attractionId}")
    public ApiResponse<List<TicketType>> getTicketTypesByAttractionId(
            @Parameter(description = "景点ID") @PathVariable Long attractionId) {
        return ApiResponse.success(ticketTypeService.getTicketTypesByAttractionId(attractionId));
    }

    @Operation(summary = "获取门票类型详情", description = "根据门票类型ID获取详情")
    @GetMapping("/{id}")
    public ApiResponse<TicketType> getTicketTypeById(
            @Parameter(description = "门票类型ID") @PathVariable Long id) {
        return ticketTypeService.getTicketTypeById(id)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error("门票类型不存在"));
    }

    @Operation(summary = "创建门票类型", description = "为指定景点创建门票类型")
    @PostMapping
    public ApiResponse<TicketType> createTicketType(@RequestBody TicketType ticketType) {
        TicketType created = ticketTypeService.createTicketType(ticketType);
        return ApiResponse.success(created);
    }

    @Operation(summary = "更新门票类型", description = "更新门票类型信息")
    @PutMapping("/{id}")
    public ApiResponse<TicketType> updateTicketType(
            @Parameter(description = "门票类型ID") @PathVariable Long id,
            @RequestBody TicketType ticketType) {
        TicketType updated = ticketTypeService.updateTicketType(id, ticketType);
        return ApiResponse.success(updated);
    }

    @Operation(summary = "更新库存", description = "更新门票类型库存")
    @PutMapping("/{id}/stock")
    public ApiResponse<TicketType> updateStock(
            @Parameter(description = "门票类型ID") @PathVariable Long id,
            @Parameter(description = "库存数量") @RequestParam Integer stock) {
        TicketType ticketType = ticketTypeService.updateStock(id, stock);
        if (ticketType != null) {
            return ApiResponse.success(ticketType);
        }
        return ApiResponse.error("门票类型不存在");
    }

    @Operation(summary = "更新门票类型状态", description = "上架或下架门票类型")
    @PutMapping("/{id}/status")
    public ApiResponse<TicketType> updateTicketTypeStatus(
            @Parameter(description = "门票类型ID") @PathVariable Long id,
            @Parameter(description = "状态（0-下架，1-上架）") @RequestParam Integer status) {
        TicketType ticketType = ticketTypeService.updateTicketTypeStatus(id, status);
        if (ticketType != null) {
            return ApiResponse.success(ticketType);
        }
        return ApiResponse.error("门票类型不存在");
    }

    @Operation(summary = "删除门票类型", description = "根据门票类型ID删除")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteTicketType(
            @Parameter(description = "门票类型ID") @PathVariable Long id) {
        boolean deleted = ticketTypeService.deleteTicketType(id);
        if (deleted) {
            return ApiResponse.success();
        }
        return ApiResponse.error("门票类型不存在");
    }
}
