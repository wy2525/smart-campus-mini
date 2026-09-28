package com.example.demo.service.impl;

import com.example.demo.service.ImageService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 图片服务实现类
 */
@Service
public class ImageServiceImpl implements ImageService {

    @Override
    public boolean validateImageUrl(String url) {
        try {
            // 验证URL格式
            if (url == null || !url.startsWith("http://") && !url.startsWith("https://")) {
                return false;
            }
            
            URL imageUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            
            // 获取响应码
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                return false;
            }
            
            // 验证Content-Type
            String contentType = connection.getContentType();
            return contentType != null && contentType.startsWith("image/");
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public Map<String, Boolean> validateImageUrls(List<String> urls) {
        Map<String, Boolean> result = new HashMap<>();
        if (urls != null && !urls.isEmpty()) {
            for (String url : urls) {
                result.put(url, validateImageUrl(url));
            }
        }
        return result;
    }

    @Override
    public String uploadImage(byte[] fileBytes, String fileName) {
        // 实际项目中，这里应该实现图片上传到文件服务器或云存储的逻辑
        // 这里为了演示，直接返回一个模拟的URL
        return "https://example.com/upload/" + fileName;
    }
}