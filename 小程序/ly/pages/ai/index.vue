<template>
  <view class="ai-container">
    <!-- 聊天消息列表 -->
    <scroll-view
      class="message-list"
      scroll-y="true"
      :scroll-into-view="scrollIntoView"
    >
      <view
        class="message-item"
        :class="'message-' + message.role"
        v-for="(message, index) in messages"
        :key="index"
        :id="'msg-' + index"
      >
        <view class="message-avatar">
          <text class="avatar-icon">{{
            message.role === "user" ? "👤" : "🤖"
          }}</text>
        </view>
        <view class="message-content">
          <!-- Loading 状态 -->
          <text class="message-text" v-if="message.loading">正在思考...</text>
          <text class="message-text" v-else>{{ message.content }}</text>
        </view>
        <text class="message-time" v-if="!message.loading">{{
          formatTime(message.time)
        }}</text>
      </view>
    </scroll-view>

    <!-- 输入区域 -->
    <view class="input-section">
      <view class="input-bar">
        <input
          class="input-field"
          type="text"
          v-model="inputText"
          placeholder="输入您的问题..."
          @confirm="onSendTap"
          :disabled="isLoading"
        />
        <button class="send-btn" @tap="onSendTap" :disabled="isLoading">
          {{ isLoading ? "发送中" : "发送" }}
        </button>
        <button class="clear-btn" @tap="onClearContext">清空</button>
      </view>
      <view class="quick-questions">
        <text class="quick-label">快捷提问：</text>
        <scroll-view class="quick-scroll" scroll-x="true">
          <view class="quick-tags">
            <text
              class="quick-tag"
              v-for="(tag, index) in quickTags"
              :key="index"
              @tap="onQuickTagTap(tag)"
            >
              {{ tag }}
            </text>
          </view>
        </scroll-view>
        <view class="quick-questions" style="margin-top: 10rpx">
          <text class="quick-label">🗺️ 行程规划：</text>
          <scroll-view class="quick-scroll" scroll-x="true">
            <view class="quick-tags">
              <text
                class="quick-tag itinerary-tag"
                v-for="(itinerary, index) in itineraryTags"
                :key="index"
                @tap="onItineraryTap(itinerary)"
              >
                {{ itinerary }}
              </text>
            </view>
          </scroll-view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { chatWithKimi } from "@/api/index.js";
import { planItinerary } from "@/api/kimi.js";
import {
  addUserMessage,
  addAssistantMessage,
  getAPIMessages,
  clearAIContext,
} from "@/utils/ai-context.js";

export default {
  data() {
    return {
      messages: [
        {
          role: "ai",
          content: "您好！我是您的旅游助手，有什么可以帮您的吗？",
          time: new Date(),
        },
      ],
      inputText: "",
      scrollIntoView: "",
      quickTags: [
        "热门景点推荐",
        "景点特色介绍",
        "门票价格查询",
        "游玩路线规划",
        "最佳旅游时间",
        "交通出行方式",
        "当地美食推荐",
        "住宿建议",
        "注意事项提醒",
      ],
      itineraryTags: [
        "北京3日游",
        "上海2日游",
        "西安3日游",
        "成都3日游",
        "杭州2日游",
        "广州2日游",
        "深圳1日游",
        "重庆3日游",
      ],
      isLoading: false,
    };
  },
  onLoad() {
    // 从本地存储加载历史对话
    this.loadChatHistory();
  },
  methods: {
    /**
     * 发送消息
     */
    async onSendTap() {
      if (!this.inputText.trim()) return;
      if (this.isLoading) return;

      const userMessage = this.inputText.trim();

      // 添加用户消息到界面
      const userMsg = {
        role: "user",
        content: userMessage,
        time: new Date(),
      };
      this.messages.push(userMsg);

      // 保存用户消息到上下文
      addUserMessage(userMessage);

      // 清空输入框
      this.inputText = "";

      // 滚动到底部
      this.scrollToBottom();

      // 添加 AI Loading 消息
      const loadingIndex = this.messages.length;
      this.messages.push({
        role: "ai",
        content: "",
        time: new Date(),
        loading: true,
      });

      // 设置 loading 状态
      this.isLoading = true;

      // 滚动到底部显示 loading
      this.scrollToBottom();

      try {
        // 调用 Kimi API
        const history = getAPIMessages();
        // 移除最后一条用户消息（因为 chatWithKimi 会自动添加）
        const apiHistory = history.length > 0 ? history.slice(0, -1) : [];

        const aiResponse = await chatWithKimi(userMessage, apiHistory);

        // 移除 loading 消息
        this.messages.splice(loadingIndex, 1);

        // 添加 AI 回复到界面
        this.messages.push({
          role: "ai",
          content: aiResponse,
          time: new Date(),
        });

        // 保存 AI 回复到上下文
        addAssistantMessage(aiResponse);

        // 滚动到底部
        this.scrollToBottom();

        // 保存对话历史到本地存储
        this.saveChatHistory();
      } catch (error) {
        console.error("[AI] 调用失败:", error);

        // 移除 loading 消息
        this.messages.splice(loadingIndex, 1);

        // 添加错误提示消息
        let errorMessage = "抱歉，我遇到了一些问题，请稍后再试。";
        if (error.message) {
          errorMessage = `抱歉，${error.message}`;
        }

        this.messages.push({
          role: "ai",
          content: errorMessage,
          time: new Date(),
        });

        // 滚动到底部
        this.scrollToBottom();
      } finally {
        // 取消 loading 状态
        this.isLoading = false;
      }
    },

    /**
     * 清空对话历史
     */
    onClearContext() {
      uni.showModal({
        title: "确认清空",
        content: "确定要清空所有对话记录吗？",
        success: (res) => {
          if (res.confirm) {
            // 清空上下文
            clearAIContext();

            // 重置消息列表，保留欢迎语
            this.messages = [
              {
                role: "ai",
                content: "您好！我是您的旅游助手，有什么可以帮您的吗？",
                time: new Date(),
              },
            ];

            // 清空本地存储
            uni.removeStorageSync("chat_history");

            uni.showToast({
              title: "已清空对话",
              icon: "success",
            });
          }
        },
      });
    },

    /**
     * 快捷标签
     */
    onQuickTagTap(tag) {
      if (this.isLoading) {
        uni.showToast({
          title: "请等待当前消息发送完成",
          icon: "none",
        });
        return;
      }
      this.inputText = tag;
      this.onSendTap();
    },

    /**
     * 行程规划标签点击
     */
    async onItineraryTap(itinerary) {
      if (this.isLoading) {
        uni.showToast({
          title: "请等待当前消息发送完成",
          icon: "none",
        });
        return;
      }

      // 从标签中提取城市和天数
      const match = itinerary.match(/(.+?)(\d+)日游/);
      if (match) {
        const destination = match[1];
        const days = match[2];

        // 显示加载提示
        uni.showLoading({
          title: "AI 正在为您规划行程...",
          mask: true,
        });

        // 清空当前对话，创建新的行程规划会话
        this.messages = [
          {
            role: "ai",
            content: `好的，我来为您规划${destination}${days}日游的行程。请稍候...`,
            time: new Date(),
          },
        ];

        try {
          // 调用行程规划 API
          const itineraryPlan = await planItinerary({
            destination: destination,
            days: days,
            budget: "不限",
            travelers: "1",
            preferences: "自由行",
          });

          // 清空加载
          uni.hideLoading();

          // 添加 AI 回复到界面
          this.messages.push({
            role: "ai",
            content: itineraryPlan,
            time: new Date(),
          });

          // 保存到上下文
          addAssistantMessage(itineraryPlan);

          // 滚动到底部
          this.scrollToBottom();

          // 保存对话历史
          this.saveChatHistory();

          uni.showToast({
            title: "行程规划成功",
            icon: "success",
          });
        } catch (error) {
          uni.hideLoading();
          console.error("行程规划失败:", error);

          this.messages.push({
            role: "ai",
            content:
              "抱歉，行程规划失败，请稍后再试。您可以告诉我具体的出行需求，我会尽力为您提供帮助。",
            time: new Date(),
          });

          this.scrollToBottom();
        }
      }
    },

    /**
     * 格式化时间
     */
    formatTime(time) {
      if (!time) return "";
      const date = new Date(time);
      const hours = String(date.getHours()).padStart(2, "0");
      const minutes = String(date.getMinutes()).padStart(2, "0");
      return `${hours}:${minutes}`;
    },

    /**
     * 滚动到底部
     */
    scrollToBottom() {
      this.$nextTick(() => {
        this.scrollIntoView = "msg-" + (this.messages.length - 1);
      });
    },

    /**
     * 保存对话历史到本地存储
     */
    saveChatHistory() {
      try {
        // 只保存最近50条消息
        const messagesToSave = this.messages.slice(-50);
        uni.setStorageSync("chat_history", JSON.stringify(messagesToSave));
      } catch (error) {
        console.error("[AI] 保存对话历史失败:", error);
      }
    },

    /**
     * 从本地存储加载对话历史
     */
    loadChatHistory() {
      try {
        const historyData = uni.getStorageSync("chat_history");
        if (historyData) {
          const historyMessages = JSON.parse(historyData);

          // 过滤掉 loading 状态的消息
          const validMessages = historyMessages.filter((msg) => !msg.loading);

          // 如果有历史记录，替换欢迎消息
          if (validMessages.length > 0) {
            this.messages = validMessages;

            // 延迟滚动到底部
            setTimeout(() => {
              this.scrollToBottom();
            }, 300);
          }
        }
      } catch (error) {
        console.error("[AI] 加载对话历史失败:", error);
      }
    },
  },
};
</script>

<style scoped>
.ai-container {
  min-height: 100vh;
  background-color: #f5f5f5;
  display: flex;
  flex-direction: column;
}

/* 消息列表 */
.message-list {
  flex: 1;
  padding: 20rpx;
  padding-bottom: 220rpx;
}

.message-item {
  display: flex;
  margin-bottom: 20rpx;
}

.message-item.message-ai {
  justify-content: flex-start;
}

.message-item.message-user {
  justify-content: flex-end;
}

.message-avatar {
  width: 70rpx;
  height: 70rpx;
  border-radius: 35rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 36rpx;
  flex-shrink: 0;
}

.message-content {
  max-width: 70%;
  padding: 20rpx;
  border-radius: 12rpx;
}

.message-item.message-ai .message-content {
  background-color: #ffffff;
  color: #333333;
}

.message-item.message-user .message-content {
  background-color: #00bcd4;
  color: #ffffff;
}

.message-text {
  font-size: 28rpx;
  line-height: 1.6;
}

.message-time {
  font-size: 22rpx;
  color: #999999;
  margin-top: 8rpx;
  display: block;
}

/* 输入区域 */
.input-section {
  background-color: #ffffff;
  padding: 20rpx;
  border-top: 1rpx solid #e0e0e0;
}

.input-bar {
  display: flex;
  align-items: center;
  padding: 20rpx;
  background-color: #f5f5f5;
  border-radius: 12rpx;
}

.input-field {
  flex: 1;
  padding: 20rpx 30rpx;
  background-color: transparent;
  border: none;
  font-size: 28rpx;
}

.send-btn {
  padding: 20rpx 40rpx;
  margin-left: 20rpx;
  background-color: #00bcd4;
  color: #ffffff;
  border-radius: 12rpx;
  font-size: 28rpx;
  border: none;
}

.send-btn[disabled] {
  background-color: #cccccc;
  opacity: 0.6;
}

.clear-btn {
  padding: 20rpx 30rpx;
  margin-left: 10rpx;
  background-color: #ff6b6b;
  color: #ffffff;
  border-radius: 12rpx;
  font-size: 26rpx;
  border: none;
}

.quick-questions {
  padding: 20rpx;
}

.quick-label {
  font-size: 24rpx;
  color: #666666;
  margin-bottom: 15rpx;
  display: block;
}

.quick-scroll {
  white-space: nowrap;
}

.quick-tags {
  display: inline-flex;
}

.quick-tag {
  display: inline-block;
  padding: 12rpx 24rpx;
  margin-right: 15rpx;
  background-color: #e3f2fd;
  color: #00bcd4;
  font-size: 24rpx;
  border-radius: 24rpx;
  white-space: nowrap;
}

.itinerary-tag {
  background-color: #fff3e0;
  color: #ff9800;
}
</style>
