<template>
  <div class="vehicle-management">
    <h2 class="page-title">车辆管理</h2>
    
    <el-card class="action-card">
      <el-row :gutter="20">
        <el-col :span="8">
          <h3>车辆入场</h3>
          <el-form :model="entryForm" label-width="80px">
            <el-form-item label="车牌号">
              <el-input v-model="entryForm.plateNumber" placeholder="请输入车牌号" />
            </el-form-item>
            <el-form-item label="车辆类型">
              <el-select v-model="entryForm.vehicleType" placeholder="请选择车辆类型">
                <el-option label="普通车辆" value="NORMAL" />
                <el-option label="临时车辆" value="TEMPORARY" />
                <el-option label="月卡车辆" value="MONTHLY" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleEntry">车辆入场</el-button>
            </el-form-item>
          </el-form>
        </el-col>
        
        <el-col :span="8">
          <h3>车辆出场</h3>
          <el-form :model="exitForm" label-width="80px">
            <el-form-item label="车牌号">
              <el-input v-model="exitForm.plateNumber" placeholder="请输入车牌号" />
            </el-form-item>
            <el-form-item>
              <el-button type="success" @click="handleExit">确认出场</el-button>
            </el-form-item>
          </el-form>
        </el-col>
        
        <el-col :span="8">
          <h3>异常放行</h3>
          <el-form :model="abnormalForm" label-width="80px">
            <el-form-item label="车牌号">
              <el-input v-model="abnormalForm.plateNumber" placeholder="请输入车牌号" />
            </el-form-item>
            <el-form-item label="放行原因">
              <el-input 
                v-model="abnormalForm.reason" 
                type="textarea" 
                :rows="2" 
                placeholder="请输入放行原因"
              />
            </el-form-item>
            <el-form-item label="操作人">
              <el-input v-model="abnormalForm.operator" placeholder="请输入操作人" />
            </el-form-item>
            <el-form-item label="减免金额">
              <el-input-number 
                v-model="abnormalForm.waivedFee" :min="0" />
            </el-form-item>
            <el-form-item>
              <el-button type="warning" @click="handleAbnormal">异常放行</el-button>
            </el-form-item>
          </el-form>
        </el-col>
      </el-row>
    </el-card>
    
    <el-card style="margin-top: 20px">
      <template #header>
        <div class="card-header">
          <span>在场车辆列表</span>
          <el-button type="primary" size="small" @click="loadActiveVehicles">刷新</el-button>
        </div>
      </template>
      
      <el-table :data="activeVehicles" border style="width: 100%">
        <el-table-column prop="plateNumber" label="车牌号" width="120" />
        <el-table-column prop="vehicleType" label="车辆类型" width="100">
          <template #default="scope">
            {{ getVehicleTypeName(scope.row.vehicleType) }}
          </template>
        </el-table-column>
        <el-table-column prop="billingType" label="计费类型" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.billingType === 'MONTHLY' ? 'success' : 'warning'">
              {{ scope.row.billingType === 'MONTHLY' ? '月卡' : '临停' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="entryTime" label="入场时间" min-width="180" />
        <el-table-column prop="parkingSpaceId" label="车位号" width="100" />
      </el-table>
    </el-card>
    
    <el-dialog
      v-model="exitDialogVisible"
      title="出场收费信息"
      width="400px"
    >
      <el-descriptions :column="1" border>
        <el-descriptions-item label="车牌号">{{ exitResult.record?.plateNumber }}
        </el-descriptions-item>
        <el-descriptions-item label="入场时间">{{ exitResult.record?.entryTime }}
        </el-descriptions-item>
        <el-descriptions-item label="出场时间">{{ exitResult.record?.exitTime }}
        </el-descriptions-item>
        <el-descriptions-item label="停车时长">{{ formatDuration(exitResult.durationMinutes) }}分钟
        </el-descriptions-item>
        <el-descriptions-item label="是否月卡">
          <el-tag :type="exitResult.isMonthlyCard ? 'success' : 'warning'">
            {{ exitResult.isMonthlyCard ? '是' : '否' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="应收金额">
          <span style="color: #f56c6c; font-weight: bold; font-size: 20px;">
            ¥{{ exitResult.totalFee }}
          </span>
        </el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button type="primary" @click="exitDialogVisible = false">确认</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { vehicleApi } from '../api'

const entryForm = ref({
  plateNumber: '',
  vehicleType: 'NORMAL'
})

const exitForm = ref({
  plateNumber: ''
})

const abnormalForm = ref({
  plateNumber: '',
  reason: '',
  operator: '',
  waivedFee: 0
})

const activeVehicles = ref([])
const exitDialogVisible = ref(false)
const exitResult = ref({})

const getVehicleTypeName = (type) => {
  const names = {
    'NORMAL': '普通车辆',
    'TEMPORARY': '临时车辆',
    'MONTHLY': '月卡车辆'
  }
  return names[type] || type
}

const formatDuration = (minutes) => {
  if (!minutes) return 0
  return minutes
}

const handleEntry = async () => {
  if (!entryForm.value.plateNumber) {
    ElMessage.warning('请输入车牌号')
    return
  }
  
  try {
    const res = await vehicleApi.entry(entryForm.value)
    if (res.code === 200) {
      ElMessage.success('车辆入场成功')
      entryForm.value = { plateNumber: '', vehicleType: 'NORMAL' }
      loadActiveVehicles()
    } else {
      ElMessage.error(res.message)
    }
  } catch (error) {
    ElMessage.error('入场失败')
  }
}

const handleExit = async () => {
  if (!exitForm.value.plateNumber) {
    ElMessage.warning('请输入车牌号')
    return
  }
  
  try {
    const res = await vehicleApi.exit(exitForm.value)
    if (res.code === 200) {
      exitResult.value = res.data
      exitDialogVisible.value = true
      exitForm.value = { plateNumber: '' }
      loadActiveVehicles()
    } else {
      ElMessage.error(res.message)
    }
  } catch (error) {
    ElMessage.error('出场失败')
  }
}

const handleAbnormal = async () => {
  if (!abnormalForm.value.plateNumber) {
    ElMessage.warning('请输入车牌号')
    return
  }
  if (!abnormalForm.value.reason) {
    ElMessage.warning('请输入放行原因')
    return
  }
  
  try {
    const res = await vehicleApi.abnormal(abnormalForm.value)
    if (res.code === 200) {
      ElMessage.success('异常放行成功')
      abnormalForm.value = { plateNumber: '', reason: '', operator: '', waivedFee: 0 }
      loadActiveVehicles()
    } else {
      ElMessage.error(res.message)
    }
  } catch (error) {
    ElMessage.error('异常放行失败')
  }
}

const loadActiveVehicles = async () => {
  try {
    const res = await vehicleApi.getActive()
    if (res.code === 200) {
      activeVehicles.value = res.data
    }
  } catch (error) {
    console.error('加载失败:', error)
  }
}

onMounted(() => {
  loadActiveVehicles()
})
</script>

<style scoped>
.vehicle-management {
  padding: 0;
}

.page-title {
  margin-bottom: 20px;
  font-size: 20px;
  font-weight: 500;
}

.action-card {
  margin-bottom: 20px;
}

.action-card h3 {
  margin-bottom: 15px;
  font-size: 16px;
  color: #303133;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
