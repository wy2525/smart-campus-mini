<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { getOrderDetail } from '@/api/order'
import type { Order } from '@/types'

const router = useRouter()
const route = useRoute()

const loading = ref(false)
const order = ref<Order | null>(null)

const fetchDetail = async () => {
  loading.value = true
  try {
    const id = parseInt(route.params.id as string)
    const res = await getOrderDetail(id)
    order.value = res.data
  } catch (error) {
    console.error('获取订单详情失败:', error)
  } finally {
    loading.value = false
  }
}

const statusFormatter = (status: string) => {
  const map: Record<string, string> = {
    unpaid: '未支付',
    toUse: '待使用',
    used: '已使用',
    completed: '已完成',
    refunded: '已退款',
    cancelled: '已取消'
  }
  return map[status] || status
}

const statusTagType = (status: string) => {
  const map: Record<string, string> = {
    unpaid: 'info',
    toUse: 'warning',
    used: 'primary',
    completed: 'success',
    refunded: 'danger',
    cancelled: 'info'
  }
  return map[status] || 'info'
}

onMounted(() => {
  fetchDetail()
})
</script>

<template>
  <div class="page-container">
    <el-card v-loading="loading" class="detail-card">
      <template #header>
        <div class="card-header">
          <span>订单详情</span>
          <el-button @click="router.back()">返回</el-button>
        </div>
      </template>

      <div v-if="order" class="order-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="订单ID">{{ order.id }}</el-descriptions-item>
          <el-descriptions-item label="订单号">{{ order.orderNo }}</el-descriptions-item>
          <el-descriptions-item label="用户昵称">
            {{ order.user?.nickname || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="用户手机号">
            {{ order.user?.phone || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="景点名称" :span="2">
            {{ order.attraction?.name }}
          </el-descriptions-item>
          <el-descriptions-item label="门票类型" :span="2">
            {{ order.ticketType?.name }}
          </el-descriptions-item>
          <el-descriptions-item label="数量">{{ order.quantity }}</el-descriptions-item>
          <el-descriptions-item label="总金额">
            ¥{{ order.totalAmount }}
          </el-descriptions-item>
          <el-descriptions-item label="游玩日期">
            {{ order.visitDate }}
          </el-descriptions-item>
          <el-descriptions-item label="游客姓名">
            {{ order.visitorName || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="游客手机号">
            {{ order.visitorPhone || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="订单状态">
            <el-tag :type="statusTagType(order.status)">
              {{ statusFormatter(order.status) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="创建时间" :span="2">
            {{ order.createTime }}
          </el-descriptions-item>
          <el-descriptions-item label="更新时间" :span="2">
            {{ order.updateTime }}
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </el-card>
  </div>
</template>

<style scoped>
.detail-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 600;
}

.order-detail {
  padding: 20px 0;
}
</style>
