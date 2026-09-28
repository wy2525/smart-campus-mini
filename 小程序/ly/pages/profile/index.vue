<template>
  <view class="profile-container">
    <!-- 顶部简洁头部 -->
    <view class="header-bar">
      <text class="header-title">我的</text>
      <view class="header-icon" @tap="onSettingsTap">
        <text>⚙️</text>
      </view>
    </view>

    <!-- 用户信息区域 -->
    <view class="user-section" @tap="onUserInfoTap">
      <view class="avatar-container">
        <image 
          class="avatar" 
          :src="userInfo.avatar || '/static/avatar-default.png'" 
          mode="aspectFill"
        ></image>
        <view class="online-dot" v-if="isLoggedIn"></view>
      </view>
      
      <view class="user-details" v-if="isLoggedIn">
        <text class="nickname">{{ userInfo.nickname || '旅行者' }}</text>
        <text class="user-phone">{{ formatPhone(userInfo.phone) }}</text>
        <view class="user-badges">
          <view class="badge level-badge" v-if="userLevel">
            <text>{{ userLevel }}</text>
          </view>
          <view class="badge vip-badge" v-if="isVip">
            <text>VIP</text>
          </view>
        </view>
      </view>
      
      <view class="guest-info" v-else>
        <text class="guest-text">点击登录</text>
        <text class="guest-sub">登录后享受更多服务</text>
      </view>
      
      <view class="arrow-icon" v-if="isLoggedIn">
        <text>›</text>
      </view>
    </view>

    <!-- 统计卡片 -->
    <view class="stats-card" v-if="isLoggedIn">
      <view class="stat-item" @tap="onOrdersTap">
        <text class="stat-num">{{ userStats.orders }}</text>
        <text class="stat-label">订单</text>
      </view>
      <view class="stat-divider"></view>
      <view class="stat-item" @tap="onFavoritesTap">
        <text class="stat-num">{{ userStats.favorites }}</text>
        <text class="stat-label">收藏</text>
      </view>
      <view class="stat-divider"></view>
      <view class="stat-item">
        <text class="stat-num">{{ userStats.points }}</text>
        <text class="stat-label">积分</text>
      </view>
    </view>

    <!-- 菜单列表 -->
    <view class="menu-list">
      <view class="menu-item" @tap="onOrdersTap">
        <view class="menu-left">
          <view class="menu-icon orders-icon">
            <text>📋</text>
          </view>
          <text class="menu-text">我的订单</text>
        </view>
        <view class="menu-right">
          <text class="menu-badge" v-if="orderCount > 0">{{ orderCount }}</text>
          <text class="arrow">›</text>
        </view>
      </view>

      <view class="menu-item" @tap="onFavoritesTap">
        <view class="menu-left">
          <view class="menu-icon favorite-icon">
            <text>❤️</text>
          </view>
          <text class="menu-text">我的收藏</text>
        </view>
        <view class="menu-right">
          <text class="arrow">›</text>
        </view>
      </view>

      <view class="menu-item" @tap="onMyGuidesTap">
        <view class="menu-left">
          <view class="menu-icon guide-icon">
            <text>✍️</text>
          </view>
          <text class="menu-text">我的攻略</text>
        </view>
        <view class="menu-right">
          <text class="arrow">›</text>
        </view>
      </view>

      <view class="menu-item" @tap="onHistoryTap">
        <view class="menu-left">
          <view class="menu-icon history-icon">
            <text>🕐</text>
          </view>
          <text class="menu-text">浏览历史</text>
        </view>
        <view class="menu-right">
          <text class="arrow">›</text>
        </view>
      </view>

      <view class="menu-item" @tap="onCouponsTap">
        <view class="menu-left">
          <view class="menu-icon coupon-icon">
            <text>🎫</text>
          </view>
          <text class="menu-text">优惠券</text>
        </view>
        <view class="menu-right">
          <text class="arrow">›</text>
        </view>
      </view>

      <view class="menu-item" @tap="onHelpTap">
        <view class="menu-left">
          <view class="menu-icon help-icon">
            <text>💬</text>
          </view>
          <text class="menu-text">帮助与反馈</text>
        </view>
        <view class="menu-right">
          <text class="arrow">›</text>
        </view>
      </view>

      <view class="menu-item" @tap="onAboutTap">
        <view class="menu-left">
          <view class="menu-icon about-icon">
            <text>ℹ️</text>
          </view>
          <text class="menu-text">关于我们</text>
        </view>
        <view class="menu-right">
          <text class="arrow">›</text>
        </view>
      </view>
    </view>

    <!-- 退出登录按钮 -->
    <view class="logout-btn" v-if="isLoggedIn" @tap="onLogoutTap">
      <text>退出登录</text>
    </view>

    <!-- 底部安全距离 -->
    <view class="safe-bottom"></view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      userInfo: {},
      isLoggedIn: false,
      isVip: false,
      userLevel: '',
      userStats: {
        orders: 0,
        favorites: 0,
        points: 0
      },
      orderCount: 0
    }
  },
  onLoad() {
    this.checkLoginStatus()
  },
  onShow() {
    this.checkLoginStatus()
  },
  methods: {
    checkLoginStatus() {
      const token = uni.getStorageSync('token')
      const userInfo = uni.getStorageSync('userInfo')
      this.isLoggedIn = !!token && !!userInfo
      this.userInfo = userInfo || {}
      
      if (this.isLoggedIn) {
        // 模拟会员状态
        this.isVip = Math.random() > 0.7
        this.userLevel = this.isVip ? 'VIP' : 'Lv.' + Math.floor(Math.random() * 5 + 1)
        this.userStats = {
          orders: Math.floor(Math.random() * 10),
          favorites: Math.floor(Math.random() * 20),
          points: Math.floor(Math.random() * 2000)
        }
        this.orderCount = this.userStats.orders
      }
    },

    formatPhone(phone) {
      if (!phone) return ''
      return phone.replace(/(\d{3})\d{4}(\d{4})/, '$1****$2')
    },

    onUserInfoTap() {
      if (!this.isLoggedIn) {
        this.onLoginTap()
      }
    },

    onOrdersTap() {
      if (!this.isLoggedIn) {
        uni.showToast({ title: '请先登录', icon: 'none' })
        return
      }
      uni.navigateTo({ url: '/pages/profile/orders' })
    },

    onFavoritesTap() {
      if (!this.isLoggedIn) {
        uni.showToast({ title: '请先登录', icon: 'none' })
        return
      }
      uni.navigateTo({ url: '/pages/profile/favorites' })
    },

    onMyGuidesTap() {
      if (!this.isLoggedIn) {
        uni.showToast({ title: '请先登录', icon: 'none' })
        return
      }
      uni.navigateTo({ url: '/pages/community/myGuides' })
    },

    onHistoryTap() {
      uni.showToast({ title: '浏览历史功能开发中', icon: 'none' })
    },

    onCouponsTap() {
      uni.showToast({ title: '优惠券功能开发中', icon: 'none' })
    },

    onSettingsTap() {
      if (!this.isLoggedIn) {
        uni.showToast({ title: '请先登录', icon: 'none' })
        return
      }
      uni.navigateTo({ url: '/pages/profile/settings' })
    },

    onHelpTap() {
      uni.showToast({ title: '帮助与反馈功能开发中', icon: 'none' })
    },

    onAboutTap() {
      uni.showModal({
        title: '关于我们',
        content: 'Travel Plus - 探索世界，记录美好\n版本 1.0.0',
        showCancel: false
      })
    },

    onLogoutTap() {
      uni.showModal({
        title: '提示',
        content: '确定要退出登录吗？',
        success: (res) => {
          if (res.confirm) {
            uni.removeStorageSync('token')
            uni.removeStorageSync('userInfo')
            uni.removeStorageSync('userId')
            
            this.isLoggedIn = false
            this.userInfo = {}
            
            uni.showToast({
              title: '已退出登录',
              icon: 'success',
              duration: 1500
            })
          }
        }
      })
    },

    onLoginTap() {
      uni.navigateTo({ url: '/pages/profile/login' })
    }
  }
}
</script>

<style scoped>
.profile-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 40rpx;
}

/* 顶部导航栏 */
.header-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 60rpx 30rpx 20rpx;
  background: #ffffff;
}

.header-title {
  font-size: 36rpx;
  font-weight: 600;
  color: #333;
}

.header-icon {
  width: 60rpx;
  height: 60rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 32rpx;
}

/* 用户信息区域 */
.user-section {
  display: flex;
  align-items: center;
  padding: 30rpx;
  margin: 20rpx 30rpx;
  background: #ffffff;
  border-radius: 20rpx;
  box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.05);
}

.avatar-container {
  position: relative;
  margin-right: 24rpx;
}

.avatar {
  width: 100rpx;
  height: 100rpx;
  border-radius: 50%;
  background: #f0f0f0;
}

.online-dot {
  position: absolute;
  bottom: 4rpx;
  right: 4rpx;
  width: 20rpx;
  height: 20rpx;
  background: #07c160;
  border-radius: 50%;
  border: 4rpx solid #ffffff;
}

.user-details {
  flex: 1;
}

.nickname {
  font-size: 32rpx;
  font-weight: 600;
  color: #333;
  display: block;
  margin-bottom: 8rpx;
}

.user-phone {
  font-size: 26rpx;
  color: #999;
  display: block;
  margin-bottom: 12rpx;
}

.user-badges {
  display: flex;
  gap: 12rpx;
}

.badge {
  padding: 6rpx 16rpx;
  border-radius: 20rpx;
  font-size: 20rpx;
  font-weight: 500;
}

.level-badge {
  background: #e8f4fd;
  color: #667eea;
}

.vip-badge {
  background: linear-gradient(135deg, #ffd700, #ff9500);
  color: #fff;
}

.guest-info {
  flex: 1;
}

.guest-text {
  font-size: 32rpx;
  font-weight: 600;
  color: #333;
  display: block;
  margin-bottom: 8rpx;
}

.guest-sub {
  font-size: 24rpx;
  color: #999;
}

.arrow-icon {
  font-size: 36rpx;
  color: #ccc;
}

/* 统计卡片 */
.stats-card {
  display: flex;
  align-items: center;
  justify-content: space-around;
  margin: 0 30rpx;
  padding: 30rpx 0;
  background: #ffffff;
  border-radius: 20rpx;
  box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.05);
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  flex: 1;
}

.stat-num {
  font-size: 40rpx;
  font-weight: 600;
  color: #333;
  margin-bottom: 8rpx;
}

.stat-label {
  font-size: 24rpx;
  color: #999;
}

.stat-divider {
  width: 1rpx;
  height: 60rpx;
  background: #eee;
}

/* 菜单列表 */
.menu-list {
  margin: 20rpx 30rpx;
  background: #ffffff;
  border-radius: 20rpx;
  overflow: hidden;
  box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.05);
}

.menu-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 28rpx 30rpx;
  border-bottom: 1rpx solid #f5f5f5;
}

.menu-item:last-child {
  border-bottom: none;
}

.menu-item:active {
  background: #fafafa;
}

.menu-left {
  display: flex;
  align-items: center;
}

.menu-icon {
  width: 56rpx;
  height: 56rpx;
  border-radius: 14rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20rpx;
  font-size: 28rpx;
}

.orders-icon { background: #e3f2fd; }
.favorite-icon { background: #fce4ec; }
.guide-icon { background: #fff3e0; }
.history-icon { background: #e8f5e9; }
.coupon-icon { background: #f3e5f5; }
.help-icon { background: #e0f2f1; }
.about-icon { background: #fff8e1; }

.menu-text {
  font-size: 30rpx;
  color: #333;
}

.menu-right {
  display: flex;
  align-items: center;
}

.menu-badge {
  padding: 4rpx 16rpx;
  background: #ff5722;
  color: #fff;
  font-size: 22rpx;
  border-radius: 20rpx;
  margin-right: 12rpx;
}

.arrow {
  font-size: 32rpx;
  color: #ccc;
}

/* 退出登录按钮 */
.logout-btn {
  margin: 40rpx 30rpx 0;
  padding: 28rpx;
  background: #ffffff;
  border-radius: 20rpx;
  text-align: center;
  box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.05);
}

.logout-btn text {
  font-size: 30rpx;
  color: #ff5722;
}

/* 底部安全距离 */
.safe-bottom {
  height: constant(safe-area-inset-bottom);
  height: env(safe-area-inset-bottom);
}
</style>
