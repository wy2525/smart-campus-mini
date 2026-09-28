<script setup lang="ts">
import { ref, onMounted } from "vue";
import { useRouter, useRoute } from "vue-router";
import { getUserDetail, getUserOrders } from "@/api/user";
import type { User } from "@/types";

const router = useRouter();
const route = useRoute();

const loading = ref(false);
const user = ref<User | null>(null);
const orders = ref<any[]>([]);

const fetchUserDetail = async () => {
  loading.value = true;
  try {
    const userId = parseInt(route.params.id as string);
    const [userRes, ordersRes] = await Promise.all([
      getUserDetail(userId),
      getUserOrders(userId),
    ]);
    user.value = userRes.data;
    try {
      orders.value = ordersRes.data ? JSON.parse(ordersRes.data) : [];
    } catch (error) {
      console.error("Failed to parse user orders data:", error);
      orders.value = [];
    }
  } catch (error) {
    console.error("获取用户详情失败:", error);
  } finally {
    loading.value = false;
  }
};

const genderFormatter = (gender?: number) => {
  const map: Record<number, string> = {
    0: "未知",
    1: "男",
    2: "女",
  };
  return map[gender || 0];
};

const statusFormatter = (status: number) => {
  return status === 1 ? "启用" : "禁用";
};

onMounted(() => {
  fetchUserDetail();
});
</script>

<template>
  <div class="page-container">
    <el-card v-loading="loading" class="user-card">
      <template #header>
        <div class="card-header">
          <span>用户详情</span>
          <el-button @click="router.back()">返回</el-button>
        </div>
      </template>

      <div v-if="user" class="user-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="用户ID">{{
            user.id
          }}</el-descriptions-item>
          <el-descriptions-item label="手机号">{{
            user.phone
          }}</el-descriptions-item>
          <el-descriptions-item label="昵称">{{
            user.nickname || "-"
          }}</el-descriptions-item>
          <el-descriptions-item label="性别">
            {{ genderFormatter(user.gender) }}
          </el-descriptions-item>
          <el-descriptions-item label="生日">{{
            user.birthday || "-"
          }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="user.status === 1 ? 'success' : 'danger'">
              {{ statusFormatter(user.status) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="注册时间" :span="2">
            {{ user.registerTime }}
          </el-descriptions-item>
          <el-descriptions-item label="更新时间" :span="2">
            {{ user.updateTime }}
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </el-card>

    <el-card class="orders-card">
      <template #header>
        <div class="card-header">
          <span>用户订单</span>
        </div>
      </template>

      <el-table :data="orders" stripe>
        <el-table-column prop="orderNo" label="订单号" width="200" />
        <el-table-column prop="attractionName" label="景点名称" />
        <el-table-column prop="ticketName" label="门票类型" />
        <el-table-column prop="quantity" label="数量" width="80" />
        <el-table-column prop="totalAmount" label="总金额" width="120">
          <template #default="{ row }"> ¥{{ row.totalAmount }} </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 'completed' ? 'success' : 'warning'">
              {{ row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
      </el-table>
    </el-card>
  </div>
</template>

<style scoped>
.user-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 600;
}

.user-detail {
  padding: 20px 0;
}

.orders-card {
  margin-bottom: 20px;
}
</style>
