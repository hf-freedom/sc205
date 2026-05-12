<template>
  <div class="statistics">
    <h2 class="page-title">统计报表</h2>
    
    <el-row :gutter="20" class="stats-grid">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon" style="background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%);">
            <span style="color: white; font-weight: bold; font-size: 24px;">%</span>
          </div>
          <div class="stat-info">
            <div class="stat-label">车位使用率</div>
            <div class="stat-value">{{ stats.spaceUtilizationRate || 0 }}%</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon" style="background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);">
            <span style="color: white; font-weight: bold; font-size: 24px;">¥</span>
          </div>
          <div class="stat-info">
            <div class="stat-label">临停收入</div>
            <div class="stat-value">¥{{ stats.temporaryParkingIncome || 0 }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon" style="background: linear-gradient(135deg, #e6a23c 0%, #ebb563 100%);">
            <span style="color: white; font-weight: bold; font-size: 24px;">卡</span>
          </div>
          <div class="stat-info">
            <div class="stat-label">月卡收入</div>
            <div class="stat-value">¥{{ stats.monthlyCardIncome || 0 }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon" style="background: linear-gradient(135deg, #f56c6c 0%, #f78989 100%);">
            <span style="color: white; font-weight: bold; font-size: 24px;">!</span>
          </div>
          <div class="stat-info">
            <div class="stat-label">异常放行次数</div>
            <div class="stat-value">{{ stats.abnormalReleaseCount || 0 }}</div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>车位状态统计</span>
          </template>
          <el-table :data="spaceStatusList" border style="width: 100%">
            <el-table-column prop="name" label="状态" width="120" />
            <el-table-column prop="count" label="数量" width="120" />
            <el-table-column label="占比">
              <template #default="scope">
                <el-progress 
                  :percentage="Math.round(scope.row.count / (stats.totalSpaces || 1) * 100)" 
                  :stroke-color="scope.row.color"
                />
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>收入统计</span>
          </template>
          <el-table :data="incomeList" border style="width: 100%">
            <el-table-column prop="name" label="收入类型" width="120" />
            <el-table-column prop="amount" label="金额" width="150">
              <template #default="scope">
                ¥{{ scope.row.amount }}
              </template>
            </el-table-column>
            <el-table-column label="占比">
              <template #default="scope">
                <el-progress 
                  :percentage="Math.round(scope.row.amount / (stats.totalIncome || 1) * 100)" 
                  :stroke-color="scope.row.color"
                />
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
    
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>车辆统计</span>
          </template>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="总车辆数">
              {{ stats.totalVehicles || 0 }}
            </el-descriptions-item>
            <el-descriptions-item label="月卡车辆占比">
              {{ stats.monthlyCardRatio || 0 }}%
            </el-descriptions-item>
            <el-descriptions-item label="当前在场车辆">
              {{ stats.currentParkingVehicles || 0 }}
            </el-descriptions-item>
            <el-descriptions-item label="有效月卡数量">
              {{ stats.monthlyCardCount || 0 }}
            </el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>车位统计</span>
          </template>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="总车位">
              {{ stats.totalSpaces || 0 }}
            </el-descriptions-item>
            <el-descriptions-item label="可用车位">
              {{ stats.availableSpaces || 0 }}
            </el-descriptions-item>
            <el-descriptions-item label="已占用">
              {{ stats.occupiedSpaces || 0 }}
            </el-descriptions-item>
            <el-descriptions-item label="已锁定">
              {{ stats.lockedSpaces || 0 }}
            </el-descriptions-item>
            <el-descriptions-item label="维修中">
              {{ stats.maintenanceSpaces || 0 }}
            </el-descriptions-item>
            <el-descriptions-item label="已预约">
              {{ stats.reservedSpaces || 0 }}
            </el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { statisticsApi } from '../api'

const stats = ref({})

const spaceStatusList = computed(() => [
  { name: '空闲', count: stats.value.availableSpaces || 0, color: '#67c23a' },
  { name: '占用', count: stats.value.occupiedSpaces || 0, color: '#409eff' },
  { name: '锁定', count: stats.value.lockedSpaces || 0, color: '#909399' },
  { name: '维修', count: stats.value.maintenanceSpaces || 0, color: '#f56c6c' },
  { name: '预约', count: stats.value.reservedSpaces || 0, color: '#e6a23c' }
])

const incomeList = computed(() => [
  { name: '临停收入', amount: stats.value.temporaryParkingIncome || 0, color: '#409eff' },
  { name: '月卡收入', amount: stats.value.monthlyCardIncome || 0, color: '#e6a23c' }
])

const loadStatistics = async () => {
  try {
    const res = await statisticsApi.get()
    if (res.code === 200) {
      stats.value = res.data
    }
  } catch (error) {
    console.error('加载统计失败:', error)
  }
}

onMounted(() => {
  loadStatistics()
})
</script>

<style scoped>
.statistics {
  padding: 0;
}

.page-title {
  margin-bottom: 20px;
  font-size: 20px;
  font-weight: 500;
}

.stats-grid {
  margin-bottom: 20px;
}

.stat-card {
  border-radius: 8px;
}

.stat-card .el-card__body {
  display: flex;
  align-items: center;
  padding: 20px;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  margin-right: 20px;
}

.stat-info {
  flex: 1;
}

.stat-label {
  font-size: 14px;
  color: #606266;
  margin-bottom: 5px;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
}
</style>
