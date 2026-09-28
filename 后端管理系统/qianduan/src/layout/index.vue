<script setup lang="ts">
import { ref, computed } from "vue";
import { useRouter, useRoute } from "vue-router";
import { ElMessageBox } from "element-plus";
import { useAuthStore, useAppStore } from "@/store";

const router = useRouter();
const route = useRoute();
const authStore = useAuthStore();
const appStore = useAppStore();

// 侧边栏折叠
const isCollapse = computed(() => appStore.sidebarCollapsed);

// 菜单列表
const menuList = computed(() => {
  return router
    .getRoutes()
    .filter((route) => route.meta?.title && !route.meta?.hidden)
    .filter((route) => route.path.startsWith("/"))
    .map((route) => ({
      path: route.path,
      title: route.meta?.title as string,
      icon: route.meta?.icon as string,
    }));
});

// 当前激活的菜单
const activeMenu = computed(() => {
  const path = route.path;
  const matched = route.matched;
  if (matched.length > 0) {
    return matched[matched.length - 1].path;
  }
  return path;
});

// 菜单图标映射
const iconMap: Record<string, string> = {
  Dashboard: "Odometer",
  Users: "User",
  Map: "Location",
  ShoppingCart: "ShoppingCart",
  FileText: "Document",
  MessageSquare: "ChatDotRound",
  Image: "Picture",
  Settings: "Setting",
  Shield: "UserFilled",
  Ticket: "Ticket",
};

// 获取图标组件名
const getIcon = (icon: string) => {
  return iconMap[icon] || "Menu";
};

// 跳转菜单
const handleMenuSelect = (index: string) => {
  router.push(index);
};

// 切换侧边栏
const toggleSidebar = () => {
  appStore.toggleSidebar();
};

// 退出登录
const handleLogout = () => {
  ElMessageBox.confirm("确定要退出登录吗？", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(() => {
      authStore.logout();
      router.push("/login");
    })
    .catch(() => {});
};
</script>

<template>
  <el-container class="layout-container">
    <!-- 侧边栏 -->
    <el-aside :width="isCollapse ? '64px' : '240px'" class="sidebar">
      <div class="logo-container">
        <el-icon class="logo-icon"><ElementPlus /></el-icon>
        <span v-if="!isCollapse" class="logo-text">旅游管理系统</span>
      </div>
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        :unique-opened="true"
        @select="handleMenuSelect"
        class="sidebar-menu"
      >
        <el-menu-item
          v-for="menu in menuList"
          :key="menu.path"
          :index="menu.path"
        >
          <el-icon><component :is="getIcon(menu.icon)" /></el-icon>
          <template #title>{{ menu.title }}</template>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <!-- 主体内容 -->
    <el-container class="main-container">
      <!-- 顶部栏 -->
      <el-header class="header">
        <div class="header-left">
          <el-icon class="collapse-icon" @click="toggleSidebar">
            <component :is="isCollapse ? 'Expand' : 'Fold'" />
          </el-icon>
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/dashboard' }">
              首页
            </el-breadcrumb-item>
            <el-breadcrumb-item
              v-for="item in route.matched.filter(r => r.meta?.title && r.path !== '/')"
              :key="item.path"
              :to="item.path !== route.path ? { path: item.path } : undefined"
            >
              {{ item.meta?.title }}
            </el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="header-right">
          <el-dropdown @command="handleLogout">
            <div class="user-info">
              <el-avatar :size="32" :src="authStore.admin?.avatar" />
              <span class="username">{{
                authStore.admin?.nickname || authStore.admin?.username
              }}</span>
              <el-icon><ArrowDown /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="logout">
                  <el-icon><SwitchButton /></el-icon>
                  退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <!-- 内容区域 -->
      <el-main class="main-content">
        <RouterView />
      </el-main>
    </el-container>
  </el-container>
</template>

<style scoped>
.layout-container {
  width: 100%;
  height: 100%;
}

/* 侧边栏样式 */
.sidebar {
  background-color: #304156;
  transition: width 0.3s;
  overflow-x: hidden;
}

.logo-container {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #2b3a4a;
  color: #ffffff;
  overflow: hidden;
}

.logo-icon {
  font-size: 24px;
  color: #409eff;
  flex-shrink: 0;
}

.logo-text {
  margin-left: 10px;
  font-size: 18px;
  font-weight: 600;
  white-space: nowrap;
}

.sidebar-menu {
  border: none;
  background-color: #304156;
}

.sidebar-menu:not(.el-menu--collapse) {
  width: 240px;
}

:deep(.el-menu-item) {
  color: #bfcbd9;
  background-color: #304156;
}

:deep(.el-menu-item:hover) {
  background-color: #263445;
}

:deep(.el-menu-item.is-active) {
  color: #409eff;
  background-color: #263445;
}

/* 主体容器样式 */
.main-container {
  display: flex;
  flex-direction: column;
  background-color: #f0f2f5;
}

/* 顶部栏样式 */
.header {
  background-color: #ffffff;
  border-bottom: 1px solid #e6e6e6;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  height: 60px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.collapse-icon {
  font-size: 20px;
  cursor: pointer;
  color: #606266;
  transition: color 0.3s;
}

.collapse-icon:hover {
  color: #409eff;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 8px 12px;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.user-info:hover {
  background-color: #f5f7fa;
}

.username {
  font-size: 14px;
  color: #303133;
}

/* 主内容区域样式 */
.main-content {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
  background-color: #f0f2f5;
}
</style>
