package com.example.demo.repository;

import com.example.demo.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 订单Repository
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

        /**
         * 根据订单号查找订单
         */
        @Query("SELECT o FROM Order o JOIN FETCH o.user JOIN FETCH o.attraction JOIN FETCH o.ticketType WHERE o.orderNo = :orderNo")
        Order findByOrderNo(@Param("orderNo") String orderNo);

        /**
         * 根据用户ID查找订单
         */
        @Query("SELECT o FROM Order o JOIN FETCH o.user JOIN FETCH o.attraction JOIN FETCH o.ticketType WHERE o.user.id = :userId")
        Page<Order> findByUserId(@Param("userId") Long userId, Pageable pageable);

        /**
         * 根据订单状态查找订单
         */
        @Query("SELECT o FROM Order o JOIN FETCH o.user JOIN FETCH o.attraction JOIN FETCH o.ticketType WHERE o.status = :status")
        Page<Order> findByStatus(@Param("status") String status, Pageable pageable);

        /**
         * 搜索订单（订单号、景点名称、用户手机号）
         */
        @Query("SELECT o FROM Order o JOIN FETCH o.user JOIN FETCH o.attraction JOIN FETCH o.ticketType WHERE o.orderNo LIKE CONCAT('%', :keyword, '%') "
                        +
                        "OR o.attraction.name LIKE CONCAT('%', :keyword, '%') " +
                        "OR o.visitorPhone LIKE CONCAT('%', :keyword, '%')")
        Page<Order> searchOrders(@Param("keyword") String keyword, Pageable pageable);

        /**
         * 根据游玩日期查找订单
         */
        @Query("SELECT o FROM Order o JOIN FETCH o.user JOIN FETCH o.attraction JOIN FETCH o.ticketType WHERE o.visitDate = :visitDate")
        Page<Order> findByVisitDate(@Param("visitDate") LocalDate visitDate, Pageable pageable);

        /**
         * 查询所有订单
         */
        @Query("SELECT o FROM Order o JOIN FETCH o.user JOIN FETCH o.attraction JOIN FETCH o.ticketType")
        Page<Order> findAll(Pageable pageable);

        /**
         * 根据ID查找订单，预加载关联数据
         */
        @Query("SELECT o FROM Order o JOIN FETCH o.user JOIN FETCH o.attraction JOIN FETCH o.ticketType WHERE o.id = :id")
        Optional<Order> findById(@Param("id") Long id);

        /**
         * 统计订单数
         */
        Long countByStatus(String status);

        /**
         * 统计订单金额
         */
        @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.status = :status")
        BigDecimal sumTotalAmountByStatus(@Param("status") String status);

        /**
         * 统计时间范围内的订单数
         */
        @Query("SELECT COUNT(o) FROM Order o WHERE o.createTime BETWEEN :startTime AND :endTime")
        Long countByCreateTimeBetween(@Param("startTime") LocalDateTime startTime,
                        @Param("endTime") LocalDateTime endTime);

        /**
         * 统计时间范围内的订单金额
         */
        @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.createTime BETWEEN :startTime AND :endTime")
        BigDecimal sumTotalAmountByCreateTimeBetween(@Param("startTime") LocalDateTime startTime,
                        @Param("endTime") LocalDateTime endTime);

        /**
         * 按景点统计订单数
         */
        @Query("SELECT o.attraction.id, COUNT(o) FROM Order o GROUP BY o.attraction.id ORDER BY COUNT(o) DESC")
        List<Object[]> countByAttraction();
}
