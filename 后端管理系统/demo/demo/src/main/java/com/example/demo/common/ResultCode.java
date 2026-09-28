package com.example.demo.common;

/**
 * 响应码枚举
 */
public enum ResultCode {

    /**
     * 成功
     */
    SUCCESS(200, "操作成功"),

    /**
     * 失败
     */
    ERROR(500, "操作失败"),

    /**
     * 参数错误
     */
    PARAM_ERROR(400, "参数错误"),

    /**
     * 未授权
     */
    UNAUTHORIZED(401, "未授权，请先登录"),

    /**
     * 禁止访问
     */
    FORBIDDEN(403, "禁止访问"),

    /**
     * 资源不存在
     */
    NOT_FOUND(404, "资源不存在"),

    /**
     * 资源已存在
     */
    RESOURCE_EXISTS(4001, "资源已存在"),

    /**
     * 用户不存在
     */
    USER_NOT_FOUND(4101, "用户不存在"),

    /**
     * 景点不存在
     */
    ATTRACTION_NOT_FOUND(4201, "景点不存在"),

    /**
     * 订单不存在
     */
    ORDER_NOT_FOUND(4301, "订单不存在"),

    /**
     * 攻略不存在
     */
    GUIDE_NOT_FOUND(4401, "攻略不存在"),

    /**
     * 评论不存在
     */
    COMMENT_NOT_FOUND(4501, "评论不存在"),

    /**
     * Banner不存在
     */
    BANNER_NOT_FOUND(4601, "Banner不存在"),

    /**
     * 管理员不存在
     */
    ADMIN_NOT_FOUND(4701, "管理员不存在"),

    /**
     * 系统配置不存在
     */
    CONFIG_NOT_FOUND(4801, "系统配置不存在");

    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
