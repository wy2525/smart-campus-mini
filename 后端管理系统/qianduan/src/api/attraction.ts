import { get, post, put, del } from '@/utils/request'
import type { ApiResponse, Attraction, PageRequest, PageData, TicketType } from '@/types'

/**
 * 获取景点列表
 */
export function getAttractionList(params: PageRequest) {
  return get<PageData<Attraction>>('/admin/attractions', { params })
}

/**
 * 获取景点详情
 */
export function getAttractionDetail(id: number) {
  return get<Attraction>(`/admin/attractions/${id}`)
}

/**
 * 创建景点
 */
export function createAttraction(data: Partial<Attraction>) {
  return post<Attraction>('/admin/attractions', data)
}

/**
 * 更新景点
 */
export function updateAttraction(id: number, data: Partial<Attraction>) {
  return put<Attraction>(`/admin/attractions/${id}`, data)
}

/**
 * 更新景点状态
 */
export function updateAttractionStatus(id: number, status: number) {
  return put<Attraction>(`/admin/attractions/${id}/status`, null, {
    params: { status }
  })
}

/**
 * 更新景点排序
 */
export function updateAttractionSort(id: number, sort: number) {
  return put<Attraction>(`/admin/attractions/${id}/sort`, null, {
    params: { sort }
  })
}

/**
 * 删除景点
 */
export function deleteAttraction(id: number) {
  return del<void>(`/admin/attractions/${id}`)
}

/**
 * 获取景点门票类型
 */
export function getAttractionTickets(id: number) {
  return get<TicketType[]>(`/admin/attractions/${id}/tickets`)
}
