<script setup lang="ts">
import { computed } from 'vue'

interface Props {
  status: string | number
  type?: 'order' | 'user' | 'attraction' | 'guide'
}

const props = withDefaults(defineProps<Props>(), {
  type: 'order'
})

// 订单状态映射
const orderStatusMap: Record<string, { text: string; type: string }> = {
  unpaid: { text: '未支付', type: 'warning' },
  toUse: { text: '待使用', type: 'primary' },
  used: { text: '已使用', type: 'success' },
  completed: { text: '已完成', type: 'success' },
  refunded: { text: '已退款', type: 'info' },
  cancelled: { text: '已取消', type: 'danger' }
}

// 用户状态映射
const userStatusMap: Record<number, { text: string; type: string }> = {
  1: { text: '启用', type: 'success' },
  0: { text: '禁用', type: 'danger' }
}

// 景点状态映射
const attractionStatusMap: Record<number, { text: string; type: string }> = {
  1: { text: '上线', type: 'success' },
  0: { text: '下线', type: 'info' }
}

// 攻略状态映射
const guideStatusMap: Record<string, { text: string; type: string }> = {
  pending: { text: '待审核', type: 'warning' },
  approved: { text: '已通过', type: 'success' },
  rejected: { text: '已拒绝', type: 'danger' }
}

const statusInfo = computed(() => {
  const status = String(props.status)
  
  switch (props.type) {
    case 'order':
      return orderStatusMap[status] || { text: status, type: 'info' }
    case 'user':
      return userStatusMap[Number(status)] || { text: '未知', type: 'info' }
    case 'attraction':
      return attractionStatusMap[Number(status)] || { text: '未知', type: 'info' }
    case 'guide':
      return guideStatusMap[status] || { text: status, type: 'info' }
    default:
      return { text: status, type: 'info' }
  }
})
</script>

<template>
  <el-tag :type="statusInfo.type as any" size="small">
    {{ statusInfo.text }}
  </el-tag>
</template>

<style scoped>
</style>

