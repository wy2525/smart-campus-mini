<template>
  <view class="guide-detail-container">
    <!-- 顶部封面区域 -->
    <view class="cover-section">
      <image
        class="cover-image"
        :src="guide.coverImage || '/static/placeholder.png'"
        mode="aspectFill"
        @error="handleImageError"
      ></image>
      <view class="cover-overlay">
        <view class="top-bar">
          <view class="back-btn" @tap="goBack">
            <text class="back-icon">←</text>
          </view>
          <view class="action-btns">
            <view class="action-btn" @tap="onShare">
              <text class="action-icon">🔗</text>
            </view>
            <view class="action-btn" @tap="onMore">
              <text class="action-icon">⋮</text>
            </view>
          </view>
        </view>
        <view class="cover-gradient"></view>
      </view>
    </view>

    <!-- 攻略信息卡片 -->
    <view class="info-card">
      <text class="guide-title">{{ guide.title }}</text>
      
      <!-- 作者信息 -->
      <view class="author-row">
        <image
          class="author-avatar"
          :src="guide.user?.avatar || '/static/avatar-default.png'"
          mode="aspectFill"
          @error="handleImageError"
        ></image>
        <view class="author-info">
          <text class="author-name">{{ guide.user ? guide.user.nickname : "匿名用户" }}</text>
          <text class="publish-time">{{ formatTime(guide.createTime) }}</text>
        </view>
        <view class="follow-btn" v-if="guide.user && guide.user.id !== userInfo?.id">
          <text class="follow-text">+ 关注</text>
        </view>
      </view>

      <!-- 标签 -->
      <view class="tags-row" v-if="guide.tags">
        <view class="tag" v-for="(tag, index) in parseTags(guide.tags)" :key="index">
          <text class="tag-text">{{ tag }}</text>
        </view>
      </view>

      <!-- 浏览量 -->
      <view class="view-count">
        <text class="view-icon">👁️</text>
        <text class="view-text">{{ guide.viewCount || 0 }} 次浏览</text>
      </view>
    </view>

    <!-- 互动栏 -->
    <view class="interaction-bar">
      <view class="interaction-item" :class="{ active: isLiked }" @tap="onLike">
        <view class="interaction-icon-wrapper">
          <text class="interaction-icon">{{ isLiked ? '❤️' : '🤍' }}</text>
        </view>
        <text class="interaction-text">{{ guide.likeCount || 0 }}</text>
      </view>
      <view class="interaction-item" :class="{ active: isFavorited }" @tap="onFavorite">
        <view class="interaction-icon-wrapper">
          <text class="interaction-icon">{{ isFavorited ? '⭐' : '☆' }}</text>
        </view>
        <text class="interaction-text">{{ guide.favoriteCount || 0 }}</text>
      </view>
      <view class="interaction-item" @tap="scrollToComments">
        <view class="interaction-icon-wrapper">
          <text class="interaction-icon">💬</text>
        </view>
        <text class="interaction-text">{{ guide.commentCount || 0 }}</text>
      </view>
      <view class="interaction-item" @tap="onShare">
        <view class="interaction-icon-wrapper">
          <text class="interaction-icon">📤</text>
        </view>
        <text class="interaction-text">分享</text>
      </view>
    </view>

    <!-- 攻略内容 -->
    <view class="content-section">
      <view class="content-card">
        <view class="content-body">
          <text class="content-text">{{ guide.content }}</text>
        </view>
        
        <!-- 景点信息 -->
        <view class="attraction-card" v-if="guide.attraction" @tap="goToAttraction(guide.attraction)">
          <image
            class="attraction-cover"
            :src="guide.attraction.coverImage || '/static/placeholder.png'"
            mode="aspectFill"
          ></image>
          <view class="attraction-info">
            <text class="attraction-name">{{ guide.attraction.name }}</text>
            <view class="attraction-meta">
              <view class="meta-item">
                <text class="meta-icon">📍</text>
                <text class="meta-text">{{ guide.attraction.address || '未知位置' }}</text>
              </view>
              <view class="meta-item">
                <text class="meta-icon">⭐</text>
                <text class="meta-text">{{ guide.attraction.rating || 0 }}分</text>
              </view>
            </view>
            <view class="attraction-tags">
              <text class="attraction-tag" v-if="guide.attraction.category">{{ guide.attraction.category }}</text>
              <text class="attraction-tag price" v-if="guide.attraction.minPrice">
                ¥{{ guide.attraction.minPrice }}起
              </text>
            </view>
          </view>
          <view class="go-btn">
            <text class="go-text">去游玩</text>
          </view>
        </view>

        <!-- 实用信息 -->
        <view class="tips-section" v-if="guide.tips">
          <view class="tips-header">
            <text class="tips-icon">💡</text>
            <text class="tips-title">实用贴士</text>
          </view>
          <text class="tips-content">{{ guide.tips }}</text>
        </view>
      </view>
    </view>

    <!-- 相关推荐 -->
    <view class="related-section" v-if="relatedGuides.length > 0">
      <view class="section-header">
        <text class="section-title">相关攻略</text>
        <text class="section-more">查看更多 ></text>
      </view>
      <scroll-view class="related-scroll" scroll-x="true">
        <view class="related-list">
          <view class="related-item" v-for="item in relatedGuides" :key="item.id" @tap="goToGuide(item.id)">
            <image class="related-cover" :src="item.coverImage || '/static/placeholder.png'" mode="aspectFill"></image>
            <text class="related-title">{{ item.title }}</text>
            <view class="related-meta">
              <text class="related-author">{{ item.user?.nickname || '匿名' }}</text>
              <text class="related-likes">❤️ {{ item.likeCount || 0 }}</text>
            </view>
          </view>
        </view>
      </scroll-view>
    </view>

    <!-- 评论区 -->
    <view class="comments-section" id="comments">
      <view class="comments-header">
        <text class="comments-title">评论</text>
        <text class="comments-count">({{ comments.length }})</text>
      </view>

      <!-- 评论输入框 -->
      <view class="comment-input-card">
        <image
          class="comment-user-avatar"
          :src="userInfo?.avatar || '/static/avatar-default.png'"
          mode="aspectFill"
        ></image>
        <view class="comment-input-wrapper">
          <input
            class="comment-input"
            type="text"
            v-model="commentContent"
            placeholder="写下你的评论..."
            placeholder-class="comment-placeholder"
            @confirm="onSubmitComment"
          />
          <button 
            class="comment-submit-btn"
            :disabled="!commentContent.trim()"
            @tap="onSubmitComment"
          >
            <text class="submit-icon">➤</text>
          </button>
        </view>
      </view>

      <!-- 评论列表 -->
      <view class="comments-list">
        <view v-if="loadingComments" class="loading-state">
          <text class="loading-text">加载评论中...</text>
        </view>
        <view v-else-if="comments.length === 0" class="empty-state">
          <text class="empty-icon">💬</text>
          <text class="empty-text">暂无评论，快来抢沙发吧！</text>
        </view>
        <view v-else class="comment-items">
          <view
            class="comment-item"
            v-for="(comment, index) in comments"
            :key="comment.id"
          >
            <image
              class="comment-avatar"
              :src="comment.user?.avatar || '/static/avatar-default.png'"
              mode="aspectFill"
              @error="handleImageError"
            ></image>
            <view class="comment-body">
              <view class="comment-header">
                <view class="comment-user-info">
                  <text class="comment-author">{{ comment.user ? comment.user.nickname : "匿名用户" }}</text>
                  <text class="comment-time">{{ formatTime(comment.createTime) }}</text>
                </view>
                <view class="comment-like" @tap="onLikeComment(comment)">
                  <text class="like-icon">{{ comment.isLiked ? '❤️' : '🤍' }}</text>
                  <text class="like-count">{{ comment.likeCount || 0 }}</text>
                </view>
              </view>
              <text class="comment-text">{{ comment.content }}</text>
              
              <!-- 回复区域 -->
              <view class="reply-section" v-if="comment.replies && comment.replies.length > 0">
                <view class="reply-item" v-for="reply in comment.replies" :key="reply.id">
                  <text class="reply-author">{{ reply.user?.nickname || '匿名' }}:</text>
                  <text class="reply-content">{{ reply.content }}</text>
                </view>
              </view>

              <!-- 评论操作 -->
              <view class="comment-actions">
                <view class="action-item" @tap="onReplyComment(comment)">
                  <text class="action-icon">💬</text>
                  <text class="action-text">回复</text>
                </view>
              </view>

              <!-- 回复输入框 -->
              <view class="reply-input-card" v-if="replyTo?.id === comment.id">
                <input
                  class="reply-input"
                  type="text"
                  v-model="replyContent"
                  placeholder="写下你的回复..."
                  placeholder-class="reply-placeholder"
                  @confirm="onSubmitReply"
                  auto-focus
                />
                <view class="reply-btns">
                  <view class="reply-cancel-btn" @tap="cancelReply">
                    <text>取消</text>
                  </view>
                  <view class="reply-confirm-btn" :class="{ disabled: !replyContent.trim() }" @tap="onSubmitReply">
                    <text>发送</text>
                  </view>
                </view>
              </view>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 底部操作栏 -->
    <view class="bottom-bar">
      <view class="bottom-input" @tap="focusComment">
        <text class="bottom-input-icon">💬</text>
        <text class="bottom-input-placeholder">说点什么...</text>
      </view>
      <view class="bottom-actions">
        <view class="bottom-action" :class="{ active: isLiked }" @tap="onLike">
          <text class="bottom-action-icon">{{ isLiked ? '❤️' : '🤍' }}</text>
          <text class="bottom-action-text">{{ guide.likeCount || 0 }}</text>
        </view>
        <view class="bottom-action" :class="{ active: isFavorited }" @tap="onFavorite">
          <text class="bottom-action-icon">{{ isFavorited ? '⭐' : '☆' }}</text>
          <text class="bottom-action-text">{{ guide.favoriteCount || 0 }}</text>
        </view>
        <view class="bottom-action" @tap="onShare">
          <text class="bottom-action-icon">📤</text>
          <text class="bottom-action-text">分享</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import {
  getGuideDetail,
  getComments,
  createComment,
  likeGuide,
  favoriteGuide,
  likeComment,
} from "@/api/index.js";
import { errorHandler } from "@/utils/error-handler.js";

export default {
  data() {
    return {
      guide: {
        id: 0,
        title: "",
        content: "",
        coverImage: "",
        createTime: "",
        viewCount: 0,
        likeCount: 0,
        favoriteCount: 0,
        commentCount: 0,
        tags: "",
        tips: "",
        user: null,
        attraction: null,
      },
      relatedGuides: [],
      loading: true,
      loadingComments: false,
      error: null,
      isLiked: false,
      isFavorited: false,
      commentContent: "",
      comments: [],
      userInfo: null,
      replyTo: null,
      replyContent: "",
      showReplyInput: false,
    };
  },
  onLoad(options) {
    let id = options.id;
    if (typeof id === "object" && id !== null) {
      id = id.id || id.toString();
    }

    if (id) {
      this.loadGuideDetail(id);
    } else {
      uni.showToast({ title: "攻略ID无效", icon: "none" });
      setTimeout(() => uni.navigateBack(), 1500);
    }
  },
  onShareAppMessage() {
    return {
      title: this.guide.title,
      path: `/pages/community/detail?id=${this.guide.id}`,
      imageUrl: this.guide.coverImage,
    };
  },
  methods: {
    goBack() {
      uni.navigateBack();
    },

    handleImageError(event) {
      event.target.src = "/static/placeholder.png";
    },

    parseTags(tagsStr) {
      if (!tagsStr) return [];
      return tagsStr.split(",").map((tag) => tag.trim()).filter((tag) => tag);
    },

    formatTime(time) {
      if (!time) return "";
      const date = new Date(time);
      const now = new Date();
      const diff = (now - date) / 1000;

      if (diff < 60) return "刚刚";
      if (diff < 3600) return `${Math.floor(diff / 60)}分钟前`;
      if (diff < 86400) return `${Math.floor(diff / 3600)}小时前`;
      if (diff < 2592000) return `${Math.floor(diff / 86400)}天前`;
      
      return date.toLocaleDateString("zh-CN", {
        year: "numeric",
        month: "2-digit",
        day: "2-digit",
      });
    },

    async loadGuideDetail(id) {
      try {
        this.loading = true;
        const res = await getGuideDetail(id);
        if (res && res.data) {
          this.guide = res.data;
          this.loadComments();
          this.loadUserInfo();
          this.loadRelatedGuides();
        }
      } catch (error) {
        this.error = errorHandler.handleGuideDetailError(error, id).message;
        uni.showToast({ title: this.error, icon: "none" });
        setTimeout(() => uni.navigateBack(), 2000);
      } finally {
        this.loading = false;
      }
    },

    loadUserInfo() {
      const userInfo = uni.getStorageSync("userInfo");
      if (userInfo) {
        this.userInfo = userInfo;
      }
    },

    async loadComments() {
      try {
        this.loadingComments = true;
        const res = await getComments(this.guide.id);
        if (res) {
          this.comments = res;
        }
      } catch (error) {
        console.error("加载评论失败:", error);
      } finally {
        this.loadingComments = false;
      }
    },

    loadRelatedGuides() {
      // 模拟加载相关攻略
      this.relatedGuides = [
        { id: 1, title: "三亚自由行攻略", coverImage: "", user: { nickname: "旅行达人" }, likeCount: 128 },
        { id: 2, title: "厦门美食探店", coverImage: "", user: { nickname: "吃货一枚" }, likeCount: 89 },
        { id: 3, title: "云南大理之旅", coverImage: "", user: { nickname: "摄影师" }, likeCount: 256 },
      ];
    },

    focusComment() {
      // 滚动到评论区
      this.scrollToComments();
    },

    scrollToComments() {
      uni.pageScrollTo({ selector: "#comments", duration: 300 });
    },

    async onSubmitComment() {
      if (!this.commentContent.trim()) return;
      if (!this.userInfo) {
        uni.showToast({ title: "请先登录", icon: "none" });
        return;
      }

      try {
        const res = await createComment({
          guideId: this.guide.id,
          userId: this.userInfo.id,
          content: this.commentContent.trim(),
        });

        if (res && res.data) {
          this.commentContent = "";
          this.comments.unshift(res.data);
          this.guide.commentCount++;
          uni.showToast({ title: "评论成功", icon: "success" });
        }
      } catch (error) {
        uni.showToast({ title: "评论失败", icon: "none" });
      }
    },

    async onLike() {
      if (!this.userInfo) {
        uni.showToast({ title: "请先登录", icon: "none" });
        return;
      }

      try {
        await likeGuide(this.guide.id);
        this.isLiked = !this.isLiked;
        this.guide.likeCount += this.isLiked ? 1 : -1;
      } catch (error) {
        uni.showToast({ title: "操作失败", icon: "none" });
      }
    },

    async onFavorite() {
      if (!this.userInfo) {
        uni.showToast({ title: "请先登录", icon: "none" });
        return;
      }

      try {
        await favoriteGuide(this.guide.id);
        this.isFavorited = !this.isFavorited;
        this.guide.favoriteCount += this.isFavorited ? 1 : -1;
        
        // 保存到本地存储（用于后端未启动时显示）
        let favoriteGuideIds = uni.getStorageSync('favoriteGuideIds') || [];
        if (this.isFavorited) {
          if (!favoriteGuideIds.includes(this.guide.id)) {
            favoriteGuideIds.push(this.guide.id);
          }
        } else {
          favoriteGuideIds = favoriteGuideIds.filter(id => id !== this.guide.id);
        }
        uni.setStorageSync('favoriteGuideIds', favoriteGuideIds);
        
        uni.showToast({
          title: this.isFavorited ? "收藏成功" : "取消收藏",
          icon: "success",
        });
      } catch (error) {
        // 后端未启动时使用本地存储模拟
        console.warn("后端连接失败，使用本地存储模拟收藏:", error);
        
        this.isFavorited = !this.isFavorited;
        this.guide.favoriteCount += this.isFavorited ? 1 : -1;
        
        // 保存到本地存储
        let favoriteGuideIds = uni.getStorageSync('favoriteGuideIds') || [];
        if (this.isFavorited) {
          if (!favoriteGuideIds.includes(this.guide.id)) {
            favoriteGuideIds.push(this.guide.id);
          }
        } else {
          favoriteGuideIds = favoriteGuideIds.filter(id => id !== this.guide.id);
        }
        uni.setStorageSync('favoriteGuideIds', favoriteGuideIds);
        
        uni.showToast({
          title: this.isFavorited ? "收藏成功（本地）" : "取消收藏",
          icon: "success",
        });
      }
    },

    onShare() {
      uni.showShareMenu({
        withShareTicket: true,
        menus: ["shareAppMessage", "shareTimeline"],
      });
    },

    onMore() {
      uni.showActionSheet({
        itemList: ["收藏", "举报", "复制链接"],
        success: (res) => {
          switch (res.tapIndex) {
            case 0:
              this.onFavorite();
              break;
            case 1:
              uni.showToast({ title: "举报功能开发中", icon: "none" });
              break;
            case 2:
              uni.setClipboardData({ data: window.location.href });
              break;
          }
        },
      });
    },

    async onLikeComment(comment) {
      if (!this.userInfo) {
        uni.showToast({ title: "请先登录", icon: "none" });
        return;
      }

      try {
        await likeComment(comment.id);
        comment.isLiked = !comment.isLiked;
        comment.likeCount += comment.isLiked ? 1 : -1;
      } catch (error) {
        uni.showToast({ title: "操作失败", icon: "none" });
      }
    },

    onReplyComment(comment) {
      if (!this.userInfo) {
        uni.showToast({ title: "请先登录", icon: "none" });
        return;
      }
      this.replyTo = comment;
      this.replyContent = "";
    },

    cancelReply() {
      this.replyTo = null;
      this.replyContent = "";
    },

    async onSubmitReply() {
      if (!this.replyContent.trim()) return;

      // 模拟回复功能
      const mockReply = {
        id: Date.now(),
        content: this.replyContent.trim(),
        createTime: new Date().toISOString(),
        likeCount: 0,
        isLiked: false,
        user: this.userInfo,
      };

      if (!this.replyTo.replies) {
        this.replyTo.replies = [];
      }
      this.replyTo.replies.push(mockReply);
      
      uni.showToast({ title: "回复成功", icon: "success" });
      this.cancelReply();
    },

    goToAttraction(attraction) {
      uni.navigateTo({
        url: `/pages/attractions/detail?id=${attraction.id}`,
      });
    },

    goToGuide(id) {
      uni.navigateTo({
        url: `/pages/community/detail?id=${id}`,
      });
    },
  },
};
</script>

<style scoped>
.guide-detail-container {
  min-height: 100vh;
  background: linear-gradient(180deg, #f8f9ff 0%, #ffffff 100%);
  padding-bottom: 140rpx;
}

/* 封面区域 */
.cover-section {
  position: relative;
  height: 500rpx;
}

.cover-image {
  width: 100%;
  height: 100%;
}

.cover-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
}

.top-bar {
  position: absolute;
  top: 60rpx;
  left: 0;
  right: 0;
  padding: 0 30rpx;
  display: flex;
  justify-content: space-between;
  align-items: center;
  z-index: 10;
}

.back-btn {
  width: 70rpx;
  height: 70rpx;
  background: rgba(0, 0, 0, 0.4);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  backdrop-filter: blur(10rpx);
}

.back-icon {
  font-size: 36rpx;
  color: #ffffff;
}

.action-btns {
  display: flex;
  gap: 20rpx;
}

.action-btn {
  width: 70rpx;
  height: 70rpx;
  background: rgba(0, 0, 0, 0.4);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  backdrop-filter: blur(10rpx);
}

.action-icon {
  font-size: 32rpx;
  color: #ffffff;
}

.cover-gradient {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 200rpx;
  background: linear-gradient(transparent, rgba(0, 0, 0, 0.5));
}

/* 信息卡片 */
.info-card {
  background: #ffffff;
  border-radius: 40rpx 40rpx 0 0;
  margin-top: -60rpx;
  position: relative;
  padding: 50rpx 40rpx;
  box-shadow: 0 -10rpx 40rpx rgba(0, 0, 0, 0.05);
}

.guide-title {
  font-size: 44rpx;
  font-weight: bold;
  color: #1a1a2e;
  line-height: 1.4;
  margin-bottom: 30rpx;
  display: block;
}

.author-row {
  display: flex;
  align-items: center;
  margin-bottom: 24rpx;
}

.author-avatar {
  width: 80rpx;
  height: 80rpx;
  border-radius: 50%;
  margin-right: 20rpx;
  border: 4rpx solid #f0f0ff;
}

.author-info {
  flex: 1;
}

.author-name {
  font-size: 30rpx;
  font-weight: 600;
  color: #333;
  display: block;
}

.publish-time {
  font-size: 24rpx;
  color: #999;
  margin-top: 6rpx;
  display: block;
}

.follow-btn {
  padding: 12rpx 30rpx;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 30rpx;
}

.follow-text {
  font-size: 26rpx;
  color: #ffffff;
  font-weight: 500;
}

.tags-row {
  display: flex;
  flex-wrap: wrap;
  gap: 16rpx;
  margin-bottom: 24rpx;
}

.tag {
  padding: 10rpx 24rpx;
  background: linear-gradient(135deg, #e8f4ff 0%, #f0e8ff 100%);
  border-radius: 20rpx;
}

.tag-text {
  font-size: 24rpx;
  color: #667eea;
}

.view-count {
  display: flex;
  align-items: center;
  gap: 10rpx;
}

.view-icon {
  font-size: 28rpx;
}

.view-text {
  font-size: 26rpx;
  color: #999;
}

/* 互动栏 */
.interaction-bar {
  display: flex;
  justify-content: space-around;
  background: #ffffff;
  padding: 24rpx 0;
  margin: 0 40rpx;
  border-radius: 24rpx;
  box-shadow: 0 8rpx 30rpx rgba(0, 0, 0, 0.08);
  margin-top: -20rpx;
  position: relative;
  z-index: 5;
}

.interaction-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8rpx;
  padding: 16rpx 30rpx;
  border-radius: 16rpx;
  transition: all 0.3s ease;
}

.interaction-item:active {
  background: #f5f5f5;
  transform: scale(0.95);
}

.interaction-item.active .interaction-icon {
  transform: scale(1.2);
}

.interaction-icon-wrapper {
  width: 60rpx;
  height: 60rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}

.interaction-icon {
  font-size: 44rpx;
  transition: transform 0.3s ease;
}

.interaction-text {
  font-size: 24rpx;
  color: #666;
}

/* 内容区域 */
.content-section {
  padding: 40rpx;
}

.content-card {
  background: #ffffff;
  border-radius: 24rpx;
  padding: 40rpx;
  box-shadow: 0 8rpx 30rpx rgba(0, 0, 0, 0.05);
}

.content-text {
  font-size: 32rpx;
  color: #333;
  line-height: 2;
  white-space: pre-wrap;
  text-indent: 64rpx;
}

/* 景点卡片 */
.attraction-card {
  display: flex;
  align-items: center;
  background: linear-gradient(135deg, #f8f9ff 0%, #ffffff 100%);
  border-radius: 20rpx;
  padding: 24rpx;
  margin-top: 40rpx;
  border: 2rpx solid #e8e8ff;
}

.attraction-cover {
  width: 160rpx;
  height: 160rpx;
  border-radius: 16rpx;
  margin-right: 20rpx;
  flex-shrink: 0;
}

.attraction-info {
  flex: 1;
  min-width: 0;
}

.attraction-name {
  font-size: 32rpx;
  font-weight: bold;
  color: #1a1a2e;
  margin-bottom: 12rpx;
  display: block;
}

.attraction-meta {
  display: flex;
  flex-direction: column;
  gap: 8rpx;
  margin-bottom: 12rpx;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 8rpx;
}

.meta-icon {
  font-size: 24rpx;
}

.meta-text {
  font-size: 24rpx;
  color: #666;
}

.attraction-tags {
  display: flex;
  gap: 12rpx;
}

.attraction-tag {
  padding: 8rpx 16rpx;
  background: #f0f0ff;
  border-radius: 12rpx;
  font-size: 22rpx;
  color: #667eea;
}

.attraction-tag.price {
  background: linear-gradient(135deg, #fff3e0 0%, #ffe0b2 100%);
  color: #ff6b35;
}

.go-btn {
  padding: 16rpx 24rpx;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 24rpx;
}

.go-text {
  font-size: 24rpx;
  color: #ffffff;
  font-weight: 500;
}

/* 实用贴士 */
.tips-section {
  margin-top: 40rpx;
  padding: 30rpx;
  background: linear-gradient(135deg, #fff9e6 0%, #fff3cd 100%);
  border-radius: 20rpx;
  border-left: 8rpx solid #ffc107;
}

.tips-header {
  display: flex;
  align-items: center;
  gap: 12rpx;
  margin-bottom: 16rpx;
}

.tips-icon {
  font-size: 36rpx;
}

.tips-title {
  font-size: 30rpx;
  font-weight: bold;
  color: #856404;
}

.tips-content {
  font-size: 28rpx;
  color: #666;
  line-height: 1.8;
}

/* 相关推荐 */
.related-section {
  padding: 0 40rpx 40rpx;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24rpx;
}

.section-title {
  font-size: 36rpx;
  font-weight: bold;
  color: #1a1a2e;
}

.section-more {
  font-size: 26rpx;
  color: #667eea;
}

.related-scroll {
  white-space: nowrap;
}

.related-list {
  display: inline-flex;
  gap: 24rpx;
}

.related-item {
  display: inline-block;
  width: 280rpx;
  background: #ffffff;
  border-radius: 20rpx;
  overflow: hidden;
  box-shadow: 0 8rpx 24rpx rgba(0, 0, 0, 0.08);
}

.related-cover {
  width: 100%;
  height: 180rpx;
}

.related-title {
  font-size: 28rpx;
  color: #333;
  font-weight: 500;
  padding: 16rpx 16rpx 0;
  display: block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.related-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12rpx 16rpx;
}

.related-author {
  font-size: 24rpx;
  color: #999;
}

.related-likes {
  font-size: 24rpx;
  color: #ff6b6b;
}

/* 评论区 */
.comments-section {
  padding: 40rpx;
  background: #ffffff;
  margin: 0 40rpx 40rpx;
  border-radius: 24rpx;
  box-shadow: 0 8rpx 30rpx rgba(0, 0, 0, 0.05);
}

.comments-header {
  display: flex;
  align-items: center;
  gap: 12rpx;
  margin-bottom: 30rpx;
}

.comments-title {
  font-size: 36rpx;
  font-weight: bold;
  color: #1a1a2e;
}

.comments-count {
  font-size: 28rpx;
  color: #999;
}

/* 评论输入框 */
.comment-input-card {
  display: flex;
  align-items: center;
  gap: 20rpx;
  padding: 24rpx;
  background: linear-gradient(135deg, #f8f9ff 0%, #f0f0ff 100%);
  border-radius: 20rpx;
  margin-bottom: 40rpx;
}

.comment-user-avatar {
  width: 72rpx;
  height: 72rpx;
  border-radius: 50%;
  flex-shrink: 0;
}

.comment-input-wrapper {
  flex: 1;
  display: flex;
  align-items: center;
  background: #ffffff;
  border-radius: 36rpx;
  padding: 0 24rpx;
  border: 2rpx solid #e8e8ff;
}

.comment-input {
  flex: 1;
  height: 80rpx;
  font-size: 28rpx;
  color: #333;
  border: none;
  outline: none;
}

.comment-placeholder {
  color: #a0aec0;
}

.comment-submit-btn {
  width: 64rpx;
  height: 64rpx;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  border: none;
}

.comment-submit-btn:disabled {
  opacity: 0.5;
}

.submit-icon {
  font-size: 28rpx;
  color: #ffffff;
}

/* 评论列表 */
.loading-state,
.empty-state {
  text-align: center;
  padding: 60rpx 0;
}

.loading-text,
.empty-text {
  font-size: 28rpx;
  color: #999;
}

.empty-icon {
  font-size: 72rpx;
  display: block;
  margin-bottom: 20rpx;
}

.comment-items {
  display: flex;
  flex-direction: column;
  gap: 32rpx;
}

.comment-item {
  display: flex;
  gap: 20rpx;
}

.comment-avatar {
  width: 72rpx;
  height: 72rpx;
  border-radius: 50%;
  flex-shrink: 0;
}

.comment-body {
  flex: 1;
}

.comment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12rpx;
}

.comment-user-info {
  display: flex;
  flex-direction: column;
}

.comment-author {
  font-size: 28rpx;
  font-weight: 600;
  color: #333;
}

.comment-time {
  font-size: 22rpx;
  color: #999;
}

.comment-like {
  display: flex;
  align-items: center;
  gap: 8rpx;
}

.like-icon {
  font-size: 32rpx;
}

.like-count {
  font-size: 24rpx;
  color: #666;
}

.comment-text {
  font-size: 28rpx;
  color: #333;
  line-height: 1.8;
  display: block;
}

/* 回复区域 */
.reply-section {
  margin-top: 16rpx;
  padding: 20rpx;
  background: #f8f9fa;
  border-radius: 16rpx;
}

.reply-item {
  margin-bottom: 12rpx;
  font-size: 26rpx;
  line-height: 1.6;
}

.reply-item:last-child {
  margin-bottom: 0;
}

.reply-author {
  color: #667eea;
  font-weight: 500;
}

.reply-content {
  color: #333;
}

/* 评论操作 */
.comment-actions {
  display: flex;
  gap: 32rpx;
  margin-top: 16rpx;
}

.action-item {
  display: flex;
  align-items: center;
  gap: 8rpx;
}

.action-icon {
  font-size: 28rpx;
}

.action-text {
  font-size: 26rpx;
  color: #999;
}

/* 回复输入框 */
.reply-input-card {
  margin-top: 20rpx;
  padding: 20rpx;
  background: #f8f9fa;
  border-radius: 16rpx;
}

.reply-input {
  width: 100%;
  height: 72rpx;
  background: #ffffff;
  border-radius: 36rpx;
  padding: 0 24rpx;
  font-size: 26rpx;
  color: #333;
  border: 2rpx solid #e8e8ff;
  outline: none;
  margin-bottom: 16rpx;
}

.reply-placeholder {
  color: #a0aec0;
}

.reply-btns {
  display: flex;
  justify-content: flex-end;
  gap: 16rpx;
}

.reply-cancel-btn {
  padding: 12rpx 32rpx;
  background: #f0f0f0;
  border-radius: 20rpx;
  font-size: 26rpx;
  color: #666;
}

.reply-confirm-btn {
  padding: 12rpx 32rpx;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 20rpx;
  font-size: 26rpx;
  color: #ffffff;
}

.reply-confirm-btn.disabled {
  opacity: 0.5;
}

/* 底部操作栏 */
.bottom-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20rpx 40rpx;
  padding-bottom: calc(20rpx + env(safe-area-inset-bottom));
  background: #ffffff;
  box-shadow: 0 -8rpx 30rpx rgba(0, 0, 0, 0.1);
  z-index: 100;
}

.bottom-input {
  flex: 1;
  display: flex;
  align-items: center;
  background: #f5f5f5;
  border-radius: 36rpx;
  padding: 20rpx 30rpx;
}

.bottom-input-icon {
  font-size: 32rpx;
  margin-right: 16rpx;
}

.bottom-input-placeholder {
  font-size: 28rpx;
  color: #999;
}

.bottom-actions {
  display: flex;
  gap: 40rpx;
  margin-left: 30rpx;
}

.bottom-action {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 6rpx;
}

.bottom-action.active .bottom-action-icon {
  transform: scale(1.2);
}

.bottom-action-icon {
  font-size: 40rpx;
  transition: transform 0.3s ease;
}

.bottom-action-text {
  font-size: 22rpx;
  color: #999;
}
</style>
