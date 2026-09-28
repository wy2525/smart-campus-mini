package com.example.demo.repository;

import com.example.demo.entity.TicketType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 门票类型Repository
 */
@Repository
public interface TicketTypeRepository extends JpaRepository<TicketType, Long> {

    /**
     * 根据景点ID查找门票类型
     */
    List<TicketType> findByAttractionId(Long attractionId);

    /**
     * 根据景点ID和状态查找门票类型
     */
    List<TicketType> findByAttractionIdAndStatus(Long attractionId, Integer status);

    /**
     * 根据状态查找门票类型
     */
    List<TicketType> findByStatus(Integer status);
}
