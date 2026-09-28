package com.example.demo.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据统计Controller
 */
@Tag(name = "数据统计", description = "数据统计相关接口")
@RestController
@RequestMapping("/api/admin/statistics")
public class StatisticsController {

        @Autowired
        private UserService userService;

        @Autowired
        private AttractionService attractionService;

        @Autowired
        private OrderService orderService;

        @Autowired
        private GuideService guideService;

        @Operation(summary = "总览数据统计", description = "获取系统总览数据")
        @GetMapping("/overview")
        public ApiResponse<Map<String, Object>> getOverviewStatistics() {
                Map<String, Object> data = new HashMap<>();

                // 用户统计
                data.put("totalUsers", userService.countByStatus(1));
                data.put("todayNewUsers", userService.countByRegisterTimeBetween(
                                LocalDate.now().atStartOfDay(),
                                LocalDateTime.now()));

                // 景点统计
                data.put("totalAttractions", attractionService.countAll());
                data.put("onlineAttractions", attractionService.countByStatus(1));

                // 订单统计
                data.put("totalOrders", orderService.countAll() != null ? orderService.countAll() : 0);
                data.put("todayOrders", orderService.countByCreateTimeBetween(
                                LocalDate.now().atStartOfDay(),
                                LocalDateTime.now()));
                data.put("todayAmount", orderService.sumTotalAmountByCreateTimeBetween(
                                LocalDate.now().atStartOfDay(),
                                LocalDateTime.now()) != null ? orderService.sumTotalAmountByCreateTimeBetween(
                                                LocalDate.now().atStartOfDay(),
                                                LocalDateTime.now()) : BigDecimal.ZERO);

                // 攻略统计
                data.put("totalGuides", guideService.getAllGuides().size());
                data.put("pendingGuides", guideService.countPendingAudit());

                return ApiResponse.success(data);
        }

        @Operation(summary = "用户统计", description = "获取用户统计数据")
        @GetMapping("/users")
        public ApiResponse<Map<String, Object>> getUserStatistics() {
                Map<String, Object> data = new HashMap<>();
                data.put("totalUsers", userService.countByStatus(1));
                data.put("todayNewUsers", userService.countByRegisterTimeBetween(
                                LocalDate.now().atStartOfDay(),
                                LocalDateTime.now()));
                data.put("thisMonthNewUsers", userService.countByRegisterTimeBetween(
                                LocalDate.now().withDayOfMonth(1).atStartOfDay(),
                                LocalDateTime.now()));
                return ApiResponse.success(data);
        }

        @Operation(summary = "订单统计", description = "获取订单统计数据")
        @GetMapping("/orders")
        public ApiResponse<Map<String, Object>> getOrderStatistics() {
                Map<String, Object> data = new HashMap<>();
                data.put("totalOrders",
                                orderService.countAll() != null ? orderService.countAll() : 0);
                data.put("todayOrders", orderService.countByCreateTimeBetween(
                                LocalDate.now().atStartOfDay(),
                                LocalDateTime.now()));
                data.put("toUseOrders", orderService.countByStatus("toUse"));
                data.put("completedOrders", orderService.countByStatus("completed"));
                data.put("refundedOrders", orderService.countByStatus("refunded"));
                data.put("totalAmount",
                                orderService.sumTotalAmountByStatus(null) != null
                                                ? orderService.sumTotalAmountByStatus(null)
                                                : BigDecimal.ZERO);
                data.put("todayAmount", orderService.sumTotalAmountByCreateTimeBetween(
                                LocalDate.now().atStartOfDay(),
                                LocalDateTime.now()) != null ? orderService.sumTotalAmountByCreateTimeBetween(
                                                LocalDate.now().atStartOfDay(),
                                                LocalDateTime.now()) : BigDecimal.ZERO);
                return ApiResponse.success(data);
        }

        @Operation(summary = "景点统计", description = "获取景点统计数据")
        @GetMapping("/attractions")
        public ApiResponse<Map<String, Object>> getAttractionStatistics() {
                Map<String, Object> data = new HashMap<>();
                data.put("totalAttractions", attractionService.countAll());
                data.put("onlineAttractions", attractionService.countByStatus(1));
                return ApiResponse.success(data);
        }

        @Operation(summary = "攻略统计", description = "获取攻略统计数据")
        @GetMapping("/guides")
        public ApiResponse<Map<String, Object>> getGuideStatistics() {
                Map<String, Object> data = new HashMap<>();
                data.put("totalGuides", guideService.getAllGuides().size());
                data.put("approvedGuides", guideService.countByAuditStatus("approved"));
                data.put("pendingGuides", guideService.countPendingAudit());
                return ApiResponse.success(data);
        }
}
