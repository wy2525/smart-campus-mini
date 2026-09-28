<script setup lang="ts">
import { computed } from 'vue'

interface Props {
  type?: 'list' | 'card' | 'detail' | 'avatar'
  rows?: number
}

const props = withDefaults(defineProps<Props>(), {
  type: 'list',
  rows: 5
})

// 骨架屏数组
const skeletonRows = computed(() => Array.from({ length: props.rows }, (_, i) => i))
</script>

<template>
  <view class="skeleton-container">
    <!-- 列表骨架屏 -->
    <template v-if="type === 'list'">
      <view 
        class="skeleton-item" 
        v-for="(_, index) in skeletonRows" 
        :key="index"
      >
        <view class="skeleton-image"></view>
        <view class="skeleton-content">
          <view class="skeleton-title"></view>
          <view class="skeleton-text"></view>
          <view class="skeleton-text short"></view>
        </view>
      </view>
    </template>

    <!-- 卡片骨架屏 -->
    <template v-else-if="type === 'card'">
      <view class="skeleton-cards">
        <view 
          class="skeleton-card" 
          v-for="(_, index) in skeletonRows" 
          :key="index"
        >
          <view class="skeleton-cover"></view>
          <view class="skeleton-card-content">
            <view class="skeleton-title"></view>
            <view class="skeleton-text"></view>
            <view class="skeleton-footer">
              <view class="skeleton-badge"></view>
              <view class="skeleton-price"></view>
            </view>
          </view>
        </view>
      </view>
    </template>

    <!-- 详情骨架屏 -->
    <template v-else-if="type === 'detail'">
      <view class="skeleton-detail">
        <view class="skeleton-banner"></view>
        <view class="skeleton-info">
          <view class="skeleton-title large"></view>
          <view class="skeleton-meta">
            <view class="skeleton-avatar"></view>
            <view class="skeleton-text"></view>
          </view>
        </view>
        <view class="skeleton-sections">
          <view 
            class="skeleton-section" 
            v-for="(_, index) in skeletonRows.slice(0, 3)" 
            :key="index"
          >
            <view class="skeleton-title"></view>
            <view class="skeleton-paragraph" v-for="(_, pIdx) in 2" :key="pIdx"></view>
          </view>
        </view>
      </view>
    </template>

    <!-- 头像骨架屏 -->
    <template v-else-if="type === 'avatar'">
      <view class="skeleton-avatar-list">
        <view 
          class="skeleton-avatar-item" 
          v-for="(_, index) in skeletonRows" 
          :key="index"
        >
          <view class="skeleton-avatar-circle"></view>
          <view class="skeleton-avatar-info">
            <view class="skeleton-title"></view>
            <view class="skeleton-text"></view>
          </view>
        </view>
      </view>
    </template>
  </view>
</template>

<style scoped>
.skeleton-container {
  width: 100%;
  padding: 20rpx;
}

/* 列表骨架屏 */
.skeleton-item {
  display: flex;
  padding: 24rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
}

.skeleton-image {
  width: 180rpx;
  height: 140rpx;
  background: linear-gradient(90deg, #f0f0f0 25%, #e8e8e8 50%, #f0f0f0 75%);
  background-size: 200% 100%;
  border-radius: 12rpx;
  animation: shimmer 1.5s infinite;
  flex-shrink: 0;
}

.skeleton-content {
  flex: 1;
  margin-left: 24rpx;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.skeleton-title {
  width: 70%;
  height: 32rpx;
  background: linear-gradient(90deg, #f0f0f0 25%, #e8e8e8 50%, #f0f0f0 75%);
  background-size: 200% 100%;
  border-radius: 8rpx;
  animation: shimmer 1.5s infinite;
  margin-bottom: 16rpx;
}

.skeleton-title.large {
  width: 90%;
  height: 48rpx;
  margin-bottom: 24rpx;
}

.skeleton-text {
  width: 100%;
  height: 24rpx;
  background: linear-gradient(90deg, #f0f0f0 25%, #e8e8e8 50%, #f0f0f0 75%);
  background-size: 200% 100%;
  border-radius: 6rpx;
  animation: shimmer 1.5s infinite;
  margin-bottom: 12rpx;
}

.skeleton-text.short {
  width: 60%;
}

/* 卡片骨架屏 */
.skeleton-cards {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 24rpx;
}

.skeleton-card {
  background: #ffffff;
  border-radius: 16rpx;
  overflow: hidden;
  box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.05);
}

.skeleton-cover {
  width: 100%;
  height: 200rpx;
  background: linear-gradient(90deg, #f0f0f0 25%, #e8e8e8 50%, #f0f0f0 75%);
  background-size: 200% 100%;
  animation: shimmer 1.5s infinite;
}

.skeleton-card-content {
  padding: 20rpx;
}

.skeleton-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 16rpx;
}

.skeleton-badge {
  width: 80rpx;
  height: 32rpx;
  background: linear-gradient(90deg, #f0f0f0 25%, #e8e8e8 50%, #f0f0f0 75%);
  background-size: 200% 100%;
  border-radius: 16rpx;
  animation: shimmer 1.5s infinite;
}

.skeleton-price {
  width: 100rpx;
  height: 36rpx;
  background: linear-gradient(90deg, #f0f0f0 25%, #e8e8e8 50%, #f0f0f0 75%);
  background-size: 200% 100%;
  border-radius: 8rpx;
  animation: shimmer 1.5s infinite;
}

/* 详情骨架屏 */
.skeleton-detail {
  padding: 0;
}

.skeleton-banner {
  width: 100%;
  height: 500rpx;
  background: linear-gradient(90deg, #f0f0f0 25%, #e8e8e8 50%, #f0f0f0 75%);
  background-size: 200% 100%;
  animation: shimmer 1.5s infinite;
}

.skeleton-info {
  background: #ffffff;
  padding: 40rpx;
  margin-top: -40rpx;
  position: relative;
  border-radius: 40rpx 40rpx 0 0;
}

.skeleton-meta {
  display: flex;
  align-items: center;
  margin-top: 24rpx;
  padding-top: 24rpx;
  border-top: 1rpx solid #f0f0f0;
}

.skeleton-avatar {
  width: 80rpx;
  height: 80rpx;
  background: linear-gradient(90deg, #f0f0f0 25%, #e8e8e8 50%, #f0f0f0 75%);
  background-size: 200% 100%;
  border-radius: 50%;
  animation: shimmer 1.5s infinite;
  margin-right: 20rpx;
}

.skeleton-sections {
  padding: 40rpx;
  background: #ffffff;
  margin-top: 20rpx;
}

.skeleton-section {
  margin-bottom: 32rpx;
}

.skeleton-paragraph {
  width: 100%;
  height: 28rpx;
  background: linear-gradient(90deg, #f0f0f0 25%, #e8e8e8 50%, #f0f0f0 75%);
  background-size: 200% 100%;
  border-radius: 6rpx;
  animation: shimmer 1.5s infinite;
  margin-top: 16rpx;
}

.skeleton-paragraph:nth-child(2) {
  width: 90%;
}

/* 头像骨架屏 */
.skeleton-avatar-list {
  display: flex;
  flex-direction: column;
}

.skeleton-avatar-item {
  display: flex;
  align-items: center;
  padding: 24rpx;
  background: #ffffff;
  border-radius: 16rpx;
  margin-bottom: 20rpx;
}

.skeleton-avatar-circle {
  width: 100rpx;
  height: 100rpx;
  background: linear-gradient(90deg, #f0f0f0 25%, #e8e8e8 50%, #f0f0f0 75%);
  background-size: 200% 100%;
  border-radius: 50%;
  animation: shimmer 1.5s infinite;
  flex-shrink: 0;
}

.skeleton-avatar-info {
  flex: 1;
  margin-left: 24rpx;
}

@keyframes shimmer {
  0% {
    background-position: 200% 0;
  }
  100% {
    background-position: -200% 0;
  }
}
</style>

