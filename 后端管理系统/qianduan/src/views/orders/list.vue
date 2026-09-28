<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getOrderList, updateOrderStatus } from '@/api/order'
import StatusTag from '@/components/common/StatusTag.vue'
import type { Order, PageRequest } from '@/types'

const router = useRouter()

const loading = ref(false)
const searchForm = reactive({
  keyword: '',
  status: '',
  visitDate: ''
})

const tableData = ref<Order[]>([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)

const fetchOrderList = async () => {
  loading.value = true
  try {
    const params: PageRequest = {
      page: currentPage.value - 1,
      size: pageSize.value,
      keyword: searchForm.keyword || undefined,
      status: searchForm.status || undefined,
      visitDate: searchForm.visitDate || undefined
    }
    const res = await getOrderList(params)
    tableData.value = res.data.content
    total.value = res.data.totalElements
  } catch (error) {
    console.error('获取订单列表失败:', error)
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  currentPage.value = 1
  fetchOrderList()
}

const handleReset = () => {
  searchForm.keyword = ''
  searchForm.status = ''
  searchForm.visitDate = ''
  currentPage.value = 1
  fetchOrderList()
}

const handleDetail = (row: Order) => {
  router.push(`/orders/${row.id}`)
}

const handleStatusChange = async (row: Order) => {
  try {
    await updateOrderStatus(row.id, row.status)
    ElMessage.success('状态更新成功')
    fetchOrderList()
  } catch (error) {
    ElMessage.error('状态更新失败')
  }
}

const handlePageChange = (page: number) => {
  currentPage.value = page
  fetchOrderList()
}

const handleSizeChange = (size: number) => {
  pageSize.value = size
  currentPage.value = 1
  fetchOrderList()
}


onMounted(() => {
  fetchOrderList()
})
</script>

<template>
  <div class="page-container">
    <div class="content-card">
      <el-form :model="searchForm" inline class="search-form">
        <el-form-item label="关键词">
          <el-input
            v-model="searchForm.keyword"
            placeholder="订单号/景点/手机号"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="订单状态">
          <el-select
            v-model="searchForm.status"
            placeholder="请选择状态"
            clearable
            style="width: 120px"
          >
            <el-option label="未支付" value="unpaid" />
            <el-option label="待使用" value="toUse" />
            <el-option label="已使用" value="used" />
            <el-option label="已完成" value="completed" />
            <el-option label="已退款" value="refunded" />
            <el-option label="已取消" value="cancelled" />
          </el-select>
        </el-form-item>
        <el-form-item label="游玩日期">
          <el-date-picker
            v-model="searchForm.visitDate"
            type="date"
            placeholder="选择日期"
            value-format="YYYY-MM-DD"
            clearable
            style="width: 150px"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>
            搜索
          </el-button>
          <el-button @click="handleReset">
            <el-icon><RefreshLeft /></el-icon>
            重置
          </el-button>
        </el-form-item>
      </el-form>

      <div class="table-container">
        <div class="table-header">
          <span class="table-title">订单列表</span>
        </div>

        <el-table v-loading="loading" :data="tableData" border stripe>
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="orderNo" label="订单号" width="180" />
          <el-table-column prop="user.nickname" label="用户" width="120" />
          <el-table-column prop="attraction.name" label="景点" width="150" />
          <el-table-column prop="ticketType.name" label="门票类型" width="120" />
          <el-table-column prop="quantity" label="数量" width="80" />
          <el-table-column prop="totalAmount" label="总金额" width="100">
            <template #default="{ row }">
              ¥{{ row.totalAmount }}
            </template>
          </el-table-column>
          <el-table-column prop="visitDate" label="游玩日期" width="120" />
          <el-table-column label="状态" width="100">
            <template #default="{ row }">
              <StatusTag :status="row.status" type="order" />
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="180" />
          <el-table-column label="操作" width="150" fixed="right">
            <template #default="{ row }">
              <el-button type="primary" link size="small" @click="handleDetail(row)">
                <el-icon><View /></el-icon>
                查看
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @current-change="handlePageChange"
          @size-change="handleSizeChange"
        />
      </div>
    </div>
  </div>
</template>

<style scoped>
.search-form {
  margin-bottom: 20px;
  padding: 20px;
  background: #ffffff;
  border-radius: 4px;
}

.table-container {
  margin-top: 20px;
}

.table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.table-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}
</style>
