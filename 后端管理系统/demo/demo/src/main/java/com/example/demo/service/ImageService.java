package com.example.demo.service;

import java.util.Map;
import java.util.List;

/**
 * 图片服务接口
 */
public interface ImageService {
    
    /**
     * 验证图片链接有效性
     * @param url 图片链接
     * @return 是否有效
     */
    boolean validateImageUrl(String url);
    
    /**
     * 批量验证图片链接有效性
     * @param urls 图片链接列表
     * @return 验证结果，key为图片链接，value为是否有效
     */
    Map<String, Boolean> validateImageUrls(List<String> urls);
    
    /**
     * 上传图片到服务器
     * @param fileBytes 图片字节数组
     * @param fileName 文件名
     * @return 图片URL
     */
    String uploadImage(byte[] fileBytes, String fileName);
}