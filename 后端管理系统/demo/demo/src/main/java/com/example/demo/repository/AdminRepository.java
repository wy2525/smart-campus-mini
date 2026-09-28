package com.example.demo.repository;

import com.example.demo.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 管理员Repository
 */
@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    /**
     * 根据用户名查找管理员
     */
    Optional<Admin> findByUsername(String username);

    /**
     * 根据状态查找管理员
     */
    List<Admin> findByStatus(Integer status);

    /**
     * 统计管理员数
     */
    Long countByStatus(Integer status);
}
