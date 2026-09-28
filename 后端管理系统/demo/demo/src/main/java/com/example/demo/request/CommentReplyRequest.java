package com.example.demo.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 评论回复请求类
 */
@Data
public class CommentReplyRequest {
    @NotNull(message = "用户ID不能为空")
    private Long userId;
    
    @NotNull(message = "攻略ID不能为空")
    private Long guideId;
    
    @NotNull(message = "父评论ID不能为空")
    private Long parentId;
    
    @NotBlank(message = "回复内容不能为空")
    @Size(max = 500, message = "回复内容长度不能超过500个字符")
    private String content;
}