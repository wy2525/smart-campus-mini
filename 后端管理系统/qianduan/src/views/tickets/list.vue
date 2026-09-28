<template>
  <div class="ticket-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>门票管理</span>
          <el-button type="primary" @click="handleCreate"
            >新增门票类型</el-button
          >
        </div>
      </template>

      <!-- 搜索区域 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="门票名称">
          <el-input
            v-model="searchForm.name"
            placeholder="请输入门票名称"
            clearable
          />
        </el-form-item>
        <el-form-item label="景点">
          <el-select
            v-model="searchForm.attractionId"
            placeholder="请选择景点"
            clearable
          >
            <el-option
              v-for="attraction in attractions"
              :key="attraction.id"
              :label="attraction.name"
              :value="attraction.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select
            v-model="searchForm.status"
            placeholder="请选择状态"
            clearable
          >
            <el-option label="上架" :value="1" />
            <el-option label="下架" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 表格 -->
      <el-table
        :data="ticketList"
        stripe
        style="width: 100%"
        v-loading="loading"
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="门票名称" min-width="150" />
        <el-table-column
          prop="attraction.name"
          label="所属景点"
          min-width="150"
        />
        <el-table-column
          prop="description"
          label="描述"
          min-width="200"
          show-tooltip-when-overflow
        />
        <el-table-column prop="price" label="价格" width="100">
          <template #default="{ row }"> ¥{{ row.price }} </template>
        </el-table-column>
        <el-table-column prop="stock" label="库存" width="100">
          <template #default="{ row }">
            <el-tag
              :type="
                row.stock > 10
                  ? 'success'
                  : row.stock > 0
                  ? 'warning'
                  : 'danger'
              "
            >
              {{ row.stock }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? "上架" : "下架" }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="sort" label="排序" width="80" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button
              size="small"
              :type="row.status === 1 ? 'danger' : 'success'"
              @click="toggleStatus(row)"
            >
              {{ row.status === 1 ? "下架" : "上架" }}
            </el-button>
            <el-button size="small" type="danger" @click="handleDelete(row)"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        :current-page="pagination.page"
        :page-size="pagination.size"
        :page-sizes="[10, 20, 50, 100]"
        :total="pagination.total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        class="pagination"
      />
    </el-card>

    <!-- 编辑弹窗 -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="600px">
      <el-form
        :model="form"
        :rules="formRules"
        ref="formRef"
        label-width="100px"
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
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitting"
          >确定</el-button
        >
      </template>
    </el-dialog>

    <!-- 库存调整弹窗 -->
    <el-dialog title="调整库存" v-model="stockDialogVisible" width="400px">
      <el-form :model="stockForm" label-width="80px">
        <el-form-item label="门票名称">
          <el-input v-model="stockForm.name" disabled />
        </el-form-item>
        <el-form-item label="当前库存">
          <el-input v-model.number="stockForm.currentStock" disabled />
        </el-form-item>
        <el-form-item label="调整数量" prop="adjustment">
          <el-input-number
            v-model="stockForm.adjustment"
            :min="-stockForm.currentStock"
            :max="10000"
          />
        </el-form-item>
        <el-form-item label="备注">
          <el-input
            v-model="stockForm.remark"
            type="textarea"
            placeholder="可选，填写调整原因"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="stockDialogVisible = false">取消</el-button>
        <el-button
          type="primary"
          @click="handleStockSubmit"
          :loading="stockSubmitting"
          >确定</el-button
        >
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import {
  getAllTicketTypes,
  createTicketType,
  updateTicketType,
  deleteTicketType,
  updateTicketTypeStatus,
  updateTicketStock,
} from "@/api/ticket";
import { getAttractionList } from "@/api/attraction";

// 响应式数据
const loading = ref(false);
const dialogVisible = ref(false);
const stockDialogVisible = ref(false);
const submitting = ref(false);
const stockSubmitting = ref(false);
const formRef = ref();

// 表单数据
const form = reactive({
  id: undefined,
  name: "",
  attractionId: undefined,
  description: "",
  price: 0,
  stock: 0,
  status: 1,
  sort: 0,
});

// 库存调整表单
const stockForm = reactive({
  id: 0,
  name: "",
  currentStock: 0,
  adjustment: 0,
  remark: "",
});

// 搜索表单
const searchForm = reactive({
  name: "",
  attractionId: undefined,
  status: undefined,
});

// 分页数据
const pagination = reactive({
  page: 1,
  size: 10,
  total: 0,
});

// 其他数据
const ticketList = ref<any[]>([]);
const attractions = ref<any[]>([]);
const dialogTitle = computed(() => (form.id ? "编辑门票类型" : "新增门票类型"));

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

// 获取门票列表
const fetchTicketList = async () => {
  loading.value = true;
  try {
    // 当前getAllTicketTypes API不支持分页和搜索参数，我们先获取全部数据再进行前端过滤
    const response = await getAllTicketTypes();
    let allTickets = response.data || [];

    // 根据搜索条件过滤数据
    if (searchForm.name) {
      allTickets = allTickets.filter((ticket) =>
        ticket.name.includes(searchForm.name)
      );
    }
    if (searchForm.attractionId) {
      allTickets = allTickets.filter(
        (ticket) => ticket.attractionId === searchForm.attractionId
      );
    }
    if (searchForm.status !== undefined) {
      allTickets = allTickets.filter(
        (ticket) => ticket.status === searchForm.status
      );
    }

    // 计算分页数据
    const startIndex = (pagination.page - 1) * pagination.size;
    const endIndex = startIndex + pagination.size;
    ticketList.value = allTickets.slice(startIndex, endIndex);
    pagination.total = allTickets.length;
  } catch (error) {
    console.error("获取门票列表失败:", error);
    ElMessage.error("获取门票列表失败");
  } finally {
    loading.value = false;
  }
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

// 处理搜索
const handleSearch = () => {
  pagination.page = 1;
  fetchTicketList();
};

// 处理重置
const handleReset = () => {
  Object.assign(searchForm, {
    name: "",
    attractionId: undefined,
    status: undefined,
  });
  pagination.page = 1;
  fetchTicketList();
};

// 处理分页大小变化
const handleSizeChange = (size: number) => {
  pagination.size = size;
  pagination.page = 1;
  fetchTicketList();
};

// 处理当前页变化
const handleCurrentChange = (page: number) => {
  pagination.page = page;
  fetchTicketList();
};

// 新增门票
const handleCreate = () => {
  Object.assign(form, {
    id: undefined,
    name: "",
    attractionId: undefined,
    description: "",
    price: 0,
    stock: 0,
    status: 1,
    sort: 0,
  });
  dialogVisible.value = true;
};

// 编辑门票
const handleEdit = (row: any) => {
  Object.assign(form, { ...row });
  dialogVisible.value = true;
};

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return;

  try {
    await formRef.value.validate();
    submitting.value = true;

    if (form.id) {
      // 更新门票
      await updateTicketType(form.id, {
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

    dialogVisible.value = false;
    fetchTicketList();
  } catch (error) {
    console.error("提交失败:", error);
    ElMessage.error("提交失败");
  } finally {
    submitting.value = false;
  }
};

// 删除门票
const handleDelete = async (row: any) => {
  try {
    await ElMessageBox.confirm(`确定要删除门票 "${row.name}" 吗？`, "提示", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning",
    });

    await deleteTicketType(row.id);
    ElMessage.success("删除成功");
    fetchTicketList();
  } catch (error) {
    if (error !== "cancel") {
      console.error("删除失败:", error);
      ElMessage.error("删除失败");
    }
  }
};

// 切换状态
const toggleStatus = async (row: any) => {
  try {
    const newStatus = row.status === 1 ? 0 : 1;
    await updateTicketTypeStatus(row.id, newStatus);
    ElMessage.success(`${row.status === 1 ? "下架" : "上架"}成功`);
    fetchTicketList();
  } catch (error) {
    console.error("状态切换失败:", error);
    ElMessage.error("状态切换失败");
  }
};

// 调整库存
const adjustStock = (row: any) => {
  Object.assign(stockForm, {
    id: row.id,
    name: row.name,
    currentStock: row.stock,
    adjustment: 0,
    remark: "",
  });
  stockDialogVisible.value = true;
};

// 提交库存调整
const handleStockSubmit = async () => {
  try {
    stockSubmitting.value = true;
    const newStock = stockForm.currentStock + stockForm.adjustment;

    if (newStock < 0) {
      ElMessage.error("调整后库存不能小于0");
      return;
    }

    await updateTicketStock(stockForm.id, newStock);
    ElMessage.success("库存调整成功");
    stockDialogVisible.value = false;
    fetchTicketList();
  } catch (error) {
    console.error("库存调整失败:", error);
    ElMessage.error("库存调整失败");
  } finally {
    stockSubmitting.value = false;
  }
};

onMounted(() => {
  fetchTicketList();
  fetchAttractions();
});
</script>

<style scoped>
.ticket-management {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-form {
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  text-align: right;
}
</style>
