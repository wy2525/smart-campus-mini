package com.example.demo.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * 攻略创建请求类
 */
@Data
public class GuideCreateRequest {
    @NotNull(message = "用户ID不能为空")
    private Long userId;
    
    @NotBlank(message = "标题不能为空")
    @Size(max = 200, message = "标题长度不能超过200个字符")
    private String title;
    
    @NotBlank(message = "内容不能为空")
    private String content; // 富文本内容
    
    private String coverImage;
    
    @NotNull(message = "景点ID不能为空")
    private Long attractionId;
    
    private List<String> imageUrls; // 图片链接列表
    private List<Long> associatedAttractionIds; // 关联景点ID列表
}