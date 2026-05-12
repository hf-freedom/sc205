<template>
  <div class="space-management">
    <h2 class="page-title">车位管理</h2>
    
    <el-card style="margin-bottom: 20px;">
      <template #header>
        <div class="card-header">
          <span>车位状态统计</span>
          <el-button type="primary" size="small" @click="loadData">刷新数据</el-button>
        </div>
      </template>
      <el-row :gutter="20">
        <el-col :span="4">
          <el-statistic title="总车位" :value="totalSpaces" />
        </el-col>
        <el-col :span="4">
          <el-statistic title="空闲" :value="availableCount">
            <template #prefix>
              <el-icon style="color: #67c23a"><SuccessFilled /></el-icon>
            </template>
          </el-statistic>
        </el-col>
        <el-col :span="4">
          <el-statistic title="占用" :value="occupiedCount">
            <template #prefix>
              <el-icon style="color: #409eff"><CircleCheckFilled /></el-icon>
            </template>
          </el-statistic>
        </el-col>
        <el-col :span="4">
          <el-statistic title="已锁定" :value="lockedCount">
            <template #prefix>
              <el-icon style="color: #909399"><LockFilled /></el-icon>
            </template>
          </el-statistic>
        </el-col>
        <el-col :span="4">
          <el-statistic title="维修中" :value="maintenanceCount">
            <template #prefix>
              <el-icon style="color: #f56c6c"><WarningFilled /></el-icon>
            </template>
          </el-statistic>
        </el-col>
        <el-col :span="4">
          <el-statistic title="已预约" :value="reservedCount">
            <template #prefix>
              <el-icon style="color: #e6a23c"><CalendarFilled /></el-icon>
            </template>
          </el-statistic>
        </el-col>
      </el-row>
    </el-card>
    
    <el-card>
      <template #header>
        <div class="card-header">
          <span>车位列表</span>
          <div>
            <el-tag effect="light" class="status-tag" style="background: #f0f9eb; color: #67c23a; margin-right: 10px;">空闲</el-tag>
            <el-tag effect="light" class="status-tag" style="background: #ecf5ff; color: #409eff; margin-right: 10px;">占用</el-tag>
            <el-tag effect="light" class="status-tag" style="background: #fdf6ec; color: #e6a23c; margin-right: 10px;">预约</el-tag>
            <el-tag effect="light" class="status-tag" style="background: #f4f4f5; color: #909399; margin-right: 10px;">锁定</el-tag>
            <el-tag effect="light" class="status-tag" style="background: #fef0f0; color: #f56c6c;">维修</el-tag>
          </div>
        </div>
      </template>
      
      <div class="space-grid">
        <el-card 
          v-for="space in spaces" 
          :key="space.id" 
          class="space-card"
          :class="['status-' + space.status.toLowerCase()]">
          <div class="space-header">
            <span class="space-no">{{ space.spaceNo }}</span>
            <el-tag size="small" :type="getSpaceTypeTag(space.spaceType)">
              {{ getSpaceTypeName(space.spaceType) }}
            </el-tag>
          </div>
          <div class="space-status">
            <el-tag :type="getStatusTag(space.status)" effect="dark" size="small">
              {{ getStatusName(space.status) }}
            </el-tag>
          </div>
          <div class="space-zone">{{ space.zone }}</div>
          <div v-if="space.plateNumber" class="space-plate">
            {{ space.plateNumber }}
          </div>
          <div v-if="space.occupiedDuration" class="space-duration">
            <span style="color: #f56c6c;">已停放 {{ Math.round(space.occupiedDuration / 60) }}小时</span>
          </div>
          <div class="space-actions">
            <el-dropdown @command="(command) => handleCommand(command, space)">
              <el-button type="primary" size="small" plain>
                操作
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item 
                    v-if="space.status === 'AVAILABLE'" 
                    command="lock">
                    锁定车位
                  </el-dropdown-item>
                  <el-dropdown-item 
                    v-if="space.status === 'LOCKED'" 
                    command="unlock">
                    解锁车位
                  </el-dropdown-item>
                  <el-dropdown-item 
                    v-if="space.status === 'AVAILABLE' || space.status === 'LOCKED'" 
                    command="maintenance">
                    设置维修
                  </el-dropdown-item>
                  <el-dropdown-item 
                    v-if="space.status === 'MAINTENANCE'" 
                    command="release-maintenance">
                    解除维修
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </el-card>
      </div>
    </el-card>
    
    <el-card style="margin-top: 20px;">
      <template #header>
        <div class="card-header">
          <span>扫描日志</span>
          <el-button type="primary" size="small" @click="loadScanLogs">刷新日志</el-button>
        </div>
      </template>
      <el-table :data="scanLogs" border style="width: 100%;" size="small">
        <el-table-column prop="scanTime" label="扫描时间" width="180" />
        <el-table-column prop="scanType" label="扫描类型" width="150">
          <template #default="scope">
            <el-tag :type="getScanTypeTag(scope.row.scanType)" size="small">
              {{ scope.row.scanType }}
            </el-tag>
          </template>
        </el-table-column>
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
import { ElMessage } from 'element-plus'
import { SuccessFilled, CircleCheckFilled, LockFilled, WarningFilled, CalendarFilled } from '@element-plus/icons-vue'
import { spaceApi, scanLogApi } from '../api'

const spaces = ref([])
const scanLogs = ref([])
let refreshInterval = null

const totalSpaces = computed(() => spaces.value.length)
const availableCount = computed(() => spaces.value.filter(s => s.status === 'AVAILABLE').length)
const occupiedCount = computed(() => spaces.value.filter(s => s.status === 'OCCUPIED').length)
const lockedCount = computed(() => spaces.value.filter(s => s.status === 'LOCKED').length)
const maintenanceCount = computed(() => spaces.value.filter(s => s.status === 'MAINTENANCE').length)
const reservedCount = computed(() => spaces.value.filter(s => s.status === 'RESERVED').length)

const getSpaceTypeTag = (type) => {
  const tags = {
    'NORMAL': '',
    'TEMPORARY': 'warning',
    'MONTHLY': 'success'
  }
  return tags[type] || ''
}

const getSpaceTypeName = (type) => {
  const names = {
    'NORMAL': '普通车位',
    'TEMPORARY': '临时车位',
    'MONTHLY': '月卡车位'
  }
  return names[type] || type
}

const getStatusTag = (status) => {
  const tags = {
    'AVAILABLE': 'success',
    'OCCUPIED': 'primary',
    'LOCKED': 'info',
    'MAINTENANCE': 'danger',
    'RESERVED': 'warning'
  }
  return tags[status] || 'info'
}

const getStatusName = (status) => {
  const names = {
    'AVAILABLE': '空闲',
    'OCCUPIED': '占用',
    'LOCKED': '锁定',
    'MAINTENANCE': '维修中',
    'RESERVED': '已预约'
  }
  return names[status] || status
}

const getScanTypeTag = (type) => {
  const tags = {
    '预约超时扫描': 'warning',
    '长期占用扫描': 'danger',
    '维修状态扫描': 'info',
    '月卡状态扫描': 'success'
  }
  return tags[type] || ''
}

const loadData = async () => {
  try {
    const res = await spaceApi.list()
    if (res.code === 200) {
      spaces.value = res.data
    }
  } catch (error) {
    console.error('加载车位失败:', error)
  }
}

const loadScanLogs = async () => {
  try {
    const res = await scanLogApi.list()
    if (res.code === 200) {
      scanLogs.value = res.data
    }
  } catch (error) {
    console.error('加载扫描日志失败:', error)
  }
}

const handleCommand = async (command, space) => {
  try {
    let res
    switch (command) {
      case 'lock':
        res = await spaceApi.lock({ spaceId: space.id })
        break
      case 'unlock':
        res = await spaceApi.unlock({ spaceId: space.id })
        break
      case 'maintenance':
        res = await spaceApi.maintenance({ spaceId: space.id })
        break
      case 'release-maintenance':
        res = await spaceApi.releaseMaintenance({ spaceId: space.id })
        break
    }
    
    if (res.code === 200) {
      ElMessage.success('操作成功')
      loadData()
    } else {
      ElMessage.error(res.message || '操作失败')
    }
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

onMounted(() => {
  loadData()
  loadScanLogs()
  refreshInterval = setInterval(() => {
    loadData()
    loadScanLogs()
  }, 10000)
})

onUnmounted(() => {
  if (refreshInterval) {
    clearInterval(refreshInterval)
  }
})
</script>

<style scoped>
.space-management {
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

.space-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
  gap: 15px;
}

.space-card {
  padding: 15px;
  border-radius: 8px;
  transition: transform 0.2s, box-shadow 0.2s;
}

.space-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}

.space-card.status-available {
  border-left: 4px solid #67c23a;
}

.space-card.status-occupied {
  border-left: 4px solid #409eff;
}

.space-card.status-reserved {
  border-left: 4px solid #e6a23c;
}

.space-card.status-locked {
  border-left: 4px solid #909399;
}

.space-card.status-maintenance {
  border-left: 4px solid #f56c6c;
}

.space-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.space-no {
  font-size: 18px;
  font-weight: bold;
  color: #303133;
}

.space-status {
  margin-bottom: 10px;
}

.space-zone {
  font-size: 13px;
  color: #606266;
  margin-bottom: 5px;
}

.space-plate {
  font-size: 13px;
  color: #409eff;
  margin-bottom: 10px;
  font-weight: 500;
}

.space-duration {
  font-size: 12px;
  margin-bottom: 10px;
}

.space-actions {
  margin-top: 10px;
}

.detail-item {
  padding: 4px 0;
  font-size: 12px;
  color: #606266;
}
</style>
