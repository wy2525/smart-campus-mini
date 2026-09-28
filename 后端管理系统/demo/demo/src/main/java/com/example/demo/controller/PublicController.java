package com.example.demo.controller;

import com.example.demo.common.ApiResponse;
import com.example.demo.entity.*;
import com.example.demo.request.*;
import com.example.demo.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * 公开API Controller
 * 提供给小程序使用的公开接口
 */
@Tag(name = "公开接口", description = "小程序端公开API接口")
@RestController
@RequestMapping("/api/public")
@CrossOrigin(origins = "*")
public class PublicController {

    @Autowired
    private UserService userService;

    @Autowired
    private AttractionService attractionService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private GuideService guideService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private BannerService bannerService;

    @Autowired
    private TicketTypeService ticketTypeService;

    // ==================== 用户认证相关 ====================

    @Operation(summary = "用户登录", description = "用户通过手机号登录")
    @PostMapping("/user/login")
    public ApiResponse<Map<String, Object>> login(@RequestBody Map<String, String> request) {
        String phone = request.get("phone");
        String password = request.get("password");
        
        // 验证必填字段
        if (phone == null || phone.trim().isEmpty()) {
            return ApiResponse.error("手机号不能为空");
        }
        if (password == null || password.trim().isEmpty()) {
            return ApiResponse.error("密码不能为空");
        }
        
        Optional<User> userOpt = userService.getUserByPhone(phone);
        if (!userOpt.isPresent()) {
            return ApiResponse.error("用户不存在");
        }
        User user = userOpt.get();
        if (user.getStatus() == 0) {
            return ApiResponse.error("账号已被禁用");
        }
        
        // 验证密码（简单比较，实际项目应使用加密验证）
        if (user.getPassword() != null && !user.getPassword().equals(password)) {
            return ApiResponse.error("密码错误");
        }
        
        // 生成token
        String token = "TOKEN_" + System.currentTimeMillis() + "_" + user.getId();
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        
        // 返回用户信息（不包含密码）
        Map<String, Object> userData = new HashMap<>();
        userData.put("id", user.getId());
        userData.put("phone", user.getPhone());
        userData.put("nickname", user.getNickname());
        userData.put("avatar", user.getAvatar());
        result.put("user", userData);
        
        return ApiResponse.success(result);
    }

    @Operation(summary = "用户注册", description = "用户注册")
    @PostMapping("/user/register")
    public ApiResponse<User> register(@RequestBody Map<String, String> request) {
        String phone = request.get("phone");
        String password = request.get("password");
        String nickname = request.get("nickname");
        
        // 验证必填字段
        if (phone == null || phone.trim().isEmpty()) {
            return ApiResponse.error("手机号不能为空");
        }
        if (password == null || password.trim().isEmpty()) {
            return ApiResponse.error("密码不能为空");
        }
        if (password.length() < 6 || password.length() > 20) {
            return ApiResponse.error("密码长度必须在6-20位之间");
        }
        
        // 验证手机号格式
        if (!phone.matches("^1[3-9]\\d{9}$")) {
            return ApiResponse.error("手机号格式不正确");
        }
        
        Optional<User> userOpt = userService.getUserByPhone(phone);
        if (userOpt.isPresent()) {
            return ApiResponse.error("手机号已注册");
        }
        
        User user = new User();
        user.setPhone(phone);
        user.setPassword(password); // 实际项目中应加密存储
        user.setNickname(nickname != null && !nickname.trim().isEmpty() ? nickname : "用户" + phone.substring(7));
        user.setStatus(1);
        user.setAvatar("https://mmbiz.qpic.cn/mmbiz/icTdbqWNOwNRna42FI242Lxia07jQodd2G6xOqniaKGWbM66M0HsWNC5pZqFKicBlnB61ic42iaic23rBjG2PpBhG88iaQA/0");
        
        User created = userService.createUser(user);
        return ApiResponse.success(created);
    }

    @Operation(summary = "获取用户信息", description = "根据token获取用户信息")
    @GetMapping("/user/info")
    public ApiResponse<User> getUserInfo(
            @Parameter(description = "用户ID") @RequestParam Long userId) {
        return userService.getUserById(userId)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error("用户不存在"));
    }

    @Operation(summary = "更新用户信息", description = "更新用户头像、昵称等信息")
    @PutMapping("/user/info")
    public ApiResponse<User> updateUserInfo(@RequestBody User user) {
        User updated = userService.updateUser(user.getId(), user);
        return ApiResponse.success(updated);
    }

    // ==================== 景点相关 ====================

    @Operation(summary = "获取景点列表", description = "分页获取景点列表")
    @GetMapping("/attractions")
    public ApiResponse<Page<Attraction>> getAttractions(
            @Parameter(description = "搜索关键词") @RequestParam(required = false) String keyword,
            @Parameter(description = "分类") @RequestParam(required = false) String category,
            @Parameter(description = "排序：default-默认，price_asc-价格升序，price_desc-价格降序，rating-评分") @RequestParam(defaultValue = "default") String sortBy,
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页条数") @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Attraction> attractions = attractionService.getAttractions(keyword, category, 1, pageable);
        return ApiResponse.success(attractions);
    }

    @Operation(summary = "获取热门景点", description = "获取热门景点（按预订量排序）")
    @GetMapping("/attractions/hot")
    public ApiResponse<List<Attraction>> getHotAttractions(
            @Parameter(description = "返回数量") @RequestParam(defaultValue = "8") int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        Page<Attraction> attractions = attractionService.getAttractions(null, null, 1, pageable);
        return ApiResponse.success(attractions.getContent());
    }

    @Operation(summary = "获取推荐景点", description = "获取推荐景点（按评分排序）")
    @GetMapping("/attractions/recommend")
    public ApiResponse<List<Attraction>> getRecommendAttractions(
            @Parameter(description = "返回数量") @RequestParam(defaultValue = "10") int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        Page<Attraction> attractions = attractionService.getAttractions(null, null, 1, pageable);
        return ApiResponse.success(attractions.getContent());
    }

    @Operation(summary = "获取景点详情", description = "根据景点ID获取详情")
    @GetMapping("/attractions/{id}")
    public ApiResponse<Attraction> getAttractionDetail(
            @Parameter(description = "景点ID") @PathVariable Long id) {
        Optional<Attraction> attractionOpt = attractionService.getAttractionById(id);
        if (!attractionOpt.isPresent()) {
            return ApiResponse.error("景点不存在");
        }
        Attraction attraction = attractionOpt.get();
        // 增加浏览量
        attraction.setViewCount((attraction.getViewCount() != null ? attraction.getViewCount() : 0) + 1);
        attractionService.updateAttraction(id, attraction);
        return ApiResponse.success(attraction);
    }

    @Operation(summary = "获取景点门票类型", description = "获取指定景点的门票类型")
    @GetMapping("/attractions/{id}/tickets")
    public ApiResponse<List<TicketType>> getAttractionTickets(
            @Parameter(description = "景点ID") @PathVariable Long id) {
        List<TicketType> tickets = ticketTypeService.getTicketTypesByAttractionId(id);
        return ApiResponse.success(tickets);
    }

    // ==================== 订单相关 ====================

    @Operation(summary = "创建订单", description = "用户创建订单")
    @PostMapping("/orders")
    public ApiResponse<Order> createOrder(@RequestBody OrderRequest request) {
        try {
            // 验证必要参数
            if (request.getUserId() == null) {
                return ApiResponse.error("用户ID不能为空");
            }

            if (request.getAttractionId() == null) {
                return ApiResponse.error("景点ID不能为空");
            }

            if (request.getTicketTypeId() == null) {
                return ApiResponse.error("门票类型ID不能为空");
            }

            if (request.getQuantity() == null || request.getQuantity() <= 0) {
                return ApiResponse.error("数量必须大于0");
            }

            if (request.getVisitDate() == null) {
                return ApiResponse.error("游玩日期不能为空");
            }

            if (request.getVisitorName() == null || request.getVisitorName().trim().isEmpty()) {
                return ApiResponse.error("游客姓名不能为空");
            }

            if (request.getVisitorPhone() == null || request.getVisitorPhone().trim().isEmpty()) {
                return ApiResponse.error("游客手机号不能为空");
            }

            // 验证用户是否存在
            Optional<User> userOpt = userService.getUserById(request.getUserId());
            if (!userOpt.isPresent()) {
                return ApiResponse.error("用户不存在");
            }

            // 验证景点是否存在
            if (!attractionService.getAttractionById(request.getAttractionId()).isPresent()) {
                return ApiResponse.error("景点不存在");
            }

            // 验证门票类型是否存在
            Optional<TicketType> ticketTypeOpt = ticketTypeService.getTicketTypeById(request.getTicketTypeId());
            if (!ticketTypeOpt.isPresent()) {
                return ApiResponse.error("门票类型不存在");
            }

            // 创建订单对象
            Order order = new Order();

            // 设置用户信息
            User user = userOpt.get();
            order.setUser(user);

            // 设置景点信息
            Optional<Attraction> attractionOpt = attractionService.getAttractionById(request.getAttractionId());
            if (attractionOpt.isPresent()) {
                order.setAttraction(attractionOpt.get());
            } else {
                return ApiResponse.error("景点不存在");
            }

            // 设置门票类型信息
            TicketType ticketType = ticketTypeOpt.get();
            order.setTicketType(ticketType);

            // 设置订单基本信息
            order.setQuantity(request.getQuantity());
            order.setVisitDate(request.getVisitDate());
            order.setVisitorName(request.getVisitorName());
            order.setVisitorPhone(request.getVisitorPhone());

            // 计算总价
            BigDecimal unitPrice = ticketType.getPrice();
            BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(request.getQuantity()));
            order.setTotalAmount(totalPrice);

            // 设置默认状态为未支付
            order.setStatus("unpaid");

            // 创建订单
            Order createdOrder = orderService.createOrder(order);
            return ApiResponse.success(createdOrder);
        } catch (Exception e) {
            return ApiResponse.error("创建订单失败: " + e.getMessage());
        }
    }

    @Operation(summary = "获取我的订单", description = "获取当前用户的订单列表")
    @GetMapping("/orders/my")
    public ApiResponse<Page<Order>> getMyOrders(
            @Parameter(description = "用户ID") @RequestParam Long userId,
            @Parameter(description = "订单状态") @RequestParam(required = false) String status,
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页条数") @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orders = orderService.getOrdersByUserId(userId, pageable);
        return ApiResponse.success(orders);
    }

    @Operation(summary = "获取订单详情", description = "根据订单ID获取详情")
    @GetMapping("/orders/{id}")
    public ApiResponse<Order> getOrderDetail(
            @Parameter(description = "订单ID") @PathVariable Long id) {
        return orderService.getOrderById(id)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error("订单不存在"));
    }

    @Operation(summary = "取消订单", description = "取消未支付的订单")
    @PutMapping("/orders/{id}/cancel")
    public ApiResponse<Order> cancelOrder(
            @Parameter(description = "订单ID") @PathVariable Long id) {
        Order order = orderService.updateOrderStatus(id, "cancelled");
        if (order != null) {
            return ApiResponse.success(order);
        }
        return ApiResponse.error("订单不存在");
    }

    // ==================== 攻略相关 ====================

    @Operation(summary = "获取攻略列表", description = "获取已审核通过的攻略列表")
    @GetMapping("/guides")
    public ApiResponse<Page<Guide>> getGuides(
            @Parameter(description = "搜索关键词") @RequestParam(required = false) String keyword,
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页条数") @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Guide> guides = guideService.getGuides(keyword, "approved", 1, pageable);
        return ApiResponse.success(guides);
    }

    @Operation(summary = "获取攻略详情", description = "根据攻略ID获取详情")
    @GetMapping("/guides/{id}")
    public ApiResponse<Map<String, Object>> getGuideDetail(
            @Parameter(description = "攻略ID") @PathVariable Long id) {
        try {
            // 验证ID有效性
            if (id == null || id <= 0) {
                return ApiResponse.error("攻略ID无效，请检查ID格式");
            }

            // 使用新方法获取攻略详情，同时加载用户信息
            Optional<Guide> guideOpt = guideService.getGuideByIdWithUser(id);
            if (!guideOpt.isPresent()) {
                return ApiResponse.error("攻略不存在，ID: " + id);
            }
            Guide guide = guideOpt.get();

            // 验证攻略状态
            if (guide.getStatus() == null || guide.getStatus() != 1) {
                String statusMessage = "攻略已下架";
                if (guide.getStatus() == 0) {
                    statusMessage = "攻略已下架";
                } else if (guide.getStatus() == 2) {
                    statusMessage = "攻略未审核";
                }
                return ApiResponse.error(statusMessage + "，ID: " + id);
            }

            // 获取景点信息（仅当攻略关联了景点时）
            Optional<Attraction> attractionOpt = Optional.empty();
            if (guide.getAttractionId() != null) {
                attractionOpt = attractionService.getAttractionById(guide.getAttractionId());
            }

            // 增加浏览量
            guideService.incrementViewCount(id);

            // 构建前端期望的攻略详情数据结构
            Map<String, Object> guideData = new HashMap<>();
            guideData.put("id", guide.getId());
            guideData.put("title", guide.getTitle());
            guideData.put("content", guide.getContent());
            guideData.put("coverImage", guide.getCoverImage());
            guideData.put("createTime", guide.getCreateTime());
            guideData.put("viewCount", guide.getViewCount());
            guideData.put("likeCount", guide.getLikeCount());
            guideData.put("commentCount", guide.getCommentCount());

            // 构建用户信息对象
            User user = guide.getUser();
            if (user != null) {
                Map<String, Object> userData = new HashMap<>();
                userData.put("id", user.getId());
                userData.put("nickname", user.getNickname());
                userData.put("avatar", user.getAvatar());
                guideData.put("user", userData);
            } else {
                guideData.put("user", null);
            }

            // 构建景点信息对象
            if (attractionOpt.isPresent()) {
                Attraction attraction = attractionOpt.get();
                Map<String, Object> attractionData = new HashMap<>();
                attractionData.put("id", attraction.getId());
                attractionData.put("name", attraction.getName());
                attractionData.put("location", attraction.getAddress());
                attractionData.put("coverImage", attraction.getCoverImage());
                guideData.put("attraction", attractionData);
            } else {
                guideData.put("attraction", null);
            }

            // 构建前端期望的数据结构：res.data 应该存在
            Map<String, Object> result = new HashMap<>();
            result.put("data", guideData);

            return ApiResponse.success(result);
        } catch (Exception e) {
            // 记录详细的错误信息
            String errorInfo = String.format(
                    "加载攻略详情失败 - 系统环境: Windows, mp版本: 1.06.2504060, 库版本: 3.13.0, 错误: %s, 堆栈: %s",
                    e.getMessage(),
                    java.util.Arrays.toString(e.getStackTrace()));
            System.err.println(errorInfo);

            // 根据不同错误类型返回更具体的错误信息
            if (e.getMessage() != null) {
                if (e.getMessage().contains("攻略ID无效")) {
                    return ApiResponse.error("攻略ID无效，请检查ID格式");
                } else if (e.getMessage().contains("攻略不存在")) {
                    return ApiResponse.error("攻略不存在，ID: " + id);
                } else if (e.getMessage().contains("已下架或未审核")) {
                    return ApiResponse.error("攻略状态异常，请检查攻略状态，ID: " + id);
                } else if (e.getMessage().contains("攻略已删除")) {
                    return ApiResponse.error("攻略已删除，ID: " + id);
                }
            }

            return ApiResponse.error("系统错误，请稍后重试。错误代码: GUIDE-001");
        }
    }

    @Operation(summary = "发布攻略", description = "用户发布新攻略")
    @PostMapping("/guides")
    public ApiResponse<Guide> createGuide(@Valid @RequestBody GuideCreateRequest request) {
        Guide created = guideService.createGuide(request);
        return ApiResponse.success(created);
    }

    @Operation(summary = "点赞攻略", description = "点赞或取消点赞")
    @PostMapping("/guides/{id}/like")
    public ApiResponse<Void> likeGuide(
            @Parameter(description = "攻略ID") @PathVariable Long id) {
        Optional<Guide> guideOpt = guideService.getGuideById(id);
        if (guideOpt.isPresent()) {
            Guide guide = guideOpt.get();
            guide.setLikeCount((guide.getLikeCount() != null ? guide.getLikeCount() : 0) + 1);
            guideService.updateGuide(id, guide);
            return ApiResponse.success();
        }
        return ApiResponse.error("攻略不存在");
    }

    @Operation(summary = "收藏攻略", description = "收藏或取消收藏")
    @PostMapping("/guides/{id}/favorite")
    public ApiResponse<Void> favoriteGuide(
            @Parameter(description = "攻略ID") @PathVariable Long id) {
        Optional<Guide> guideOpt = guideService.getGuideById(id);
        if (guideOpt.isPresent()) {
            Guide guide = guideOpt.get();
            guide.setFavoriteCount((guide.getFavoriteCount() != null ? guide.getFavoriteCount() : 0) + 1);
            guideService.updateGuide(id, guide);
            return ApiResponse.success();
        }
        return ApiResponse.error("攻略不存在");
    }

    // ==================== 评论相关 ====================

    @Operation(summary = "获取攻略评论", description = "获取指定攻略的评论列表")
    @GetMapping("/comments")
    public ApiResponse<List<Comment>> getComments(
            @Parameter(description = "攻略ID") @RequestParam Long guideId) {
        Pageable pageable = PageRequest.of(0, 100);
        Page<Comment> commentPage = commentService.getCommentsByGuideId(guideId, pageable);
        return ApiResponse.success(commentPage.getContent());
    }

    @Operation(summary = "发表评论", description = "用户发表评论")
    @PostMapping("/comments")
    public ApiResponse<Comment> createComment(@Valid @RequestBody CommentCreateRequest request) {
        Comment created = commentService.createComment(request);
        return ApiResponse.success(created);
    }

    @Operation(summary = "回复评论", description = "用户回复评论")
    @PostMapping("/comments/reply")
    public ApiResponse<Comment> replyComment(@Valid @RequestBody CommentReplyRequest request) {
        Comment created = commentService.createCommentReply(request);
        return ApiResponse.success(created);
    }

    @Operation(summary = "点赞评论", description = "点赞评论")
    @PostMapping("/comments/{id}/like")
    public ApiResponse<Void> likeComment(
            @Parameter(description = "评论ID") @PathVariable Long id) {
        Optional<Comment> commentOpt = commentService.getCommentById(id);
        if (commentOpt.isPresent()) {
            Comment comment = commentOpt.get();
            comment.setLikeCount((comment.getLikeCount() != null ? comment.getLikeCount() : 0) + 1);
            commentService.updateComment(id, comment);
            return ApiResponse.success();
        }
        return ApiResponse.error("评论不存在");
    }

    // ==================== Banner相关 ====================

    @Operation(summary = "获取轮播图", description = "获取上线的Banner列表")
    @GetMapping("/banners")
    public ApiResponse<List<Banner>> getBanners() {
        List<Banner> banners = bannerService.getOnlineBanners();
        return ApiResponse.success(banners);
    }
}
