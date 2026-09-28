<template>
  <view class="bookings-container">
    <view class="welcome-section">
      <text class="welcome-text">预订功能</text>
      <text class="welcome-sub">请选择以下功能</text>
    </view>

    <view class="quick-entries">
      <view class="entry-card" @tap="onAttractionsTap">
        <text class="entry-icon">🎫</text>
        <text class="entry-title">景点预订</text>
        <text class="entry-desc">浏览热门景点，选择门票类型</text>
      </view>

      <view class="entry-card" @tap="onMyOrdersTap">
        <text class="entry-icon">📋</text>
        <text class="entry-title">我的订单</text>
        <text class="entry-desc">查看和管理您的订单</text>
      </view>

      <view class="entry-card" @tap="onSearchTap">
        <text class="entry-icon">🔍</text>
        <text class="entry-title">景点搜索</text>
        <text class="entry-desc">搜索心仪景点</text>
      </view>
    </view>

    <view class="tips-section">
      <view class="tip-card">
        <text class="tip-title">💡 小贴士</text>
        <view class="tip-content">
          <text class="tip-item">• 提前1-2天预订可获得更好价格</text>
          <text class="tip-item">• 关注景区开放时间，合理安排行程</text>
          <text class="tip-item">• 如遇恶劣天气，及时联系景区客服</text>
          <text class="tip-item">• 带好身份证，部分景点需实名购票</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  methods: {
    /**
     * 景点预订
     */
    onAttractionsTap() {
      uni.switchTab({
        url: '/pages/home/index'
      })
    },

    /**
     * 我的订单
     */
    onMyOrdersTap() {
      const token = uni.getStorageSync('token')
      if (!token) {
        uni.showToast({
          title: '请先登录',
          icon: 'none'
        })
        setTimeout(() => {
          uni.navigateTo({
            url: '/pages/profile/login'
          })
        }, 1500)
        return
      }

      uni.navigateTo({
        url: '/pages/profile/orders'
      })
    },

    /**
     * 景点搜索
     */
    onSearchTap() {
      uni.navigateTo({
        url: '/pages/attractions/list'
      })
    }
  }
}
</script>

<style scoped>
.bookings-container {
  min-height: 100vh;
  background-color: #f5f5f5;
}

/* 欢迎区域 */
.welcome-section {
  text-align: center;
  padding: 80rpx 40rpx;
}

.welcome-text {
  font-size: 40rpx;
  font-weight: bold;
  color: #333333;
  display: block;
  margin-bottom: 15rpx;
}

.welcome-sub {
  font-size: 28rpx;
  color: #666666;
}

/* 快捷入口 */
.quick-entries {
  padding: 0 30rpx 30rpx;
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.entry-card {
  display: flex;
  align-items: center;
  background-color: #ffffff;
  padding: 30rpx;
  border-radius: 16rpx;
  box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.05);
}

.entry-icon {
  font-size: 60rpx;
  margin-right: 20rpx;
}

.entry-title {
  font-size: 30rpx;
  color: #333333;
  font-weight: bold;
  flex: 1;
}

.entry-desc {
  font-size: 24rpx;
  color: #999999;
  flex: 1;
}

/* 小贴士 */
.tips-section {
  padding: 30rpx;
}

.tip-card {
  background-color: #ffffff;
  border-radius: 16rpx;
  padding: 30rpx;
}

.tip-title {
  font-size: 28rpx;
  color: #333333;
  font-weight: bold;
  margin-bottom: 20rpx;
}

.tip-content {
  display: flex;
  flex-direction: column;
  gap: 15rpx;
}

.tip-item {
  font-size: 24rpx;
  color: #666666;
  line-height: 1.6;
}
</style>
