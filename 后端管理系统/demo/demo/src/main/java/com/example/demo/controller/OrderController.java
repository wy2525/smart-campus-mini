package com.example.demo.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.entity.Order;
import com.example.demo.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 订单管理Controller
 */
@Tag(name = "订单管理", description = "订单管理相关接口")
@RestController
@RequestMapping("/api/admin/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Operation(summary = "获取订单列表", description = "分页获取订单列表，支持搜索和筛选")
    @GetMapping
    public ApiResponse<Page<Order>> getOrders(
            @Parameter(description = "搜索关键词（订单号、景点名称、用户手机号）") @RequestParam(required = false) String keyword,
            @Parameter(description = "订单状态") @RequestParam(required = false) String status,
            @Parameter(description = "游玩日期") @RequestParam(required = false) LocalDate visitDate,
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页条数") @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orders = orderService.getOrders(keyword, status, visitDate, pageable);
        return ApiResponse.success(orders);
    }

    @Operation(summary = "获取订单详情", description = "根据订单ID获取订单详情")
    @GetMapping("/{id}")
    public ApiResponse<Order> getOrderById(
            @Parameter(description = "订单ID") @PathVariable Long id) {
        return orderService.getOrderById(id)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error("订单不存在"));
    }

    @Operation(summary = "更新订单信息", description = "更新订单信息")
    @PutMapping("/{id}")
    public ApiResponse<Order> updateOrder(
            @Parameter(description = "订单ID") @PathVariable Long id,
            @RequestBody Order order) {
        Order updated = orderService.updateOrder(id, order);
        return ApiResponse.success(updated);
    }

    @Operation(summary = "更新订单状态", description = "更新订单状态")
    @PutMapping("/{id}/status")
    public ApiResponse<Order> updateOrderStatus(
            @Parameter(description = "订单ID") @PathVariable Long id,
            @Parameter(description = "订单状态") @RequestParam String status) {
        Order order = orderService.updateOrderStatus(id, status);
        if (order != null) {
            return ApiResponse.success(order);
        }
        return ApiResponse.error("订单不存在");
    }

    @Operation(summary = "订单统计", description = "订单统计数据")
    @GetMapping("/statistics")
    public ApiResponse<OrderStatistics> getOrderStatistics() {
        OrderStatistics statistics = new OrderStatistics();
        statistics.setTotalCount(orderService.countByStatus(null) != null ? orderService.countByStatus(null) : 0);
        statistics.setToUseCount(orderService.countByStatus("toUse"));
        statistics.setCompletedCount(orderService.countByStatus("completed"));
        statistics.setRefundedCount(orderService.countByStatus("refunded"));
        statistics.setTotalAmount(orderService.sumTotalAmountByStatus(null) != null ? orderService.sumTotalAmountByStatus(null) : java.math.BigDecimal.ZERO);
        statistics.setTodayAmount(orderService.sumTotalAmountByCreateTimeBetween(
                java.time.LocalDateTime.now().toLocalDate().atStartOfDay(),
                java.time.LocalDateTime.now()
        ));
        return ApiResponse.success(statistics);
    }

    /**
     * 订单统计数据
     */
    public static class OrderStatistics {
        private Long totalCount;
        private Long toUseCount;
        private Long completedCount;
        private Long refundedCount;
        private java.math.BigDecimal totalAmount;
        private java.math.BigDecimal todayAmount;

        public Long getTotalCount() { return totalCount; }
        public void setTotalCount(Long totalCount) { this.totalCount = totalCount; }
        public Long getToUseCount() { return toUseCount; }
        public void setToUseCount(Long toUseCount) { this.toUseCount = toUseCount; }
        public Long getCompletedCount() { return completedCount; }
        public void setCompletedCount(Long completedCount) { this.completedCount = completedCount; }
        public Long getRefundedCount() { return refundedCount; }
        public void setRefundedCount(Long refundedCount) { this.refundedCount = refundedCount; }
        public java.math.BigDecimal getTotalAmount() { return totalAmount; }
        public void setTotalAmount(java.math.BigDecimal totalAmount) { this.totalAmount = totalAmount; }
        public java.math.BigDecimal getTodayAmount() { return todayAmount; }
        public void setTodayAmount(java.math.BigDecimal todayAmount) { this.todayAmount = todayAmount; }
    }
}
