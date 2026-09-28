<template>
  <view class="list-container">
    <!-- 搜索栏 -->
    <view class="search-section">
      <view class="search-bar">
        <text class="search-icon">🔍</text>
        <input
          class="search-input"
          type="text"
          v-model="keyword"
          placeholder="搜索景点名称..."
          @confirm="onSearchConfirm"
        />
      </view>
    </view>

    <!-- 筛选栏 -->
    <view class="filter-section">
      <scroll-view class="filter-scroll" scroll-x="true">
        <view class="filter-tags">
          <text
            class="filter-tag"
            :class="{ active: activeFilter === 'all' }"
            @tap="onFilterTap('all')"
          >
            全部
          </text>
          <text
            class="filter-tag"
            :class="{ active: activeFilter === '北京市' }"
            @tap="onFilterTap('北京市')"
          >
            北京市
          </text>
          <text
            class="filter-tag"
            :class="{ active: activeFilter === '上海市' }"
            @tap="onFilterTap('上海市')"
          >
            上海市
          </text>
          <text
            class="filter-tag"
            :class="{ active: activeFilter === '自然景观' }"
            @tap="onFilterTap('自然景观')"
          >
            自然景观
          </text>
          <text
            class="filter-tag"
            :class="{ active: activeFilter === '历史古迹' }"
            @tap="onFilterTap('历史古迹')"
          >
            历史古迹
          </text>
        </view>
      </scroll-view>
    </view>

    <!-- 排序栏 -->
    <view class="sort-section">
      <text class="sort-label">排序：</text>
      <text
        class="sort-item"
        :class="{ active: sortBy === 'default' }"
        @tap="onSortTap('default')"
      >
        默认
      </text>
      <text
        class="sort-item"
        :class="{ active: sortBy === 'price_asc' }"
        @tap="onSortTap('price_asc')"
      >
        价格↑
      </text>
      <text
        class="sort-item"
        :class="{ active: sortBy === 'price_desc' }"
        @tap="onSortTap('price_desc')"
      >
        价格↓
      </text>
      <text
        class="sort-item"
        :class="{ active: sortBy === 'rating' }"
        @tap="onSortTap('rating')"
      >
        评分
      </text>
    </view>

    <!-- 景点列表 -->
    <view class="attraction-list">
      <view
        class="attraction-item"
        v-for="(attraction, index) in attractions"
        :key="index"
        @tap="onAttractionTap(attraction)"
      >
        <image class="attraction-image" :src="attraction.coverImage" mode="aspectFill"></image>
        <view class="attraction-info">
          <text class="attraction-name">{{ attraction.name }}</text>
          <view class="attraction-tags">
            <text
              class="tag-item"
              v-for="(tag, tagIndex) in getTags(attraction.tags)"
              :key="tagIndex"
            >
              {{ tag }}
            </text>
          </view>
          <text class="attraction-desc">{{ attraction.description || '暂无描述' }}</text>
          <view class="attraction-bottom">
            <view class="rating">
              <text class="rating-star">★</text>
              <text class="rating-value">{{ attraction.rating || 0 }}</text>
            </view>
            <text class="price">{{ attraction.minPrice ? '¥' + attraction.minPrice : '免费' }}</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 加载状态 -->
    <view v-if="loading" class="loading">
      <text>加载中...</text>
    </view>

    <!-- 无数据状态 -->
    <view v-if="!loading && attractions.length === 0" class="empty">
      <text class="empty-icon">🏛️</text>
      <text class="empty-text">暂无相关景点</text>
      <text class="empty-tip">换个筛选条件试试吧</text>
    </view>
  </view>
</template>

<script>
import { getAttractionsList } from '@/api/index.js'

export default {
  data() {
    return {
      keyword: '',
      activeFilter: 'all',
      sortBy: 'default',
      attractions: [],
      loading: false,
      page: 0,
      pageSize: 10,
      hasMore: true
    }
  },
  onLoad(options) {
    // 接收参数
    if (options.category) {
      this.activeFilter = options.category
    }
    if (options.keyword) {
      this.keyword = options.keyword
    }
    this.loadAttractions()
  },
  onPullDownRefresh() {
    this.page = 0
    this.attractions = []
    this.hasMore = true
    this.loadAttractions().then(() => {
      uni.stopPullDownRefresh()
    })
  },
  onReachBottom() {
    if (this.hasMore && !this.loading) {
      this.loadMore()
    }
  },
  methods: {
    /**
     * 加载景点列表
     */
    async loadAttractions() {
      this.loading = true
      try {
        const params = {
          page: this.page,
          size: this.pageSize
        }

        if (this.keyword) {
          params.keyword = this.keyword
        }

        if (this.activeFilter !== 'all') {
          params.category = this.activeFilter
        }

        if (this.sortBy !== 'default') {
          params.sortBy = this.sortBy
        }

        const res = await getAttractionsList(params)

        if (this.page === 0) {
          this.attractions = res.content || []
        } else {
          this.attractions = [...this.attractions, ...(res.content || [])]
        }

        // 判断是否还有更多数据
        this.hasMore = !res.last
      } catch (error) {
        console.error('加载景点列表失败:', error)
        uni.showToast({
          title: '加载失败',
          icon: 'none'
        })
      } finally {
        this.loading = false
      }
    },

    /**
     * 加载更多
     */
    loadMore() {
      this.page++
      this.loadAttractions()
    },

    /**
     * 搜索
     */
    onSearchConfirm() {
      this.page = 0
      this.attractions = []
      this.hasMore = true
      this.loadAttractions()
    },

    /**
     * 筛选
     */
    onFilterTap(filter) {
      if (this.activeFilter === filter) return
      this.activeFilter = filter
      this.page = 0
      this.attractions = []
      this.hasMore = true
      this.loadAttractions()
    },

    /**
     * 排序
     */
    onSortTap(sort) {
      if (this.sortBy === sort) return
      this.sortBy = sort
      this.page = 0
      this.attractions = []
      this.hasMore = true
      this.loadAttractions()
    },

    /**
     * 点击景点
     */
    onAttractionTap(attraction) {
      uni.navigateTo({
        url: `/pages/attractions/detail?id=${attraction.id}`
      })
    },

    /**
     * 解析标签
     */
    getTags(tagsStr) {
      if (!tagsStr) return []
      return tagsStr.split(',').map(tag => tag.trim()).filter(tag => tag)
    }
  }
}
</script>

<style scoped>
.list-container {
  min-height: 100vh;
  background-color: #f5f5f5;
}

/* 搜索栏 */
.search-section {
  background-color: #ffffff;
  padding: 20rpx 30rpx;
}

.search-bar {
  display: flex;
  align-items: center;
  padding: 20rpx 30rpx;
  background-color: #f5f5f5;
  border-radius: 40rpx;
}

.search-icon {
  font-size: 32rpx;
  margin-right: 15rpx;
}

.search-input {
  flex: 1;
  font-size: 28rpx;
  border: none;
  outline: none;
}

/* 筛选栏 */
.filter-section {
  background-color: #ffffff;
  padding: 20rpx 30rpx;
  border-bottom: 1rpx solid #f0f0f0;
}

.filter-scroll {
  white-space: nowrap;
}

.filter-tags {
  display: inline-flex;
}

.filter-tag {
  display: inline-block;
  padding: 12rpx 24rpx;
  margin-right: 20rpx;
  background-color: #f5f5f5;
  color: #666666;
  border-radius: 24rpx;
  font-size: 26rpx;
  white-space: nowrap;
}

.filter-tag.active {
  background-color: #00bcd4;
  color: #ffffff;
}

/* 排序栏 */
.sort-section {
  background-color: #ffffff;
  padding: 20rpx 30rpx;
  display: flex;
  align-items: center;
  border-bottom: 1rpx solid #f0f0f0;
}

.sort-label {
  font-size: 28rpx;
  color: #666666;
  margin-right: 20rpx;
}

.sort-item {
  padding: 8rpx 20rpx;
  margin-right: 20rpx;
  font-size: 26rpx;
  color: #666666;
  background-color: #f5f5f5;
  border-radius: 20rpx;
}

.sort-item.active {
  background-color: #00bcd4;
  color: #ffffff;
}

/* 景点列表 */
.attraction-list {
  padding: 20rpx;
}

.attraction-item {
  display: flex;
  background-color: #ffffff;
  border-radius: 16rpx;
  padding: 20rpx;
  margin-bottom: 20rpx;
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

.attraction-tags {
  display: flex;
  flex-wrap: wrap;
  margin-bottom: 10rpx;
}

.tag-item {
  display: inline-block;
  padding: 4rpx 12rpx;
  margin-right: 10rpx;
  margin-bottom: 10rpx;
  background-color: #e3f2fd;
  color: #00bcd4;
  font-size: 22rpx;
  border-radius: 6rpx;
}

.attraction-desc {
  font-size: 24rpx;
  color: #999999;
  margin-bottom: 15rpx;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.attraction-bottom {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: auto;
}

.rating {
  display: flex;
  align-items: center;
}

.rating-star {
  color: #ff9800;
  font-size: 24rpx;
  margin-right: 5rpx;
}

.rating-value {
  font-size: 24rpx;
  color: #ff9800;
}

.price {
  font-size: 30rpx;
  color: #ff5722;
  font-weight: bold;
}

/* 状态 */
.loading {
  text-align: center;
  padding: 100rpx;
  font-size: 28rpx;
  color: #999999;
}

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
