<script setup lang="ts">
import { ref, reactive, onMounted } from "vue";
import { useRouter } from "vue-router";
import { ElMessage, ElMessageBox } from "element-plus";
import {
  getAttractionList,
  updateAttractionStatus,
  deleteAttraction,
} from "@/api/attraction";
import type { Attraction, PageRequest } from "@/types";

const router = useRouter();

const loading = ref(false);
const searchForm = reactive({
  keyword: "",
  category: "",
  status: undefined as number | undefined,
});

const tableData = ref<Attraction[]>([]);
const total = ref(0);
const currentPage = ref(1);
const pageSize = ref(10);

const fetchAttractionList = async () => {
  loading.value = true;
  try {
    const params: PageRequest = {
      page: currentPage.value - 1,
      size: pageSize.value,
      keyword: searchForm.keyword || undefined,
      category: searchForm.category || undefined,
      status: searchForm.status,
    };
    const res = await getAttractionList(params);
    tableData.value = res.data.content;
    total.value = res.data.totalElements;
  } catch (error) {
    console.error("获取景点列表失败:", error);
  } finally {
    loading.value = false;
  }
};

const handleSearch = () => {
  currentPage.value = 1;
  fetchAttractionList();
};

const handleReset = () => {
  searchForm.keyword = "";
  searchForm.category = "";
  searchForm.status = undefined;
  currentPage.value = 1;
  fetchAttractionList();
};

const handleCreate = () => {
  router.push("/attractions/create");
};

const handleDetail = (row: Attraction) => {
  router.push(`/attractions/${row.id}`);
};

const handleEdit = (row: Attraction) => {
  router.push(`/attractions/${row.id}/edit`);
};

const handleStatusChange = async (row: Attraction) => {
  try {
    await updateAttractionStatus(row.id, row.status);
    ElMessage.success("状态更新成功");
  } catch (error) {
    row.status = row.status === 1 ? 0 : 1;
    ElMessage.error("状态更新失败");
  }
};

const handleDelete = (row: Attraction) => {
  ElMessageBox.confirm(`确定要删除景点 ${row.name} 吗？`, "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(async () => {
      try {
        await deleteAttraction(row.id);
        ElMessage.success("删除成功");
        fetchAttractionList();
      } catch (error) {
        ElMessage.error("删除失败");
      }
    })
    .catch(() => {});
};

const handlePageChange = (page: number) => {
  currentPage.value = page;
  fetchAttractionList();
};

const handleSizeChange = (size: number) => {
  pageSize.value = size;
  currentPage.value = 1;
  fetchAttractionList();
};

const statusFormatter = (row: Attraction) => {
  return row.status === 1 ? "上线" : "下线";
};

// 导出数据
const handleExport = () => {
  ElMessage.info("导出功能开发中...");
  // TODO: 实现导出功能
};

onMounted(() => {
  fetchAttractionList();
});
</script>

<template>
  <div class="page-container">
    <div class="content-card">
      <el-form :model="searchForm" inline class="search-form">
        <el-form-item label="关键词">
          <el-input
            v-model="searchForm.keyword"
            placeholder="请输入景点名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="分类">
          <el-input
            v-model="searchForm.category"
            placeholder="请输入分类"
            clearable
            style="width: 150px"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-select
            v-model="searchForm.status"
            placeholder="请选择状态"
            clearable
            style="width: 120px"
          >
            <el-option label="上线" :value="1" />
            <el-option label="下线" :value="0" />
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
          <span class="table-title">景点列表</span>
          <div class="table-actions">
            <el-button type="success" size="small" @click="handleExport">
              <el-icon><Download /></el-icon>
              导出数据
            </el-button>
            <el-button type="primary" size="small" @click="handleCreate">
              <el-icon><Plus /></el-icon>
              新增景点
            </el-button>
          </div>
        </div>

        <el-table v-loading="loading" :data="tableData" border stripe>
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="id" label="景点ID" width="80" />
          <el-table-column prop="name" label="景点名称" width="150" />
          <el-table-column prop="category" label="分类" width="100" />
          <el-table-column label="封面" width="100">
            <template #default="{ row }">
              <el-image
                v-if="row.coverImage"
                :src="row.coverImage"
                style="width: 60px; height: 60px"
                fit="cover"
                :preview-src-list="[row.coverImage]"
              />
              <span v-else>-</span>
            </template>
          </el-table-column>
          <el-table-column prop="minPrice" label="最低价格" width="100">
            <template #default="{ row }"> ¥{{ row.minPrice }} </template>
          </el-table-column>
          <el-table-column prop="rating" label="评分" width="80" />
          <el-table-column prop="viewCount" label="浏览量" width="80" />
          <el-table-column prop="bookingCount" label="预订量" width="80" />
          <el-table-column prop="sort" label="排序" width="80" />
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
          <el-table-column label="操作" width="220" fixed="right">
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
                type="primary"
                link
                size="small"
                @click="handleEdit(row)"
              >
                <el-icon><Edit /></el-icon>
                编辑
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

.table-actions {
  display: flex;
  gap: 10px;
  align-items: center;
}
</style>
