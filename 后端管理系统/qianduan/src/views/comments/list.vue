<script setup lang="ts">
import { ref, reactive, onMounted } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import {
  getCommentList,
  updateCommentStatus,
  deleteComment,
  batchDeleteComments,
} from "@/api/comment";
import type { Comment, PageRequest } from "@/types";

const loading = ref(false);
const searchForm = reactive({
  keyword: "",
  status: undefined as number | undefined,
});

const tableData = ref<Comment[]>([]);
const total = ref(0);
const currentPage = ref(1);
const pageSize = ref(10);
const selectedRows = ref<Comment[]>([]);

const fetchCommentList = async () => {
  loading.value = true;
  try {
    const params: PageRequest = {
      page: currentPage.value - 1,
      size: pageSize.value,
      keyword: searchForm.keyword || undefined,
      status: searchForm.status,
    };
    const res = await getCommentList(params);
    tableData.value = res.data.content;
    total.value = res.data.totalElements;
  } catch (error) {
    console.error("获取评论列表失败:", error);
  } finally {
    loading.value = false;
  }
};

const handleSearch = () => {
  currentPage.value = 1;
  fetchCommentList();
};

const handleReset = () => {
  searchForm.keyword = "";
  searchForm.status = undefined;
  currentPage.value = 1;
  fetchCommentList();
};

const handleStatusChange = async (row: Comment) => {
  try {
    await updateCommentStatus(row.id, row.status);
    ElMessage.success("状态更新成功");
  } catch (error) {
    row.status = row.status === 1 ? 0 : 1;
    ElMessage.error("状态更新失败");
  }
};

const handleDelete = (row: Comment) => {
  ElMessageBox.confirm("确定要删除该评论吗？", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(async () => {
      try {
        await deleteComment(row.id);
        ElMessage.success("删除成功");
        fetchCommentList();
      } catch (error) {
        ElMessage.error("删除失败");
      }
    })
    .catch(() => {});
};

const handleBatchDelete = () => {
  if (selectedRows.value.length === 0) {
    ElMessage.warning("请选择要删除的评论");
    return;
  }
  ElMessageBox.confirm(
    `确定要删除选中的 ${selectedRows.value.length} 条评论吗？`,
    "提示",
    {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    },
  )
    .then(async () => {
      try {
        const ids = selectedRows.value.map((item) => item.id);
        await batchDeleteComments(ids);
        ElMessage.success("批量删除成功");
        fetchCommentList();
      } catch (error) {
        ElMessage.error("批量删除失败");
      }
    })
    .catch(() => {});
};

const handlePageChange = (page: number) => {
  currentPage.value = page;
  fetchCommentList();
};

const handleSizeChange = (size: number) => {
  pageSize.value = size;
  currentPage.value = 1;
  fetchCommentList();
};

const handleSelectionChange = (selection: Comment[]) => {
  selectedRows.value = selection;
};

const statusFormatter = (row: Comment) => {
  return row.status === 1 ? "显示" : "隐藏";
};

onMounted(() => {
  fetchCommentList();
});
</script>

<template>
  <div class="page-container">
    <div class="content-card">
      <el-form :model="searchForm" inline class="search-form">
        <el-form-item label="关键词">
          <el-input
            v-model="searchForm.keyword"
            placeholder="评论内容/用户昵称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-select
            v-model="searchForm.status"
            placeholder="请选择状态"
            clearable
            style="width: 120px"
          >
            <el-option label="显示" :value="1" />
            <el-option label="隐藏" :value="0" />
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
          <span class="table-title">评论列表</span>
          <el-button type="danger" size="small" @click="handleBatchDelete">
            <el-icon><Delete /></el-icon>
            批量删除
          </el-button>
        </div>

        <el-table
          v-loading="loading"
          :data="tableData"
          border
          stripe
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="55" />
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="id" label="ID" width="60" />
          <el-table-column prop="user.nickname" label="用户" width="120" />
          <el-table-column
            prop="content"
            label="评论内容"
            min-width="250"
            show-overflow-tooltip
          />
          <el-table-column prop="likeCount" label="点赞数" width="80" />
          <el-table-column label="状态" width="100" align="center">
            <template #default="{ row }">
              <el-switch
                v-model="row.status"
                :active-value="1"
                :inactive-value="0"
                @change="handleStatusChange(row)"
              />
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间" width="180" />
          <el-table-column label="操作" width="150" fixed="right">
            <template #default="{ row }">
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
