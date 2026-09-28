package com.example.demo.service;

import com.example.demo.entity.TicketType;
import com.example.demo.repository.TicketTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 门票类型Service
 */
@Service
@Transactional
public class TicketTypeService {

    @Autowired
    private TicketTypeRepository ticketTypeRepository;

    /**
     * 根据景点ID获取门票类型列表
     */
    public List<TicketType> getTicketTypesByAttractionId(Long attractionId) {
        return ticketTypeRepository.findByAttractionId(attractionId);
    }

    /**
     * 根据景点ID和状态获取门票类型列表
     */
    public List<TicketType> getTicketTypesByAttractionIdAndStatus(Long attractionId, Integer status) {
        return ticketTypeRepository.findByAttractionIdAndStatus(attractionId, status);
    }

    /**
     * 根据ID获取门票类型详情
     */
    public Optional<TicketType> getTicketTypeById(Long id) {
        return ticketTypeRepository.findById(id);
    }

    /**
     * 创建门票类型
     */
    public TicketType createTicketType(TicketType ticketType) {
        return ticketTypeRepository.save(ticketType);
    }

    /**
     * 更新门票类型
     */
    public TicketType updateTicketType(Long id, TicketType ticketType) {
        ticketType.setId(id);
        return ticketTypeRepository.save(ticketType);
    }

    /**
     * 更新库存
     */
    public TicketType updateStock(Long id, Integer stock) {
        Optional<TicketType> ticketTypeOpt = ticketTypeRepository.findById(id);
        if (ticketTypeOpt.isPresent()) {
            TicketType ticketType = ticketTypeOpt.get();
            ticketType.setStock(stock);
            return ticketTypeRepository.save(ticketType);
        }
        return null;
    }

    /**
     * 更新门票类型状态
     */
    public TicketType updateTicketTypeStatus(Long id, Integer status) {
        Optional<TicketType> ticketTypeOpt = ticketTypeRepository.findById(id);
        if (ticketTypeOpt.isPresent()) {
            TicketType ticketType = ticketTypeOpt.get();
            ticketType.setStatus(status);
            return ticketTypeRepository.save(ticketType);
        }
        return null;
    }

    /**
     * 删除门票类型
     */
    public boolean deleteTicketType(Long id) {
        if (ticketTypeRepository.existsById(id)) {
            ticketTypeRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * 获取所有门票类型
     */
    public List<TicketType> getAllTicketTypes() {
        return ticketTypeRepository.findAll();
    }

    /**
     * 根据状态获取门票类型
     */
    public List<TicketType> getTicketTypesByStatus(Integer status) {
        return ticketTypeRepository.findByStatus(status);
    }
}
