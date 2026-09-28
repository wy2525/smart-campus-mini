package com.example.demo.service;

import com.example.demo.entity.SystemConfig;
import com.example.demo.repository.SystemConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 系统配置Service
 */
@Service
@Transactional
public class SystemConfigService {

    @Autowired
    private SystemConfigRepository systemConfigRepository;

    /**
     * 获取所有配置
     */
    public List<SystemConfig> getAllConfigs() {
        return systemConfigRepository.findAll();
    }

    /**
     * 根据ID获取配置详情
     */
    public Optional<SystemConfig> getConfigById(Long id) {
        return systemConfigRepository.findById(id);
    }

    /**
     * 根据配置键获取配置
     */
    public Optional<SystemConfig> getConfigByKey(String configKey) {
        return systemConfigRepository.findByConfigKey(configKey);
    }

    /**
     * 获取配置值
     */
    public String getConfigValue(String configKey) {
        Optional<SystemConfig> configOpt = systemConfigRepository.findByConfigKey(configKey);
        return configOpt.map(SystemConfig::getConfigValue).orElse(null);
    }

    /**
     * 创建配置
     */
    public SystemConfig createConfig(SystemConfig config) {
        return systemConfigRepository.save(config);
    }

    /**
     * 更新配置
     */
    public SystemConfig updateConfig(Long id, SystemConfig config) {
        config.setId(id);
        return systemConfigRepository.save(config);
    }

    /**
     * 删除配置
     */
    public boolean deleteConfig(Long id) {
        if (systemConfigRepository.existsById(id)) {
            systemConfigRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * 更新或创建配置
     */
    public SystemConfig saveOrUpdateConfig(String configKey, String configValue, String description) {
        Optional<SystemConfig> configOpt = systemConfigRepository.findByConfigKey(configKey);
        SystemConfig config;
        if (configOpt.isPresent()) {
            config = configOpt.get();
            config.setConfigValue(configValue);
            if (description != null) {
                config.setDescription(description);
            }
        } else {
            config = new SystemConfig();
            config.setConfigKey(configKey);
            config.setConfigValue(configValue);
            config.setDescription(description);
        }
        return systemConfigRepository.save(config);
    }
}
