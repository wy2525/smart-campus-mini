<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import {
  getSystemConfigList,
  createSystemConfig,
  updateConfig,
  deleteSystemConfig
} from '@/api/system'
import type { SystemConfig } from '@/types'

const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('新增配置')
const formRef = ref()

const formData = reactive({
  configKey: '',
  configValue: '',
  description: ''
})

const rules = {
  configKey: [{ required: true, message: '请输入配置键', trigger: 'blur' }],
  description: [{ required: true, message: '请输入配置说明', trigger: 'blur' }]
}

const tableData = ref<SystemConfig[]>([])

const fetchConfigList = async () => {
  loading.value = true
  try {
    const res = await getSystemConfigList()
    tableData.value = res.data
  } catch (error) {
    console.error('获取系统配置失败:', error)
  } finally {
    loading.value = false
  }
}

const handleCreate = () => {
  dialogTitle.value = '新增配置'
  Object.assign(formData, {
    configKey: '',
    configValue: '',
    description: ''
  })
  dialogVisible.value = true
}

const handleEdit = (row: SystemConfig) => {
  dialogTitle.value = '编辑配置'
  Object.assign(formData, {
    configKey: row.configKey,
    configValue: row.configValue || '',
    description: row.description || ''
  })
  dialogVisible.value = true
}

const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  loading.value = true
  try {
    if (dialogTitle.value === '编辑配置') {
      const id = tableData.value.find(item => item.configKey === formData.configKey)?.id
      if (id) {
        await updateConfig(id, formData)
        ElMessage.success('更新成功')
      }
    } else {
      await createSystemConfig(formData)
      ElMessage.success('创建成功')
    }
    dialogVisible.value = false
    fetchConfigList()
  } catch (error) {
    console.error('提交失败:', error)
  } finally {
    loading.value = false
  }
}

const handleDelete = (row: SystemConfig) => {
  ElMessageBox.confirm(`确定要删除配置 ${row.configKey} 吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteSystemConfig(row.id)
      ElMessage.success('删除成功')
      fetchConfigList()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

onMounted(() => {
  fetchConfigList()
})
</script>

<template>
  <div class="page-container">
    <div class="content-card">
      <div class="table-header">
        <span class="table-title">系统配置</span>
        <el-button type="primary" size="small" @click="handleCreate">
          <el-icon><Plus /></el-icon>
          新增配置
        </el-button>
      </div>

      <el-table v-loading="loading" :data="tableData" border stripe>
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="configKey" label="配置键" width="200" />
        <el-table-column prop="configValue" label="配置值" min-width="200" show-overflow-tooltip />
        <el-table-column prop="description" label="配置说明" min-width="250" show-overflow-tooltip />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column prop="updateTime" label="更新时间" width="180" />
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
        class="config-form"
      >
        <el-form-item label="配置键" prop="configKey">
          <el-input v-model="formData.configKey" placeholder="请输入配置键" clearable />
        </el-form-item>
        <el-form-item label="配置值">
          <el-input
            v-model="formData.configValue"
            type="textarea"
            :rows="4"
            placeholder="请输入配置值"
            clearable
          />
        </el-form-item>
        <el-form-item label="配置说明" prop="description">
          <el-input v-model="formData.description" placeholder="请输入配置说明" clearable />
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

.config-form {
  padding: 20px 0;
}
</style>
