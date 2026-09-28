// Vue 文件类型声明
declare module '*.vue' {
  import type { DefineComponent } from 'vue'
  const component: DefineComponent<{}, {}, any>
  export default component
}

// 具体API模块类型声明
declare module '@/api/guide' {
  export {};
}