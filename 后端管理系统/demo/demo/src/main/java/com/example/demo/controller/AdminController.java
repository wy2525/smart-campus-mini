package com.example.demo.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.entity.Admin;
import com.example.demo.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 管理员管理Controller
 */
@Tag(name = "管理员管理", description = "管理员管理相关接口")
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Operation(summary = "管理员登录", description = "管理员登录接口")
    @PostMapping("/login")
    public ApiResponse<Map<String, Object>> login(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");
        
        Optional<Admin> adminOpt = adminService.authenticate(username, password);
        if (adminOpt.isEmpty()) {
            return ApiResponse.error("用户名或密码错误");
        }
        
        Admin admin = adminOpt.get();
        if (admin.getStatus() == 0) {
            return ApiResponse.error("账号已被禁用");
        }
        
        // 生成简单token（实际应该使用JWT）
        String token = "token_" + System.currentTimeMillis() + "_" + admin.getId();
        
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("userInfo", admin);
        
        return ApiResponse.success(data);
    }

    @Operation(summary = "获取管理员列表", description = "获取所有管理员列表")
    @GetMapping
    public ApiResponse<List<Admin>> getAllAdmins() {
        return ApiResponse.success(adminService.getAllAdmins());
    }

    @Operation(summary = "获取管理员详情", description = "根据管理员ID获取详情")
    @GetMapping("/{id}")
    public ApiResponse<Admin> getAdminById(
            @Parameter(description = "管理员ID") @PathVariable Long id) {
        return adminService.getAdminById(id)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error("管理员不存在"));
    }

    @Operation(summary = "创建管理员", description = "创建新管理员")
    @PostMapping
    public ApiResponse<Admin> createAdmin(@RequestBody Admin admin) {
        Admin created = adminService.createAdmin(admin);
        return ApiResponse.success(created);
    }

    @Operation(summary = "更新管理员", description = "更新管理员信息")
    @PutMapping("/{id}")
    public ApiResponse<Admin> updateAdmin(
            @Parameter(description = "管理员ID") @PathVariable Long id,
            @RequestBody Admin admin) {
        Admin updated = adminService.updateAdmin(id, admin);
        return ApiResponse.success(updated);
    }

    @Operation(summary = "更新管理员状态", description = "启用或禁用管理员")
    @PutMapping("/{id}/status")
    public ApiResponse<Admin> updateAdminStatus(
            @Parameter(description = "管理员ID") @PathVariable Long id,
            @Parameter(description = "状态（0-禁用，1-启用）") @RequestParam Integer status) {
        Admin admin = adminService.updateAdminStatus(id, status);
        if (admin != null) {
            return ApiResponse.success(admin);
        }
        return ApiResponse.error("管理员不存在");
    }

    @Operation(summary = "删除管理员", description = "根据管理员ID删除")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteAdmin(
            @Parameter(description = "管理员ID") @PathVariable Long id) {
        boolean deleted = adminService.deleteAdmin(id);
        if (deleted) {
            return ApiResponse.success();
        }
        return ApiResponse.error("管理员不存在");
    }

    @Operation(summary = "重置密码", description = "重置管理员密码")
    @PutMapping("/{id}/password")
    public ApiResponse<Admin> resetPassword(
            @Parameter(description = "管理员ID") @PathVariable Long id,
            @Parameter(description = "新密码") @RequestParam String newPassword) {
        Admin admin = adminService.resetPassword(id, newPassword);
        if (admin != null) {
            return ApiResponse.success(admin);
        }
        return ApiResponse.error("管理员不存在");
    }
}
