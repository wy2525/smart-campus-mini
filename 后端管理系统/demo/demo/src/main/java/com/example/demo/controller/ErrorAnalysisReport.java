package com.example.demo.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.entity.GuideResponse;
import com.example.demo.entity.OrderRequest;

/**
 * 错误分析报告类
 */
public class ErrorAnalysisReport {

    /**
     * 攻略详情加载失败分析
     */
    public static String analyzeGuideDetailError(String errorMessage, Exception e) {
        StringBuilder report = new StringBuilder();
        report.append("\n=== 攻略详情加载失败分析报告 ===\n");
        report.append("错误信息：").append(errorMessage).append("\n");
        report.append("错误类型：").append(e.getClass().getSimpleName()).append("\n");
        report.append("错误位置：").append(getErrorLocation(e)).append("\n");
        report.append("系统环境：Windows, mp版本: 1.06.2504060, 库版本: 3.13.0\n");
        report.append("调用堆栈：\n").append(getStackTrace(e));
        report.append("\n=== 分析建议 ===\n");
        report.append("1. 检查攻略ID是否正确\n");
        report.append("2. 验证数据库连接是否正常\n");
        report.append("3. 确认攻略数据是否存在\n");
        report.append("4. 检查用户权限和访问限制\n");
        
        return report.toString();
    }

    /**
     * 订单提交失败分析
     */
    public static String analyzeOrderCreateError(OrderRequest request, Exception e) {
        StringBuilder report = new StringBuilder();
        report.append("\n=== 订单提交失败分析报告 ===\n");
        report.append("请求参数：\n");
        report.append("  用户ID：").append(request.getUserId()).append("\n");
        report.append("  景点ID：").append(request.getAttractionId()).append("\n");
        report.append("  门票类型ID：").append(request.getTicketTypeId()).append("\n");
        report.append("  数量：").append(request.getQuantity()).append("\n");
        report.append("  游玩日期：").append(request.getVisitDate()).append("\n");
        report.append("  游客姓名：").append(request.getVisitorName()).append("\n");
        report.append("  游客手机号：").append(request.getVisitorPhone()).append("\n");
        report.append("错误信息：").append(e.getMessage()).append("\n");
        report.append("错误位置：").append(getErrorLocation(e)).append("\n");
        report.append("调用堆栈：\n").append(getStackTrace(e));
        
        String validationError = ErrorHandler.validateOrderRequest(request);
        if (validationError != null) {
            report.append("\n=== 参数验证错误 ===\n");
            report.append("  ").append(validationError);
        }
        
        report.append("\n=== 分析建议 ===\n");
        report.append("1. 检查必填参数是否完整\n");
        report.append("2. 验证用户和景点信息是否正确\n");
        report.append("3. 确认门票类型是否存在\n");
        report.append("4. 检查日期格式是否正确\n");
        
        return report.toString();
    }

    /**
     * 攻略详情JSON数据结构分析
     */
    public static String analyzeGuideDetailJson(GuideResponse guideResponse) {
        StringBuilder report = new StringBuilder();
        report.append("\n=== 攻略详情JSON数据结构分析 ===\n");
        report.append("数据完整性：").append(ErrorHandler.validateGuideDetailJson(guideResponse) ? "完整" : "不完整").append("\n");
        
        report.append("关键字段分析：\n");
        report.append("  标题：").append(guideResponse.getTitle() != null ? "存在" : "缺失").append("\n");
        report.append("  内容：").append(guideResponse.getContent() != null ? "存在" : "缺失").append("\n");
        report.append("  封面图片：").append(guideResponse.getCoverImage() != null ? "存在" : "缺失").append("\n");
        report.append("  创建时间：").append(guideResponse.getCreateTime() != null ? "存在" : "缺失").append("\n");
        report.append("  用户信息：").append(guideResponse.getUserName() != null ? "存在" : "缺失").append("\n");
        report.append("  景点信息：").append(guideResponse.getAttractionName() != null ? "存在" : "缺失").append("\n");
        
        report.append("\n=== 数据结构建议 ===\n");
        report.append("确保包含以下关键字段：\n");
        report.append("  - title: 攻略标题\n");
        report.append("  - content: 攻略内容\n");
        report.append("  - coverImage: 封面图片\n");
        report.append("  - createTime: 创建时间\n");
        report.append("  - viewCount: 浏览数\n");
        report.append("  - likeCount: 点赞数\n");
        report.append("  - favoriteCount: 收藏数\n");
        report.append("  - commentCount: 评论数\n");
        
        return report.toString();
    }

    /**
     * 获取错误位置信息
     */
    private static String getErrorLocation(Exception e) {
        if (e.getStackTrace().length > 0) {
            StackTraceElement element = e.getStackTrace()[0];
            return element.getClassName() + "." + element.getMethodName() + 
                   " (" + element.getFileName() + ":" + element.getLineNumber() + ")";
        }
        return "未知位置";
    }

    /**
     * 获取调用堆栈信息
     */
    private static String getStackTrace(Exception e) {
        StringBuilder stackTrace = new StringBuilder();
        for (StackTraceElement element : e.getStackTrace()) {
            stackTrace.append("  at ")
                    .append(element.getClassName())
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
}