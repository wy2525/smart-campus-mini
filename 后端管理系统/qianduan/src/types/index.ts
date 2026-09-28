/**
 * 统一API响应格式
 */
export interface ApiResponse<T = any> {
  code: number
  message: string
  data: T
}

/**
 * 分页请求参数
 */
export interface PageRequest {
  page?: number
  size?: number
  keyword?: string
  status?: number | string
  [key: string]: any
}

/**
 * 分页响应数据
 */
export interface PageData<T> {
  content: T[]
  totalElements: number
  totalPages: number
  size: number
  number: number
  first: boolean
  last: boolean
}

/**
 * 用户实体
 */
export interface User {
  id: number
  phone: string
  nickname?: string
  avatar?: string
  gender?: number // 0-未知，1-男，2-女
  birthday?: string
  status: number // 0-禁用，1-启用
  registerTime: string
  updateTime: string
}

/**
 * 景点实体
 */
export interface Attraction {
  id: number
  name: string
  category?: string
  coverImage?: string
  images?: string // 多个URL，逗号分隔
  description?: string
  address?: string
  longitude?: number
  latitude?: number
  phone?: string
  openTime?: string
  notes?: string
  tags?: string // 多个标签，逗号分隔
  minPrice?: number
  rating?: number
  viewCount?: number
  bookingCount?: number
  status: number // 0-下线，1-上线
  sort?: number
  createTime: string
  updateTime: string
}

/**
 * 门票类型实体
 */
export interface TicketType {
  id: number
  attractionId?: number
  attraction?: Attraction
  name: string
  description?: string
  price: number
  stock: number
  status: number // 0-下架，1-上架
  sort?: number
  createTime: string
  updateTime: string
}

/**
 * 订单实体
 */
export interface Order {
  id: number
  orderNo: string
  userId: number
  user?: User
  attractionId: number
  attraction?: Attraction
  ticketTypeId: number
  ticketType?: TicketType
  quantity: number
  totalAmount: number
  visitDate: string
  visitorName?: string
  visitorPhone?: string
  status: string // unpaid-未支付，toUse-待使用，used-已使用，completed-已完成，refunded-已退款，cancelled-已取消
  createTime: string
  updateTime: string
}

/**
 * 订单统计
 */
export interface OrderStatistics {
  totalCount: number
  toUseCount: number
  completedCount: number
  refundedCount: number
  totalAmount: number
  todayAmount: number
}

/**
 * 攻略实体
 */
export interface Guide {
  id: number
  userId: number
  user?: User
  title: string
  coverImage?: string
  images?: string // 多个URL，逗号分隔
  content?: string // 富文本
  auditStatus: string // pending-待审核，approved-已通过，rejected-已拒绝
  auditRemark?: string
  status: number // 0-下架，1-上架
  isRecommended?: boolean
  viewCount?: number
  likeCount?: number
  favoriteCount?: number
  commentCount?: number
  createTime: string
  updateTime: string
}

/**
 * 评论实体
 */
export interface Comment {
  id: number
  userId: number
  user?: User
  guideId: number
  guide?: Guide
  content: string
  likeCount?: number
  status: number // 0-隐藏，1-显示
  createTime: string
}

/**
 * Banner实体
 */
export interface Banner {
  id: number
  title: string
  imageUrl: string
  attractionId?: number
  attraction?: Attraction
  linkUrl?: string
  status: number // 0-下线，1-上线
  sort?: number
  createTime: string
  updateTime: string
}

/**
 * 管理员实体
 */
export interface Admin {
  id: number
  username: string
  nickname?: string
  avatar?: string
  phone?: string
  email?: string
  role: string // super_admin-超级管理员，admin-管理员
  status: number // 0-禁用，1-启用
  createTime: string
  updateTime: string
}

/**
 * 系统配置实体
 */
export interface SystemConfig {
  id: number
  configKey: string
  configValue?: string
  description?: string
  createTime: string
  updateTime: string
}

/**
 * 登录请求
 */
export interface LoginRequest {
  username: string
  password: string
}

/**
 * 登录响应
 */
export interface LoginResponse {
  token: string
  admin: Admin
}

/**
 * 数据统计总览
 */
export interface StatisticsOverview {
  totalUsers: number
  todayNewUsers: number
  totalAttractions: number
  onlineAttractions: number
  totalOrders: number
  todayOrders: number
  todayAmount: number
  totalGuides: number
  pendingGuides: number
}

/**
 * 用户统计
 */
export interface UserStatistics {
  totalCount: number
  todayCount: number
  monthCount: number
}

/**
 * 订单统计
 */
export interface OrderStatisticsData {
  totalCount: number
  todayCount: number
  toUseCount: number
  completedCount: number
  refundedCount: number
  totalAmount: number
  todayAmount: number
}

/**
 * 景点统计
 */
export interface AttractionStatistics {
  totalCount: number
  onlineCount: number
}

/**
 * 攻略统计
 */
export interface GuideStatistics {
  totalCount: number
  approvedCount: number
  pendingCount: number
}
