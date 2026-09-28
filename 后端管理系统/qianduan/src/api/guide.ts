import { get, put, del } from '@/utils/request'
import type { ApiResponse, Guide, PageRequest, PageData } from '@/types'

/**
 * 获取攻略列表
 */
export function getGuideList(params: PageRequest) {
  return get<PageData<Guide>>('/admin/guides', { params })
}

/**
 * 获取攻略详情
 */
export function getGuideDetail(id: number) {
  return get<Guide>(`/admin/guides/${id}`)
}

/**
 * 审核通过
 */
export function approveGuide(id: number) {
  return put<Guide>(`/admin/guides/${id}/approve`)
}

/**
 * 审核拒绝
 */
export function rejectGuide(id: number, remark?: string) {
  return put<Guide>(`/admin/guides/${id}/reject`, null, {
    params: { remark }
  })
}

/**
 * 更新攻略状态
 */
export function updateGuideStatus(id: number, status: number) {
  return put<Guide>(`/admin/guides/${id}/status`, null, {
    params: { status }
  })
}

/**
 * 删除攻略
 */
export function deleteGuide(id: number) {
  return del<void>(`/admin/guides/${id}`)
}

/**
 * 统计待审核攻略
 */
export function getPendingGuideCount() {
  return get<number>('/admin/guides/statistics/pending')
}
