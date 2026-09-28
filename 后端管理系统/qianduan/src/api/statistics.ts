import { get } from '@/utils/request'
import type { ApiResponse } from '@/types'

/**
 * 总览数据统计
 */
export function getOverviewStatistics() {
  return get<Record<string, number>>('/admin/statistics/overview')
}

/**
 * 用户统计
 */
export function getUserStatistics() {
  return get<Record<string, number>>('/admin/statistics/users')
}

/**
 * 订单统计
 */
export function getOrderStatisticsData() {
  return get<Record<string, number>>('/admin/statistics/orders')
}

/**
 * 景点统计
 */
export function getAttractionStatistics() {
  return get<Record<string, number>>('/admin/statistics/attractions')
}

/**
 * 攻略统计
 */
export function getGuideStatistics() {
  return get<Record<string, number>>('/admin/statistics/guides')
}
