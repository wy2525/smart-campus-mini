import axios, { type AxiosInstance, type AxiosRequestConfig, type AxiosResponse, type InternalAxiosRequestConfig } from 'axios'
import { ElMessage } from 'element-plus'
import type { ApiResponse } from '@/types'

/**
 * 创建axios实例
 */
const service: AxiosInstance = axios.create({
  baseURL: '/api',
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json;charset=UTF-8'
  }
})

/**
 * 请求拦截器
 */
service.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    // 从localStorage获取token
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error: any) => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

/**
 * 响应拦截器
 */
service.interceptors.response.use(
  (response: AxiosResponse<ApiResponse>) => {
    const res = response.data

    // 如果返回的状态码不是200，说明接口异常
    if (res.code !== 200) {
      console.error('接口错误:', res.message)

      // 401: 未授权，跳转到登录页
      if (res.code === 401) {
        localStorage.removeItem('token')
        localStorage.removeItem('admin')
        window.location.href = '/login'
      }

      // 显示错误消息
      ElMessage.error(res.message || '请求失败')

      return Promise.reject(new Error(res.message || '请求失败'))
    }

    return res
  },
  (error: any) => {
    console.error('响应错误:', error)

    // 超时处理
    if (error.code === 'ECONNABORTED' && error.message.includes('timeout')) {
      ElMessage.error('请求超时')
      return Promise.reject(error)
    }

    // 网络错误
    if (!error.response) {
      ElMessage.error('网络连接失败')
      return Promise.reject(error)
    }

    // 服务器错误
    const status = error.response.status
    let message = '请求失败'

    switch (status) {
      case 400:
        message = '请求参数错误'
        break
      case 401:
        message = '未授权，请重新登录'
        localStorage.removeItem('token')
        localStorage.removeItem('admin')
        window.location.href = '/login'
        break
      case 403:
        message = '拒绝访问'
        break
      case 404:
        message = '请求的资源不存在'
        break
      case 500:
        message = '服务器内部错误'
        break
      case 502:
        message = '网关错误'
        break
      case 503:
        message = '服务不可用'
        break
      case 504:
        message = '网关超时'
        break
      default:
        message = `请求失败 (${status})`
    }

    ElMessage.error(message)
    return Promise.reject(error)
  }
)

/**
 * 封装GET请求
 */
export function get<T = any>(url: string, config?: AxiosRequestConfig): Promise<ApiResponse<T>> {
  return service.get(url, config)
}

/**
 * 封装POST请求
 */
export function post<T = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<ApiResponse<T>> {
  return service.post(url, data, config)
}

/**
 * 封装PUT请求
 */
export function put<T = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<ApiResponse<T>> {
  return service.put(url, data, config)
}

/**
 * 封装DELETE请求
 */
export function del<T = any>(url: string, config?: AxiosRequestConfig): Promise<ApiResponse<T>> {
  return service.delete(url, config)
}

export default service
