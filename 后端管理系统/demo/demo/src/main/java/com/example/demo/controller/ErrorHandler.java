package com.example.demo.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.entity.GuideResponse;
import com.example.demo.entity.OrderRequest;
import org.springframework.web.bind.annotation.*;

/**
 * 错误处理和日志分析工具类
 */
public class ErrorHandler {

    /**
     * 攻略详情加载失败错误信息
     */
    public static ApiResponse<String> handleGuideDetailError(Exception e, Long guideId) {
        // 构建详细的错误信息
        String errorMessage = String.format("攻略详情加载失败 - 系统环境: Windows, mp版本: 1.06.2504060, 库版本: 3.13.0, 错误: %s, 堆栈: %s", 
                                       e.getMessage(), 
                                       getStackTrace(e));
        
        System.err.println(errorMessage);
        return ApiResponse.error("未获取到攻略数据");
    }

    /**
     * 订单提交失败错误信息
     */
    public static ApiResponse<String> handleOrderCreateError(OrderRequest request, Exception e) {
        String errorDetails = String.format("订单提交失败 - 参数: userId=%s, attractionId=%s, ticketTypeId=%s, quantity=%s, visitDate=%s, visitorName=%s, visitorPhone=%s, 错误: %s, 堆栈: %s", 
                                        request.getUserId(),
                                        request.getAttractionId(),
                                        request.getTicketTypeId(),
                                        request.getQuantity(),
                                        request.getVisitDate(),
                                        request.getVisitorName(),
                                        request.getVisitorPhone(),
                                        e.getMessage(),
                                        getStackTrace(e));
        
        System.err.println(errorDetails);
        return ApiResponse.error("订单创建失败");
    }

    /**
     * 获取完整的调用堆栈信息
     */
    private static String getStackTrace(Exception e) {
        StringBuilder stackTrace = new StringBuilder();
        for (StackTraceElement element : e.getStackTrace()) {
            stackTrace.append(element.getClassName())
                    .append(".")
                    .append(element.getMethodName())
                    .append(" (")
                    .append(element.getFileName())
                    .append(":")
                    .append(element.getLineNumber())
                    .append(")\n");
        }
        return stackTrace.toString();
    }

    /**
     * 验证攻略详情JSON数据结构的完整性
     */
    public static boolean validateGuideDetailJson(GuideResponse guideResponse) {
        if (guideResponse == null) {
            return false;
        }
        
        return guideResponse.getTitle() != null && 
               guideResponse.getContent() != null && 
               guideResponse.getCoverImage() != null &&
               guideResponse.getCreateTime() != null;
    }

    /**
     * 验证订单提交参数的完整性
     */
    public static String validateOrderRequest(OrderRequest request) {
        if (request.getUserId() == null) {
            return "用户ID不能为空";
        }
        if (request.getAttractionId() == null) {
            return "景点ID不能为空";
        }
        if (request.getTicketTypeId() == null) {
            return "门票类型ID不能为空";
        }
        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            return "数量必须大于0";
        }
        if (request.getVisitDate() == null) {
            return "游玩日期不能为空";
        }
        if (request.getVisitorName() == null || request.getVisitorName().trim().isEmpty()) {
            return "游客姓名不能为空";
        }
        if (request.getVisitorPhone() == null || request.getVisitorPhone().trim().isEmpty()) {
            return "游客手机号不能为空";
        }
        return null;
    }
}