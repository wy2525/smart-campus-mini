// Store 模块类型声明

// 导入实际的 store 类型
import type { useAuthStore } from '@/store/auth';
import type { useAppStore } from '@/store/app';

// 为 '@/store' 模块提供类型声明
declare module '@/store' {
  export { useAuthStore } from '@/store/auth';
  export { useAppStore } from '@/store/app';
}

// 定义可能需要的全局类型
export type { useAuthStore, useAppStore };