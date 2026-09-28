package com.example.demo.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理Controller
 */
@Tag(name = "用户管理", description = "用户管理相关接口")
@RestController
@RequestMapping("/api/admin/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "获取用户列表", description = "分页获取用户列表，支持搜索和筛选")
    @GetMapping
    public ApiResponse<Page<User>> getUsers(
            @Parameter(description = "搜索关键词（手机号或昵称）") @RequestParam(required = false) String keyword,
            @Parameter(description = "状态（0-禁用，1-启用）") @RequestParam(required = false) Integer status,
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页条数") @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userService.getUsers(keyword, status, pageable);
        return ApiResponse.success(users);
    }

    @Operation(summary = "获取用户详情", description = "根据用户ID获取用户详情")
    @GetMapping("/{id}")
    public ApiResponse<User> getUserById(
            @Parameter(description = "用户ID") @PathVariable Long id) {
        return userService.getUserById(id)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error("用户不存在"));
    }

    @Operation(summary = "更新用户信息", description = "更新用户基本信息")
    @PutMapping("/{id}")
    public ApiResponse<User> updateUser(
            @Parameter(description = "用户ID") @PathVariable Long id,
            @RequestBody User user) {
        User updatedUser = userService.updateUser(id, user);
        return ApiResponse.success(updatedUser);
    }

    @Operation(summary = "更新用户状态", description = "启用或禁用用户")
    @PutMapping("/{id}/status")
    public ApiResponse<User> updateUserStatus(
            @Parameter(description = "用户ID") @PathVariable Long id,
            @Parameter(description = "状态（0-禁用，1-启用）") @RequestParam Integer status) {
        User user = userService.updateUserStatus(id, status);
        if (user != null) {
            return ApiResponse.success(user);
        }
        return ApiResponse.error("用户不存在");
    }

    @Operation(summary = "删除用户", description = "根据用户ID删除用户")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUser(
            @Parameter(description = "用户ID") @PathVariable Long id) {
        boolean deleted = userService.deleteUser(id);
        if (deleted) {
            return ApiResponse.success();
        }
        return ApiResponse.error("用户不存在");
    }

    @Operation(summary = "获取用户订单", description = "获取指定用户的订单列表")
    @GetMapping("/{id}/orders")
    public ApiResponse<String> getUserOrders(
            @Parameter(description = "用户ID") @PathVariable Long id) {
        return ApiResponse.success("用户订单列表");
    }

    @Operation(summary = "统计用户数", description = "统计用户总数")
    @GetMapping("/statistics/count")
    public ApiResponse<Long> countUsers(
            @Parameter(description = "状态（0-禁用，1-启用）") @RequestParam(required = false) Integer status) {
        Long count = userService.countByStatus(status != null ? status : 1);
        return ApiResponse.success(count);
    }
}
