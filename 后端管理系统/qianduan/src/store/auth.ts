import { defineStore } from "pinia";
import { ref, computed } from "vue";
import type { Admin } from "@/types";
import { login as loginApi, getAdminDetail } from "@/api/auth";

export const useAuthStore = defineStore("auth", () => {
  // 状态
  const token = ref<string>(localStorage.getItem("token") || "");
  const admin = ref<Admin | null>(null);

  // 初始化时从 localStorage 恢复管理员信息
  try {
    const adminData = localStorage.getItem("admin");
    if (adminData) {
      admin.value = JSON.parse(adminData);
    }
  } catch (error) {
    console.error("Failed to parse admin data from localStorage:", error);
    localStorage.removeItem("admin"); // 移除损坏的数据
  }

  // 计算属性
  const isLoggedIn = computed(() => !!token.value);
  const isAdmin = computed(() => admin.value?.role === "super_admin");
  const isManager = computed(() => admin.value?.role === "admin");

  // 登录
  async function login(username: string, password: string) {
    const res = await loginApi({ username, password });
    token.value = res.data.token;
    admin.value = res.data.admin;
    localStorage.setItem("token", res.data.token);
    localStorage.setItem("admin", JSON.stringify(res.data.admin));
    return res;
  }

  // 登出
  function logout() {
    token.value = "";
    admin.value = null;
    localStorage.removeItem("token");
    localStorage.removeItem("admin");
  }

  // 刷新用户信息
  async function refreshUserInfo() {
    if (admin.value) {
      const res = await getAdminDetail(admin.value.id);
      admin.value = res.data;
      localStorage.setItem("admin", JSON.stringify(res.data));
    }
  }

  // 更新管理员信息
  function updateAdminInfo(info: Partial<Admin>) {
    if (admin.value) {
      admin.value = { ...admin.value, ...info };
      localStorage.setItem("admin", JSON.stringify(admin.value));
    }
  }

  return {
    token,
    admin,
    isLoggedIn,
    isAdmin,
    isManager,
    login,
    logout,
    refreshUserInfo,
    updateAdminInfo,
  };
});
