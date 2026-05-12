<template>
  <div class="monthly-card-management">
    <h2 class="page-title">月卡管理</h2>
    
    <el-card>
      <template #header>
        <div class="card-header">
          <span>月卡列表</span>
          <el-button type="primary" @click="dialogVisible = true">新增月卡</el-button>
        </div>
      </template>
      
      <el-table :data="monthlyCards" border style="width: 100%">
        <el-table-column prop="plateNumber" label="车牌号" width="120" />
        <el-table-column prop="ownerName" label="车主姓名" width="100" />
        <el-table-column prop="ownerPhone" label="联系电话" width="130" />
        <el-table-column prop="startDate" label="开始日期" width="120" />
        <el-table-column prop="endDate" label="结束日期" width="120" />
        <el-table-column prop="monthlyFee" label="月费" width="80">
          <template #default="scope">
            ¥{{ scope.row.monthlyFee }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="getStatusTag(scope.row.status)">
              {{ getStatusName(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button 
              type="success" 
              size="small" 
              @click="showRenewDialog(scope.row)"
              :disabled="scope.row.status === 'CANCELLED'">
              续费
            </el-button>
            <el-button 
              type="danger" 
              size="small" 
              @click="handleCancel(scope.row)"
              :disabled="scope.row.status === 'CANCELLED'">
              注销
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    
    <el-dialog
      v-model="dialogVisible"
      title="新增月卡"
      width="500px"
    >
      <el-form :model="cardForm" label-width="100px">
        <el-form-item label="车牌号">
          <el-input v-model="cardForm.plateNumber" placeholder="请输入车牌号" />
        </el-form-item>
        <el-form-item label="车主姓名">
          <el-input v-model="cardForm.ownerName" placeholder="请输入车主姓名" />
        </el-form-item>
        <el-form-item label="联系电话">
          <el-input v-model="cardForm.ownerPhone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="月费">
          <el-input-number v-model="cardForm.monthlyFee" :min="0" :default-value="300" />
        </el-form-item>
        <el-form-item label="开始日期">
          <el-date-picker
            v-model="cardForm.startDate"
            type="date"
            placeholder="选择开始日期"
            style="width: 100%;"
          />
        </el-form-item>
        <el-form-item label="结束日期">
          <el-date-picker
            v-model="cardForm.endDate"
            type="date"
            placeholder="选择结束日期"
            style="width: 100%;"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleCreate">确定</el-button>
      </template>
    </el-dialog>
    
    <el-dialog
      v-model="renewDialogVisible"
      title="续费月卡"
      width="400px"
    >
      <el-descriptions :column="1" border>
        <el-descriptions-item label="车牌号">{{ currentCard?.plateNumber }}
        </el-descriptions-item>
        <el-descriptions-item label="当前有效期">
          {{ currentCard?.startDate }} 至 {{ currentCard?.endDate }}
        </el-descriptions-item>
      </el-descriptions>
      <el-form label-width="80px" style="margin-top: 20px;">
        <el-form-item label="续期月数">
          <el-input-number v-model="renewMonths" :min="1" :default-value="1" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="renewDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleRenew">确定续费</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { monthlyCardApi } from '../api'

const monthlyCards = ref([])
const dialogVisible = ref(false)
const renewDialogVisible = ref(false)
const currentCard = ref(null)
const renewMonths = ref(1)

const cardForm = ref({
  plateNumber: '',
  ownerName: '',
  ownerPhone: '',
  monthlyFee: 300,
  startDate: null,
  endDate: null
})

const getStatusTag = (status) => {
  const tags = {
    'ACTIVE': 'success',
    'EXPIRED': 'warning',
    'CANCELLED': 'danger'
  }
  return tags[status] || 'info'
}

const getStatusName = (status) => {
  const names = {
    'ACTIVE': '生效中',
    'EXPIRED': '已过期',
    'CANCELLED': '已注销'
  }
  return names[status] || status
}

const loadMonthlyCards = async () => {
  try {
    const res = await monthlyCardApi.list()
    if (res.code === 200) {
      monthlyCards.value = res.data
    }
  } catch (error) {
    console.error('加载月卡失败:', error)
  }
}

const handleCreate = async () => {
  if (!cardForm.value.plateNumber) {
    ElMessage.warning('请输入车牌号')
    return
  }
  
  try {
    const res = await monthlyCardApi.create(cardForm.value)
    if (res.code === 200) {
      ElMessage.success('创建成功')
      dialogVisible.value = false
      cardForm.value = {
        plateNumber: '',
        ownerName: '',
        ownerPhone: '',
        monthlyFee: 300,
        startDate: null,
        endDate: null
      }
      loadMonthlyCards()
    }
  } catch (error) {
    ElMessage.error('创建失败')
  }
}

const showRenewDialog = (card) => {
  currentCard.value = card
  renewMonths.value = 1
  renewDialogVisible.value = true
}

const handleRenew = async () => {
  try {
    const res = await monthlyCardApi.renew({
      cardId: currentCard.value.id,
      months: renewMonths.value
    })
    if (res.code === 200) {
      ElMessage.success('续费成功')
      renewDialogVisible.value = false
      loadMonthlyCards()
    }
  } catch (error) {
    ElMessage.error('续费失败')
  }
}

const handleCancel = async (card) => {
  try {
    await ElMessageBox.confirm('确定要注销该月卡吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const res = await monthlyCardApi.cancel({ cardId: card.id })
    if (res.code === 200) {
      ElMessage.success('注销成功')
      loadMonthlyCards()
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('注销失败')
    }
  }
}

onMounted(() => {
  loadMonthlyCards()
})
</script>

<style scoped>
.monthly-card-management {
  padding: 0;
}

.page-title {
  margin-bottom: 20px;
  font-size: 20px;
  font-weight: 500;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
