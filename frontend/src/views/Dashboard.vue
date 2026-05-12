<template>
  <div class="dashboard">
    <h2 class="page-title">实时监控</h2>
    
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card available">
          <div class="stat-content">
            <div class="stat-number">{{ stats.availableSpaces || 0 }}</div>
            <div class="stat-label">空闲车位</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card occupied">
          <div class="stat-content">
            <div class="stat-number">{{ stats.occupiedSpaces || 0 }}</div>
            <div class="stat-label">占用车位</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card reserved">
          <div class="stat-content">
            <div class="stat-number">{{ stats.reservedSpaces || 0 }}</div>
            <div class="stat-label">预约车位</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card current">
          <div class="stat-content">
            <div class="stat-number">{{ stats.currentParkingVehicles || 0 }}</div>
            <div class="stat-label">在场车辆</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="space-row">
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>车位状态分布</span>
            </div>
          </template>
          <div class="space-grid">
            <div 
              v-for="space in spaces" 
              :key="space.id" 
              class="space-item"
              :class="getStatusClass(space.status)"
            >
              <div class="space-no">{{ space.spaceNo }}</div>
              <div class="space-plate" v-if="space.plateNumber">{{ space.plateNumber }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>在场车辆</span>
            </div>
          </template>
          <el-table :data="activeVehicles" border style="width: 100%" size="small">
            <el-table-column prop="plateNumber" label="车牌号" width="120" />
            <el-table-column prop="vehicleType" label="车辆类型" width="100">
              <template #default="scope">
                <el-tag :type="getVehicleTypeTag(scope.row.vehicleType)">
                  {{ getVehicleTypeName(scope.row.vehicleType) }}
                </el-tag>
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
            <el-table-column prop="parkingSpaceId" label="车位" width="80" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { statisticsApi, spaceApi, vehicleApi } from '../api'

const stats = ref({})
const spaces = ref([])
const activeVehicles = ref([])
let refreshInterval = null

const loadData = async () => {
  try {
    const [statsRes, spacesRes, vehiclesRes] = await Promise.all([
      statisticsApi.get(),
      spaceApi.list(),
      vehicleApi.getActive()
    ])
    
    if (statsRes.code === 200) {
      stats.value = statsRes.data
    }
    if (spacesRes.code === 200) {
      spaces.value = spacesRes.data
    }
    if (vehiclesRes.code === 200) {
      activeVehicles.value = vehiclesRes.data
    }
  } catch (error) {
    console.error('加载数据失败:', error)
  }
}

const getStatusClass = (status) => {
  const classes = {
    'AVAILABLE': 'available',
    'OCCUPIED': 'occupied',
    'LOCKED': 'locked',
    'MAINTENANCE': 'maintenance',
    'RESERVED': 'reserved'
  }
  return classes[status] || ''
}

const getVehicleTypeTag = (type) => {
  const tags = {
    'NORMAL': '',
    'TEMPORARY': 'warning',
    'MONTHLY': 'success'
  }
  return tags[type] || ''
}

const getVehicleTypeName = (type) => {
  const names = {
    'NORMAL': '普通',
    'TEMPORARY': '临时',
    'MONTHLY': '月卡'
  }
  return names[type] || type
}

onMounted(() => {
  loadData()
  refreshInterval = setInterval(loadData, 5000)
})

onUnmounted(() => {
  if (refreshInterval) {
    clearInterval(refreshInterval)
  }
})
</script>

<style scoped>
.dashboard {
  padding: 0;
}

.page-title {
  margin-bottom: 20px;
  font-size: 20px;
  font-weight: 500;
}

.stats-row {
  margin-bottom: 20px;
}

.stat-card {
  height: 120px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.stat-card.available {
  background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%);
  color: #fff;
  border: none;
}

.stat-card.occupied {
  background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
  color: #fff;
  border: none;
}

.stat-card.reserved {
  background: linear-gradient(135deg, #e6a23c 0%, #ebb563 100%);
  color: #fff;
  border: none;
}

.stat-card.current {
  background: linear-gradient(135deg, #f56c6c 0%, #f78989 100%);
  color: #fff;
  border: none;
}

.stat-content {
  text-align: center;
}

.stat-number {
  font-size: 36px;
  font-weight: bold;
}

.stat-label {
  font-size: 14px;
  margin-top: 5px;
}

.space-row {
  margin-bottom: 20px;
}

.card-header {
  font-size: 16px;
  font-weight: 500;
}

.space-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  max-height: 400px;
  overflow-y: auto;
}

.space-item {
  width: 70px;
  height: 70px;
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  color: #fff;
  cursor: pointer;
  transition: transform 0.2s;
}

.space-item:hover {
  transform: scale(1.05);
}

.space-item.available {
  background-color: #67c23a;
}

.space-item.occupied {
  background-color: #409eff;
}

.space-item.locked {
  background-color: #909399;
}

.space-item.maintenance {
  background-color: #f56c6c;
}

.space-item.reserved {
  background-color: #e6a23c;
}

.space-no {
  font-weight: bold;
  font-size: 13px;
}

.space-plate {
  font-size: 10px;
  margin-top: 3px;
  opacity: 0.9;
}
</style>
