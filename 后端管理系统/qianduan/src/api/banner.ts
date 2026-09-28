import { get, post, put, del } from '@/utils/request'
import type { ApiResponse, Banner } from '@/types'

/**
 * 获取Banner列表
 */
export function getBannerList() {
  return get<Banner[]>('/admin/banners')
}

/**
 * 获取Banner详情
 */
export function getBannerDetail(id: number) {
  return get<Banner>(`/admin/banners/${id}`)
}

/**
 * 创建Banner
 */
export function createBanner(data: Partial<Banner>) {
  return post<Banner>('/admin/banners', data)
}

/**
 * 更新Banner
 */
export function updateBanner(id: number, data: Partial<Banner>) {
  return put<Banner>(`/admin/banners/${id}`, data)
}

/**
 * 更新Banner状态
 */
export function updateBannerStatus(id: number, status: number) {
  return put<Banner>(`/admin/banners/${id}/status`, null, {
    params: { status }
  })
}

/**
 * 更新Banner排序
 */
export function updateBannerSort(id: number, sort: number) {
  return put<Banner>(`/admin/banners/${id}/sort`, null, {
    params: { sort }
  })
}

/**
 * 删除Banner
 */
export function deleteBanner(id: number) {
  return del<void>(`/admin/banners/${id}`)
}
