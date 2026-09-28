import { get, post, put, del } from '@/utils/request'
import type { ApiResponse, LoginRequest, LoginResponse, Admin } from '@/types'

/**
 * 管理员登录
 */
export function login(data: LoginRequest) {
  return post<LoginResponse>('/admin/login', data)
}

/**
 * 获取管理员列表
 */
export function getAdminList() {
  return get<Admin[]>('/admin')
}

/**
 * 获取管理员详情
 */
export function getAdminDetail(id: number) {
  return get<Admin>(`/admin/${id}`)
}

/**
 * 创建管理员
 */
export function createAdmin(data: Partial<Admin>) {
  return post<Admin>('/admin', data)
}

/**
 * 更新管理员
 */
export function updateAdmin(id: number, data: Partial<Admin>) {
  return put<Admin>(`/admin/${id}`, data)
}

/**
 * 更新管理员状态
 */
export function updateAdminStatus(id: number, status: number) {
  return put<Admin>(`/admin/${id}/status`, null, {
    params: { status }
  })
}

/**
 * 删除管理员
 */
export function deleteAdmin(id: number) {
  return del<void>(`/admin/${id}`)
}

/**
 * 重置管理员密码
 */
export function resetAdminPassword(id: number, newPassword: string) {
  return put<Admin>(`/admin/${id}/password`, null, {
    params: { newPassword }
  })
}
