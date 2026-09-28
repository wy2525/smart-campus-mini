<script setup lang="ts">
import { ref, onMounted } from "vue";
import { useRouter, useRoute } from "vue-router";
import { getGuideDetail } from "@/api/guide";
import type { Guide } from "@/types";

const router = useRouter();
const route = useRoute();

const loading = ref(false);
const guide = ref<Guide | null>(null);

const fetchDetail = async () => {
  loading.value = true;
  try {
    const id = parseInt(route.params.id as string);
    const res = await getGuideDetail(id);
    guide.value = res.data || res;
  } catch (error) {
    console.error("获取攻略详情失败:", error);
  } finally {
    loading.value = false;
  }
};

const auditStatusFormatter = (status: string) => {
  const map: Record<string, string> = {
    pending: "待审核",
    approved: "已通过",
    rejected: "已拒绝",
  };
  return map[status] || status;
};

const auditStatusTagType = (status: string) => {
  const map: Record<string, string> = {
    pending: "warning",
    approved: "success",
    rejected: "danger",
  };
  return map[status] || "info";
};

const statusFormatter = (status: number) => {
  return status === 1 ? "上架" : "下架";
};

onMounted(() => {
  fetchDetail();
});
</script>

<template>
  <div class="page-container">
    <el-card v-loading="loading" class="detail-card">
      <template #header>
        <div class="card-header">
          <span>攻略详情</span>
          <el-button @click="router.back()">返回</el-button>
        </div>
      </template>

      <div v-if="guide" class="guide-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="攻略ID">{{
            guide.id
          }}</el-descriptions-item>
          <el-descriptions-item label="标题">{{
            guide.title
          }}</el-descriptions-item>
          <el-descriptions-item label="作者">
            {{ guide.user?.nickname || "-" }}
          </el-descriptions-item>
          <el-descriptions-item label="作者手机号">
            {{ guide.user?.phone || "-" }}
          </el-descriptions-item>
          <el-descriptions-item label="审核状态">
            <el-tag :type="auditStatusTagType(guide.auditStatus)">
              {{ auditStatusFormatter(guide.auditStatus) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="上架状态">
            <el-tag :type="guide.status === 1 ? 'success' : 'danger'">
              {{ statusFormatter(guide.status) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="浏览量">{{
            guide.viewCount
          }}</el-descriptions-item>
          <el-descriptions-item label="点赞数">{{
            guide.likeCount
          }}</el-descriptions-item>
          <el-descriptions-item label="收藏数">{{
            guide.favoriteCount
          }}</el-descriptions-item>
          <el-descriptions-item label="评论数">{{
            guide.commentCount
          }}</el-descriptions-item>
          <el-descriptions-item label="是否推荐">
            <el-tag :type="guide.isRecommended ? 'success' : 'info'">
              {{ guide.isRecommended ? "是" : "否" }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="封面图" :span="2">
            <el-image
              v-if="guide.coverImage"
              :src="guide.coverImage"
              style="width: 200px"
              fit="cover"
              :preview-src-list="[guide.coverImage]"
            />
            <span v-else>-</span>
          </el-descriptions-item>
          <el-descriptions-item label="审核意见" :span="2">
            {{ guide.auditRemark || "-" }}
          </el-descriptions-item>
          <el-descriptions-item label="内容" :span="2">
            <div class="content" v-html="guide.content"></div>
          </el-descriptions-item>
          <el-descriptions-item label="创建时间" :span="2">
            {{ guide.createTime }}
          </el-descriptions-item>
          <el-descriptions-item label="更新时间" :span="2">
            {{ guide.updateTime }}
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </el-card>
  </div>
</template>

<style scoped>
.detail-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 600;
}

.guide-detail {
  padding: 20px 0;
}

.content {
  max-height: 500px;
  overflow-y: auto;
  line-height: 1.8;
}
</style>
