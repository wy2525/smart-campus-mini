import { get, put } from '@/utils/request'
import type { ApiResponse, Order, OrderStatistics, PageRequest, PageData } from '@/types'

/**
 * 获取订单列表
 */
export function getOrderList(params: PageRequest) {
  return get<PageData<Order>>('/admin/orders', { params })
}

/**
 * 获取订单详情
 */
export function getOrderDetail(id: number) {
  return get<Order>(`/admin/orders/${id}`)
}

/**
 * 更新订单信息
 */
export function updateOrder(id: number, data: Partial<Order>) {
  return put<Order>(`/admin/orders/${id}`, data)
}

/**
 * 更新订单状态
 */
export function updateOrderStatus(id: number, status: string) {
  return put<Order>(`/admin/orders/${id}/status`, null, {
    params: { status }
  })
}

/**
 * 订单统计
 */
export function getOrderStatistics() {
  return get<OrderStatistics>('/admin/orders/statistics')
}
