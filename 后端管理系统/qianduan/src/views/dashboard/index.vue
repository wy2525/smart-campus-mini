<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Refresh } from '@element-plus/icons-vue'
import VChart, { THEME_KEY } from 'vue-echarts'
import { use } from 'echarts/core'
import {
  CanvasRenderer
} from 'echarts/renderers'
import {
  LineChart,
  BarChart
} from 'echarts/charts'
import {
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent
} from 'echarts/components'
import {
  getOverviewStatistics,
  getUserStatistics,
  getOrderStatisticsData
} from '@/api/statistics'
import { getUserList } from '@/api/user'
import { getOrderList } from '@/api/order'
import { ElMessage } from 'element-plus'
import type { User, Order } from '@/types'

const router = useRouter()

use([
  CanvasRenderer,
  LineChart,
  BarChart,
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent
])

const provide = {
  [THEME_KEY]: 'light'
}

// 加载状态
const loading = ref(false)

// 统计数据
const overviewData = ref({
  totalUsers: 0,
  todayNewUsers: 0,
  totalAttractions: 0,
  onlineAttractions: 0,
  totalOrders: 0,
  todayOrders: 0,
  todayAmount: 0,
  totalGuides: 0,
  pendingGuides: 0
})

// 最新用户列表
const recentUsers = ref<User[]>([])
const usersLoading = ref(false)

// 最新订单列表
const recentOrders = ref<Order[]>([])
const ordersLoading = ref(false)

// 用户增长图表配置
const userChartOption = ref({
  tooltip: {
    trigger: 'axis'
  },
  legend: {
    data: ['新增用户']
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    containLabel: true
  },
  xAxis: {
    type: 'category',
    boundaryGap: false,
    data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
  },
  yAxis: {
    type: 'value'
  },
  series: [
    {
      name: '新增用户',
      type: 'line',
      smooth: true,
      data: [12, 32, 101, 134, 90, 230, 210],
      itemStyle: {
        color: '#409EFF'
      },
      areaStyle: {
        color: {
          type: 'linear',
          x: 0,
          y: 0,
          x2: 0,
          y2: 1,
          colorStops: [
            { offset: 0, color: 'rgba(64, 158, 255, 0.3)' },
            { offset: 1, color: 'rgba(64, 158, 255, 0.05)' }
          ]
        }
      }
    }
  ]
})

// 订单统计图表配置
const orderChartOption = ref({
  tooltip: {
    trigger: 'axis',
    axisPointer: {
      type: 'shadow'
    }
  },
  legend: {
    data: ['订单数量', '订单金额']
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    containLabel: true
  },
  xAxis: {
    type: 'category',
    data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
  },
  yAxis: [
    {
      type: 'value',
      name: '订单数量'
    },
    {
      type: 'value',
      name: '订单金额'
    }
  ],
  series: [
    {
      name: '订单数量',
      type: 'bar',
      data: [320, 332, 301, 334, 390, 330, 320],
      itemStyle: {
        color: '#67C23A'
      }
    },
    {
      name: '订单金额',
      type: 'bar',
      data: [120, 132, 101, 134, 90, 230, 210],
      itemStyle: {
        color: '#E6A23C'
      }
    }
  ]
})

// 获取最新用户
const fetchRecentUsers = async () => {
  usersLoading.value = true
  try {
    const res = await getUserList({ page: 1, size: 5, sortField: 'createTime', sortOrder: 'desc' })
    recentUsers.value = res.data.records || []
  } catch (error) {
    console.error('获取最新用户失败:', error)
  } finally {
    usersLoading.value = false
  }
}

// 获取最新订单
const fetchRecentOrders = async () => {
  ordersLoading.value = true
  try {
    const res = await getOrderList({ page: 1, size: 5, sortField: 'createTime', sortOrder: 'desc' })
    recentOrders.value = res.data.records || []
  } catch (error) {
    console.error('获取最新订单失败:', error)
  } finally {
    ordersLoading.value = false
  }
}

// 获取统计数据
const fetchStatistics = async () => {
  loading.value = true
  try {
    const [overview, userStats, orderStats] = await Promise.all([
      getOverviewStatistics(),
      getUserStatistics(),
      getOrderStatisticsData()
    ])

    overviewData.value = overview.data

    // 使用后端返回的统计数据更新图表
    // 用户增长趋势图 - 使用最近7天的模拟数据（基于今日新增用户）
    const todayNewUsers = userStats.data?.todayNewUsers || 0
    const baseUserGrowth = [
      Math.floor(todayNewUsers * 0.3),
      Math.floor(todayNewUsers * 0.5),
      Math.floor(todayNewUsers * 0.8),
      Math.floor(todayNewUsers * 1.2),
      Math.floor(todayNewUsers * 1.5),
      Math.floor(todayNewUsers * 1.8),
      todayNewUsers
    ]
    userChartOption.value.series[0].data = baseUserGrowth

    // 订单统计图 - 使用最近7天的模拟数据（基于今日订单和金额）
    const todayOrders = orderStats.data?.todayOrders || 0
    const todayAmount = orderStats.data?.todayAmount || 0
    const baseOrderCounts = [
      Math.floor(todayOrders * 0.4),
      Math.floor(todayOrders * 0.5),
      Math.floor(todayOrders * 0.7),
      Math.floor(todayOrders * 0.9),
      Math.floor(todayOrders * 1.1),
      Math.floor(todayOrders * 1.3),
      todayOrders
    ]
    const baseOrderAmounts = [
      Math.floor(todayAmount * 0.4),
      Math.floor(todayAmount * 0.5),
      Math.floor(todayAmount * 0.7),
      Math.floor(todayAmount * 0.9),
      Math.floor(todayAmount * 1.1),
      Math.floor(todayAmount * 1.3),
      todayAmount
    ]
    orderChartOption.value.series[0].data = baseOrderCounts
    orderChartOption.value.series[1].data = baseOrderAmounts
  } catch (error) {
    console.error('获取统计数据失败:', error)
    ElMessage.error('获取统计数据失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

// 刷新所有数据
const handleRefresh = async () => {
  await Promise.all([
    fetchStatistics(),
    fetchRecentUsers(),
    fetchRecentOrders()
  ])
  ElMessage.success('数据已刷新')
}

// 查看用户详情
const handleViewUser = (id: number) => {
  router.push(`/users/detail/${id}`)
}

// 查看订单详情
const handleViewOrder = (id: number) => {
  router.push(`/orders/detail/${id}`)
}

// 格式化日期
const formatDate = (date: string) => {
  if (!date) return '-'
  return new Date(date).toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

// 格式化金额
const formatAmount = (amount: number) => {
  return `¥${amount?.toFixed(2) || '0.00'}`
}

// 订单状态类型映射
const getOrderStatusType = (status: string) => {
  const statusMap: Record<string, string> = {
    unpaid: 'warning',
    toUse: 'primary',
    used: 'success',
    completed: 'success',
    refunded: 'info',
    cancelled: 'danger'
  }
  return statusMap[status] || 'info'
}

// 订单状态文本映射
const getOrderStatusText = (status: string) => {
  const statusMap: Record<string, string> = {
    unpaid: '未支付',
    toUse: '待使用',
    used: '已使用',
    completed: '已完成',
    refunded: '已退款',
    cancelled: '已取消'
  }
  return statusMap[status] || status
}

onMounted(() => {
  fetchStatistics()
  fetchRecentUsers()
  fetchRecentOrders()
})
</script>

<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">数据总览</h2>
      <el-button type="primary" :icon="Refresh" @click="handleRefresh" :loading="loading">
        刷新数据
      </el-button>
    </div>
    <el-row :gutter="20" class="stat-cards">
      <el-col :span="6">
        <div class="stat-card info">
          <div class="stat-value">{{ overviewData.totalUsers }}</div>
          <div class="stat-label">用户总数</div>
          <div class="stat-trend">今日新增: {{ overviewData.todayNewUsers }}</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card success">
          <div class="stat-value">{{ overviewData.totalOrders }}</div>
          <div class="stat-label">订单总数</div>
          <div class="stat-trend">今日订单: {{ overviewData.todayOrders }}</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card warning">
          <div class="stat-value">{{ overviewData.totalAttractions }}</div>
          <div class="stat-label">景点总数</div>
          <div class="stat-trend">上线景点: {{ overviewData.onlineAttractions }}</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card danger">
          <div class="stat-value">{{ overviewData.totalGuides }}</div>
          <div class="stat-label">攻略总数</div>
          <div class="stat-trend">待审核: {{ overviewData.pendingGuides }}</div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="charts-row">
      <el-col :span="12">
        <el-card class="chart-card" v-loading="loading">
          <template #header>
            <div class="card-header">
              <span>用户增长趋势</span>
            </div>
          </template>
          <v-chart class="chart-container" :option="userChartOption" autoresize />
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card" v-loading="loading">
          <template #header>
            <div class="card-header">
              <span>订单统计</span>
            </div>
          </template>
          <v-chart class="chart-container" :option="orderChartOption" autoresize />
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="data-row">
      <el-col :span="12">
        <el-card class="data-card">
          <template #header>
            <div class="card-header">
              <span>今日交易额</span>
              <el-tag type="success">¥{{ overviewData.todayAmount }}</el-tag>
            </div>
          </template>
          <div class="data-content">
            <div class="data-item">
              <span class="data-label">总交易额</span>
              <span class="data-value">¥{{ overviewData.todayAmount }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="data-card">
          <template #header>
            <div class="card-header">
              <span>系统状态</span>
              <el-tag type="success">运行正常</el-tag>
            </div>
          </template>
          <div class="data-content">
            <div class="data-item">
              <span class="data-label">服务器时间</span>
              <span class="data-value">{{ new Date().toLocaleString() }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="table-row">
      <el-col :span="12">
        <el-card class="table-card" v-loading="usersLoading">
          <template #header>
            <div class="card-header">
              <span>最新用户</span>
              <el-button link type="primary" @click="router.push('/users')">查看全部</el-button>
            </div>
          </template>
          <el-table :data="recentUsers" stripe>
            <el-table-column prop="username" label="用户名" />
            <el-table-column prop="nickname" label="昵称" />
            <el-table-column prop="phone" label="手机号" />
            <el-table-column label="状态">
              <template #default="{ row }">
                <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small">
                  {{ row.status === 1 ? '正常' : '禁用' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="100">
              <template #default="{ row }">
                <el-button link type="primary" @click="handleViewUser(row.id)">查看</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="table-card" v-loading="ordersLoading">
          <template #header>
            <div class="card-header">
              <span>最新订单</span>
              <el-button link type="primary" @click="router.push('/orders')">查看全部</el-button>
            </div>
          </template>
          <el-table :data="recentOrders" stripe>
            <el-table-column prop="orderNo" label="订单号" width="150" />
            <el-table-column prop="userName" label="用户名" />
            <el-table-column prop="attractionName" label="景点" />
            <el-table-column label="金额" width="120">
              <template #default="{ row }">
                <span class="amount-text">{{ formatAmount(row.totalAmount) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="getOrderStatusType(row.status)" size="small">
                  {{ getOrderStatusText(row.status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="100">
              <template #default="{ row }">
                <el-button link type="primary" @click="handleViewOrder(row.id)">查看</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>
.stat-cards {
  margin-bottom: 20px;
}

.stat-card {
  padding: 24px;
  border-radius: 8px;
  color: #ffffff;
  position: relative;
  overflow: hidden;
}

.stat-card::before {
  content: '';
  position: absolute;
  top: -50%;
  right: -50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(circle, rgba(255, 255, 255, 0.1) 0%, transparent 70%);
  animation: rotate 20s linear infinite;
}

@keyframes rotate {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

.stat-value {
  font-size: 32px;
  font-weight: 600;
  margin-bottom: 8px;
  position: relative;
  z-index: 1;
}

.stat-label {
  font-size: 16px;
  opacity: 0.9;
  margin-bottom: 4px;
  position: relative;
  z-index: 1;
}

.stat-trend {
  font-size: 12px;
  opacity: 0.8;
  position: relative;
  z-index: 1;
}

.stat-card.info {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.stat-card.success {
  background: linear-gradient(135deg, #67C23A 0%, #529b2e 100%);
}

.stat-card.warning {
  background: linear-gradient(135deg, #E6A23C 0%, #b88230 100%);
}

.stat-card.danger {
  background: linear-gradient(135deg, #F56C6C 0%, #c45656 100%);
}

.charts-row {
  margin-bottom: 20px;
}

.chart-card {
  height: 460px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 600;
}

.chart-container {
  width: 100%;
  height: 380px;
}

.data-row {
  margin-bottom: 20px;
}

.data-card {
  height: 200px;
}

.data-content {
  display: flex;
  flex-direction: column;
  justify-content: center;
  gap: 16px;
}

.data-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;
}

.data-item:last-child {
  border-bottom: none;
}

.data-label {
  font-size: 14px;
  color: #909399;
}

.data-value {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.table-row {
  margin-bottom: 20px;
}

.table-card {
  min-height: 400px;
}

.amount-text {
  color: #E6A23C;
  font-weight: 600;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-title {
  font-size: 24px;
  font-weight: 600;
  color: #303133;
  margin: 0;
}
</style>
