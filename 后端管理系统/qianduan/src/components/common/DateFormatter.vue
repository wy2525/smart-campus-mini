<script setup lang="ts">
import { computed } from 'vue'

interface Props {
  date: string | Date | number | null | undefined
  format?: 'date' | 'datetime' | 'time' | 'relative'
}

const props = withDefaults(defineProps<Props>(), {
  format: 'datetime'
})

const formattedDate = computed(() => {
  if (!props.date) return '-'
  
  const date = new Date(props.date)
  if (isNaN(date.getTime())) return '-'
  
  switch (props.format) {
    case 'date':
      return date.toLocaleDateString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit'
      })
    case 'datetime':
      return date.toLocaleString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
      })
    case 'time':
      return date.toLocaleTimeString('zh-CN', {
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
      })
    case 'relative':
      return getRelativeTime(date)
    default:
      return date.toLocaleString('zh-CN')
  }
})

function getRelativeTime(date: Date): string {
  const now = new Date()
  const diff = now.getTime() - date.getTime()
  const seconds = Math.floor(diff / 1000)
  const minutes = Math.floor(seconds / 60)
  const hours = Math.floor(minutes / 60)
  const days = Math.floor(hours / 24)
  
  if (days > 0) {
    return `${days}天前`
  } else if (hours > 0) {
    return `${hours}小时前`
  } else if (minutes > 0) {
    return `${minutes}分钟前`
  } else {
    return '刚刚'
  }
}
</script>

<template>
  <span>{{ formattedDate }}</span>
</template>

<style scoped>
</style>

