import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useAppStore = defineStore('app', () => {
  // 侧边栏折叠状态
  const sidebarCollapsed = ref(false)

  // 主题模式
  const theme = ref<'light' | 'dark'>('light')

  // 页面加载状态
  const loading = ref(false)

  // 设置侧边栏折叠状态
  function setSidebarCollapsed(collapsed: boolean) {
    sidebarCollapsed.value = collapsed
  }

  // 切换侧边栏
  function toggleSidebar() {
    sidebarCollapsed.value = !sidebarCollapsed.value
  }

  // 设置主题
  function setTheme(mode: 'light' | 'dark') {
    theme.value = mode
    document.documentElement.setAttribute('data-theme', mode)
  }

  // 设置加载状态
  function setLoading(state: boolean) {
    loading.value = state
  }

  return {
    sidebarCollapsed,
    theme,
    loading,
    setSidebarCollapsed,
    toggleSidebar,
    setTheme,
    setLoading,
  }
})
