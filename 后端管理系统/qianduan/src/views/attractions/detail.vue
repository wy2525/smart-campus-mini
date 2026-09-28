<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { getAttractionDetail, getAttractionTickets } from '@/api/attraction'
import type { Attraction, TicketType } from '@/types'

const router = useRouter()
const route = useRoute()

const loading = ref(false)
const attraction = ref<Attraction | null>(null)
const tickets = ref<TicketType[]>([])

const fetchDetail = async () => {
  loading.value = true
  try {
    const id = parseInt(route.params.id as string)
    const [attractionRes, ticketsRes] = await Promise.all([
      getAttractionDetail(id),
      getAttractionTickets(id)
    ])
    attraction.value = attractionRes.data
    tickets.value = ticketsRes.data
  } catch (error) {
    console.error('获取景点详情失败:', error)
  } finally {
    loading.value = false
  }
}

const statusFormatter = (status: number) => {
  return status === 1 ? '上线' : '下线'
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
          <span>景点详情</span>
          <el-button @click="router.back()">返回</el-button>
        </div>
      </template>

      <div v-if="attraction" class="attraction-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="景点ID">{{ attraction.id }}</el-descriptions-item>
          <el-descriptions-item label="景点名称">{{ attraction.name }}</el-descriptions-item>
          <el-descriptions-item label="分类">{{ attraction.category }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="attraction.status === 1 ? 'success' : 'danger'">
              {{ statusFormatter(attraction.status) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="最低价格">
            ¥{{ attraction.minPrice }}
          </el-descriptions-item>
          <el-descriptions-item label="评分">{{ attraction.rating }}</el-descriptions-item>
          <el-descriptions-item label="浏览量">{{ attraction.viewCount }}</el-descriptions-item>
          <el-descriptions-item label="预订量">{{ attraction.bookingCount }}</el-descriptions-item>
          <el-descriptions-item label="地址" :span="2">
            {{ attraction.address }}
          </el-descriptions-item>
          <el-descriptions-item label="经度">{{ attraction.longitude }}</el-descriptions-item>
          <el-descriptions-item label="纬度">{{ attraction.latitude }}</el-descriptions-item>
          <el-descriptions-item label="联系电话" :span="2">
            {{ attraction.phone }}
          </el-descriptions-item>
          <el-descriptions-item label="开放时间" :span="2">
            {{ attraction.openTime }}
          </el-descriptions-item>
          <el-descriptions-item label="标签" :span="2">
            {{ attraction.tags }}
          </el-descriptions-item>
          <el-descriptions-item label="封面图" :span="2">
            <el-image
              v-if="attraction.coverImage"
              :src="attraction.coverImage"
              style="width: 200px"
              fit="cover"
              :preview-src-list="[attraction.coverImage]"
            />
            <span v-else>-</span>
          </el-descriptions-item>
          <el-descriptions-item label="介绍" :span="2">
            {{ attraction.description }}
          </el-descriptions-item>
          <el-descriptions-item label="注意事项" :span="2">
            {{ attraction.notes }}
          </el-descriptions-item>
          <el-descriptions-item label="创建时间" :span="2">
            {{ attraction.createTime }}
          </el-descriptions-item>
          <el-descriptions-item label="更新时间" :span="2">
            {{ attraction.updateTime }}
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </el-card>

    <el-card class="tickets-card">
      <template #header>
        <div class="card-header">
          <span>门票类型</span>
        </div>
      </template>

      <el-table :data="tickets" stripe>
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="name" label="门票名称" />
        <el-table-column prop="description" label="描述" />
        <el-table-column prop="price" label="价格" width="100">
          <template #default="{ row }">
            ¥{{ row.price }}
          </template>
        </el-table-column>
        <el-table-column prop="stock" label="库存" width="80" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '上架' : '下架' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="sort" label="排序" width="80" />
      </el-table>
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

.attraction-detail {
  padding: 20px 0;
}

.tickets-card {
  margin-bottom: 20px;
}
</style>
