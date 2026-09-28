import { get, post, put, del } from '@/utils/request'
import type { ApiResponse, User, PageRequest, PageData } from '@/types'

/**
 * 获取用户列表
 */
export function getUserList(params: PageRequest) {
  return get<PageData<User>>('/admin/users', { params })
}

/**
 * 获取用户详情
 */
export function getUserDetail(id: number) {
  return get<User>(`/admin/users/${id}`)
}

/**
 * 更新用户信息
 */
export function updateUser(id: number, data: Partial<User>) {
  return put<User>(`/admin/users/${id}`, data)
}

/**
 * 更新用户状态
 */
export function updateUserStatus(id: number, status: number) {
  return put<User>(`/admin/users/${id}/status`, null, {
    params: { status }
  })
}

/**
 * 删除用户
 */
export function deleteUser(id: number) {
  return del<void>(`/admin/users/${id}`)
}

/**
 * 获取用户订单
 */
export function getUserOrders(id: number) {
  return get<string>(`/admin/users/${id}/orders`)
}

/**
 * 统计用户数
 */
export function getUserCount(status?: number) {
  return get<number>('/admin/users/statistics/count', {
    params: { status }
  })
}
