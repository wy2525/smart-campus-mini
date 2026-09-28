<template>
  <view class="settings-container">
    <!-- 用户信息 -->
    <view class="section">
      <view class="section-title">
        <text>用户信息</text>
      </view>
      <view class="form-card">
        <view class="form-item">
          <text class="label">昵称</text>
          <input
            class="input"
            type="text"
            v-model="form.nickname"
            placeholder="请输入昵称"
            maxlength="20"
          />
        </view>
        <view class="form-item">
          <text class="label">手机号</text>
          <input
            class="input"
            type="number"
            v-model="form.phone"
            placeholder="请输入手机号"
            maxlength="11"
            disabled
          />
        </view>
        <view class="form-item">
          <text class="label">性别</text>
          <picker mode="selector" :range="genderOptions" @change="onGenderChange">
            <view class="picker-view">
              <text>{{ getGenderText(form.gender) }}</text>
            </view>
          </picker>
        </view>
      </view>
    </view>

    <!-- 头像设置 -->
    <view class="section">
      <view class="section-title">
        <text>头像设置</text>
      </view>
      <view class="avatar-card">
        <image
          class="avatar-preview"
          :src="form.avatar || '/static/avatar-default.png'"
          mode="aspectFill"
          @tap="onAvatarTap"
        ></image>
        <text class="avatar-tip">点击更换头像</text>
      </view>
    </view>

    <!-- 关于我们 -->
    <view class="section">
      <view class="section-title">
        <text>关于我们</text>
      </view>
      <view class="about-card">
        <view class="about-item">
          <text class="about-label">应用名称</text>
          <text class="about-value">旅游小程序</text>
        </view>
        <view class="about-item">
          <text class="about-label">版本号</text>
          <text class="about-value">v1.0.0</text>
        </view>
      </view>
    </view>

    <!-- 底部按钮 -->
    <view class="bottom-section">
      <button class="btn btn-save" @tap="onSaveTap">保存修改</button>
    </view>
  </view>
</template>

<script>
import { getUserInfo, updateUserInfo } from '@/api/index.js'

export default {
  data() {
    return {
      form: {
        nickname: '',
        phone: '',
        gender: 1,
        avatar: ''
      },
      genderOptions: ['男', '女', '未知']
    }
  },
  onLoad() {
    this.loadUserInfo()
  },
  methods: {
    /**
     * 加载用户信息
     */
    async loadUserInfo() {
      try {
        const userInfo = uni.getStorageSync('userInfo')
        if (userInfo) {
          this.form.nickname = userInfo.nickname || ''
          this.form.phone = userInfo.phone || ''
          this.form.gender = userInfo.gender || 1
          this.form.avatar = userInfo.avatar || ''
        }
      } catch (error) {
        console.error('加载用户信息失败:', error)
      }
    },

    /**
     * 获取性别文本
     */
    getGenderText(gender) {
      const textMap = {
        1: '未知',
        2: '男',
        3: '女'
      }
      return textMap[gender] || '未知'
    },

    /**
     * 性别选择
     */
    onGenderChange(e) {
      this.form.gender = e.detail.value + 1
    },

    /**
     * 点击头像
     */
    onAvatarTap() {
      uni.showToast({
        title: '头像上传功能开发中',
        icon: 'none'
      })
    },

    /**
     * 保存修改
     */
    async onSaveTap() {
      if (!this.form.nickname) {
        uni.showToast({
          title: '请输入昵称',
          icon: 'none'
        })
        return
      }

      uni.showLoading({
        title: '保存中...'
      })

      try {
        const userInfo = uni.getStorageSync('userInfo')
        if (!userInfo || !userInfo.id) {
          uni.hideLoading()
          uni.showToast({
            title: '请先登录',
            icon: 'none'
          })
          return
        }

        const updateData = {
          id: userInfo.id,
          nickname: this.form.nickname,
          gender: this.form.gender
        }

        await updateUserInfo(updateData)

        // 更新本地存储
        userInfo.nickname = this.form.nickname
        userInfo.gender = this.form.gender
        uni.setStorageSync('userInfo', userInfo)

        uni.hideLoading()

        uni.showToast({
          title: '保存成功',
          icon: 'success',
          duration: 1500
        })
      } catch (error) {
        uni.hideLoading()
        console.error('保存失败:', error)
        uni.showToast({
          title: '保存失败，请重试',
          icon: 'none'
        })
      }
    }
  }
}
</script>

<style scoped>
.settings-container {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding-bottom: 120rpx;
}

/* 区块 */
.section {
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

/* 表单 */
.form-card,
.about-card,
.avatar-card {
  padding: 0 20rpx;
}

.form-item {
  margin-bottom: 30rpx;
}

.label {
  font-size: 28rpx;
  color: #666666;
  margin-bottom: 15rpx;
  display: block;
}

.input {
  width: 100%;
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

.picker-view {
  padding: 24rpx 30rpx;
  background-color: #f5f5f5;
  border-radius: 12rpx;
  border: 1rpx solid #e0e0e0;
  text-align: center;
  font-size: 28rpx;
  color: #333333;
}

/* 头像 */
.avatar-preview {
  width: 200rpx;
  height: 200rpx;
  border-radius: 100rpx;
  margin-bottom: 20rpx;
}

.avatar-tip {
  text-align: center;
  font-size: 24rpx;
  color: #00bcd4;
}

/* 关于 */
.about-item {
  display: flex;
  padding: 15rpx 0;
  border-bottom: 1rpx solid #f5f5f5;
}

.about-item:last-child {
  border-bottom: none;
}

.about-label {
  font-size: 26rpx;
  color: #666666;
  width: 200rpx;
  flex-shrink: 0;
}

.about-value {
  flex: 1;
  font-size: 26rpx;
  color: #333333;
}

/* 底部按钮 */
.bottom-section {
  padding: 30rpx;
}

.btn {
  width: 100%;
  padding: 28rpx 0;
  height: 90rpx;
  line-height: 90rpx;
  border-radius: 12rpx;
  font-size: 32rpx;
  border: none;
}

.btn-save {
  background-color: #00bcd4;
  color: #ffffff;
}
</style>
