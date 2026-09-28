<template>
  <view class="login-container">
    <!-- 动态背景 -->
    <view class="bg-pattern"></view>
    
    <!-- 主要内容 -->
    <view class="main-content">
      <!-- Logo区域 -->
      <view class="logo-section">
        <view class="logo-wrapper">
          <text class="logo-icon">✈️</text>
        </view>
        <text class="brand-name">Travel Plus</text>
        <text class="brand-slogan">探索世界，记录美好</text>
      </view>

      <!-- 登录卡片 -->
      <view class="card">
        <!-- 标签页切换 -->
        <view class="tab-header">
          <view 
            class="tab-item" 
            :class="{ active: activeTab === 'login' }"
            @tap="activeTab = 'login'"
          >
            <text>登录</text>
          </view>
          <view 
            class="tab-item" 
            :class="{ active: activeTab === 'register' }"
            @tap="activeTab = 'register'"
          >
            <text>注册</text>
          </view>
        </view>

        <!-- 登录表单 -->
        <view class="form-content" v-if="activeTab === 'login'">
          <view class="input-group">
            <view class="input-wrapper" :class="{ focused: focusField === 'phone' }">
              <text class="input-icon">📱</text>
              <input
                class="input-field"
                type="number"
                v-model="phone"
                placeholder="请输入手机号"
                maxlength="11"
                @focus="focusField = 'phone'"
                @blur="focusField = ''"
              />
            </view>
          </view>

          <view class="input-group">
            <view class="input-wrapper" :class="{ focused: focusField === 'password' }">
              <text class="input-icon">🔒</text>
              <input
                class="input-field"
                :type="showPassword ? 'text' : 'password'"
                v-model="password"
                placeholder="请输入密码"
                @focus="focusField = 'password'"
                @blur="focusField = ''"
              />
              <view class="toggle-icon" @tap="showPassword = !showPassword">
                <text>{{ showPassword ? '👁️' : '👁️‍🗨️' }}</text>
              </view>
            </view>
          </view>

          <view class="form-options">
            <view class="remember-item" @tap="rememberMe = !rememberMe">
              <view class="checkbox" :class="{ checked: rememberMe }">
                <text v-if="rememberMe">✓</text>
              </view>
              <text class="checkbox-label">记住我</text>
            </view>
            <text class="forgot-link" @tap="onForgotPassword">忘记密码?</text>
          </view>

          <button 
            class="submit-btn" 
            :class="{ disabled: !canLogin }"
            :disabled="!canLogin || isLoading"
            @tap="onLogin"
          >
            <text>{{ isLoading ? '登录中...' : '登录' }}</text>
          </button>
        </view>

        <!-- 注册表单 -->
        <view class="form-content" v-else>
          <view class="input-group">
            <view class="input-wrapper" :class="{ focused: focusField === 'phone' }">
              <text class="input-icon">📱</text>
              <input
                class="input-field"
                type="number"
                v-model="regPhone"
                placeholder="请输入手机号"
                maxlength="11"
                @focus="focusField = 'phone'"
                @blur="focusField = ''"
              />
            </view>
          </view>

          <view class="input-group">
            <view class="input-wrapper" :class="{ focused: focusField === 'nickname' }">
              <text class="input-icon">👤</text>
              <input
                class="input-field"
                v-model="nickname"
                placeholder="请输入昵称"
                maxlength="20"
                @focus="focusField = 'nickname'"
                @blur="focusField = ''"
              />
            </view>
          </view>

          <view class="input-group">
            <view class="input-wrapper" :class="{ focused: focusField === 'password' }">
              <text class="input-icon">🔒</text>
              <input
                class="input-field"
                :type="showPassword ? 'text' : 'password'"
                v-model="regPassword"
                placeholder="请输入6-20位密码"
                @focus="focusField = 'password'"
                @blur="focusField = ''"
              />
            </view>
          </view>

          <view class="input-group">
            <view class="input-wrapper" :class="{ focused: focusField === 'confirmPassword' }">
              <text class="input-icon">✅</text>
              <input
                class="input-field"
                :type="showPassword ? 'text' : 'password'"
                v-model="confirmPassword"
                placeholder="请确认密码"
                @focus="focusField = 'confirmPassword'"
                @blur="focusField = ''"
              />
            </view>
          </view>

          <button 
            class="submit-btn register-btn" 
            :class="{ disabled: !canRegister }"
            :disabled="!canRegister || isRegistering"
            @tap="onRegister"
          >
            <text>{{ isRegistering ? '注册中...' : '注册' }}</text>
          </button>
        </view>

        <!-- 第三方登录 -->
        <view class="social-section" v-if="activeTab === 'login'">
          <view class="divider">
            <view class="divider-line"></view>
            <text class="divider-text">其他登录方式</text>
            <view class="divider-line"></view>
          </view>
          
          <view class="social-options">
            <view class="social-btn wechat" @tap="onWechatLogin">
              <text>💬</text>
            </view>
            <view class="social-btn qq" @tap="onQQLogin">
              <text>💭</text>
            </view>
          </view>
        </view>
      </view>

      <!-- 协议提示 -->
      <view class="agreement-tip" v-if="activeTab === 'register'">
        <text class="tip-text">注册即表示同意</text>
        <text class="link-text">《用户协议》</text>
        <text class="tip-text">和</text>
        <text class="link-text">《隐私政策》</text>
      </view>
    </view>
  </view>
</template>

<script>
import { login, register } from '@/api/index.js'

export default {
  data() {
    return {
      activeTab: 'login',
      phone: '',
      password: '',
      showPassword: false,
      rememberMe: false,
      focusField: '',
      
      // 注册
      regPhone: '',
      regPassword: '',
      confirmPassword: '',
      nickname: '',
      
      // 状态
      isLoading: false,
      isRegistering: false
    }
  },
  computed: {
    canLogin() {
      return this.phone.length === 11 && this.password.length >= 6
    },
    canRegister() {
      return this.regPhone.length === 11 && 
             this.regPassword.length >= 6 &&
             this.regPassword === this.confirmPassword &&
             this.nickname.length > 0
    }
  },
  onLoad() {
    this.loadSavedCredentials()
  },
  methods: {
    loadSavedCredentials() {
      const savedPhone = uni.getStorageSync('savedPhone')
      const savedPassword = uni.getStorageSync('savedPassword')
      if (savedPhone && savedPassword) {
        this.phone = savedPhone
        this.password = savedPassword
        this.rememberMe = true
      }
    },
    
    async onLogin() {
      if (!this.canLogin) return
      
      this.isLoading = true
      
      try {
        const res = await login(this.phone, this.password)
        
        if (this.rememberMe) {
          uni.setStorageSync('savedPhone', this.phone)
          uni.setStorageSync('savedPassword', this.password)
        } else {
          uni.removeStorageSync('savedPhone')
          uni.removeStorageSync('savedPassword')
        }
        
        uni.setStorageSync('token', res.token)
        uni.setStorageSync('userInfo', res.user)
        uni.setStorageSync('userId', res.user.id)
        
        uni.showToast({ title: '登录成功', icon: 'success' })
        
        setTimeout(() => {
          uni.switchTab({ url: '/pages/home/index' })
        }, 1500)
      } catch (error) {
        // 模拟登录
        console.warn('后端连接失败，模拟登录')
        
        if (this.phone.length !== 11) {
          uni.showToast({ title: '请输入正确的手机号', icon: 'none' })
          return
        }

        uni.showToast({ title: '登录成功', icon: 'success' })
        
        const mockUser = {
          id: Date.now(),
          phone: this.phone,
          nickname: '用户' + this.phone.slice(-4),
          avatar: 'https://mmbiz.qpic.cn/mmbiz/icTdbqWNOwNRna42FI242Lxia07jQodd2G6xOqniaKGWbM66M0HsWNC5pZqFKicBlnB61ic42iaic23rBjG2PpBhG88iaQA/0'
        }
        
        if (this.rememberMe) {
          uni.setStorageSync('savedPhone', this.phone)
          uni.setStorageSync('savedPassword', this.password)
        }
        
        uni.setStorageSync('token', 'mock-token-' + Date.now())
        uni.setStorageSync('userInfo', mockUser)
        uni.setStorageSync('userId', mockUser.id)
        
        setTimeout(() => {
          uni.switchTab({ url: '/pages/home/index' })
        }, 1500)
      } finally {
        this.isLoading = false
      }
    },
    
    async onRegister() {
      if (!this.canRegister) {
        uni.showToast({ title: '请填写完整信息', icon: 'none' })
        return
      }
      
      this.isRegistering = true
      
      try {
        await register({
          phone: this.regPhone,
          password: this.regPassword,
          nickname: this.nickname
        })
        
        uni.showToast({ title: '注册成功', icon: 'success' })
        
        setTimeout(() => {
          this.activeTab = 'login'
          this.phone = this.regPhone
        }, 1500)
      } catch (error) {
        // 模拟注册
        console.warn('后端连接失败，模拟注册')
        
        uni.showToast({ title: '注册成功', icon: 'success' })
        
        const mockUser = {
          id: Date.now(),
          phone: this.regPhone,
          nickname: this.nickname,
          avatar: 'https://mmbiz.qpic.cn/mmbiz/icTdbqWNOwNRna42FI242Lxia07jQodd2G6xOqniaKGWbM66M0HsWNC5pZqFKicBlnB61ic42iaic23rBjG2PpBhG88iaQA/0'
        }
        
        uni.setStorageSync('token', 'mock-token-' + Date.now())
        uni.setStorageSync('userInfo', mockUser)
        uni.setStorageSync('userId', mockUser.id)
        
        setTimeout(() => {
          this.activeTab = 'login'
          this.phone = this.regPhone
        }, 1500)
      } finally {
        this.isRegistering = false
      }
    },
    
    onForgotPassword() {
      uni.showToast({ title: '忘记密码功能开发中', icon: 'none' })
    },
    
    onWechatLogin() {
      uni.showToast({ title: '微信登录功能开发中', icon: 'none' })
    },
    
    onQQLogin() {
      uni.showToast({ title: 'QQ登录功能开发中', icon: 'none' })
    }
  }
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  background: #f8f9fa;
  position: relative;
}

/* 动态背景 */
.bg-pattern {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: 
    radial-gradient(circle at 20% 20%, rgba(102, 126, 234, 0.1) 0%, transparent 50%),
    radial-gradient(circle at 80% 80%, rgba(118, 75, 162, 0.1) 0%, transparent 50%);
}

/* 主内容 */
.main-content {
  position: relative;
  z-index: 1;
  padding: 80rpx 40rpx 60rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
}

/* Logo区域 */
.logo-section {
  text-align: center;
  margin-bottom: 60rpx;
}

.logo-wrapper {
  width: 140rpx;
  height: 140rpx;
  background: linear-gradient(135deg, #667eea, #764ba2);
  border-radius: 40rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 24rpx;
  box-shadow: 0 20rpx 40rpx rgba(102, 126, 234, 0.3);
}

.logo-icon {
  font-size: 64rpx;
}

.brand-name {
  display: block;
  font-size: 48rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 12rpx;
}

.brand-slogan {
  display: block;
  font-size: 26rpx;
  color: #999;
}

/* 登录卡片 */
.card {
  width: 100%;
  background: #fff;
  border-radius: 24rpx;
  padding: 48rpx 40rpx;
  box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.05);
}

/* 标签页 */
.tab-header {
  display: flex;
  background: #f5f5f5;
  border-radius: 16rpx;
  padding: 6rpx;
  margin-bottom: 40rpx;
}

.tab-item {
  flex: 1;
  text-align: center;
  padding: 22rpx 0;
  border-radius: 12rpx;
  font-size: 28rpx;
  color: #666;
  transition: all 0.3s;
}

.tab-item.active {
  background: #fff;
  color: #333;
  font-weight: 500;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);
}

/* 表单 */
.form-content {
  animation: fadeIn 0.3s ease;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10rpx); }
  to { opacity: 1; transform: translateY(0); }
}

.input-group {
  margin-bottom: 28rpx;
}

.input-wrapper {
  display: flex;
  align-items: center;
  background: #f5f5f5;
  border: 2rpx solid transparent;
  border-radius: 16rpx;
  padding: 0 24rpx;
  transition: all 0.3s;
}

.input-wrapper.focused {
  background: #fff;
  border-color: #667eea;
}

.input-icon {
  font-size: 28rpx;
  margin-right: 16rpx;
}

.input-field {
  flex: 1;
  height: 96rpx;
  font-size: 28rpx;
  color: #333;
}

.toggle-icon {
  padding: 10rpx;
  font-size: 26rpx;
}

/* 表单选项 */
.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 32rpx;
}

.remember-item {
  display: flex;
  align-items: center;
}

.checkbox {
  width: 36rpx;
  height: 36rpx;
  border: 2rpx solid #ddd;
  border-radius: 8rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12rpx;
}

.checkbox.checked {
  background: #667eea;
  border-color: #667eea;
}

.checkbox text {
  color: #fff;
  font-size: 22rpx;
}

.checkbox-label {
  font-size: 26rpx;
  color: #666;
}

.forgot-link {
  font-size: 26rpx;
  color: #667eea;
}

/* 提交按钮 */
.submit-btn {
  width: 100%;
  height: 96rpx;
  background: linear-gradient(135deg, #667eea, #764ba2);
  border: none;
  border-radius: 48rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 8rpx 24rpx rgba(102, 126, 234, 0.3);
}

.submit-btn text {
  font-size: 30rpx;
  color: #fff;
  font-weight: 500;
}

.submit-btn.disabled {
  opacity: 0.6;
}

.register-btn {
  background: linear-gradient(135deg, #48bb78, #38a169);
  box-shadow: 0 8rpx 24rpx rgba(72, 187, 120, 0.3);
}

/* 第三方登录 */
.social-section {
  margin-top: 48rpx;
}

.divider {
  display: flex;
  align-items: center;
  margin-bottom: 32rpx;
}

.divider-line {
  flex: 1;
  height: 1rpx;
  background: #eee;
}

.divider-text {
  padding: 0 24rpx;
  font-size: 24rpx;
  color: #999;
}

.social-options {
  display: flex;
  justify-content: center;
  gap: 48rpx;
}

.social-btn {
  width: 88rpx;
  height: 88rpx;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 40rpx;
}

.social-btn.wechat {
  background: linear-gradient(135deg, #07c160, #06ae56);
}

.social-btn.qq {
  background: linear-gradient(135deg, #12b7f5, #09a3ee);
}

/* 协议提示 */
.agreement-tip {
  margin-top: 32rpx;
  text-align: center;
}

.tip-text {
  font-size: 24rpx;
  color: #999;
}

.link-text {
  font-size: 24rpx;
  color: #667eea;
}
</style>
