<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import {
  getBannerList,
  createBanner,
  updateBanner,
  updateBannerStatus,
  updateBannerSort,
  deleteBanner
} from '@/api/banner'
import type { Banner } from '@/types'

const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('新增Banner')
const formRef = ref()

const formData = reactive({
  title: '',
  imageUrl: '',
  attractionId: undefined as number | undefined,
  linkUrl: '',
  status: 0,
  sort: 0
})

const rules = {
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  imageUrl: [{ required: true, message: '请输入图片URL', trigger: 'blur' }]
}

const tableData = ref<Banner[]>([])

const fetchBannerList = async () => {
  loading.value = true
  try {
    const res = await getBannerList()
    tableData.value = res.data
  } catch (error) {
    console.error('获取Banner列表失败:', error)
  } finally {
    loading.value = false
  }
}

const handleCreate = () => {
  dialogTitle.value = '新增Banner'
  Object.assign(formData, {
    title: '',
    imageUrl: '',
    attractionId: undefined,
    linkUrl: '',
    status: 0,
    sort: 0
  })
  dialogVisible.value = true
}

const handleEdit = (row: Banner) => {
  dialogTitle.value = '编辑Banner'
  Object.assign(formData, {
    title: row.title,
    imageUrl: row.imageUrl,
    attractionId: row.attractionId,
    linkUrl: row.linkUrl,
    status: row.status,
    sort: row.sort
  })
  dialogVisible.value = true
}

const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  loading.value = true
  try {
    if (dialogTitle.value === '编辑Banner') {
      const id = tableData.value.find(item => item.title === formData.title)?.id
      if (id) {
        await updateBanner(id, formData)
        ElMessage.success('更新成功')
      }
    } else {
      await createBanner(formData)
      ElMessage.success('创建成功')
    }
    dialogVisible.value = false
    fetchBannerList()
  } catch (error) {
    console.error('提交失败:', error)
  } finally {
    loading.value = false
  }
}

const handleStatusChange = async (row: Banner) => {
  try {
    await updateBannerStatus(row.id, row.status)
    ElMessage.success('状态更新成功')
  } catch (error) {
    row.status = row.status === 1 ? 0 : 1
    ElMessage.error('状态更新失败')
  }
}

const handleSortChange = async (row: Banner) => {
  try {
    await updateBannerSort(row.id, row.sort)
    ElMessage.success('排序更新成功')
    fetchBannerList()
  } catch (error) {
    ElMessage.error('排序更新失败')
  }
}

const handleDelete = (row: Banner) => {
  ElMessageBox.confirm(`确定要删除Banner ${row.title} 吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteBanner(row.id)
      ElMessage.success('删除成功')
      fetchBannerList()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

const statusFormatter = (row: Banner) => {
  return row.status === 1 ? '上线' : '下线'
}

onMounted(() => {
  fetchBannerList()
})
</script>

<template>
  <div class="page-container">
    <div class="content-card">
      <div class="table-header">
        <span class="table-title">Banner列表</span>
        <el-button type="primary" size="small" @click="handleCreate">
          <el-icon><Plus /></el-icon>
          新增Banner
        </el-button>
      </div>

      <el-table v-loading="loading" :data="tableData" border stripe>
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="title" label="标题" width="150" />
        <el-table-column label="图片" width="200">
          <template #default="{ row }">
            <el-image
              :src="row.imageUrl"
              style="width: 180px; height: 80px"
              fit="cover"
              :preview-src-list="[row.imageUrl]"
            />
          </template>
        </el-table-column>
        <el-table-column prop="linkUrl" label="跳转链接" min-width="200" show-overflow-tooltip />
        <el-table-column prop="sort" label="排序" width="100">
          <template #default="{ row }">
            <el-input-number
              v-model="row.sort"
              :min="0"
              size="small"
              @change="handleSortChange(row)"
            />
          </template>
        </el-table-column>
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
            <el-button type="primary" link size="small" @click="handleEdit(row)">
              <el-icon><Edit /></el-icon>
              编辑
            </el-button>
            <el-button type="danger" link size="small" @click="handleDelete(row)">
              <el-icon><Delete /></el-icon>
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="rules"
        label-width="100px"
        class="banner-form"
      >
        <el-form-item label="标题" prop="title">
          <el-input v-model="formData.title" placeholder="请输入标题" clearable />
        </el-form-item>
        <el-form-item label="图片URL" prop="imageUrl">
          <el-input v-model="formData.imageUrl" placeholder="请输入图片URL" clearable />
        </el-form-item>
        <el-form-item label="跳转链接">
          <el-input v-model="formData.linkUrl" placeholder="请输入跳转链接" clearable />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="formData.sort" :min="0" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="formData.status">
            <el-radio :label="1">上线</el-radio>
            <el-radio :label="0">下线</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="loading">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
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

.banner-form {
  padding: 20px 0;
}
</style>
