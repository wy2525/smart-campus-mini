<template>
  <div class="ticket-form">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>{{ isEdit ? "编辑门票类型" : "新增门票类型" }}</span>
          <el-button @click="goBack">返回</el-button>
        </div>
      </template>

      <el-form
        :model="form"
        :rules="formRules"
        ref="formRef"
        label-width="100px"
        style="max-width: 600px"
      >
        <el-form-item label="门票名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入门票名称" />
        </el-form-item>

        <el-form-item label="所属景点" prop="attractionId">
          <el-select
            v-model="form.attractionId"
            placeholder="请选择所属景点"
            style="width: 100%"
          >
            <el-option
              v-for="attraction in attractions"
              :key="attraction.id"
              :label="attraction.name"
              :value="attraction.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="描述">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="4"
            placeholder="请输入门票描述"
          />
        </el-form-item>

        <el-form-item label="价格" prop="price">
          <el-input-number
            v-model="form.price"
            :min="0"
            :step="0.01"
            :precision="2"
            placeholder="请输入价格"
            style="width: 100%"
          />
        </el-form-item>

        <el-form-item label="库存" prop="stock">
          <el-input-number
            v-model="form.stock"
            :min="0"
            placeholder="请输入库存数量"
            style="width: 100%"
          />
        </el-form-item>

        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">上架</el-radio>
            <el-radio :label="0">下架</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="排序">
          <el-input-number
            v-model="form.sort"
            :min="0"
            placeholder="排序值越小越靠前"
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSubmit" :loading="submitting"
            >保存</el-button
          >
          <el-button @click="goBack">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from "vue";
import { useRouter, useRoute } from "vue-router";
import { ElMessage } from "element-plus";
import {
  getTicketTypeDetail,
  createTicketType,
  updateTicketType,
} from "@/api/ticket";
import { getAttractionList } from "@/api/attraction";

const router = useRouter();
const route = useRoute();

// 响应式数据
const formRef = ref();
const submitting = ref(false);

// 表单数据
const form = reactive({
  id: undefined as number | undefined,
  name: "",
  attractionId: undefined as number | undefined,
  description: "",
  price: 0,
  stock: 0,
  status: 1,
  sort: 0,
});

// 其他数据
const attractions = ref<any[]>([]);

// 计算属性
const isEdit = computed(() => !!route.params.id);

// 表单验证规则
const formRules = {
  name: [
    { required: true, message: "请输入门票名称", trigger: "blur" },
    {
      min: 1,
      max: 50,
      message: "门票名称长度应在1-50个字符之间",
      trigger: "blur",
    },
  ],
  attractionId: [
    { required: true, message: "请选择所属景点", trigger: "change" },
  ],
  price: [
    { required: true, message: "请输入价格", trigger: "blur" },
    { type: "number", min: 0, message: "价格不能小于0", trigger: "blur" },
  ],
  stock: [
    { required: true, message: "请输入库存数量", trigger: "blur" },
    { type: "number", min: 0, message: "库存不能小于0", trigger: "blur" },
  ],
  status: [{ required: true, message: "请选择状态", trigger: "change" }],
};

// 获取景点列表
const fetchAttractions = async () => {
  try {
    const response = await getAttractionList({ page: 0, size: 100 });
    attractions.value = response.data?.content || [];
  } catch (error) {
    console.error("获取景点列表失败:", error);
    ElMessage.error("获取景点列表失败");
  }
};

// 获取门票详情（编辑模式）
const fetchTicketDetail = async () => {
  if (!isEdit.value) return;

  try {
    const response = await getTicketTypeDetail(Number(route.params.id));
    Object.assign(form, response.data);
  } catch (error) {
    console.error("获取门票详情失败:", error);
    ElMessage.error("获取门票详情失败");
    goBack();
  }
};

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return;

  try {
    await formRef.value.validate();
    submitting.value = true;

    if (isEdit.value) {
      // 更新门票
      await updateTicketType(form.id!, {
        name: form.name,
        attractionId: form.attractionId,
        description: form.description,
        price: form.price,
        stock: form.stock,
        status: form.status,
        sort: form.sort,
      });
      ElMessage.success("更新门票成功");
    } else {
      // 创建门票
      await createTicketType({
        name: form.name,
        attractionId: form.attractionId,
        description: form.description,
        price: form.price,
        stock: form.stock,
        status: form.status,
        sort: form.sort,
      });
      ElMessage.success("创建门票成功");
    }

    goBack();
  } catch (error) {
    console.error("提交失败:", error);
    ElMessage.error("提交失败");
  } finally {
    submitting.value = false;
  }
};

// 返回列表页
const goBack = () => {
  router.push("/tickets");
};

onMounted(async () => {
  await fetchAttractions();
  if (isEdit.value) {
    form.id = Number(route.params.id);
    await fetchTicketDetail();
  }
});
</script>

<style scoped>
.ticket-form {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
