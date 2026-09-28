<script setup lang="ts">
import { computed } from 'vue'

interface Props {
  type?: 'empty' | 'error' | 'network' | 'search' | 'favorite' | 'order'
  message?: string
  description?: string
  showAction?: boolean
  actionText?: string
}

const props = withDefaults(defineProps<Props>(), {
  type: 'empty',
  message: '',
  description: '',
  showAction: true,
  actionText: '刷新重试'
})

const emit = defineEmits<{
  (e: 'action'): void
}>()

// 图标映射
const iconMap: Record<string, string> = {
  empty: '📭',
  error: '😵',
  network: '🌐',
  search: '🔍',
  favorite: '⭐',
  order: '📋'
}

// 默认文案
const defaultMessages: Record<string, { title: string; desc: string }> = {
  empty: { title: '暂无数据', desc: '暂时没有相关内容' },
  error: { title: '页面出错', desc: '抱歉，页面出现了问题' },
  network: { title: '网络异常', desc: '请检查您的网络连接' },
  search: { title: '未找到结果', desc: '换个关键词试试吧' },
  favorite: { title: '暂无收藏', desc: '收藏您喜欢的景点和攻略' },
  order: { title: '暂无订单', desc: '快去预订景点门票吧' }
}

const displayMessage = computed(() => props.message || defaultMessages[props.type]?.title || '暂无数据')
const displayDescription = computed(() => props.description || defaultMessages[props.type]?.desc || '')
const displayIcon = computed(() => iconMap[props.type] || '📭')

const handleAction = () => {
  emit('action')
}
</script>

<template>
  <view class="empty-container">
    <view class="empty-icon-wrapper">
      <text class="empty-icon">{{ displayIcon }}</text>
    </view>
    <text class="empty-title">{{ displayMessage }}</text>
    <text class="empty-desc" v-if="displayDescription">{{ displayDescription }}</text>
    <view class="empty-action" v-if="showAction" @tap="handleAction">
      <text class="action-text">{{ actionText }}</text>
    </view>
  </view>
</template>

<style scoped>
.empty-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 100rpx 60rpx;
}

.empty-icon-wrapper {
  width: 160rpx;
  height: 160rpx;
  background: linear-gradient(135deg, #f5f7fa 0%, #e4e8eb 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 32rpx;
  animation: float 3s ease-in-out infinite;
}

@keyframes float {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-10rpx);
  }
}

.empty-icon {
  font-size: 80rpx;
}

.empty-title {
  font-size: 36rpx;
  font-weight: 600;
  color: #333333;
  margin-bottom: 16rpx;
}

.empty-desc {
  font-size: 28rpx;
  color: #999999;
  text-align: center;
  line-height: 1.6;
  margin-bottom: 40rpx;
}

.empty-action {
  padding: 24rpx 60rpx;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 50rpx;
  box-shadow: 0 12rpx 32rpx rgba(102, 126, 234, 0.3);
  transition: all 0.3s ease;
}

.empty-action:active {
  transform: scale(0.95);
  box-shadow: 0 8rpx 24rpx rgba(102, 126, 234, 0.2);
}

.action-text {
  font-size: 30rpx;
  color: #ffffff;
  font-weight: 500;
}
</style>

