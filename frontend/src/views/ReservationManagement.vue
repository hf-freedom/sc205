<template>
  <div class="reservation-management">
    <h2 class="page-title">预约管理</h2>
    
    <el-card style="margin-bottom: 20px;">
      <template #header>
        <div class="card-header">
          <span>预约状态统计</span>
          <el-button type="primary" size="small" @click="loadData">刷新数据</el-button>
        </div>
      </template>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-statistic title="总预约" :value="totalReservations" />
        </el-col>
        <el-col :span="6">
          <el-statistic title="有效预约" :value="activeCount">
            <template #prefix>
              <el-icon style="color: #67c23a"><SuccessFilled /></el-icon>
            </template>
          </el-statistic>
        </el-col>
        <el-col :span="6">
          <el-statistic title="已使用" :value="usedCount">
            <template #prefix>
              <el-icon style="color: #409eff"><CircleCheckFilled /></el-icon>
            </template>
          </el-statistic>
        </el-col>
        <el-col :span="6">
          <el-statistic title="已过期" :value="expiredCount">
            <template #prefix>
              <el-icon style="color: #f56c6c"><WarningFilled /></el-icon>
            </template>
          </el-statistic>
        </el-col>
      </el-row>
    </el-card>
    
    <el-card style="margin-bottom: 20px;">
      <template #header>
        <div class="card-header">
          <span>创建预约</span>
        </div>
      </template>
      
      <el-form :model="reservationForm" label-width="120px" :inline="true">
        <el-form-item label="车牌号">
          <el-input v-model="reservationForm.plateNumber" placeholder="请输入车牌号" />
        </el-form-item>
        <el-form-item label="车辆类型">
          <el-select v-model="reservationForm.vehicleType" placeholder="请选择" style="width: 150px;">
            <el-option label="普通车辆" value="NORMAL" />
            <el-option label="临时车辆" value="TEMPORARY" />
            <el-option label="月卡车辆" value="MONTHLY" />
          </el-select>
        </el-form-item>
        <el-form-item label="车位">
          <el-select v-model="reservationForm.parkingSpaceId" placeholder="请选择" style="width: 200px;">
            <el-option 
              v-for="space in availableSpaces" 
              :key="space.id" 
              :label="space.spaceNo + ' (' + getSpaceTypeName(space.spaceType) + ' - ' + space.zone + ')'" 
              :value="space.id" 
            />
          </el-select>
        </el-form-item>
        <el-form-item label="预计入场时间">
          <el-date-picker
            v-model="reservationForm.expectedEntryTime"
            type="datetime"
            placeholder="选择日期时间"
            style="width: 220px;"
          />
        </el-form-item>
        <el-form-item label="联系电话">
          <el-input v-model="reservationForm.contactPhone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleCreate">创建预约</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    
    <el-card>
      <template #header>
        <div class="card-header">
          <span>预约列表</span>
          <div>
            <el-tag effect="light" style="background: #f0f9eb; color: #67c23a; margin-right: 10px;">有效</el-tag>
            <el-tag effect="light" style="background: #ecf5ff; color: #409eff; margin-right: 10px;">已使用</el-tag>
            <el-tag effect="light" style="background: #f4f4f5; color: #909399; margin-right: 10px;">已取消</el-tag>
            <el-tag effect="light" style="background: #fef0f0; color: #f56c6c;">已过期</el-tag>
          </div>
        </div>
      </template>
      
      <el-table :data="reservations" border style="width: 100%">
        <el-table-column prop="plateNumber" label="车牌号" width="120" />
        <el-table-column prop="vehicleType" label="车辆类型" width="120">
          <template #default="scope">
            {{ getVehicleTypeName(scope.row.vehicleType) }}
          </template>
        </el-table-column>
        <el-table-column prop="parkingSpaceId" label="车位" width="100" />
        <el-table-column prop="expectedEntryTime" label="预计入场" width="180" />
        <el-table-column prop="expiredTime" label="过期时间" width="180" />
        <el-table-column prop="actualEntryTime" label="实际入场" width="180" />
        <el-table-column prop="contactPhone" label="联系电话" width="130" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="getStatusTag(scope.row.status)">
              {{ getStatusName(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="scope">
            <el-button 
              type="danger" 
              size="small" 
              @click="handleCancel(scope.row)"
              :disabled="scope.row.status !== 'ACTIVE'">
              取消预约
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    
    <el-card style="margin-top: 20px;">
      <template #header>
        <div class="card-header">
          <span>扫描日志（预约超时扫描）</span>
          <el-button type="primary" size="small" @click="loadReservationScanLogs">刷新日志</el-button>
        </div>
      </template>
      <el-table :data="reservationScanLogs" border style="width: 100%;" size="small">
        <el-table-column prop="scanTime" label="扫描时间" width="180" />
        <el-table-column prop="totalScanned" label="扫描数量" width="100" align="center" />
        <el-table-column prop="changedCount" label="变更数量" width="100" align="center">
          <template #default="scope">
            <el-tag :type="scope.row.changedCount > 0 ? 'danger' : 'success'" size="small">
              {{ scope.row.changedCount }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="changedDetails" label="变更详情" min-width="300">
          <template #default="scope">
            <div v-if="scope.row.changedDetails && scope.row.changedDetails.length > 0">
              <div v-for="(detail, index) in scope.row.changedDetails" :key="index" class="detail-item">
                {{ detail }}
              </div>
            </div>
            <div v-else style="color: #909399;">无变更</div>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { SuccessFilled, CircleCheckFilled, WarningFilled } from '@element-plus/icons-vue'
import { reservationApi, spaceApi, scanLogApi } from '../api'

const reservations = ref([])
const availableSpaces = ref([])
const reservationScanLogs = ref([])
let refreshInterval = null

const reservationForm = ref({
  plateNumber: '',
  vehicleType: 'NORMAL',
  parkingSpaceId: '',
  expectedEntryTime: null,
  contactPhone: ''
})

const totalReservations = computed(() => reservations.value.length)
const activeCount = computed(() => reservations.value.filter(r => r.status === 'ACTIVE').length)
const usedCount = computed(() => reservations.value.filter(r => r.status === 'USED').length)
const expiredCount = computed(() => reservations.value.filter(r => r.status === 'EXPIRED').length)

const getSpaceTypeName = (type) => {
  const names = {
    'NORMAL': '普通',
    'TEMPORARY': '临停',
    'MONTHLY': '月卡'
  }
  return names[type] || type
}

const getVehicleTypeName = (type) => {
  const names = {
    'NORMAL': '普通车辆',
    'TEMPORARY': '临时车辆',
    'MONTHLY': '月卡车辆'
  }
  return names[type] || type
}

const getStatusTag = (status) => {
  const tags = {
    'ACTIVE': 'success',
    'USED': 'primary',
    'CANCELLED': 'info',
    'EXPIRED': 'danger'
  }
  return tags[status] || 'info'
}

const getStatusName = (status) => {
  const names = {
    'ACTIVE': '有效',
    'USED': '已使用',
    'CANCELLED': '已取消',
    'EXPIRED': '已过期'
  }
  return names[status] || status
}

const loadData = async () => {
  try {
    const [resRes, spaceRes] = await Promise.all([
      reservationApi.list(),
      spaceApi.list()
    ])
    
    if (resRes.code === 200) {
      reservations.value = resRes.data
    }
    
    if (spaceRes.code === 200) {
      availableSpaces.value = spaceRes.data.filter(s => s.status === 'AVAILABLE')
    }
  } catch (error) {
    console.error('加载数据失败:', error)
  }
}

const loadReservationScanLogs = async () => {
  try {
    const res = await scanLogApi.byType('预约超时扫描')
    if (res.code === 200) {
      reservationScanLogs.value = res.data
    }
  } catch (error) {
    console.error('加载扫描日志失败:', error)
  }
}

const handleCreate = async () => {
  if (!reservationForm.value.plateNumber) {
    ElMessage.warning('请输入车牌号')
    return
  }
  if (!reservationForm.value.parkingSpaceId) {
    ElMessage.warning('请选择车位')
    return
  }
  if (!reservationForm.value.expectedEntryTime) {
    ElMessage.warning('请选择预计入场时间')
    return
  }
  
  try {
    const formData = {
      ...reservationForm.value,
      expectedEntryTime: reservationForm.value.expectedEntryTime.toISOString()
    }
    
    const res = await reservationApi.create(formData)
    if (res.code === 200) {
      ElMessage.success('预约成功')
      reservationForm.value = {
        plateNumber: '',
        vehicleType: 'NORMAL',
        parkingSpaceId: '',
        expectedEntryTime: null,
        contactPhone: ''
      }
      loadData()
    } else {
      ElMessage.error(res.message || '预约失败')
    }
  } catch (error) {
    ElMessage.error('预约失败')
  }
}

const handleCancel = async (reservation) => {
  try {
    await ElMessageBox.confirm('确定要取消该预约吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const res = await reservationApi.cancel({ reservationId: reservation.id })
    if (res.code === 200) {
      ElMessage.success('取消成功')
      loadData()
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('取消失败')
    }
  }
}

onMounted(() => {
  loadData()
  loadReservationScanLogs()
  refreshInterval = setInterval(() => {
    loadData()
    loadReservationScanLogs()
  }, 10000)
})

onUnmounted(() => {
  if (refreshInterval) {
    clearInterval(refreshInterval)
  }
})
</script>

<style scoped>
.reservation-management {
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

.detail-item {
  padding: 4px 0;
  font-size: 12px;
  color: #606266;
}
</style>
