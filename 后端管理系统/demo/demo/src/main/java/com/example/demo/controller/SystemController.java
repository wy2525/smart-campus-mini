package com.example.demo.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.entity.SystemConfig;
import com.example.demo.service.SystemConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统管理Controller
 */
@Tag(name = "系统管理", description = "系统配置管理相关接口")
@RestController
@RequestMapping("/api/admin/config")
public class SystemController {

    @Autowired
    private SystemConfigService systemConfigService;

    @Operation(summary = "获取系统配置", description = "获取所有系统配置")
    @GetMapping
    public ApiResponse<List<SystemConfig>> getAllConfigs() {
        return ApiResponse.success(systemConfigService.getAllConfigs());
    }

    @Operation(summary = "获取配置详情", description = "根据配置ID获取详情")
    @GetMapping("/{id}")
    public ApiResponse<SystemConfig> getConfigById(
            @Parameter(description = "配置ID") @PathVariable Long id) {
        return systemConfigService.getConfigById(id)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error("配置不存在"));
    }

    @Operation(summary = "获取配置值", description = "根据配置键获取配置值")
    @GetMapping("/key/{configKey}")
    public ApiResponse<String> getConfigValue(
            @Parameter(description = "配置键") @PathVariable String configKey) {
        String value = systemConfigService.getConfigValue(configKey);
        if (value != null) {
            return ApiResponse.success(value);
        }
        return ApiResponse.error("配置不存在");
    }

    @Operation(summary = "更新系统配置", description = "更新系统配置")
    @PutMapping
    public ApiResponse<Void> updateConfig(
            @RequestBody Map<String, String> configMap) {
        configMap.forEach((key, value) -> {
            systemConfigService.saveOrUpdateConfig(key, value, null);
        });
        return ApiResponse.success();
    }

    @Operation(summary = "创建配置", description = "创建新配置")
    @PostMapping
    public ApiResponse<SystemConfig> createConfig(@RequestBody SystemConfig config) {
        SystemConfig created = systemConfigService.createConfig(config);
        return ApiResponse.success(created);
    }

    @Operation(summary = "更新配置", description = "更新配置")
    @PutMapping("/{id}")
    public ApiResponse<SystemConfig> updateConfig(
            @Parameter(description = "配置ID") @PathVariable Long id,
            @RequestBody SystemConfig config) {
        SystemConfig updated = systemConfigService.updateConfig(id, config);
        return ApiResponse.success(updated);
    }

    @Operation(summary = "删除配置", description = "根据配置ID删除")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteConfig(
            @Parameter(description = "配置ID") @PathVariable Long id) {
        boolean deleted = systemConfigService.deleteConfig(id);
        if (deleted) {
            return ApiResponse.success();
        }
        return ApiResponse.error("配置不存在");
    }
}
