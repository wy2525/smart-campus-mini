<script setup lang="ts">
import { ref, reactive, onMounted } from "vue";
import { useRouter } from "vue-router";
import { ElMessage, ElMessageBox } from "element-plus";
import {
  getGuideList,
  approveGuide,
  rejectGuide,
  updateGuideStatus,
  deleteGuide,
} from "@/api/guide";
import type { Guide, PageRequest } from "@/types";

const router = useRouter();

const loading = ref(false);
const searchForm = reactive({
  keyword: "",
  auditStatus: "",
  status: undefined as number | undefined,
});

const tableData = ref<Guide[]>([]);
const total = ref(0);
const currentPage = ref(1);
const pageSize = ref(10);

const fetchGuideList = async () => {
  loading.value = true;
  try {
    const params: PageRequest = {
      page: currentPage.value - 1,
      size: pageSize.value,
      keyword: searchForm.keyword || undefined,
      auditStatus: searchForm.auditStatus || undefined,
      status: searchForm.status,
    };
    const res = await getGuideList(params);
    tableData.value = res.data.content;
    total.value = res.data.totalElements;
  } catch (error) {
    console.error("获取攻略列表失败:", error);
  } finally {
    loading.value = false;
  }
};

const handleSearch = () => {
  currentPage.value = 1;
  fetchGuideList();
};

const handleReset = () => {
  searchForm.keyword = "";
  searchForm.auditStatus = "";
  searchForm.status = undefined;
  currentPage.value = 1;
  fetchGuideList();
};

const handleDetail = (row: Guide) => {
  router.push(`/guides/${row.id}`);
};

const handleApprove = async (row: Guide) => {
  try {
    await approveGuide(row.id);
    ElMessage.success("审核通过");
    fetchGuideList();
  } catch (error) {
    ElMessage.error("审核失败");
  }
};

const handleReject = async (row: Guide) => {
  ElMessageBox.prompt("请输入拒绝原因", "审核拒绝", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    inputPattern: /\S/,
    inputErrorMessage: "请输入拒绝原因",
  })
    .then(async ({ value }) => {
      try {
        await rejectGuide(row.id, value);
        ElMessage.success("审核拒绝成功");
        fetchGuideList();
      } catch (error) {
        ElMessage.error("审核拒绝失败");
      }
    })
    .catch(() => {});
};

const handleStatusChange = async (row: Guide) => {
  try {
    await updateGuideStatus(row.id, row.status);
    ElMessage.success("状态更新成功");
  } catch (error) {
    row.status = row.status === 1 ? 0 : 1;
    ElMessage.error("状态更新失败");
  }
};

const handleDelete = (row: Guide) => {
  ElMessageBox.confirm(`确定要删除攻略 ${row.title} 吗？`, "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(async () => {
      try {
        await deleteGuide(row.id);
        ElMessage.success("删除成功");
        fetchGuideList();
      } catch (error) {
        ElMessage.error("删除失败");
      }
    })
    .catch(() => {});
};

const handlePageChange = (page: number) => {
  currentPage.value = page;
  fetchGuideList();
};

const handleSizeChange = (size: number) => {
  pageSize.value = size;
  currentPage.value = 1;
  fetchGuideList();
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

const statusFormatter = (row: Guide) => {
  return row.status === 1 ? "上架" : "下架";
};

onMounted(() => {
  fetchGuideList();
});
</script>

<template>
  <div class="page-container">
    <div class="content-card">
      <el-form :model="searchForm" inline class="search-form">
        <el-form-item label="关键词">
          <el-input
            v-model="searchForm.keyword"
            placeholder="标题/作者"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="审核状态">
          <el-select
            v-model="searchForm.auditStatus"
            placeholder="请选择状态"
            clearable
            style="width: 120px"
          >
            <el-option label="待审核" value="pending" />
            <el-option label="已通过" value="approved" />
            <el-option label="已拒绝" value="rejected" />
          </el-select>
        </el-form-item>
        <el-form-item label="上架状态">
          <el-select
            v-model="searchForm.status"
            placeholder="请选择状态"
            clearable
            style="width: 120px"
          >
            <el-option label="上架" :value="1" />
            <el-option label="下架" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>
            搜索
          </el-button>
          <el-button @click="handleReset">
            <el-icon><RefreshLeft /></el-icon>
            重置
          </el-button>
        </el-form-item>
      </el-form>

      <div class="table-container">
        <div class="table-header">
          <span class="table-title">攻略列表</span>
        </div>

        <el-table v-loading="loading" :data="tableData" border stripe>
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="id" label="ID" width="60" />
          <el-table-column
            prop="title"
            label="标题"
            min-width="200"
            show-overflow-tooltip
          />
          <el-table-column prop="user.nickname" label="作者" width="120" />
          <el-table-column label="审核状态" width="100">
            <template #default="{ row }">
              <el-tag :type="auditStatusTagType(row.auditStatus)">
                {{ auditStatusFormatter(row.auditStatus) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="上架状态" width="100" align="center">
            <template #default="{ row }">
              <el-switch
                v-model="row.status"
                :active-value="1"
                :inactive-value="0"
                @change="handleStatusChange(row)"
              />
            </template>
          </el-table-column>
          <el-table-column prop="viewCount" label="浏览量" width="80" />
          <el-table-column prop="likeCount" label="点赞数" width="80" />
          <el-table-column prop="commentCount" label="评论数" width="80" />
          <el-table-column prop="createTime" label="创建时间" width="180" />
          <el-table-column label="操作" width="280" fixed="right">
            <template #default="{ row }">
              <el-button
                type="primary"
                link
                size="small"
                @click="handleDetail(row)"
              >
                <el-icon><View /></el-icon>
                查看
              </el-button>
              <el-button
                v-if="row.auditStatus === 'pending'"
                type="success"
                link
                size="small"
                @click="handleApprove(row)"
              >
                <el-icon><Check /></el-icon>
                通过
              </el-button>
              <el-button
                v-if="row.auditStatus === 'pending'"
                type="danger"
                link
                size="small"
                @click="handleReject(row)"
              >
                <el-icon><Close /></el-icon>
                拒绝
              </el-button>
              <el-button
                type="danger"
                link
                size="small"
                @click="handleDelete(row)"
              >
                <el-icon><Delete /></el-icon>
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @current-change="handlePageChange"
          @size-change="handleSizeChange"
        />
      </div>
    </div>
  </div>
</template>

<style scoped>
.search-form {
  margin-bottom: 20px;
  padding: 20px;
  background: #ffffff;
  border-radius: 4px;
}

.table-container {
  margin-top: 20px;
}

.table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.table-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}
</style>
