<template>
  <view class="home-container">
    <!-- 顶部搜索栏 -->
    <view class="header">
      <view class="search-bar" @tap="onSearchTap">
        <text class="search-icon">🔍</text>
        <text class="search-placeholder">搜索景点、城市...</text>
      </view>
    </view>

    <!-- 轮播图区域 -->
    <view class="banner-section">
      <swiper
        class="banner-swiper"
        :indicator-dots="true"
        :autoplay="true"
        :interval="5000"
        :duration="500"
        indicator-color="rgba(255,255,255,0.5)"
        indicator-active-color="#fff"
        circular="true"
      >
        <swiper-item v-for="(banner, index) in bannerList" :key="index">
          <image
            class="banner-image"
            :src="banner.imageUrl"
            mode="aspectFill"
          ></image>
        </swiper-item>
      </swiper>
    </view>

    <!-- 快捷入口 -->
    <view class="quick-access-section">
      <view class="quick-access-grid">
        <view
          class="quick-access-item"
          v-for="(item, index) in quickAccessList"
          :key="index"
          @tap="onQuickAccessTap(item)"
        >
          <view class="quick-access-icon" :class="item.class">
            <text>{{ item.icon }}</text>
          </view>
          <text class="quick-access-text">{{ item.name }}</text>
        </view>
      </view>
    </view>

    <!-- 热门景点 -->
    <view class="section" v-if="hotAttractions.length > 0">
      <view class="section-header">
        <text class="section-title">热门景点</text>
        <text class="section-more" @tap="onMoreHotTap">查看更多 ›</text>
      </view>
      <scroll-view class="hot-scroll" scroll-x="true" enhanced="true" show-scrollbar="false">
        <view class="hot-list">
          <view
            class="hot-item"
            v-for="(attraction, index) in hotAttractions"
            :key="index"
            @tap="onAttractionTap(attraction)"
          >
            <image 
              class="hot-image" 
              :src="attraction.coverImage || '/static/placeholder.png'" 
              mode="aspectFill"
              @error="onImageError"
            ></image>
            <view class="hot-info">
              <text class="hot-name">{{ attraction.name }}</text>
              <view class="hot-meta">
                <text class="hot-rating">★ {{ attraction.rating || 0 }}</text>
                <text class="hot-price">{{ attraction.minPrice ? '¥' + attraction.minPrice : '免费' }}</text>
              </view>
            </view>
          </view>
        </view>
      </scroll-view>
    </view>

    <!-- 为你推荐 -->
    <view class="section">
      <view class="section-header">
        <text class="section-title">为你推荐</text>
        <text class="section-more" @tap="onMoreRecommendTap">查看更多 ›</text>
      </view>
      <view class="recommend-list">
        <view
          class="recommend-item"
          v-for="(attraction, index) in recommendAttractions"
          :key="index"
          @tap="onAttractionTap(attraction)"
        >
          <image class="recommend-image" :src="attraction.coverImage" mode="aspectFill" @error="onImageError"></image>
          <view class="recommend-info">
            <text class="recommend-name">{{ attraction.name }}</text>
            <view class="recommend-tags" v-if="getTags(attraction.tags).length > 0">
              <text
                class="tag-item"
                v-for="(tag, tagIndex) in getTags(attraction.tags).slice(0, 2)"
                :key="tagIndex"
              >
                {{ tag }}
              </text>
            </view>
            <view class="recommend-bottom">
              <view class="rating-info">
                <text class="rating-star">★</text>
                <text class="rating-value">{{ attraction.rating || 0 }}</text>
              </view>
              <text class="recommend-price">{{ attraction.minPrice ? '¥' + attraction.minPrice : '免费' }}</text>
            </view>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { getHotAttractions, getRecommendAttractions, getAttractionsList } from '@/api/index.js'

export default {
  data() {
    return {
      // 本地轮播图
      localBanners: [
        { imageUrl: '/static/images/01.jpg', id: 1 },
        { imageUrl: '/static/images/02.jpg', id: 2 },
        { imageUrl: '/static/images/03.jpg', id: 3 },
        { imageUrl: '/static/images/04.jpg', id: 4 },
        { imageUrl: '/static/images/05.jpg', id: 5 }
      ],
      bannerList: [],
      hotAttractions: [],
      recommendAttractions: [],
      quickAccessList: [
        { id: 1, name: '热门', icon: '🔥', class: 'hot' },
        { id: 2, name: '周边', icon: '🚗', class: 'nearby' },
        { id: 3, name: '亲子', icon: '👨‍👩‍👧', class: 'family' },
        { id: 4, name: '博物馆', icon: '🏛️', class: 'museum' },
        { id: 5, name: '乐园', icon: '🎢', class: 'park' },
        { id: 6, name: '攻略', icon: '📝', class: 'guide' }
      ]
    }
  },
  onLoad() {
    this.loadHomeData()
  },
  onPullDownRefresh() {
    this.loadHomeData().then(() => {
      uni.stopPullDownRefresh()
    })
  },
  methods: {
    async loadHomeData() {
      try {
        // 优先使用本地轮播图
        this.bannerList = this.localBanners
        
        // 并行请求所有数据
        const [hotRes, recommendRes] = await Promise.all([
          getHotAttractions().catch(() => []),
          getRecommendAttractions().catch(() => [])
        ])

        this.hotAttractions = hotRes || []
        this.recommendAttractions = recommendRes || []
        
        // 如果热门景点或推荐景点为空，尝试从景点列表获取
        if (this.hotAttractions.length === 0 || this.recommendAttractions.length === 0) {
          try {
            const attractionsListRes = await getAttractionsList('', '', 1, 0, 10)
            const attractionsList = attractionsListRes || []
            
            if (this.hotAttractions.length === 0 && attractionsList.length > 0) {
              this.hotAttractions = attractionsList.slice(0, 8)
            }
            if (this.recommendAttractions.length === 0 && attractionsList.length > 0) {
              this.recommendAttractions = attractionsList.slice(0, 10)
            }
          } catch (error) {
            console.error('获取景点列表失败:', error)
          }
        }
      } catch (error) {
        console.error('加载首页数据失败:', error)
      }
    },

    async loadMoreRecommend() {
      uni.showToast({
        title: '已加载全部推荐',
        icon: 'none',
        duration: 1500
      })
    },

    getTags(tagsStr) {
      if (!tagsStr) return []
      return tagsStr.split(',').map(tag => tag.trim()).filter(tag => tag)
    },

    onSearchTap() {
      uni.navigateTo({
        url: '/pages/search/index'
      })
    },

    onBannerTap(banner) {
      if (banner.attractionId) {
        uni.navigateTo({
          url: `/pages/attractions/detail?id=${banner.attractionId}`
        })
      }
    },

    onQuickAccessTap(item) {
      if (item.id === 6) {
        uni.switchTab({
          url: `/pages/community/index`
        })
      } else {
        uni.navigateTo({
          url: `/pages/attractions/list?category=${item.name}`
        })
      }
    },

    onAttractionTap(attraction) {
      uni.navigateTo({
        url: `/pages/attractions/detail?id=${attraction.id}`
      })
    },

    onMoreHotTap() {
      uni.navigateTo({
        url: '/pages/attractions/list?category=hot'
      })
    },

    onMoreRecommendTap() {
      uni.navigateTo({
        url: '/pages/attractions/list'
      })
    },

    onImageError(e) {
      console.error('图片加载失败:', e)
      if (e.target) {
        e.target.src = '/static/placeholder.png'
      }
    }
  }
}
</script>

<style scoped>
.home-container {
  min-height: 100vh;
  background-color: #f8f8f8;
  padding-bottom: 30rpx;
}

/* 顶部头部 */
.header {
  background: #fff;
  padding: 20rpx 30rpx;
  position: sticky;
  top: 0;
  z-index: 100;
}

/* 搜索栏 */
.search-bar {
  display: flex;
  align-items: center;
  padding: 22rpx 30rpx;
  background-color: #f5f5f5;
  border-radius: 36rpx;
}

.search-icon {
  font-size: 28rpx;
  margin-right: 16rpx;
}

.search-placeholder {
  font-size: 26rpx;
  color: #999;
}

/* 轮播图 */
.banner-section {
  margin: 24rpx 30rpx;
}

.banner-swiper {
  height: 360rpx;
  border-radius: 24rpx;
  overflow: hidden;
}

.banner-image {
  width: 100%;
  height: 100%;
}

/* 快捷入口 */
.quick-access-section {
  background: #fff;
  padding: 30rpx 20rpx;
  margin-bottom: 24rpx;
}

.quick-access-grid {
  display: flex;
  justify-content: space-between;
}

.quick-access-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 10rpx 0;
}

.quick-access-icon {
  width: 88rpx;
  height: 88rpx;
  border-radius: 24rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 40rpx;
  margin-bottom: 12rpx;
}

.quick-access-icon.hot { background: linear-gradient(135deg, #ff6b6b, #ff8e8e); }
.quick-access-icon.nearby { background: linear-gradient(135deg, #4ecdc4, #6be0d8); }
.quick-access-icon.family { background: linear-gradient(135deg, #a18cd1, #b9a3e0); }
.quick-access-icon.museum { background: linear-gradient(135deg, #667eea, #8b9cf6); }
.quick-access-icon.park { background: linear-gradient(135deg, #f093fb, #f5a8fc); }
.quick-access-icon.guide { background: linear-gradient(135deg, #feca57, #ffd87d); }

.quick-access-text {
  font-size: 24rpx;
  color: #666;
}

/* 通用区块 */
.section {
  background: #fff;
  margin-bottom: 24rpx;
  padding: 24rpx 30rpx;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: 600;
  color: #1a1a1a;
}

.section-more {
  font-size: 26rpx;
  color: #999;
}

/* 热门景点 */
.hot-scroll {
  margin: 0 -30rpx;
  padding: 0 30rpx;
}

.hot-list {
  display: flex;
  gap: 24rpx;
}

.hot-item {
  flex-shrink: 0;
  width: 260rpx;
}

.hot-image {
  width: 100%;
  height: 180rpx;
  border-radius: 16rpx;
  margin-bottom: 16rpx;
}

.hot-info {
  padding: 0 4rpx;
}

.hot-name {
  font-size: 28rpx;
  color: #333;
  font-weight: 500;
  display: block;
  margin-bottom: 10rpx;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.hot-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.hot-rating {
  font-size: 22rpx;
  color: #ff9800;
}

.hot-price {
  font-size: 26rpx;
  color: #ff5722;
  font-weight: 600;
}

/* 推荐景点 */
.recommend-list {
  display: flex;
  flex-direction: column;
  gap: 24rpx;
}

.recommend-item {
  display: flex;
  background: #f8f8f8;
  border-radius: 20rpx;
  padding: 16rpx;
  gap: 20rpx;
}

.recommend-image {
  width: 220rpx;
  height: 160rpx;
  border-radius: 16rpx;
  flex-shrink: 0;
}

.recommend-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.recommend-name {
  font-size: 30rpx;
  color: #333;
  font-weight: 500;
  margin-bottom: 10rpx;
}

.recommend-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 10rpx;
  margin-bottom: 10rpx;
}

.tag-item {
  padding: 6rpx 14rpx;
  background: #e8f4fd;
  color: #667eea;
  font-size: 20rpx;
  border-radius: 8rpx;
}

.recommend-bottom {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.rating-info {
  display: flex;
  align-items: center;
  gap: 6rpx;
}

.rating-star {
  color: #ff9800;
  font-size: 24rpx;
}

.rating-value {
  font-size: 24rpx;
  color: #ff9800;
}

.recommend-price {
  font-size: 30rpx;
  color: #ff5722;
  font-weight: 600;
}
</style>
