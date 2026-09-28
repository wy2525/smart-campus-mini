<template>
  <view class="ticket-container">
    <!-- 景点信息卡片 -->
    <view class="attraction-card">
      <image class="attraction-image" :src="attraction.coverImage" mode="aspectFill"></image>
      <view class="attraction-info">
        <text class="attraction-name">{{ attraction.name }}</text>
        <text class="attraction-desc">{{ attraction.description || '' }}</text>
      </view>
    </view>

    <!-- 门票类型列表 -->
    <view class="tickets-section">
      <view class="section-title">
        <text>选择门票类型</text>
      </view>

      <view class="tickets-list">
        <view
          class="ticket-item"
          v-for="(ticket, index) in tickets"
          :key="index"
          :class="{ selected: selectedTicketId === ticket.id }"
          @tap="onTicketSelect(ticket)"
        >
          <view class="ticket-info">
            <text class="ticket-name">{{ ticket.name }}</text>
            <text class="ticket-desc">{{ ticket.description || '' }}</text>
          </view>
          <view class="ticket-right">
            <text class="ticket-price">¥{{ ticket.price }}</text>
            <text class="ticket-stock">库存：{{ ticket.stock }}</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 数量选择 -->
    <view class="quantity-section">
      <view class="quantity-bar">
        <text class="quantity-label">数量</text>
        <view class="quantity-selector">
          <button class="qty-btn" @tap="onQtyMinus">-</button>
          <input class="qty-input" type="number" v-model="quantity" disabled />
          <button class="qty-btn" @tap="onQtyPlus">+</button>
        </view>
      </view>
    </view>

    <!-- 总价 -->
    <view class="total-section">
      <view class="total-info">
        <text class="total-label">总计：</text>
        <text class="total-price">¥{{ totalPrice }}</text>
      </view>
    </view>

    <!-- 底部按钮 -->
    <view class="bottom-bar">
      <button class="btn btn-secondary" @tap="onBack">返回</button>
      <button class="btn btn-primary" @tap="onNext">下一步</button>
    </view>
  </view>
</template>

<script>
import { getAttractionDetail, getAttractionTickets } from '@/api/index.js'

export default {
  data() {
    return {
      attractionId: null,
      ticketId: null,
      attraction: {},
      tickets: [],
      selectedTicketId: null,
      selectedTicket: {},
      quantity: 1,
      loading: false
    }
  },
  computed: {
    /**
     * 计算总价
     */
    totalPrice() {
      if (!this.selectedTicket || !this.selectedTicket.price) return '0.00'
      return (this.selectedTicket.price * this.quantity).toFixed(2)
    }
  },
  onLoad(options) {
    if (options.attractionId) {
      this.attractionId = options.attractionId
    }
    if (options.ticketId) {
      this.ticketId = options.ticketId
    }
    this.loadTicketData()
  },
  methods: {
    /**
     * 加载门票数据
     */
    async loadTicketData() {
      this.loading = true
      try {
        const [attractionRes, ticketsRes] = await Promise.all([
          getAttractionDetail(this.attractionId),
          getAttractionTickets(this.attractionId)
        ])

        this.attraction = attractionRes || {}
        this.tickets = ticketsRes || []

        // 默认选择第一个门票
        if (this.ticketId) {
          const ticket = this.tickets.find(t => t.id == this.ticketId)
          if (ticket) {
            this.selectedTicketId = ticket.id
            this.selectedTicket = ticket
          }
        } else if (this.tickets.length > 0) {
          this.selectedTicketId = this.tickets[0].id
          this.selectedTicket = this.tickets[0]
        }

        uni.setNavigationBarTitle({
          title: '门票选择'
        })
      } catch (error) {
        console.error('加载门票数据失败:', error)
        uni.showToast({
          title: '加载失败',
          icon: 'none'
        })
      } finally {
        this.loading = false
      }
    },

    /**
     * 选择门票
     */
    onTicketSelect(ticket) {
      // 检查库存
      if (ticket.stock <= 0) {
        uni.showToast({
          title: '该门票已售罄',
          icon: 'none'
        })
        return
      }

      this.selectedTicketId = ticket.id
      this.selectedTicket = ticket
      // 重置数量
      this.quantity = 1
    },

    /**
     * 减少数量
     */
    onQtyMinus() {
      if (this.quantity > 1) {
        this.quantity--
      }
    },

    /**
     * 增加数量
     */
    onQtyPlus() {
      // 检查库存
      if (this.quantity >= this.selectedTicket.stock) {
        uni.showToast({
          title: '超过库存上限',
          icon: 'none'
        })
        return
      }

      this.quantity++
    },

    /**
     * 返回
     */
    onBack() {
      uni.navigateBack()
    },

    /**
     * 下一步
     */
    onNext() {
      if (!this.selectedTicketId) {
        uni.showToast({
          title: '请选择门票类型',
          icon: 'none'
        })
        return
      }

      // 传递订单数据
      const orderData = {
        attractionId: this.attraction.id,
        attractionName: this.attraction.name,
        ticketId: this.selectedTicket.id,
        ticketName: this.selectedTicket.name,
        ticketPrice: this.selectedTicket.price,
        quantity: this.quantity,
        totalPrice: this.totalPrice
      }

      // 存储订单数据
      uni.setStorageSync('orderData', orderData)

      uni.navigateTo({
        url: '/pages/attractions/order'
      })
    }
  }
}
</script>

<style scoped>
.ticket-container {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding-bottom: 120rpx;
}

/* 景点卡片 */
.attraction-card {
  display: flex;
  background-color: #ffffff;
  padding: 30rpx;
  margin: 20rpx;
  border-radius: 16rpx;
}

.attraction-image {
  width: 200rpx;
  height: 160rpx;
  border-radius: 12rpx;
  margin-right: 20rpx;
  flex-shrink: 0;
}

.attraction-info {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.attraction-name {
  font-size: 30rpx;
  color: #333333;
  font-weight: bold;
  margin-bottom: 10rpx;
}

.attraction-desc {
  font-size: 24rpx;
  color: #999999;
}

/* 门票列表 */
.tickets-section {
  background-color: #ffffff;
  padding: 30rpx;
  margin: 20rpx;
}

.section-title {
  font-size: 30rpx;
  font-weight: bold;
  color: #333333;
  margin-bottom: 20rpx;
}

.tickets-list {
  margin-top: 20rpx;
}

.ticket-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 25rpx;
  border: 2rpx solid #f0f0f0;
  border-radius: 12rpx;
  margin-bottom: 20rpx;
}

.ticket-item.selected {
  border-color: #00bcd4;
  background-color: #e3f2fd;
}

.ticket-info {
  flex: 1;
}

.ticket-name {
  font-size: 28rpx;
  color: #333333;
  font-weight: 500;
  display: block;
  margin-bottom: 8rpx;
}

.ticket-desc {
  font-size: 24rpx;
  color: #999999;
}

.ticket-right {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
}

.ticket-price {
  font-size: 32rpx;
  color: #ff5722;
  font-weight: bold;
  margin-bottom: 5rpx;
}

.ticket-stock {
  font-size: 24rpx;
  color: #999999;
}

/* 数量选择 */
.quantity-section {
  background-color: #ffffff;
  padding: 30rpx;
  margin: 20rpx;
}

.quantity-bar {
  display: flex;
  align-items: center;
  padding: 20rpx 30rpx;
  background-color: #f5f5f5;
  border-radius: 12rpx;
}

.quantity-label {
  font-size: 28rpx;
  color: #666666;
  margin-right: 20rpx;
}

.quantity-selector {
  display: flex;
  align-items: center;
  flex: 1;
}

.qty-btn {
  width: 70rpx;
  height: 70rpx;
  line-height: 70rpx;
  padding: 0;
  background-color: #ffffff;
  color: #00bcd4;
  border: 2rpx solid #00bcd4;
  border-radius: 8rpx;
  font-size: 36rpx;
  font-weight: bold;
}

.qty-input {
  width: 100rpx;
  height: 70rpx;
  text-align: center;
  font-size: 32rpx;
  border: none;
  background-color: transparent;
}

/* 总价 */
.total-section {
  background-color: #ffffff;
  padding: 30rpx;
  margin: 20rpx;
  border-top: 2rpx solid #f0f0f0;
}

.total-info {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20rpx;
}

.total-label {
  font-size: 28rpx;
  color: #666666;
  margin-right: 15rpx;
}

.total-price {
  font-size: 48rpx;
  color: #ff5722;
  font-weight: bold;
}

/* 底部按钮 */
.bottom-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background-color: #ffffff;
  padding: 20rpx 30rpx;
  box-shadow: 0 -2rpx 10rpx rgba(0, 0, 0, 0.1);
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

.btn-primary {
  background-color: #00bcd4;
  color: #ffffff;
}

.btn-secondary {
  background-color: transparent;
  color: #00bcd4;
  border: 2rpx solid #00bcd4;
}
</style>
