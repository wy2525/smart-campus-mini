package com.example.demo.service;

import com.example.demo.entity.Order;
import com.example.demo.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 订单Service
 */
@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    /**
     * 分页查询订单列表
     */
    public Page<Order> getOrders(String keyword, String status, LocalDate visitDate, Pageable pageable) {
        if (keyword != null && !keyword.isEmpty()) {
            return orderRepository.searchOrders(keyword, pageable);
        } else if (status != null) {
            return orderRepository.findByStatus(status, pageable);
        } else if (visitDate != null) {
            return orderRepository.findByVisitDate(visitDate, pageable);
        }
        return orderRepository.findAll(pageable);
    }

    /**
     * 根据ID获取订单详情
     */
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    /**
     * 根据订单号获取订单
     */
    public Order getOrderByOrderNo(String orderNo) {
        return orderRepository.findByOrderNo(orderNo);
    }

    /**
     * 根据用户ID获取订单列表
     */
    public Page<Order> getOrdersByUserId(Long userId, Pageable pageable) {
        return orderRepository.findByUserId(userId, pageable);
    }

    /**
     * 创建订单
     */
    public Order createOrder(Order order) {
        // 生成订单号
        String orderNo = "ORD" + System.currentTimeMillis()
                + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        order.setOrderNo(orderNo);
        return orderRepository.save(order);
    }

    /**
     * 更新订单信息
     */
    public Order updateOrder(Long id, Order order) {
        order.setId(id);
        return orderRepository.save(order);
    }

    /**
     * 更新订单状态
     */
    public Order updateOrderStatus(Long id, String status) {
        Optional<Order> orderOpt = orderRepository.findById(id);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setStatus(status);
            return orderRepository.save(order);
        }
        return null;
    }

    /**
     * 统计在线景点数
     */
    public Long countByStatus(String status) {
        return orderRepository.countByStatus(status);
    }

    /**
     * 统计所有订单数
     */
    public Long countAll() {
        return orderRepository.count();
    }

    /**
     * 统计订单金额
     */
    public BigDecimal sumTotalAmountByStatus(String status) {
        BigDecimal result = orderRepository.sumTotalAmountByStatus(status);
        return result != null ? result : BigDecimal.ZERO;
    }

    /**
     * 统计时间范围内的订单数
     */
    public Long countByCreateTimeBetween(LocalDateTime startTime, LocalDateTime endTime) {
        return orderRepository.countByCreateTimeBetween(startTime, endTime);
    }

    /**
     * 统计时间范围内的订单金额
     */
    public BigDecimal sumTotalAmountByCreateTimeBetween(LocalDateTime startTime, LocalDateTime endTime) {
        BigDecimal result = orderRepository.sumTotalAmountByCreateTimeBetween(startTime, endTime);
        return result != null ? result : BigDecimal.ZERO;
    }

    /**
     * 按景点统计订单数
     */
    public List<Object[]> countByAttraction() {
        return orderRepository.countByAttraction();
    }

    /**
     * 获取所有订单
     */
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
