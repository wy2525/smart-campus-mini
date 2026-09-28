package com.example.demo.service;

import com.example.demo.entity.Comment;
import com.example.demo.entity.Guide;
import com.example.demo.entity.User;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.GuideRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.request.CommentCreateRequest;
import com.example.demo.request.CommentReplyRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 评论Service
 */
@Service
@Transactional
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private GuideRepository guideRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * 分页查询评论列表
     */
    public Page<Comment> getComments(String keyword, Integer status, Pageable pageable) {
        if (keyword != null && !keyword.isEmpty()) {
            return commentRepository.searchComments(keyword, pageable);
        } else if (status != null) {
            return commentRepository.findByStatus(status, pageable);
        }
        return commentRepository.findAll(pageable);
    }

    /**
     * 根据ID获取评论详情
     */
    public Optional<Comment> getCommentById(Long id) {
        return commentRepository.findById(id);
    }

    /**
     * 根据攻略ID获取评论列表
     */
    public Page<Comment> getCommentsByGuideId(Long guideId, Pageable pageable) {
        return commentRepository.findByGuideId(guideId, pageable);
    }

    /**
     * 根据用户ID获取评论列表
     */
    public Page<Comment> getCommentsByUserId(Long userId, Pageable pageable) {
        return commentRepository.findByUserId(userId, pageable);
    }

    /**
     * 创建评论
     */
    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }

    /**
     * 创建评论（从请求创建）
     */
    public Comment createComment(CommentCreateRequest request) {
        // 验证用户是否存在
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        // 验证攻略是否存在
        Guide guide = guideRepository.findById(request.getGuideId())
                .orElseThrow(() -> new RuntimeException("攻略不存在"));

        // 创建评论实体
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setGuide(guide);
        comment.setContent(request.getContent());
        comment.setStatus(1); // 默认审核通过

        // 保存评论
        Comment savedComment = commentRepository.save(comment);

        // 更新攻略的评论数
        updateGuideCommentCount(request.getGuideId());

        return savedComment;
    }

    /**
     * 回复评论
     */
    public Comment createCommentReply(CommentReplyRequest request) {
        // 验证用户是否存在
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        // 验证攻略是否存在
        Guide guide = guideRepository.findById(request.getGuideId())
                .orElseThrow(() -> new RuntimeException("攻略不存在"));

        // 验证父评论是否存在
        Comment parentComment = commentRepository.findById(request.getParentId())
                .orElseThrow(() -> new RuntimeException("父评论不存在"));

        // 创建回复评论实体
        Comment reply = new Comment();
        reply.setUser(user);
        reply.setGuide(guide);
        reply.setParent(parentComment);
        reply.setContent(request.getContent());
        reply.setStatus(1); // 默认审核通过

        // 保存回复
        Comment savedReply = commentRepository.save(reply);

        // 更新攻略的评论数
        updateGuideCommentCount(request.getGuideId());

        return savedReply;
    }

    /**
     * 更新攻略评论数
     */
    private void updateGuideCommentCount(Long guideId) {
        guideRepository.findById(guideId).ifPresent(guide -> {
            Long commentCount = commentRepository.countByGuideIdAndStatus(guideId, 1);
            guide.setCommentCount(commentCount.intValue());
            guideRepository.save(guide);
        });
    }

    /**
     * 更新评论状态
     */
    public Comment updateCommentStatus(Long id, Integer status) {
        Optional<Comment> commentOpt = commentRepository.findById(id);
        if (commentOpt.isPresent()) {
            Comment comment = commentOpt.get();
            comment.setStatus(status);
            return commentRepository.save(comment);
        }
        return null;
    }

    /**
     * 更新评论
     */
    public Comment updateComment(Long id, Comment comment) {
        comment.setId(id);
        return commentRepository.save(comment);
    }

    /**
     * 删除评论
     */
    public boolean deleteComment(Long id) {
        Optional<Comment> commentOpt = commentRepository.findById(id);
        if (commentOpt.isPresent()) {
            Comment comment = commentOpt.get();
            Long guideId = comment.getGuide().getId();
            commentRepository.deleteById(id);
            // 更新攻略的评论数
            updateGuideCommentCount(guideId);
            return true;
        }
        return false;
    }

    /**
     * 批量删除评论
     */
    public void deleteComments(List<Long> ids) {
        // 收集所有需要更新评论数的攻略ID
        ids.forEach(id -> {
            Optional<Comment> commentOpt = commentRepository.findById(id);
            if (commentOpt.isPresent()) {
                Comment comment = commentOpt.get();
                Long guideId = comment.getGuide().getId();
                commentRepository.deleteById(id);
                // 更新攻略的评论数
                updateGuideCommentCount(guideId);
            }
        });
    }

    /**
     * 统计评论数
     */
    public Long countByStatus(Integer status) {
        return commentRepository.countByStatus(status);
    }

    /**
     * 获取所有评论
     */
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }
}
