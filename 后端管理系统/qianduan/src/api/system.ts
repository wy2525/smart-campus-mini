import { get, post, put, del } from '@/utils/request'
import type { ApiResponse, SystemConfig } from '@/types'

/**
 * 获取系统配置
 */
export function getSystemConfigList() {
  return get<SystemConfig[]>('/admin/config')
}

/**
 * 获取配置详情
 */
export function getSystemConfigDetail(id: number) {
  return get<SystemConfig>(`/admin/config/${id}`)
}

/**
 * 获取配置值
 */
export function getSystemConfigValue(configKey: string) {
  return get<string>(`/admin/config/key/${configKey}`)
}

/**
 * 更新系统配置
 */
export function updateSystemConfig(data: Record<string, string>) {
  return put<void>('/admin/config', data)
}

/**
 * 创建配置
 */
export function createSystemConfig(data: Partial<SystemConfig>) {
  return post<SystemConfig>('/admin/config', data)
}

/**
 * 更新配置
 */
export function updateConfig(id: number, data: Partial<SystemConfig>) {
  return put<SystemConfig>(`/admin/config/${id}`, data)
}

/**
 * 删除配置
 */
export function deleteSystemConfig(id: number) {
  return del<void>(`/admin/config/${id}`)
}
