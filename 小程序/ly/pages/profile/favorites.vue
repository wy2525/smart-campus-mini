<template>
  <view class="favorites-container">
    <!-- 标签 -->
    <view class="tabs-section">
      <view
        class="tab-item"
        :class="{ active: activeTab === 'attractions' }"
        @tap="onTabTap('attractions')"
      >
        收藏景点
      </view>
      <view
        class="tab-item"
        :class="{ active: activeTab === 'guides' }"
        @tap="onTabTap('guides')"
      >
        收藏攻略
      </view>
    </view>

    <!-- 列表 -->
    <view class="list-section">
      <!-- 景点列表 -->
      <view v-if="activeTab === 'attractions'" class="attractions-list">
        <view
          class="item"
          v-for="(item, index) in favorites.attractions"
          :key="index"
          @tap="onAttractionTap(item)"
        >
          <image class="item-image" :src="item.coverImage || '/static/placeholder.png'" mode="aspectFill"></image>
          <view class="item-info">
            <text class="item-name">{{ item.name }}</text>
            <view class="item-bottom">
              <view class="rating">
                <text class="star">★</text>
                <text class="value">{{ item.rating || 0 }}</text>
              </view>
              <text class="price">{{ item.minPrice ? '¥' + item.minPrice : '免费' }}</text>
            </view>
          </view>
        </view>
      </view>

      <!-- 攻略列表 -->
      <view v-if="activeTab === 'guides'" class="guides-list">
        <view
          class="item"
          v-for="(item, index) in favorites.guides"
          :key="index"
          @tap="onGuideTap(item)"
        >
          <image class="item-image" :src="item.coverImage || '/static/placeholder.png'" mode="aspectFill"></image>
          <view class="item-info">
            <text class="item-title">{{ item.title }}</text>
            <view class="item-meta">
              <text class="author">{{ item.user ? item.user.nickname : '匿名' }}</text>
              <text class="stats">{{ item.viewCount || 0 }}浏览</text>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 空状态 -->
    <view v-if="!loading && isEmpty" class="empty">
      <text class="empty-icon">❤️</text>
      <text class="empty-text">暂无收藏</text>
      <text class="empty-tip">去收藏心仪内容吧</text>
    </view>
  </view>
</template>

<script>
import { getFavoriteAttractions, getFavoriteGuides } from '@/api/index.js'

export default {
  data() {
    return {
      activeTab: 'guides',
      favorites: {
        attractions: [],
        guides: []
      },
      loading: false,
      isEmpty: true
    }
  },
  onShow() {
    this.loadFavorites()
  },
  methods: {
    /**
     * 加载收藏数据
     */
    async loadFavorites() {
      this.loading = true
      
      try {
        // 并行加载收藏的景点和攻略
        const [attractionsRes, guidesRes] = await Promise.all([
          getFavoriteAttractions(),
          getFavoriteGuides()
        ])
        
        this.favorites.attractions = attractionsRes || []
        this.favorites.guides = guidesRes || []
        
        this.isEmpty = this.favorites.attractions.length === 0 && this.favorites.guides.length === 0
      } catch (error) {
        console.error('加载收藏失败:', error)
        
        // 如果后端未启动，使用模拟数据演示
        this.useMockData()
      } finally {
        this.loading = false
      }
    },
    
    /**
     * 使用模拟数据（后端未启动时）
     */
    useMockData() {
      // 检查本地存储中是否有收藏的攻略ID列表
      const favoriteGuideIds = uni.getStorageSync('favoriteGuideIds') || []
      
      if (favoriteGuideIds.length === 0) {
        this.isEmpty = true
        return
      }
      
      // 模拟所有攻略数据（实际项目中应该从后端获取）
      const allGuides = [
        {
          id: 1,
          title: '北京三日游完美攻略',
          coverImage: 'https://mmbiz.qpic.cn/mmbiz_jpg/DpO5fiaCsyJJwnNryicvS1icnQicbT9Gnlv4iaO5p26uB6T9S2h8icR45ibZ8PricbZ19icic9cO6S3icicicO37WicS24u5hA/0',
          user: { nickname: '旅行达人' },
          viewCount: 1234
        },
        {
          id: 2,
          title: '上海美食探店指南',
          coverImage: 'https://mmbiz.qpic.cn/mmbiz_jpg/DpO5fiaCsyJJwnNryicvS1icnQicbT9Gnlv4iaO5p26uB6T9S2h8icR45ibZ8PricbZ19icic9cO6S3icicicO37WicS24u5hA/0',
          user: { nickname: '美食家' },
          viewCount: 856
        },
        {
          id: 3,
          title: '杭州西湖一日游',
          coverImage: 'https://mmbiz.qpic.cn/mmbiz_jpg/DpO5fiaCsyJJwnNryicvS1icnQicbT9Gnlv4iaO5p26uB6T9S2h8icR45ibZ8PricbZ19icic9cO6S3icicicO37WicS24u5hA/0',
          user: { nickname: '西湖小子' },
          viewCount: 2345
        },
        {
          id: 4,
          title: '西安兵马俑历史之旅',
          coverImage: 'https://mmbiz.qpic.cn/mmbiz_jpg/DpO5fiaCsyJJwnNryicvS1icnQicbT9Gnlv4iaO5p26uB6T9S2h8icR45ibZ8PricbZ19icic9cO6S3icicicO37WicS24u5hA/0',
          user: { nickname: '历史爱好者' },
          viewCount: 3456
        },
        {
          id: 5,
          title: '成都旅游必去景点',
          coverImage: 'https://mmbiz.qpic.cn/mmbiz_jpg/DpO5fiaCsyJJwnNryicvS1icnQicbT9Gnlv4iaO5p26uB6T9S2h8icR45ibZ8PricbZ19icic9cO6S3icicicO37WicS24u5hA/0',
          user: { nickname: '熊猫爱好者' },
          viewCount: 1567
        }
      ]
      
      // 根据收藏ID筛选
      this.favorites.guides = allGuides.filter(guide => favoriteGuideIds.includes(guide.id))
      
      this.favorites.attractions = []
      this.isEmpty = this.favorites.attractions.length === 0 && this.favorites.guides.length === 0
    },

    /**
     * 切换标签
     */
    onTabTap(tab) {
      this.activeTab = tab
    },

    /**
     * 点击景点
     */
    onAttractionTap(item) {
      uni.navigateTo({
        url: `/pages/attractions/detail?id=${item.id}`
      })
    },

    /**
     * 点击攻略
     */
    onGuideTap(item) {
      uni.navigateTo({
        url: `/pages/community/detail?id=${item.id}`
      })
    }
  }
}
</script>

<style scoped>
.favorites-container {
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

/* 列表 */
.list-section {
  padding: 0 20rpx;
}

.attractions-list,
.guides-list {
  display: flex;
  flex-direction: column;
}

.item {
  display: flex;
  background-color: #ffffff;
  border-radius: 16rpx;
  padding: 20rpx;
  margin-bottom: 20rpx;
}

.item-image {
  width: 200rpx;
  height: 160rpx;
  border-radius: 12rpx;
  margin-right: 20rpx;
  flex-shrink: 0;
}

.item-info {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.item-name {
  font-size: 28rpx;
  color: #333333;
  font-weight: 500;
  margin-bottom: 10rpx;
}

.item-bottom {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: auto;
}

.rating {
  display: flex;
  align-items: center;
}

.star {
  color: #ff9800;
  font-size: 24rpx;
  margin-right: 5rpx;
}

.value {
  font-size: 24rpx;
  color: #ff9800;
}

.price {
  font-size: 26rpx;
  color: #ff5722;
  font-weight: bold;
}

.item-title {
  font-size: 28rpx;
  color: #333333;
  font-weight: 500;
  margin-bottom: 10rpx;
}

.item-meta {
  display: flex;
  align-items: center;
  font-size: 24rpx;
  color: #999999;
}

.author {
  margin-right: 20rpx;
}

.stats {
  flex: 1;
}

/* 空状态 */
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
