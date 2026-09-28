<template>
  <view class="community-container">
    <!-- 顶部搜索栏 -->
    <view class="header">
      <view class="search-bar">
        <text class="search-icon">🔍</text>
        <input
          class="search-input"
          type="text"
          v-model="keyword"
          placeholder="搜索攻略..."
          @confirm="onSearchConfirm"
        />
      </view>
      <view class="publish-btn" @tap="onPublishTap">
        <text>发布</text>
      </view>
    </view>

    <!-- 分类标签 -->
    <scroll-view class="category-scroll" scroll-x="true" enhanced="true" show-scrollbar="false">
      <view class="category-list">
        <view 
          class="category-item" 
          :class="{ active: activeCategory === 'all' }"
          @tap="onCategoryTap('all')"
        >
          <text>全部</text>
        </view>
        <view 
          class="category-item" 
          v-for="(cat, index) in categories" 
          :key="index"
          :class="{ active: activeCategory === cat }"
          @tap="onCategoryTap(cat)"
        >
          <text>{{ cat }}</text>
        </view>
      </view>
    </scroll-view>

    <!-- 瀑布流布局 -->
    <view class="waterfall-container">
      <view class="waterfall-column column-left">
        <view
          class="guide-card"
          v-for="(guide, index) in leftGuides"
          :key="index"
          @tap="onGuideTap(guide)"
        >
          <image 
            class="card-cover" 
            :src="guide.coverImage || '/static/placeholder.png'" 
            mode="widthFix"
            @error="onImageError"
          ></image>
          <view class="card-content">
            <text class="card-title">{{ guide.title }}</text>
            <view class="card-user">
              <image class="user-avatar" :src="guide.user?.avatar || '/static/avatar-default.png'" mode="aspectFill"></image>
              <text class="user-name">{{ guide.user?.nickname || '匿名' }}</text>
            </view>
            <view class="card-stats">
              <view class="stat">
                <text>👁</text>
                <text>{{ guide.viewCount || 0 }}</text>
              </view>
              <view class="stat">
                <text>❤️</text>
                <text>{{ guide.likeCount || 0 }}</text>
              </view>
              <view class="stat">
                <text>💬</text>
                <text>{{ guide.commentCount || 0 }}</text>
              </view>
            </view>
          </view>
        </view>
      </view>
      
      <view class="waterfall-column column-right">
        <view
          class="guide-card"
          v-for="(guide, index) in rightGuides"
          :key="index"
          @tap="onGuideTap(guide)"
        >
          <image 
            class="card-cover" 
            :src="guide.coverImage || '/static/placeholder.png'" 
            mode="widthFix"
            @error="onImageError"
          ></image>
          <view class="card-content">
            <text class="card-title">{{ guide.title }}</text>
            <view class="card-user">
              <image class="user-avatar" :src="guide.user?.avatar || '/static/avatar-default.png'" mode="aspectFill"></image>
              <text class="user-name">{{ guide.user?.nickname || '匿名' }}</text>
            </view>
            <view class="card-stats">
              <view class="stat">
                <text>👁</text>
                <text>{{ guide.viewCount || 0 }}</text>
              </view>
              <view class="stat">
                <text>❤️</text>
                <text>{{ guide.likeCount || 0 }}</text>
              </view>
              <view class="stat">
                <text>💬</text>
                <text>{{ guide.commentCount || 0 }}</text>
              </view>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 加载状态 -->
    <view v-if="loading" class="loading">
      <text>加载中...</text>
    </view>

    <!-- 无数据状态 -->
    <view v-if="!loading && guides.length === 0" class="empty">
      <text class="empty-icon">📝</text>
      <text class="empty-text">暂无攻略</text>
      <text class="empty-tip">发布你的第一个攻略吧</text>
    </view>
  </view>
</template>

<script>
import { getGuidesList } from '@/api/index.js'

export default {
  data() {
    return {
      keyword: '',
      activeCategory: 'all',
      categories: ['攻略', '游记', '问答', '美食', '住宿', '交通'],
      guides: [],
      loading: false,
      page: 0,
      pageSize: 10,
      hasMore: true
    }
  },
  computed: {
    leftGuides() {
      return this.guides.filter((_, index) => index % 2 === 0)
    },
    rightGuides() {
      return this.guides.filter((_, index) => index % 2 === 1)
    }
  },
  onLoad() {
    this.loadGuides()
  },
  onPullDownRefresh() {
    this.page = 0
    this.guides = []
    this.hasMore = true
    this.loadGuides().then(() => {
      uni.stopPullDownRefresh()
    })
  },
  onReachBottom() {
    if (this.hasMore && !this.loading) {
      this.loadMore()
    }
  },
  methods: {
    async loadGuides() {
      this.loading = true
      try {
        const params = {
          page: this.page,
          size: this.pageSize
        }

        if (this.keyword) {
          params.keyword = this.keyword
        }

        if (this.activeCategory !== 'all') {
          params.category = this.activeCategory
        }

        const res = await getGuidesList(params)

        if (this.page === 0) {
          this.guides = res.content || []
        } else {
          this.guides = [...this.guides, ...(res.content || [])]
        }

        this.hasMore = !res.last
      } catch (error) {
        console.error('加载攻略列表失败:', error)
        uni.showToast({
          title: '加载失败',
          icon: 'none'
        })
      } finally {
        this.loading = false
      }
    },

    loadMore() {
      this.page++
      this.loadGuides()
    },

    onSearchConfirm() {
      this.page = 0
      this.guides = []
      this.hasMore = true
      this.loadGuides()
    },

    onCategoryTap(category) {
      if (this.activeCategory === category) return
      this.activeCategory = category
      this.page = 0
      this.guides = []
      this.hasMore = true
      this.loadGuides()
    },

    onPublishTap() {
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
        url: '/pages/community/post'
      })
    },

    onGuideTap(guide) {
      uni.navigateTo({
        url: `/pages/community/detail?id=${guide.id}`
      })
    },

    onImageError(e) {
      if (e.target) {
        e.target.src = '/static/placeholder.png'
      }
    }
  }
}
</script>

<style scoped>
.community-container {
  min-height: 100vh;
  background-color: #f5f5f5;
}

/* 顶部头部 */
.header {
  background: #fff;
  padding: 20rpx 30rpx;
  display: flex;
  align-items: center;
  gap: 20rpx;
  position: sticky;
  top: 0;
  z-index: 100;
}

.search-bar {
  flex: 1;
  display: flex;
  align-items: center;
  padding: 18rpx 24rpx;
  background-color: #f5f5f5;
  border-radius: 36rpx;
}

.search-icon {
  font-size: 28rpx;
  margin-right: 16rpx;
}

.search-input {
  flex: 1;
  font-size: 26rpx;
}

.publish-btn {
  padding: 18rpx 32rpx;
  background: linear-gradient(135deg, #667eea, #764ba2);
  border-radius: 36rpx;
}

.publish-btn text {
  font-size: 26rpx;
  color: #fff;
  font-weight: 500;
}

/* 分类标签 */
.category-scroll {
  background: #fff;
  padding: 16rpx 0;
  border-bottom: 1rpx solid #f0f0f0;
}

.category-list {
  display: flex;
  padding: 0 20rpx;
  gap: 16rpx;
}

.category-item {
  flex-shrink: 0;
  padding: 12rpx 28rpx;
  background: #f5f5f5;
  border-radius: 30rpx;
  font-size: 26rpx;
  color: #666;
  transition: all 0.3s;
}

.category-item.active {
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: #fff;
}

/* 瀑布流布局 */
.waterfall-container {
  display: flex;
  padding: 20rpx;
  gap: 20rpx;
}

.waterfall-column {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.guide-card {
  background: #fff;
  border-radius: 20rpx;
  overflow: hidden;
  box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.05);
}

.card-cover {
  width: 100%;
  border-radius: 20rpx 20rpx 0 0;
}

.card-content {
  padding: 20rpx;
}

.card-title {
  font-size: 28rpx;
  color: #333;
  font-weight: 500;
  display: block;
  margin-bottom: 16rpx;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.card-user {
  display: flex;
  align-items: center;
  margin-bottom: 16rpx;
}

.user-avatar {
  width: 40rpx;
  height: 40rpx;
  border-radius: 50%;
  margin-right: 12rpx;
}

.user-name {
  font-size: 24rpx;
  color: #999;
}

.card-stats {
  display: flex;
  gap: 24rpx;
}

.stat {
  display: flex;
  align-items: center;
  gap: 6rpx;
  font-size: 22rpx;
  color: #999;
}

/* 状态 */
.loading {
  text-align: center;
  padding: 40rpx;
  font-size: 26rpx;
  color: #999;
}

.empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 100rpx 20rpx;
}

.empty-icon {
  font-size: 100rpx;
  margin-bottom: 24rpx;
}

.empty-text {
  font-size: 30rpx;
  color: #333;
  margin-bottom: 12rpx;
}

.empty-tip {
  font-size: 26rpx;
  color: #999;
}
</style>
