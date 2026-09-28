<script setup lang="ts">
import { ref, reactive, onMounted } from "vue";
import { useRouter } from "vue-router";
import { ElMessage, ElMessageBox } from "element-plus";
import { getUserList, updateUserStatus, deleteUser } from "@/api/user";
import type { User, PageRequest } from "@/types";

const router = useRouter();

// 加载状态
const loading = ref(false);

// 搜索表单
const searchForm = reactive({
  keyword: "",
  status: undefined as number | undefined,
});

// 表格数据
const tableData = ref<User[]>([]);
const total = ref(0);
const currentPage = ref(1);
const pageSize = ref(10);

// 选中的行
const selectedRows = ref<User[]>([]);

// 获取用户列表
const fetchUserList = async () => {
  loading.value = true;
  try {
    const params: PageRequest = {
      page: currentPage.value - 1,
      size: pageSize.value,
      keyword: searchForm.keyword || undefined,
      status: searchForm.status,
    };
    const res = await getUserList(params);
    tableData.value = res.data.content;
    total.value = res.data.totalElements;
  } catch (error) {
    console.error("获取用户列表失败:", error);
  } finally {
    loading.value = false;
  }
};

// 搜索
const handleSearch = () => {
  currentPage.value = 1;
  fetchUserList();
};

// 重置
const handleReset = () => {
  searchForm.keyword = "";
  searchForm.status = undefined;
  currentPage.value = 1;
  fetchUserList();
};

// 查看详情
const handleDetail = (row: User) => {
  router.push(`/users/${row.id}`);
};

// 更新状态
const handleStatusChange = async (row: User) => {
  try {
    await updateUserStatus(row.id, row.status);
    ElMessage.success("状态更新成功");
  } catch (error) {
    row.status = row.status === 1 ? 0 : 1;
    ElMessage.error("状态更新失败");
  }
};

// 删除用户
const handleDelete = (row: User) => {
  ElMessageBox.confirm(
    `确定要删除用户 ${row.nickname || row.phone} 吗？`,
    "提示",
    {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    },
  )
    .then(async () => {
      try {
        await deleteUser(row.id);
        ElMessage.success("删除成功");
        fetchUserList();
      } catch (error) {
        ElMessage.error("删除失败");
      }
    })
    .catch(() => {});
};

// 分页变化
const handlePageChange = (page: number) => {
  currentPage.value = page;
  fetchUserList();
};

const handleSizeChange = (size: number) => {
  pageSize.value = size;
  currentPage.value = 1;
  fetchUserList();
};

// 选择变化
const handleSelectionChange = (selection: User[]) => {
  selectedRows.value = selection;
};

// 性别格式化
const genderFormatter = (row: User) => {
  const map: Record<number, string> = {
    0: "未知",
    1: "男",
    2: "女",
  };
  return map[row.gender || 0];
};

// 状态格式化
const statusFormatter = (row: User) => {
  return row.status === 1 ? "启用" : "禁用";
};

// 批量删除
const handleBatchDelete = () => {
  if (selectedRows.value.length === 0) {
    ElMessage.warning("请选择要删除的用户");
    return;
  }
  ElMessageBox.confirm(
    `确定要删除选中的 ${selectedRows.value.length} 个用户吗？`,
    "提示",
    {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    },
  )
    .then(async () => {
      try {
        const deletePromises = selectedRows.value.map((user) =>
          deleteUser(user.id)
        );
        await Promise.all(deletePromises);
        ElMessage.success("批量删除成功");
        selectedRows.value = [];
        fetchUserList();
      } catch (error) {
        ElMessage.error("批量删除失败");
      }
    })
    .catch(() => {});
};

// 导出数据
const handleExport = () => {
  ElMessage.info("导出功能开发中...");
  // TODO: 实现导出功能
};

// 新增用户
const handleCreate = () => {
  ElMessage.info("新增用户功能开发中...");
  // TODO: 实现新增用户功能
};

onMounted(() => {
  fetchUserList();
});
</script>

<template>
  <div class="page-container">
    <div class="content-card">
      <!-- 搜索表单 -->
      <el-form :model="searchForm" inline class="search-form">
        <el-form-item label="关键词">
          <el-input
            v-model="searchForm.keyword"
            placeholder="请输入手机号或昵称"
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
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
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

      <!-- 数据表格 -->
      <div class="table-container">
        <div class="table-header">
          <span class="table-title">用户列表</span>
          <div class="table-actions">
            <el-button 
              type="danger" 
              size="small" 
              :disabled="selectedRows.length === 0"
              @click="handleBatchDelete"
            >
              <el-icon><Delete /></el-icon>
              批量删除 ({{ selectedRows.length }})
            </el-button>
            <el-button type="primary" size="small" @click="handleExport">
              <el-icon><Download /></el-icon>
              导出数据
            </el-button>
            <el-button type="primary" size="small" @click="handleCreate">
              <el-icon><Plus /></el-icon>
              新增用户
            </el-button>
          </div>
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
          <el-table-column prop="id" label="用户ID" width="80" />
          <el-table-column prop="phone" label="手机号" width="120" />
          <el-table-column prop="nickname" label="昵称" width="120" />
          <el-table-column label="性别" width="80" align="center">
            <template #default="{ row }">
              {{ genderFormatter(row) }}
            </template>
          </el-table-column>
          <el-table-column prop="birthday" label="生日" width="120" />
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
          <el-table-column prop="registerTime" label="注册时间" width="180" />
          <el-table-column label="操作" width="200" fixed="right">
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

        <!-- 分页 -->
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

.table-actions {
  display: flex;
  gap: 10px;
  align-items: center;
}
</style>
