<template>
  <view class="order-container">
    <!-- 订单信息 -->
    <view class="order-section">
      <view class="section-title">
        <text>订单信息</text>
      </view>
      <view class="order-card">
        <view class="order-row">
          <text class="label">景点</text>
          <text class="value">{{ orderData.attractionName }}</text>
        </view>
        <view class="order-row">
          <text class="label">门票类型</text>
          <text class="value">{{ orderData.ticketName }}</text>
        </view>
        <view class="order-row">
          <text class="label">单价</text>
          <text class="value price">¥{{ orderData.ticketPrice }}</text>
        </view>
        <view class="order-row">
          <text class="label">数量</text>
          <text class="value">x{{ orderData.quantity }}</text>
        </view>
        <view class="order-row total">
          <text class="label">总价</text>
          <text class="value total-price">¥{{ orderData.totalPrice }}</text>
        </view>
      </view>
    </view>

    <!-- 联系人信息 -->
    <view class="contact-section">
      <view class="section-title">
        <text>联系人信息</text>
      </view>
      <view class="contact-card">
        <view class="form-item">
          <text class="form-label">姓名</text>
          <input
            class="form-input"
            type="text"
            v-model="visitorName"
            placeholder="请输入游客姓名"
            maxlength="20"
          />
        </view>
        <view class="form-item">
          <text class="form-label">手机号</text>
          <input
            class="form-input"
            type="number"
            v-model="visitorPhone"
            placeholder="请输入手机号"
            maxlength="11"
          />
        </view>
        <view class="form-item">
          <text class="form-label">游玩日期</text>
          <picker mode="date" :value="visitDate" @change="onDateChange">
            <view class="picker-view">
              <text>{{ visitDate || "请选择日期" }}</text>
            </view>
          </picker>
        </view>
      </view>
    </view>

    <!-- 底部按钮 -->
    <view class="bottom-bar">
      <button class="btn btn-secondary" @tap="onBack">返回</button>
      <button class="btn btn-primary" @tap="onSubmit">提交订单</button>
    </view>
  </view>
</template>

<script>
import { createOrder } from "@/api/index.js";

export default {
  data() {
    return {
      orderData: {},
      visitorName: "",
      visitorPhone: "",
      visitDate: "",
      loading: false,
    };
  },
  onLoad() {
    // 获取存储的订单数据
    const orderData = uni.getStorageSync("orderData");
    if (orderData) {
      this.orderData = orderData;
    }

    // 获取用户信息
    const userInfo = uni.getStorageSync("userInfo");
    if (userInfo && userInfo.phone) {
      this.visitorPhone = userInfo.phone;
    }

    // 设置默认日期为明天
    const tomorrow = new Date();
    tomorrow.setDate(tomorrow.getDate() + 1);
    this.visitDate = this.formatDate(tomorrow);
  },
  methods: {
    /**
     * 格式化日期
     */
    formatDate(date) {
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, "0");
      const day = String(date.getDate()).padStart(2, "0");
      return `${year}-${month}-${day}`;
    },

    /**
     * 日期选择
     */
    onDateChange(e) {
      this.visitDate = e.detail.value;
    },

    /**
     * 返回
     */
    onBack() {
      uni.navigateBack();
    },

    /**
     * 提交订单
     */
    async onSubmit() {
      // 验证表单
      if (!this.visitorName) {
        uni.showToast({
          title: "请输入游客姓名",
          icon: "none",
        });
        return;
      }

      if (!this.visitorPhone) {
        uni.showToast({
          title: "请输入手机号",
          icon: "none",
        });
        return;
      }

      if (!this.visitDate) {
        uni.showToast({
          title: "请选择游玩日期",
          icon: "none",
        });
        return;
      }

      // 验证手机号格式
      if (!/^1[3-9]\d{9}$/.test(this.visitorPhone)) {
        uni.showToast({
          title: "手机号格式不正确",
          icon: "none",
        });
        return;
      }

      // 验证所有必要参数
      if (!this.visitorName || !this.visitorPhone || !this.visitDate) {
        uni.showToast({
          title: "请填写完整联系人信息",
          icon: "none",
        });
        return;
      }

      // 验证订单数据
      if (
        !this.orderData ||
        !this.orderData.attractionId ||
        !this.orderData.ticketId ||
        !this.orderData.quantity
      ) {
        uni.showToast({
          title: "订单信息不完整",
          icon: "none",
        });
        return;
      }

      // 获取用户ID
      const userInfo = uni.getStorageSync("userInfo");
      if (!userInfo || !userInfo.id) {
        uni.showToast({
          title: "请先登录",
          icon: "none",
        });
        return;
      }

      this.loading = true;

      try {
        const orderParams = {
          userId: userInfo.id,
          attractionId: this.orderData.attractionId,
          ticketTypeId: this.orderData.ticketId,
          quantity: this.orderData.quantity,
          visitDate: this.visitDate,
          visitorName: this.visitorName,
          visitorPhone: this.visitorPhone,
        };

        // 打印参数以便调试
        console.log("提交订单参数:", orderParams);

        const order = await createOrder(orderParams);

        uni.showToast({
          title: "订单提交成功",
          icon: "success",
          duration: 1500,
        });

        setTimeout(() => {
          // 跳转到订单详情页
          uni.navigateTo({
            url: `/pages/profile/orderDetail?id=${order.id}`,
          });

          // 清空临时订单数据
          uni.removeStorageSync("orderData");
        }, 1500);
      } catch (error) {
        console.error("提交订单失败:", error);
        uni.showToast({
          title: "提交失败，请重试",
          icon: "none",
        });
      } finally {
        this.loading = false;
      }
    },
  },
};
</script>

<style scoped>
.order-container {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding-bottom: 120rpx;
}

/* 订单信息 */
.order-section {
  background-color: #ffffff;
  padding: 30rpx;
  margin: 20rpx;
  border-radius: 16rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333333;
  margin-bottom: 20rpx;
  padding-bottom: 15rpx;
  border-bottom: 2rpx solid #00bcd4;
}

.order-card {
  padding: 0 20rpx;
}

.order-row {
  display: flex;
  justify-content: space-between;
  padding: 15rpx 0;
  border-bottom: 1rpx solid #f5f5f5;
}

.order-row:last-child {
  border-bottom: none;
}

.order-row.total {
  padding-top: 20rpx;
  margin-top: 10rpx;
  border-top: 2rpx solid #00bcd4;
  border-bottom: none;
}

.label {
  font-size: 28rpx;
  color: #666666;
}

.value {
  font-size: 28rpx;
  color: #333333;
  font-weight: 500;
}

.price {
  color: #ff5722;
  font-weight: bold;
}

.total-price {
  font-size: 36rpx;
  color: #ff5722;
  font-weight: bold;
}

/* 联系人信息 */
.contact-section {
  background-color: #ffffff;
  padding: 30rpx;
  margin: 20rpx;
  border-radius: 16rpx;
}

.contact-card {
  padding: 0 20rpx;
}

.form-item {
  margin-bottom: 30rpx;
}

.form-label {
  font-size: 28rpx;
  color: #666666;
  margin-bottom: 15rpx;
  display: block;
}

.form-input {
  width: 100%;
  padding: 24rpx 30rpx;
  background-color: #f5f5f5;
  border-radius: 12rpx;
  font-size: 28rpx;
  border: none;
  outline: none;
}

.form-input:focus {
  background-color: #ffffff;
}

.picker-view {
  padding: 24rpx 30rpx;
  background-color: #f5f5f5;
  border-radius: 12rpx;
  border: 1rpx solid #e0e0e0;
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
