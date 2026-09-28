<template>
  <view class="orders-container">
    <!-- 标签切换 -->
    <view class="tabs-section">
      <view
        class="tab-item"
        :class="{ active: activeTab === 'all' }"
        @tap="onTabTap('all')"
      >
        全部
      </view>
      <view
        class="tab-item"
        :class="{ active: activeTab === 'toUse' }"
        @tap="onTabTap('toUse')"
      >
        待使用
      </view>
      <view
        class="tab-item"
        :class="{ active: activeTab === 'used' }"
        @tap="onTabTap('used')"
      >
        已使用
      </view>
      <view
        class="tab-item"
        :class="{ active: activeTab === 'completed' }"
        @tap="onTabTap('completed')"
      >
        已完成
      </view>
    </view>

    <!-- 订单列表 -->
    <view class="orders-list">
      <view
        class="order-item"
        v-for="(order, index) in orders"
        :key="index"
        @tap="onOrderTap(order)"
      >
        <view class="order-header">
          <text class="order-no">订单号：{{ order.orderNo }}</text>
          <text class="order-status" :class="'status-' + order.status">
            {{ getStatusText(order.status) }}
          </text>
        </view>
        <view class="order-info">
          <text class="attraction-name">{{ order.attraction ? order.attraction.name : '未知景点' }}</text>
          <view class="ticket-info">
            <text class="ticket-name">{{ order.ticketType ? order.ticketType.name : '未知门票' }}</text>
            <text class="ticket-qty">x{{ order.quantity }}</text>
          </view>
        </view>
        <view class="order-bottom">
          <view class="visit-info">
            <text class="visit-label">游玩日期：</text>
            <text class="visit-date">{{ order.visitDate }}</text>
          </view>
          <text class="total-price">¥{{ order.totalAmount }}</text>
        </view>
      </view>
    </view>

    <!-- 加载状态 -->
    <view v-if="loading" class="loading">
      <text>加载中...</text>
    </view>

    <!-- 无订单状态 -->
    <view v-if="!loading && orders.length === 0" class="empty">
      <text class="empty-icon">📋</text>
      <text class="empty-text">暂无订单</text>
      <text class="empty-tip">去预订心仪景点吧</text>
    </view>
  </view>
</template>

<script>
import { getMyOrders } from '@/api/index.js'

export default {
  data() {
    return {
      activeTab: 'all',
      orders: [],
      loading: false,
      page: 0,
      pageSize: 10,
      hasMore: true
    }
  },
  onLoad() {
    this.loadOrders()
  },
  onPullDownRefresh() {
    this.page = 0
    this.orders = []
    this.hasMore = true
    this.loadOrders().then(() => {
      uni.stopPullDownRefresh()
    })
  },
  onReachBottom() {
    if (this.hasMore && !this.loading) {
      this.loadMore()
    }
  },
  methods: {
    /**
     * 加载订单列表
     */
    async loadOrders() {
      this.loading = true
      try {
        const userInfo = uni.getStorageSync('userInfo')
        if (!userInfo || !userInfo.id) {
          uni.showToast({
            title: '请先登录',
            icon: 'none'
          })
          return
        }

        const params = {
          userId: userInfo.id,
          page: this.page,
          size: this.pageSize
        }

        if (this.activeTab !== 'all') {
          params.status = this.activeTab
        }

        const res = await getMyOrders(params)

        if (this.page === 0) {
          this.orders = res.content || []
        } else {
          this.orders = [...this.orders, ...(res.content || [])]
        }

        this.hasMore = !res.last
      } catch (error) {
        console.error('加载订单失败:', error)
        uni.showToast({
          title: '加载失败',
          icon: 'none'
        })
      } finally {
        this.loading = false
      }
    },

    /**
     * 加载更多
     */
    loadMore() {
      this.page++
      this.loadOrders()
    },

    /**
     * 切换标签
     */
    onTabTap(tab) {
      if (this.activeTab === tab) return
      this.activeTab = tab
      this.page = 0
      this.orders = []
      this.hasMore = true
      this.loadOrders()
    },

    /**
     * 点击订单
     */
    onOrderTap(order) {
      uni.navigateTo({
        url: `/pages/profile/orderDetail?id=${order.id}`
      })
    },

    /**
     * 获取状态文本
     */
    getStatusText(status) {
      const statusMap = {
        'unpaid': '未支付',
        'toUse': '待使用',
        'used': '已使用',
        'completed': '已完成',
        'refunded': '已退款',
        'cancelled': '已取消'
      }
      return statusMap[status] || status
    }
  }
}
</script>

<style scoped>
.orders-container {
  min-height: 100vh;
  background-color: #f5f5f5;
}

/* 标签 */
.tabs-section {
  background-color: #ffffff;
  padding: 20rpx 30rpx;
  display: flex;
  gap: 10rpx;
  margin-bottom: 20rpx;
}

.tab-item {
  flex: 1;
  text-align: center;
  padding: 20rpx 0;
  background-color: #f5f5f5;
  color: #666666;
  border-radius: 8rpx;
  font-size: 28rpx;
}

.tab-item.active {
  background-color: #00bcd4;
  color: #ffffff;
  font-weight: bold;
}

/* 订单列表 */
.orders-list {
  padding: 0 20rpx;
}

.order-item {
  display: flex;
  flex-direction: column;
  background-color: #ffffff;
  border-radius: 16rpx;
  padding: 25rpx;
  margin-bottom: 20rpx;
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-bottom: 20rpx;
  border-bottom: 1rpx solid #f5f5f5;
}

.order-no {
  font-size: 24rpx;
  color: #666666;
}

.order-status {
  font-size: 24rpx;
  padding: 6rpx 16rpx;
  border-radius: 8rpx;
  font-weight: 500;
}

.order-status.status-unpaid {
  color: #ff9800;
  background-color: #fff3e0;
}

.order-status.status-toUse {
  color: #00bcd4;
  background-color: #e3f2fd;
}

.order-status.status-used {
  color: #4caf50;
  background-color: #e8f5e9;
}

.order-status.status-completed {
  color: #999999;
  background-color: #f5f5f5;
}

.order-status.status-refunded {
  color: #ff5722;
  background-color: #ffebee;
}

.order-status.status-cancelled {
  color: #9e9e9e;
  background-color: #f5f5f5;
}

.order-info {
  padding: 15rpx 0;
}

.attraction-name {
  font-size: 28rpx;
  color: #333333;
  font-weight: 500;
  margin-bottom: 10rpx;
  display: block;
}

.ticket-info {
  display: flex;
  align-items: center;
  margin-bottom: 15rpx;
}

.ticket-name {
  font-size: 26rpx;
  color: #666666;
  flex: 1;
}

.ticket-qty {
  font-size: 24rpx;
  color: #999999;
  margin-left: 10rpx;
}

.order-bottom {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  padding-top: 15rpx;
  border-top: 1rpx solid #f0f0f0;
}

.visit-info {
  display: flex;
  align-items: center;
}

.visit-label {
  font-size: 24rpx;
  color: #666666;
  margin-right: 10rpx;
}

.visit-date {
  font-size: 24rpx;
  color: #333333;
}

.total-price {
  font-size: 32rpx;
  color: #ff5722;
  font-weight: bold;
}

/* 状态 */
.loading {
  text-align: center;
  padding: 100rpx;
  font-size: 28rpx;
  color: #999999;
}

.empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 100rpx;
}

.empty-icon {
  font-size: 100rpx;
  margin-bottom: 20rpx;
}

.empty-text {
  font-size: 28rpx;
  color: #333333;
  margin-bottom: 10rpx;
}

.empty-tip {
  font-size: 24rpx;
  color: #999999;
}
</style>
