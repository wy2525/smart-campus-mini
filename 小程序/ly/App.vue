<script>
export default {
  onLaunch: function() {
    console.log('App Launch')
    // 检查登录状态
    this.checkLoginStatus()
  },
  onShow: function() {
    console.log('App Show')
  },
  onHide: function() {
    console.log('App Hide')
  },
  methods: {
    /**
     * 检查登录状态
     */
    checkLoginStatus() {
      const token = uni.getStorageSync('token')
      const userInfo = uni.getStorageSync('userInfo')
      if (token && userInfo) {
        console.log('用户已登录:', userInfo.nickname)
      }
    }
  }
}
</script>

<style>
/* 全局样式 */
page {
  background-color: #f5f5f5;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', sans-serif;
}

/* 通用样式 */
.container {
  min-height: 100vh;
  background-color: #f5f5f5;
}

/* 卡片样式 */
.card {
  background-color: #ffffff;
  border-radius: 16rpx;
  padding: 30rpx;
  margin: 20rpx;
}

/* 按钮样式 */
.btn {
  padding: 24rpx 48rpx;
  border-radius: 40rpx;
  font-size: 30rpx;
  text-align: center;
  color: #ffffff;
  border: none;
  outline: none;
}

.btn-primary {
  background-color: #00bcd4;
}

.btn-secondary {
  background-color: #f5f5f5;
  color: #00bcd4;
  border: 2rpx solid #00bcd4;
}

/* 文本样式 */
.text-primary {
  color: #00bcd4;
}

.text-secondary {
  color: #999999;
}

.text-error {
  color: #ff5722;
}

/* 输入框样式 */
.input {
  padding: 24rpx 30rpx;
  background-color: #f5f5f5;
  border-radius: 12rpx;
  font-size: 28rpx;
  border: none;
  outline: none;
}

.input:focus {
  background-color: #ffffff;
}
</style>
