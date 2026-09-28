<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import {
  getAdminList,
  createAdmin,
  updateAdmin,
  updateAdminStatus,
  deleteAdmin,
  resetAdminPassword
} from '@/api/auth'
import type { Admin } from '@/types'

const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('新增管理员')
const formRef = ref()

const formData = reactive({
  username: '',
  password: '',
  nickname: '',
  phone: '',
  email: '',
  role: 'admin',
  status: 1
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ],
  role: [{ required: true, message: '请选择角色', trigger: 'change' }]
}

const tableData = ref<Admin[]>([])

const fetchAdminList = async () => {
  loading.value = true
  try {
    const res = await getAdminList()
    tableData.value = res.data
  } catch (error) {
    console.error('获取管理员列表失败:', error)
  } finally {
    loading.value = false
  }
}

const handleCreate = () => {
  dialogTitle.value = '新增管理员'
  Object.assign(formData, {
    username: '',
    password: '',
    nickname: '',
    phone: '',
    email: '',
    role: 'admin',
    status: 1
  })
  dialogVisible.value = true
}

const handleEdit = (row: Admin) => {
  dialogTitle.value = '编辑管理员'
  Object.assign(formData, {
    username: row.username,
    password: '',
    nickname: row.nickname || '',
    phone: row.phone || '',
    email: row.email || '',
    role: row.role,
    status: row.status
  })
  dialogVisible.value = true
}

const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  loading.value = true
  try {
    if (dialogTitle.value === '编辑管理员') {
      const id = tableData.value.find(item => item.username === formData.username)?.id
      if (id) {
        const data = { ...formData }
        delete data.password
        await updateAdmin(id, data)
        ElMessage.success('更新成功')
      }
    } else {
      await createAdmin(formData)
      ElMessage.success('创建成功')
    }
    dialogVisible.value = false
    fetchAdminList()
  } catch (error) {
    console.error('提交失败:', error)
  } finally {
    loading.value = false
  }
}

const handleStatusChange = async (row: Admin) => {
  try {
    await updateAdminStatus(row.id, row.status)
    ElMessage.success('状态更新成功')
  } catch (error) {
    row.status = row.status === 1 ? 0 : 1
    ElMessage.error('状态更新失败')
  }
}

const handleResetPassword = (row: Admin) => {
  ElMessageBox.prompt('请输入新密码', '重置密码', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    inputType: 'password',
    inputPattern: /^.{6,}$/,
    inputErrorMessage: '密码长度不能少于6位'
  }).then(async ({ value }) => {
    try {
      await resetAdminPassword(row.id, value)
      ElMessage.success('密码重置成功')
    } catch (error) {
      ElMessage.error('密码重置失败')
    }
  }).catch(() => {})
}

const handleDelete = (row: Admin) => {
  ElMessageBox.confirm(`确定要删除管理员 ${row.username} 吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteAdmin(row.id)
      ElMessage.success('删除成功')
      fetchAdminList()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

const roleFormatter = (role: string) => {
  const map: Record<string, string> = {
    super_admin: '超级管理员',
    admin: '管理员'
  }
  return map[role] || role
}

const roleTagType = (role: string) => {
  return role === 'super_admin' ? 'danger' : 'primary'
}

const statusFormatter = (row: Admin) => {
  return row.status === 1 ? '启用' : '禁用'
}

onMounted(() => {
  fetchAdminList()
})
</script>

<template>
  <div class="page-container">
    <div class="content-card">
      <div class="table-header">
        <span class="table-title">管理员列表</span>
        <el-button type="primary" size="small" @click="handleCreate">
          <el-icon><Plus /></el-icon>
          新增管理员
        </el-button>
      </div>

      <el-table v-loading="loading" :data="tableData" border stripe>
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="username" label="用户名" width="150" />
        <el-table-column prop="nickname" label="昵称" width="120" />
        <el-table-column prop="phone" label="手机号" width="120" />
        <el-table-column prop="email" label="邮箱" width="180" />
        <el-table-column label="角色" width="120">
          <template #default="{ row }">
            <el-tag :type="roleTagType(row.role)">
              {{ roleFormatter(row.role) }}
            </el-tag>
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
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleEdit(row)">
              <el-icon><Edit /></el-icon>
              编辑
            </el-button>
            <el-button type="warning" link size="small" @click="handleResetPassword(row)">
              <el-icon><Lock /></el-icon>
              重置密码
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
        class="admin-form"
      >
        <el-form-item label="用户名" prop="username">
          <el-input v-model="formData.username" placeholder="请输入用户名" clearable />
        </el-form-item>
        <el-form-item v-if="dialogTitle === '新增管理员'" label="密码" prop="password">
          <el-input
            v-model="formData.password"
            type="password"
            placeholder="请输入密码"
            show-password
            clearable
          />
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="formData.nickname" placeholder="请输入昵称" clearable />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="formData.phone" placeholder="请输入手机号" clearable />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="formData.email" placeholder="请输入邮箱" clearable />
        </el-form-item>
        <el-form-item label="角色" prop="role">
          <el-radio-group v-model="formData.role">
            <el-radio label="admin">管理员</el-radio>
            <el-radio label="super_admin">超级管理员</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="formData.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
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

.admin-form {
  padding: 20px 0;
}
</style>
