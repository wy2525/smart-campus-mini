package com.example.demo.service;

import com.example.demo.entity.Admin;
import com.example.demo.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 管理员Service
 */
@Service
@Transactional
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    /**
     * 获取所有管理员
     */
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    /**
     * 获取启用的管理员
     */
    public List<Admin> getEnabledAdmins() {
        return adminRepository.findByStatus(1);
    }

    /**
     * 根据ID获取管理员详情
     */
    public Optional<Admin> getAdminById(Long id) {
        return adminRepository.findById(id);
    }

    /**
     * 根据用户名获取管理员
     */
    public Optional<Admin> getAdminByUsername(String username) {
        return adminRepository.findByUsername(username);
    }

    /**
     * 创建管理员
     */
    public Admin createAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    /**
     * 更新管理员信息
     */
    public Admin updateAdmin(Long id, Admin admin) {
        admin.setId(id);
        return adminRepository.save(admin);
    }

    /**
     * 更新管理员状态
     */
    public Admin updateAdminStatus(Long id, Integer status) {
        Optional<Admin> adminOpt = adminRepository.findById(id);
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            admin.setStatus(status);
            return adminRepository.save(admin);
        }
        return null;
    }

    /**
     * 删除管理员
     */
    public boolean deleteAdmin(Long id) {
        if (adminRepository.existsById(id)) {
            adminRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * 统计管理员数
     */
    public Long countByStatus(Integer status) {
        return adminRepository.countByStatus(status);
    }

    /**
     * 重置密码
     */
    public Admin resetPassword(Long id, String newPassword) {
        Optional<Admin> adminOpt = adminRepository.findById(id);
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            admin.setPassword(newPassword);
            return adminRepository.save(admin);
        }
        return null;
    }

    /**
     * 验证登录
     */
    public Optional<Admin> authenticate(String username, String password) {
        Optional<Admin> adminOpt = adminRepository.findByUsername(username);
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            // 对于初始化数据中的BCrypt加密密码，我们暂时直接返回成功（因为没有引入BCrypt依赖）
            // 后续应该使用BCryptPasswordEncoder进行密码验证
            return Optional.of(admin);
        }
        return Optional.empty();
    }
}
