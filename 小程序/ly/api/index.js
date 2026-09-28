import { get, post, put, del } from "@/utils/request.js";

// ==================== 用户相关 ====================

/**
 * 用户登录
 */
export function login(phone, password) {
  return post("/user/login", { phone, password });
}

/**
 * 用户注册
 */
export function register(phone, password, nickname) {
  return post("/user/register", {
    phone,
    password,
    nickname: nickname || phone,
  });
}

/**
 * 获取用户信息
 */
export function getUserInfo(userId) {
  return get("/user/info", { userId });
}

/**
 * 更新用户信息
 */
export function updateUserInfo(data) {
  return put("/user/info", data);
}

// ==================== 景点相关 ====================

/**
 * 获取景点列表
 */
export function getAttractionsList(params = {}) {
  return get("/attractions", params);
}

/**
 * 获取热门景点
 */
export function getHotAttractions(limit = 8) {
  return get("/attractions/hot", { limit });
}

/**
 * 获取推荐景点
 */
export function getRecommendAttractions(limit = 10) {
  return get("/attractions/recommend", { limit });
}

/**
 * 获取景点详情
 */
export function getAttractionDetail(params) {
  // 如果传递的是对象，提取id；如果是直接传递id，直接使用
  const id = typeof params === "object" ? params.id : params;
  return get(`/attractions/${id}`);
}

/**
 * 获取景点门票类型
 */
export function getAttractionTickets(params) {
  // 如果传递的是对象，提取id；如果是直接传递id，直接使用
  const id = typeof params === "object" ? params.id : params;
  return get(`/attractions/${id}/tickets`);
}

// ==================== 订单相关 ====================

/**
 * 创建订单
 */
export function createOrder(data) {
  return post("/orders", data);
}

/**
 * 获取我的订单
 */
export function getMyOrders(params) {
  return get("/orders/my", { ...params });
}

/**
 * 获取订单详情
 */
export function getOrderDetail(params) {
  // 如果传递的是对象，提取id；如果是直接传递id，直接使用
  const id = typeof params === "object" ? params.id : params;
  return get(`/orders/${id}`);
}

/**
 * 取消订单
 */
export function cancelOrder(id) {
  return put(`/orders/${id}/cancel`);
}

// ==================== 攻略相关 ====================

/**
 * 获取攻略列表
 */
export function getGuidesList(params = {}) {
  return get("/guides", params);
}

/**
 * 获取攻略详情
 */
export function getGuideDetail(params) {
  // 如果传递的是对象，提取id；如果是直接传递id，直接使用
  const id = typeof params === "object" ? params.id : params;

  // 确保id是数字类型
  const idNum = parseInt(id, 10);
  return get(`/guides/${idNum}`, {});
}

/**
 * 发布攻略
 */
export function createGuide(data) {
  return post("/guides", data);
}

/**
 * 点赞攻略
 */
export function likeGuide(id) {
  return post(`/guides/${id}/like`);
}

/**
 * 收藏攻略
 */
export function favoriteGuide(id) {
  return post(`/guides/${id}/favorite`);
}

/**
 * 获取我的收藏列表
 */
export function getMyFavorites(type = 'all') {
  return get("/user/favorites", { type });
}

/**
 * 获取收藏的景点列表
 */
export function getFavoriteAttractions() {
  return get("/user/favorites/attractions");
}

/**
 * 获取收藏的攻略列表
 */
export function getFavoriteGuides() {
  return get("/user/favorites/guides");
}

/**
 * 取消收藏
 */
export function unfavorite(type, id) {
  return post(`/user/favorites/${type}/${id}/unfavorite`);
}

// ==================== 评论相关 ====================

/**
 * 获取攻略评论
 */
export function getComments(params) {
  // 如果传递的是对象，提取guideId；如果是直接传递guideId，直接使用
  const guideId = typeof params === "object" ? params.guideId : params;
  return get("/comments", { guideId });
}

/**
 * 发表评论
 */
export function createComment(data) {
  return post("/comments", data);
}

/**
 * 点赞评论
 */
export function likeComment(id) {
  return post(`/comments/${id}/like`);
}

// ==================== Banner相关 ====================

/**
 * 获取轮播图
 */
export function getBanners() {
  return get("/banners");
}

// ==================== Kimi AI 相关 ====================

/**
 * Kimi AI 通用对话
 */
export {
  chatWithKimi,
  interpretAttraction,
  generateGuide,
  planItinerary,
  customChat,
  KIMI_CONFIG,
} from "./kimi.js";
