import { get, post, put, del } from '@/utils/request'
import type { ApiResponse, TicketType } from '@/types'

/**
 * 获取所有门票类型
 */
export function getAllTicketTypes() {
  return get<TicketType[]>('/admin/ticket-types')
}

/**
 * 获取景点门票类型
 */
export function getAttractionTicketTypes(attractionId: number) {
  return get<TicketType[]>(`/admin/ticket-types/attraction/${attractionId}`)
}

/**
 * 获取门票类型详情
 */
export function getTicketTypeDetail(id: number) {
  return get<TicketType>(`/admin/ticket-types/${id}`)
}

/**
 * 创建门票类型
 */
export function createTicketType(data: Partial<TicketType>) {
  return post<TicketType>('/admin/ticket-types', data)
}

/**
 * 更新门票类型
 */
export function updateTicketType(id: number, data: Partial<TicketType>) {
  return put<TicketType>(`/admin/ticket-types/${id}`, data)
}

/**
 * 更新库存
 */
export function updateTicketStock(id: number, stock: number) {
  return put<TicketType>(`/admin/ticket-types/${id}/stock`, null, {
    params: { stock }
  })
}

/**
 * 更新门票类型状态
 */
export function updateTicketTypeStatus(id: number, status: number) {
  return put<TicketType>(`/admin/ticket-types/${id}/status`, null, {
    params: { status }
  })
}

/**
 * 删除门票类型
 */
export function deleteTicketType(id: number) {
  return del<void>(`/admin/ticket-types/${id}`)
}
