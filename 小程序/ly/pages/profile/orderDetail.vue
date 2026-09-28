<template>
  <view class="detail-container">
    <!-- 订单状态 -->
    <view class="status-section">
      <view class="status-card" :class="'status-' + order.status">
        <text class="status-icon">{{ getStatusIcon(order.status) }}</text>
        <text class="status-text">{{ getStatusText(order.status) }}</text>
      </view>
    </view>

    <!-- 订单信息 -->
    <view class="info-section">
      <view class="section-title">
        <text>订单信息</text>
      </view>
      <view class="info-card">
        <view class="info-row">
          <text class="label">订单号</text>
          <text class="value">{{ order.orderNo }}</text>
        </view>
        <view class="info-row">
          <text class="label">下单时间</text>
          <text class="value">{{ order.createTime }}</text>
        </view>
        <view class="info-row">
          <text class="label">游玩日期</text>
          <text class="value">{{ order.visitDate }}</text>
        </view>
      </view>
    </view>

    <!-- 景点信息 -->
    <view class="attraction-section">
      <view class="section-title">
        <text>景点信息</text>
      </view>
      <view class="attraction-card">
        <image class="attraction-image" :src="order.attraction ? order.attraction.coverImage : ''" mode="aspectFill"></image>
        <view class="attraction-info">
          <text class="name">{{ order.attraction ? order.attraction.name : '未知' }}</text>
          <text class="desc">{{ order.attraction ? order.attraction.description : '' }}</text>
          <text class="address">{{ order.attraction ? order.attraction.address : '' }}</text>
        </view>
      </view>
    </view>

    <!-- 门票信息 -->
    <view class="ticket-section">
      <view class="section-title">
        <text>门票信息</text>
      </view>
      <view class="ticket-card">
        <view class="ticket-row">
          <text class="label">门票类型</text>
          <text class="value">{{ order.ticketType ? order.ticketType.name : '未知' }}</text>
        </view>
        <view class="ticket-row">
          <text class="label">数量</text>
          <text class="value">{{ order.quantity }}</text>
        </view>
        <view class="ticket-row">
          <text class="label">单价</text>
          <text class="value price">¥{{ order.ticketType ? order.ticketType.price : '0' }}</text>
        </view>
        <view class="ticket-row total">
          <text class="label">总价</text>
          <text class="value price">¥{{ order.totalAmount }}</text>
        </view>
      </view>
    </view>

    <!-- 游客信息 -->
    <view class="visitor-section">
      <view class="section-title">
        <text>游客信息</text>
      </view>
      <view class="visitor-card">
        <view class="visitor-row">
          <text class="label">姓名</text>
          <text class="value">{{ order.visitorName }}</text>
        </view>
        <view class="visitor-row">
          <text class="label">手机号</text>
          <text class="value">{{ order.visitorPhone }}</text>
        </view>
      </view>
    </view>

    <!-- 底部操作 -->
    <view class="bottom-section" v-if="order.status === 'unpaid'">
      <button class="btn btn-cancel" @tap="onCancelTap">取消订单</button>
      <button class="btn btn-pay" @tap="onPayTap">立即支付</button>
    </view>

    <!-- 已完成状态提示 -->
    <view class="tip-section" v-if="order.status === 'used' || order.status === 'completed'">
      <text class="tip-text">感谢您的使用</text>
    </view>
  </view>
</template>

<script>
import { getOrderDetail, cancelOrder } from '@/api/index.js'

export default {
  data() {
    return {
      orderId: null,
      order: {},
      loading: false
    }
  },
  onLoad(options) {
    if (options.id) {
      this.orderId = options.id
      this.loadOrderDetail()
    }
  },
  methods: {
    /**
     * 加载订单详情
     */
    async loadOrderDetail() {
      this.loading = true
      try {
        const order = await getOrderDetail(this.orderId)
        this.order = order || {}

        uni.setNavigationBarTitle({
          title: '订单详情'
        })
      } catch (error) {
        console.error('加载订单详情失败:', error)
        uni.showToast({
          title: '加载失败',
          icon: 'none'
        })
      } finally {
        this.loading = false
      }
    },

    /**
     * 获取状态图标
     */
    getStatusIcon(status) {
      const iconMap = {
        'unpaid': '⏰',
        'toUse': '🎫',
        'used': '✅',
        'completed': '✨',
        'refunded': '💰',
        'cancelled': '🚫'
      }
      return iconMap[status] || '📋'
    },

    /**
     * 获取状态文本
     */
    getStatusText(status) {
      const statusMap = {
        'unpaid': '待支付',
        'toUse': '待使用',
        'used': '已使用',
        'completed': '已完成',
        'refunded': '已退款',
        'cancelled': '已取消'
      }
      return statusMap[status] || status
    },

    /**
     * 取消订单
     */
    async onCancelTap() {
      uni.showModal({
        title: '提示',
        content: '确定要取消订单吗？',
        success: async (res) => {
          if (res.confirm) {
            try {
              await cancelOrder(this.orderId)
              uni.showToast({
                title: '订单已取消',
                icon: 'success',
                duration: 1500
              })

              setTimeout(() => {
                uni.navigateBack()
              }, 1500)
            } catch (error) {
              console.error('取消订单失败:', error)
              uni.showToast({
                title: '取消失败',
                icon: 'none'
              })
            }
          }
        }
      })
    },

    /**
     * 立即支付
     */
    onPayTap() {
      uni.showToast({
        title: '支付功能开发中',
        icon: 'none'
      })
    }
  }
}
</script>

<style scoped>
.detail-container {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding-bottom: 120rpx;
}

/* 状态卡片 */
.status-section {
  padding: 30rpx;
}

.status-card {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40rpx;
  border-radius: 16rpx;
  margin-bottom: 20rpx;
}

.status-icon {
  font-size: 60rpx;
  margin-right: 20rpx;
}

.status-text {
  font-size: 32rpx;
  font-weight: bold;
}

.status-card.status-unpaid {
  background-color: #fff3e0;
  color: #ff9800;
}

.status-card.status-toUse {
  background-color: #e3f2fd;
  color: #00bcd4;
}

.status-card.status-used,
.status-card.status-completed {
  background-color: #e8f5e9;
  color: #4caf50;
}

.status-card.status-refunded {
  background-color: #ffebee;
  color: #ff5722;
}

.status-card.status-cancelled {
  background-color: #f5f5f5;
  color: #9e9e9e;
}

/* 信息卡片 */
.info-section,
.attraction-section,
.ticket-section,
.visitor-section {
  background-color: #ffffff;
  padding: 30rpx;
  margin: 20rpx;
  border-radius: 16rpx;
}

.section-title {
  font-size: 30rpx;
  font-weight: bold;
  color: #333333;
  margin-bottom: 20rpx;
  padding-bottom: 15rpx;
  border-bottom: 2rpx solid #00bcd4;
}

.info-card,
.attraction-card,
.ticket-card,
.visitor-card {
  padding: 0 20rpx;
}

.info-row,
.ticket-row,
.visitor-row {
  display: flex;
  padding: 15rpx 0;
  border-bottom: 1rpx solid #f5f5f5;
}

.info-row:last-child,
.ticket-row:last-child,
.visitor-row:last-child {
  border-bottom: none;
}

.label {
  font-size: 26rpx;
  color: #666666;
  width: 180rpx;
  flex-shrink: 0;
}

.value {
  flex: 1;
  font-size: 26rpx;
  color: #333333;
}

.price {
  font-weight: bold;
}

.ticket-row.total {
  padding-top: 20rpx;
  margin-top: 10rpx;
  border-top: 2rpx solid #00bcd4;
  border-bottom: none;
}

.attraction-image {
  width: 100%;
  height: 300rpx;
  border-radius: 12rpx;
  margin-bottom: 20rpx;
}

.name {
  font-size: 30rpx;
  color: #333333;
  font-weight: bold;
  margin-bottom: 10rpx;
}

.desc,
.address {
  font-size: 26rpx;
  color: #666666;
  line-height: 1.6;
  margin-bottom: 10rpx;
}

/* 底部操作 */
.bottom-section {
  padding: 30rpx;
  display: flex;
  gap: 20rpx;
}

.btn {
  flex: 1;
  padding: 28rpx 0;
  height: 90rpx;
  line-height: 90rpx;
  border-radius: 12rpx;
  font-size: 32rpx;
  border: none;
}

.btn-cancel {
  background-color: #f5f5f5;
  color: #666666;
  border: 2rpx solid #999999;
}

.btn-pay {
  background-color: #00bcd4;
  color: #ffffff;
}

/* 提示 */
.tip-section {
  text-align: center;
  padding: 30rpx;
}

.tip-text {
  font-size: 28rpx;
  color: #999999;
}
</style>
