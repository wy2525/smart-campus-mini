package com.example.demo.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.entity.Comment;
import com.example.demo.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 评论管理Controller
 */
@Tag(name = "评论管理", description = "评论管理相关接口")
@RestController
@RequestMapping("/api/admin/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Operation(summary = "获取评论列表", description = "分页获取评论列表，支持搜索和筛选")
    @GetMapping
    public ApiResponse<Page<Comment>> getComments(
            @Parameter(description = "搜索关键词（评论内容或用户昵称）") @RequestParam(required = false) String keyword,
            @Parameter(description = "状态（0-隐藏，1-显示）") @RequestParam(required = false) Integer status,
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页条数") @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> comments = commentService.getComments(keyword, status, pageable);
        return ApiResponse.success(comments);
    }

    @Operation(summary = "获取评论详情", description = "根据评论ID获取评论详情")
    @GetMapping("/{id}")
    public ApiResponse<Comment> getCommentById(
            @Parameter(description = "评论ID") @PathVariable Long id) {
        return commentService.getCommentById(id)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error("评论不存在"));
    }

    @Operation(summary = "更新评论状态", description = "更新评论状态")
    @PutMapping("/{id}/status")
    public ApiResponse<Comment> updateCommentStatus(
            @Parameter(description = "评论ID") @PathVariable Long id,
            @Parameter(description = "状态（0-隐藏，1-显示）") @RequestParam Integer status) {
        Comment comment = commentService.updateCommentStatus(id, status);
        if (comment != null) {
            return ApiResponse.success(comment);
        }
        return ApiResponse.error("评论不存在");
    }

    @Operation(summary = "删除评论", description = "根据评论ID删除评论")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteComment(
            @Parameter(description = "评论ID") @PathVariable Long id) {
        boolean deleted = commentService.deleteComment(id);
        if (deleted) {
            return ApiResponse.success();
        }
        return ApiResponse.error("评论不存在");
    }

    @Operation(summary = "批量删除评论", description = "批量删除评论")
    @DeleteMapping("/batch")
    public ApiResponse<Void> deleteComments(
            @Parameter(description = "评论ID列表") @RequestBody List<Long> ids) {
        commentService.deleteComments(ids);
        return ApiResponse.success();
    }
}
