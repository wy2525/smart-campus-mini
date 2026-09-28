<template>
  <view class="detail-container">
    <!-- 景点封面图 -->
    <view class="cover-section">
      <swiper
        class="cover-swiper"
        :indicator-dots="true"
        :autoplay="true"
        :interval="3000"
      >
        <swiper-item v-for="(image, index) in images" :key="index">
          <image class="cover-image" :src="image" mode="aspectFill"></image>
        </swiper-item>
      </swiper>
    </view>

    <!-- 景点信息 -->
    <view class="info-section">
      <view class="info-header">
        <text class="name">{{ attraction.name }}</text>
        <view class="rating-section">
          <text class="rating-star">★</text>
          <text class="rating-value">{{ attraction.rating || 0 }}</text>
          <text class="rating-count"
            >{{ attraction.viewCount || 0 }}人浏览</text
          >
        </view>
      </view>

      <view class="info-tags">
        <text
          class="tag"
          v-for="(tag, index) in getTags(attraction.tags)"
          :key="index"
        >
          {{ tag }}
        </text>
      </view>

      <view class="info-row">
        <text class="label">地址：</text>
        <text class="value">{{ attraction.address }}</text>
      </view>

      <view class="info-row">
        <text class="label">开放时间：</text>
        <text class="value">{{ attraction.openTime || "全天开放" }}</text>
      </view>

      <view class="info-row">
        <text class="label">联系电话：</text>
        <text class="value">{{ attraction.phone || "暂无" }}</text>

        <view class="info-row">
          <text class="label">门票价格：</text>
          <text class="value price">¥{{ attraction.minPrice || 0 }}</text>
        </view>

        <view class="info-row">
          <text class="label">景点评分：</text>
          <text class="value">{{ attraction.rating || 0 }}/5</text>
        </view>

        <view class="info-row">
          <text class="label">景点介绍：</text>
          <text class="value description">{{ attraction.description }}</text>
        </view>

        <view class="info-row">
          <text class="label">注意事项：</text>
          <text class="value">{{ attraction.notes || "暂无" }}</text>
        </view>
      </view>

      <view class="info-row price-row">
        <text class="label">最低票价：</text>
        <text class="price-value">{{
          attraction.minPrice ? "¥" + attraction.minPrice : "免费"
        }}</text>
      </view>

      <!-- AI 智能解读按钮 -->
      <button
        class="ai-interpret-btn"
        @tap="onAIInterpret"
        :disabled="aiLoading"
      >
        <text v-if="!aiLoading">🤖 AI 智能解读</text>
        <text v-else>AI 正在解读中...</text>
      </button>
    </view>

    <!-- 景点介绍 -->
    <view class="desc-section">
      <view class="section-title">
        <text>景点介绍</text>
      </view>
      <text class="desc-text">{{ attraction.description || "暂无介绍" }}</text>
    </view>

    <!-- 注意事项 -->
    <view class="notes-section" v-if="attraction.notes">
      <view class="section-title">
        <text>注意事项</text>
      </view>
      <text class="notes-text">{{ attraction.notes }}</text>
    </view>

    <!-- 门票类型 -->
    <view class="tickets-section">
      <view class="section-title">
        <text>门票类型</text>
      </view>
      <view class="tickets-list">
        <view
          class="ticket-item"
          v-for="(ticket, index) in tickets"
          :key="index"
          @tap="onTicketTap(ticket)"
        >
          <view class="ticket-info">
            <text class="ticket-name">{{ ticket.name }}</text>
            <text class="ticket-desc">{{ ticket.description || "" }}</text>
          </view>
          <view class="ticket-price">
            <text class="price">¥{{ ticket.price }}</text>
          </view>
        </view>
      </view>
    </view>

    <!-- AI 智能解读区域 -->
    <view class="ai-interpretation-section" v-if="aiInterpretation || aiError">
      <view class="ai-section-header">
        <text class="section-title">🤖 AI 智能解读</text>
        <text v-if="aiInterpretation" class="toggle-btn" @tap="toggleAISection">
          {{ aiExpanded ? "收起" : "展开" }}
        </text>
      </view>

      <!-- Loading 状态 -->
      <view v-if="aiLoading" class="ai-loading-view">
        <text class="loading-text">AI 正在为您解读...</text>
      </view>

      <!-- 错误提示 -->
      <view v-else-if="aiError" class="ai-error-view">
        <text class="error-icon">⚠️</text>
        <text class="error-text">{{ aiError }}</text>
        <button class="retry-btn" @tap="onAIInterpret">重试</button>
      </view>

      <!-- 解读内容 -->
      <view
        v-else-if="aiInterpretation && aiExpanded"
        class="ai-interpretation-content"
      >
        <view class="ai-item">
          <text class="ai-item-title">📜 历史背景</text>
          <text class="ai-item-text">{{ aiInterpretation }}</text>
        </view>
        <view class="ai-item">
          <text class="ai-item-title">✨ 核心亮点</text>
          <text class="ai-item-text">{{ aiInterpretation }}</text>
        </view>
        <view class="ai-item">
          <text class="ai-item-title">💡 游览建议</text>
          <text class="ai-item-text">{{ aiInterpretation }}</text>
        </view>
        <view class="ai-item">
          <text class="ai-item-title">⚠️ 注意事项</text>
          <text class="ai-item-text">{{ aiInterpretation }}</text>
        </view>
      </view>
    </view>

    <!-- 底部预订按钮 -->
    <view class="bottom-bar">
      <button class="btn btn-primary" @tap="onBookingTap">立即预订</button>
    </view>
  </view>
</template>

<script>
import {
  getAttractionDetail,
  getAttractionTickets,
  interpretAttraction,
} from "@/api/index.js";

export default {
  data() {
    return {
      attractionId: null,
      attraction: {},
      images: [],
      tickets: [],
      loading: false,
      // AI 相关状态
      aiLoading: false,
      aiError: "",
      aiInterpretation: null,
      aiExpanded: true,
    };
  },
  onLoad(options) {
    if (options.id) {
      this.attractionId = options.id;
      this.loadAttractionDetail();
    }
  },
  onShareAppMessage() {
    return {
      title: this.attraction.name,
      path: `/pages/attractions/detail?id=${this.attractionId}`,
      imageUrl: this.attraction.coverImage,
    };
  },
  methods: {
    /**
     * 加载景点详情
     */
    async loadAttractionDetail() {
      this.loading = true;
      try {
        // 并行加载景点详情和门票类型
        const [detailRes, ticketsRes] = await Promise.all([
          getAttractionDetail(this.attractionId),
          getAttractionTickets(this.attractionId),
        ]);

        this.attraction = detailRes || {};
        this.tickets = ticketsRes || [];

        // 解析图片集
        if (this.attraction.images) {
          this.images = this.attraction.images
            .split(",")
            .filter((img) => img.trim());
        } else if (this.attraction.coverImage) {
          this.images = [this.attraction.coverImage];
        } else {
          this.images = [];
        }

        // 设置标题
        uni.setNavigationBarTitle({
          title: this.attraction.name,
        });
      } catch (error) {
        console.error("加载景点详情失败:", error);
        uni.showToast({
          title: "加载失败",
          icon: "none",
        });
      } finally {
        this.loading = false;
      }
    },

    /**
     * 解析标签
     */
    getTags(tagsStr) {
      if (!tagsStr) return [];
      return tagsStr
        .split(",")
        .map((tag) => tag.trim())
        .filter((tag) => tag);
    },

    /**
     * 点击门票
     */
    onTicketTap(ticket) {
      uni.navigateTo({
        url: `/pages/attractions/ticket?attractionId=${this.attractionId}&ticketId=${ticket.id}`,
      });
    },

    /**
     * 点击预订
     */
    onBookingTap() {
      // 选择第一个门票
      if (this.tickets.length > 0) {
        this.onTicketTap(this.tickets[0]);
      } else {
        uni.showToast({
          title: "暂无可售门票",
          icon: "none",
        });
      }
    },

    /**
     * AI 智能解读
     */
    async onAIInterpret() {
      if (this.aiLoading) return;

      this.aiLoading = true;
      this.aiError = "";
      this.aiInterpretation = null;

      try {
        const { name, description, tags, address, openTime } = this.attraction;
        const interpretation = await interpretAttraction({
          name,
          description,
          tags: tags || "",
          address,
          openTime,
        });

        this.aiInterpretation = interpretation;
        this.aiExpanded = true;

        uni.showToast({
          title: "AI 解读完成",
          icon: "success",
        });
      } catch (error) {
        console.error("AI 解读失败:", error);
        this.aiError = error.message || "AI 解读失败，请稍后再试";
      } finally {
        this.aiLoading = false;
      }
    },

    /**
     * 展开/收起 AI 解读
     */
    toggleAISection() {
      this.aiExpanded = !this.aiExpanded;
    },
  },
};
</script>

<style scoped>
.detail-container {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding-bottom: 120rpx;
}

/* 封面图 */
.cover-section {
  background-color: #ffffff;
}

.cover-swiper {
  height: 500rpx;
}

.cover-image {
  width: 100%;
  height: 100%;
}

/* 景点信息 */
.info-section {
  background-color: #ffffff;
  padding: 30rpx;
  margin-top: 20rpx;
}

.info-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-bottom: 20rpx;
  border-bottom: 1rpx solid #f0f0f0;
}

.name {
  font-size: 36rpx;
  font-weight: bold;
  color: #333333;
  flex: 1;
}

.rating-section {
  display: flex;
  align-items: center;
}

.rating-star {
  color: #ff9800;
  font-size: 28rpx;
  margin-right: 8rpx;
}

.rating-value {
  font-size: 32rpx;
  color: #ff9800;
  font-weight: bold;
  margin-right: 20rpx;
}

.rating-count {
  font-size: 24rpx;
  color: #999999;
}

.info-tags {
  display: flex;
  flex-wrap: wrap;
  padding: 20rpx 0;
}

.tag {
  display: inline-block;
  padding: 6rpx 16rpx;
  margin-right: 10rpx;
  margin-bottom: 10rpx;
  background-color: #e3f2fd;
  color: #00bcd4;
}

.price {
  color: #ff5722;
  font-weight: bold;
}

.info-row {
  padding: 15rpx 0;
  border-bottom: 1rpx solid #f5f5f5;
}

.info-row:last-child {
  border-bottom: none;
}

.label {
  font-size: 28rpx;
  color: #666666;
  margin-right: 20rpx;
}

.value {
  font-size: 28rpx;
  color: #333333;
  font-weight: 500;
}

.description {
  font-size: 28rpx;
  color: #666666;
  line-height: 1.6;
  margin-top: 10rpx;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
  font-size: 24rpx;
  border-radius: 6rpx;
}

.info-row {
  display: flex;
  padding: 15rpx 0;
  border-bottom: 1rpx solid #f5f5f5;
}

.info-row:last-child {
  border-bottom: none;
}

.label {
  font-size: 26rpx;
  color: #666666;
  width: 180rpx;
  flex-shrink: 0;
}

.value {
  font-size: 26rpx;
  color: #333333;
  flex: 1;
}

.price-row {
  margin-top: 20rpx;
  padding-top: 20rpx;
}

.price-value {
  font-size: 36rpx;
  color: #ff5722;
  font-weight: bold;
}

/* 介绍和注意事项 */
.desc-section,
.notes-section {
  background-color: #ffffff;
  padding: 30rpx;
  margin-top: 20rpx;
  border-radius: 16rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.section-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #2c3e50;
  margin-bottom: 24rpx;
  padding-bottom: 18rpx;
  border-bottom: 3rpx solid #00bcd4;
  display: flex;
  align-items: center;
}

.section-title::before {
  content: "";
  display: inline-block;
  width: 10rpx;
  height: 32rpx;
  background-color: #00bcd4;
  margin-right: 12rpx;
  border-radius: 5rpx;
}

.desc-text,
.notes-text {
  font-size: 28rpx;
  color: #555555;
  line-height: 2;
  text-indent: 56rpx;
  margin-bottom: 20rpx;
  display: block;
  white-space: pre-wrap;
}

.desc-text:last-child,
.notes-text:last-child {
  margin-bottom: 0;
}

.notes-text {
  color: #ff7043;
  font-weight: 500;
}

/* 门票类型 */
.tickets-section {
  background-color: #ffffff;
  padding: 30rpx;
  margin-top: 20rpx;
}

.tickets-list {
  margin-top: 20rpx;
}

.ticket-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 25rpx;
  border: 1rpx solid #f0f0f0;
  border-radius: 12rpx;
  margin-bottom: 20rpx;
}

.ticket-info {
  flex: 1;
}

.ticket-name {
  font-size: 28rpx;
  color: #333333;
  font-weight: 500;
  display: block;
  margin-bottom: 8rpx;
}

.ticket-desc {
  font-size: 24rpx;
  color: #999999;
}

.ticket-price {
  flex-shrink: 0;
}

.price {
  font-size: 36rpx;
  color: #ff5722;
  font-weight: bold;
}

/* 底部栏 */
.bottom-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background-color: #ffffff;
  padding: 20rpx 30rpx;
  box-shadow: 0 -2rpx 10rpx rgba(0, 0, 0, 0.1);
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

.btn-primary {
  background-color: #00bcd4;
  color: #ffffff;
}

/* AI 智能解读按钮 */
.ai-interpret-btn {
  width: 100%;
  padding: 24rpx 0;
  background-color: #e3f2fd;
  color: #00bcd4;
  border-radius: 12rpx;
  font-size: 28rpx;
  border: 2rpx solid #00bcd4;
  margin-top: 20rpx;
}

.ai-interpret-btn[disabled] {
  opacity: 0.6;
  background-color: #f5f5f5;
  border-color: #cccccc;
  color: #999999;
}

/* AI 解读区域 */
.ai-interpretation-section {
  background-color: #ffffff;
  padding: 30rpx;
  margin-top: 20rpx;
  border-radius: 12rpx;
  box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);
}

.ai-section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
}

.toggle-btn {
  font-size: 26rpx;
  color: #00bcd4;
  padding: 8rpx 20rpx;
  border: 1rpx solid #00bcd4;
  border-radius: 20rpx;
  background-color: #ffffff;
}

.ai-loading-view {
  text-align: center;
  padding: 40rpx 0;
}

.loading-text {
  color: #00bcd4;
  font-size: 28rpx;
}

.ai-error-view {
  text-align: center;
  padding: 40rpx 0;
}

.error-icon {
  font-size: 48rpx;
  display: block;
  margin-bottom: 20rpx;
}

.error-text {
  color: #ff5722;
  font-size: 26rpx;
  display: block;
  margin-bottom: 20rpx;
}

.retry-btn {
  background-color: #00bcd4;
  color: #ffffff;
  border-radius: 12rpx;
  padding: 16rpx 40rpx;
  font-size: 26rpx;
  border: none;
}

.ai-interpretation-content {
  max-height: 800rpx;
  overflow-y: auto;
}

.ai-item {
  margin-bottom: 30rpx;
  padding-bottom: 20rpx;
  border-bottom: 1rpx dashed #e0e0e0;
}

.ai-item:last-child {
  margin-bottom: 0;
  padding-bottom: 0;
  border-bottom: none;
}

.ai-item-title {
  font-size: 28rpx;
  font-weight: bold;
  color: #333333;
  display: block;
  margin-bottom: 15rpx;
}

.ai-item-text {
  font-size: 26rpx;
  color: #666666;
  line-height: 1.8;
  display: block;
  white-space: pre-wrap;
}
</style>
