package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 用户Service
 */
@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * 分页查询用户列表
     */
    public Page<User> getUsers(String keyword, Integer status, Pageable pageable) {
        if (keyword != null && !keyword.isEmpty()) {
            return userRepository.searchUsers(keyword, pageable);
        } else if (status != null) {
            return userRepository.findByStatus(status, pageable);
        }
        return userRepository.findAll(pageable);
    }

    /**
     * 根据ID获取用户详情
     */
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * 根据手机号获取用户
     */
    public Optional<User> getUserByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

    /**
     * 创建用户
     */
    public User createUser(User user) {
        return userRepository.save(user);
    }

    /**
     * 更新用户信息
     */
    public User updateUser(Long id, User user) {
        user.setId(id);
        return userRepository.save(user);
    }

    /**
     * 更新用户状态
     */
    public User updateUserStatus(Long id, Integer status) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setStatus(status);
            return userRepository.save(user);
        }
        return null;
    }

    /**
     * 删除用户
     */
    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * 统计用户数
     */
    public Long countByStatus(Integer status) {
        return userRepository.countByStatus(status);
    }

    /**
     * 统计时间范围内的用户数
     */
    public Long countByRegisterTimeBetween(LocalDateTime startTime, LocalDateTime endTime) {
        return userRepository.countByRegisterTimeBetween(startTime, endTime);
    }

    /**
     * 获取所有用户
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
