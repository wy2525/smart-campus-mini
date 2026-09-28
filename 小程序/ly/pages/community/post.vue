<template>
  <view class="post-container">
    <!-- 表单区域 -->
    <view class="form-section">
      <view class="section-title">
        <text>发布新攻略</text>
      </view>

      <!-- 标题 -->
      <view class="form-item">
        <text class="label">标题</text>
        <input
          class="input"
          type="text"
          v-model="form.title"
          placeholder="请输入攻略标题"
          maxlength="100"
        />
      </view>

      <!-- 选择景点 -->
      <view class="form-item">
        <text class="label">关联景点</text>
        <view class="picker-view" @tap="onAttractionTap">
          <text class="picker-text">
            {{ selectedAttraction ? selectedAttraction.name : '请选择景点' }}
          </text>
          <text class="picker-arrow">></text>
        </view>
      </view>

      <!-- 封面图 -->
      <view class="form-item">
        <text class="label">封面图</text>
        <view class="upload-view" @tap="onCoverTap">
          <image
            class="cover-preview"
            :src="form.coverImage || '/static/upload-placeholder.png'"
            mode="aspectFill"
          ></image>
          <text class="upload-tip">点击上传封面</text>
        </view>
      </view>

      <!-- 内容 -->
      <view class="form-item content-item">
        <view class="label-wrapper">
          <text class="label">攻略内容</text>
          <view class="ai-btn" @tap="onAIPanelTap" :class="{ 'ai-disabled': aiLoading }">
            <text class="ai-btn-text">🤖 AI 助写</text>
          </view>
        </view>
        <textarea
          class="textarea"
          v-model="form.content"
          placeholder="分享您的旅游经验..."
          maxlength="500"
          :auto-height="true"
          :disabled="aiLoading"
        ></textarea>
        <text class="char-count">{{ form.content.length }}/500</text>
      </view>
    </view>

    <!-- AI 助写选项面板 -->
    <view class="ai-panel" v-if="showAIPanel" @tap="onAIPanelClose">
      <view class="ai-panel-content" @tap.stop>
        <view class="ai-panel-header">
          <text class="ai-panel-title">AI 助写</text>
          <text class="ai-panel-close" @tap="onAIPanelClose">×</text>
        </view>
        <view class="ai-panel-body">
          <view class="ai-option" @tap="onAIOptionTap('generate')">
            <view class="ai-option-icon">✨</view>
            <view class="ai-option-info">
              <text class="ai-option-title">生成完整攻略</text>
              <text class="ai-option-desc">根据标题和景点信息生成完整攻略</text>
            </view>
          </view>
          <view class="ai-option" @tap="onAIOptionTap('optimize')" v-if="form.content.length > 0">
            <view class="ai-option-icon">✍️</view>
            <view class="ai-option-info">
              <text class="ai-option-title">优化现有内容</text>
              <text class="ai-option-desc">优化已有内容的表达和结构</text>
            </view>
          </view>
          <view class="ai-option" @tap="onAIOptionTap('expand')" v-if="form.content.length > 0">
            <view class="ai-option-icon">📝</view>
            <view class="ai-option-info">
              <text class="ai-option-title">扩展内容</text>
              <text class="ai-option-desc">基于现有内容进行扩展补充</text>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- AI 加载提示 -->
    <view class="ai-loading-overlay" v-if="aiLoading">
      <view class="ai-loading-content">
        <view class="ai-loading-spinner"></view>
        <text class="ai-loading-text">AI 正在生成...</text>
      </view>
    </view>

    <!-- 底部按钮 -->
    <view class="bottom-bar">
      <button class="btn btn-secondary" @tap="onCancelTap" :disabled="aiLoading">取消</button>
      <button class="btn btn-primary" @tap="onSubmitTap" :disabled="aiLoading">
        {{ aiLoading ? 'AI 生成中...' : '发布' }}
      </button>
    </view>
  </view>
</template>

<script>
import { createGuide, generateGuide } from '@/api/index.js'

export default {
  data() {
    return {
      form: {
        title: '',
        attractionId: null,
        attractionName: '',
        coverImage: '',
        content: ''
      },
      selectedAttraction: null,
      loading: false,
      aiLoading: false,
      showAIPanel: false
    }
  },
  methods: {
    /**
     * 选择景点
     */
    onAttractionTap() {
      uni.showToast({
        title: '景点选择功能开发中',
        icon: 'none'
      })
    },

    /**
     * 上传封面
     */
    onCoverTap() {
      uni.showToast({
        title: '图片上传功能开发中',
        icon: 'none'
      })
    },

    /**
     * 显示 AI 助写面板
     */
    onAIPanelTap() {
      if (this.aiLoading) {
        return
      }
      this.showAIPanel = true
    },

    /**
     * 关闭 AI 助写面板
     */
    onAIPanelClose() {
      this.showAIPanel = false
    },

    /**
     * 处理 AI 助写选项
     */
    async onAIOptionTap(option) {
      this.showAIPanel = false
      
      // 检查是否输入了标题
      if (!this.form.title || this.form.title.length < 3) {
        uni.showToast({
          title: '请先输入标题（至少3个字）',
          icon: 'none'
        })
        return
      }

      this.aiLoading = true

      try {
        // 准备输入数据
        const inputData = {
          attractionName: this.selectedAttraction ? this.selectedAttraction.name : '',
          days: '1',
          style: '自由行',
          keywords: option === 'optimize' ? '优化内容' : (option === 'expand' ? '扩展内容' : '生成攻略')
        }

        // 如果已有内容，作为参考内容
        if (this.form.content && this.form.content.length > 0) {
          inputData.keywords += '\n现有内容：' + this.form.content
        }

        // 调用 generateGuide API（返回的是字符串，不是对象）
        const result = await generateGuide(inputData)

        // 将生成的内容填充到表单
        if (result && typeof result === 'string') {
          this.form.content = result
          uni.showToast({
            title: 'AI 生成成功',
            icon: 'success',
            duration: 1500
          })
        } else {
          uni.showToast({
            title: 'AI 生成失败，请重试',
            icon: 'none'
          })
        }
      } catch (error) {
        console.error('AI 生成失败:', error)
        let errorMsg = 'AI 生成失败，请重试'
        
        // 根据错误类型显示更友好的提示
        if (error.message) {
          if (error.message.includes('network') || error.message.includes('timeout')) {
            errorMsg = '网络连接失败，请检查网络后重试'
          } else if (error.message.includes('service') || error.message.includes('unavailable')) {
            errorMsg = 'AI 服务暂时不可用，请稍后再试'
          }
        }
        
        uni.showToast({
          title: errorMsg,
          icon: 'none',
          duration: 2000
        })
      } finally {
        this.aiLoading = false
      }
    },

    /**
     * 取消
     */
    onCancelTap() {
      if (this.aiLoading) {
        return
      }
      uni.navigateBack()
    },

    /**
     * 提交发布
     */
    async onSubmitTap() {
      if (this.aiLoading) {
        return
      }

      // 验证表单
      if (!this.form.title) {
        uni.showToast({
          title: '请输入标题',
          icon: 'none'
        })
        return
      }

      if (this.form.title.length < 5) {
        uni.showToast({
          title: '标题至少5个字',
          icon: 'none'
        })
        return
      }

      if (!this.form.content) {
        uni.showToast({
          title: '请输入攻略内容',
          icon: 'none'
        })
        return
      }

      if (this.form.content.length < 20) {
        uni.showToast({
          title: '内容至少20个字',
          icon: 'none'
        })
        return
      }

      // 获取用户信息
      const userInfo = uni.getStorageSync('userInfo')
      if (!userInfo || !userInfo.id) {
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

      this.loading = true

      try {
        const guideData = {
          userId: userInfo.id,
          title: this.form.title,
          content: this.form.content
        }

        if (this.form.attractionId) {
          guideData.attractionId = this.form.attractionId
        }

        if (this.form.coverImage) {
          guideData.coverImage = this.form.coverImage
        }

        await createGuide(guideData)

        uni.showToast({
          title: '发布成功，等待审核',
          icon: 'success',
          duration: 1500
        })

        setTimeout(() => {
          uni.navigateBack()
        }, 1500)
      } catch (error) {
        console.error('发布攻略失败:', error)
        uni.showToast({
          title: '发布失败，请重试',
          icon: 'none'
        })
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style scoped>
.post-container {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding-bottom: 120rpx;
}

/* 表单 */
.form-section {
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

.form-item {
  margin-bottom: 30rpx;
}

.label {
  font-size: 28rpx;
  color: #666666;
  margin-bottom: 15rpx;
  display: block;
}

/* 标签和AI按钮包装器 */
.label-wrapper {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15rpx;
}

/* AI 助写按钮 */
.ai-btn {
  padding: 10rpx 20rpx;
  background-color: #00bcd4;
  border-radius: 20rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: opacity 0.3s;
}

.ai-btn.ai-disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.ai-btn-text {
  font-size: 24rpx;
  color: #ffffff;
  font-weight: 500;
}

.input,
.picker-view {
  width: 100%;
  padding: 24rpx 30rpx;
  background-color: #f5f5f5;
  border-radius: 12rpx;
  border: 1rpx solid #e0e0e0;
  font-size: 28rpx;
}

.textarea {
  width: 100%;
  padding: 24rpx 30rpx;
  background-color: #f5f5f5;
  border-radius: 12rpx;
  border: 1rpx solid #e0e0e0;
  font-size: 28rpx;
  min-height: 300rpx;
}

.char-count {
  text-align: right;
  font-size: 24rpx;
  color: #999999;
  margin-top: 8rpx;
  display: block;
}

.picker-text {
  color: #333333;
}

.picker-arrow {
  font-size: 24rpx;
  color: #999999;
}

.upload-view {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40rpx;
  background-color: #f5f5f5;
  border-radius: 12rpx;
  border: 2rpx dashed #e0e0e0;
}

.cover-preview {
  width: 200rpx;
  height: 200rpx;
  border-radius: 12rpx;
  margin-right: 20rpx;
}

.upload-tip {
  font-size: 24rpx;
  color: #00bcd4;
}

/* AI 助写面板 */
.ai-panel {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 999;
  display: flex;
  align-items: flex-end;
}

.ai-panel-content {
  width: 100%;
  background-color: #ffffff;
  border-radius: 24rpx 24rpx 0 0;
  animation: slideUp 0.3s ease-out;
}

@keyframes slideUp {
  from {
    transform: translateY(100%);
  }
  to {
    transform: translateY(0);
  }
}

.ai-panel-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 30rpx;
  border-bottom: 1rpx solid #e0e0e0;
}

.ai-panel-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333333;
}

.ai-panel-close {
  font-size: 48rpx;
  color: #999999;
  line-height: 1;
}

.ai-panel-body {
  padding: 30rpx;
}

.ai-option {
  display: flex;
  align-items: center;
  padding: 30rpx;
  background-color: #f5f5f5;
  border-radius: 16rpx;
  margin-bottom: 20rpx;
  transition: background-color 0.3s;
}

.ai-option:active {
  background-color: #e0e0e0;
}

.ai-option:last-child {
  margin-bottom: 0;
}

.ai-option-icon {
  font-size: 48rpx;
  margin-right: 24rpx;
}

.ai-option-info {
  flex: 1;
}

.ai-option-title {
  font-size: 30rpx;
  font-weight: bold;
  color: #333333;
  display: block;
  margin-bottom: 8rpx;
}

.ai-option-desc {
  font-size: 24rpx;
  color: #999999;
  display: block;
}

/* AI 加载遮罩 */
.ai-loading-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.7);
  z-index: 1000;
  display: flex;
  align-items: center;
  justify-content: center;
}

.ai-loading-content {
  background-color: #ffffff;
  border-radius: 16rpx;
  padding: 60rpx 80rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.ai-loading-spinner {
  width: 60rpx;
  height: 60rpx;
  border: 4rpx solid #f3f3f3;
  border-top: 4rpx solid #00bcd4;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 20rpx;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

.ai-loading-text {
  font-size: 28rpx;
  color: #333333;
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

.btn:disabled {
  opacity: 0.5;
}

.btn-secondary {
  background-color: transparent;
  color: #00bcd4;
  border: 2rpx solid #00bcd4;
}

.btn-primary {
  background-color: #00bcd4;
  color: #ffffff;
}
</style>
