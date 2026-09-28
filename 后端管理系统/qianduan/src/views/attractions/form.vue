<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
  createAttraction,
  updateAttraction,
  getAttractionDetail
} from '@/api/attraction'
import type { Attraction } from '@/types'

const router = useRouter()
const route = useRoute()

const loading = ref(false)
const isEdit = ref(false)
const formRef = ref()

const formData = reactive({
  name: '',
  category: '',
  coverImage: '',
  images: '',
  description: '',
  address: '',
  longitude: 0,
  latitude: 0,
  phone: '',
  openTime: '',
  notes: '',
  tags: '',
  minPrice: 0,
  rating: 0,
  viewCount: 0,
  bookingCount: 0,
  status: 0,
  sort: 0
})

const rules = {
  name: [{ required: true, message: '请输入景点名称', trigger: 'blur' }],
  category: [{ required: true, message: '请输入分类', trigger: 'blur' }]
}

const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  loading.value = true
  try {
    if (isEdit.value) {
      const id = parseInt(route.params.id as string)
      await updateAttraction(id, formData)
      ElMessage.success('更新成功')
    } else {
      await createAttraction(formData)
      ElMessage.success('创建成功')
    }
    router.back()
  } catch (error) {
    console.error('提交失败:', error)
  } finally {
    loading.value = false
  }
}

const handleCancel = () => {
  router.back()
}

onMounted(async () => {
  if (route.params.id) {
    isEdit.value = true
    try {
      const id = parseInt(route.params.id as string)
      const res = await getAttractionDetail(id)
      Object.assign(formData, res.data)
    } catch (error) {
      console.error('获取景点详情失败:', error)
    }
  }
})
</script>

<template>
  <div class="page-container">
    <el-card class="form-card" v-loading="loading">
      <template #header>
        <div class="card-header">
          <span>{{ isEdit ? '编辑景点' : '新增景点' }}</span>
          <div>
            <el-button @click="handleCancel">取消</el-button>
            <el-button type="primary" @click="handleSubmit" :loading="loading">
              保存
            </el-button>
          </div>
        </div>
      </template>

      <el-form
        ref="formRef"
        :model="formData"
        :rules="rules"
        label-width="120px"
        class="attraction-form"
      >
        <el-form-item label="景点名称" prop="name">
          <el-input v-model="formData.name" placeholder="请输入景点名称" clearable />
        </el-form-item>

        <el-form-item label="分类" prop="category">
          <el-input v-model="formData.category" placeholder="请输入分类" clearable />
        </el-form-item>

        <el-form-item label="封面图">
          <el-input v-model="formData.coverImage" placeholder="请输入封面图URL" clearable />
        </el-form-item>

        <el-form-item label="图片集">
          <el-input
            v-model="formData.images"
            type="textarea"
            :rows="3"
            placeholder="请输入图片URL，多个用逗号分隔"
            clearable
          />
        </el-form-item>

        <el-form-item label="介绍">
          <el-input
            v-model="formData.description"
            type="textarea"
            :rows="4"
            placeholder="请输入景点介绍"
            clearable
          />
        </el-form-item>

        <el-form-item label="地址">
          <el-input v-model="formData.address" placeholder="请输入地址" clearable />
        </el-form-item>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="经度">
              <el-input-number v-model="formData.longitude" :precision="7" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="纬度">
              <el-input-number v-model="formData.latitude" :precision="7" :min="0" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="联系电话">
          <el-input v-model="formData.phone" placeholder="请输入联系电话" clearable />
        </el-form-item>

        <el-form-item label="开放时间">
          <el-input v-model="formData.openTime" placeholder="请输入开放时间" clearable />
        </el-form-item>

        <el-form-item label="注意事项">
          <el-input
            v-model="formData.notes"
            type="textarea"
            :rows="3"
            placeholder="请输入注意事项"
            clearable
          />
        </el-form-item>

        <el-form-item label="标签">
          <el-input
            v-model="formData.tags"
            placeholder="请输入标签，多个用逗号分隔"
            clearable
          />
        </el-form-item>

        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="最低价格">
              <el-input-number v-model="formData.minPrice" :precision="2" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="评分">
              <el-input-number v-model="formData.rating" :precision="1" :min="0" :max="5" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="排序">
              <el-input-number v-model="formData.sort" :min="0" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="状态">
          <el-radio-group v-model="formData.status">
            <el-radio :label="1">上线</el-radio>
            <el-radio :label="0">下线</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<style scoped>
.form-card {
  max-width: 900px;
  margin: 0 auto;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 600;
}

.attraction-form {
  padding: 20px 0;
}
</style>
