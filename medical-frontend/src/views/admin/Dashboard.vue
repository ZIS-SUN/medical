<template>
  <div class="dashboard-page">
    <h1 class="page-title">数据看板</h1>

    <!-- 数据卡片 -->
    <el-row :gutter="20" class="stats-cards">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <el-icon :size="40" color="#FF6B00"><Calendar /></el-icon>
            <div class="stat-info">
              <p class="stat-value">{{ stats.todayAppointments }}</p>
              <p class="stat-label">今日预约</p>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <el-icon :size="40" color="#52C41A"><User /></el-icon>
            <div class="stat-info">
              <p class="stat-value">{{ stats.totalDoctors }}</p>
              <p class="stat-label">医生总数</p>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <el-icon :size="40" color="#FAAD14"><UserFilled /></el-icon>
            <div class="stat-info">
              <p class="stat-value">{{ stats.totalUsers }}</p>
              <p class="stat-label">患者总数</p>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <el-icon :size="40" color="#FF4D4F"><ChatDotRound /></el-icon>
            <div class="stat-info">
              <p class="stat-value">{{ stats.totalConsultations }}</p>
              <p class="stat-label">问诊总数</p>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 预约趋势图 -->
    <el-card class="chart-card">
      <h3>预约趋势（近7天）</h3>
      <div ref="chartRef" style="width: 100%; height: 400px;"></div>
    </el-card>

    <!-- 最近预约 -->
    <el-card>
      <h3>最近预约</h3>
      <el-table :data="recentAppointments" style="width: 100%">
        <el-table-column prop="appointmentNo" label="预约单号" width="180" />
        <el-table-column prop="userName" label="患者" width="100" />
        <el-table-column prop="doctorName" label="医生" width="100" />
        <el-table-column prop="departmentName" label="科室" width="100" />
        <el-table-column prop="appointmentDate" label="预约日期" width="120" />
        <el-table-column prop="timeSlot" label="时间段" width="100" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">{{ row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" />
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { getDashboardStats } from '@/api/admin'
import * as echarts from 'echarts'
import {
  Calendar,
  User,
  UserFilled,
  ChatDotRound
} from '@element-plus/icons-vue'

const stats = ref({
  todayAppointments: 0,
  totalDoctors: 0,
  totalUsers: 0,
  totalConsultations: 0
})

const recentAppointments = ref([])
const chartRef = ref()
let chartInstance = null

const getStatusType = (status) => {
  const map = {
    '待确认': 'warning',
    '已确认': 'primary',
    '就诊中': 'info',
    '已完成': 'success',
    '已取消': 'danger'
  }
  return map[status] || 'info'
}

const initChart = (trendData) => {
  if (chartInstance) {
    chartInstance.dispose()
  }

  chartInstance = echarts.init(chartRef.value)

  const option = {
    tooltip: {
      trigger: 'axis'
    },
    xAxis: {
      type: 'category',
      data: trendData.map(item => item.date),
      axisLabel: {
        rotate: 45
      }
    },
    yAxis: {
      type: 'value',
      name: '预约数量'
    },
    series: [{
      name: '预约数',
      data: trendData.map(item => item.count),
      type: 'line',
      smooth: true,
      areaStyle: {
        color: {
          type: 'linear',
          x: 0,
          y: 0,
          x2: 0,
          y2: 1,
          colorStops: [
            { offset: 0, color: 'rgba(255, 107, 0, 0.3)' },
            { offset: 1, color: 'rgba(255, 107, 0, 0.05)' }
          ]
        }
      },
      itemStyle: {
        color: '#FF6B00'
      },
      lineStyle: {
        width: 3
      }
    }]
  }

  chartInstance.setOption(option)
}

onMounted(async () => {
  try {
    const data = await getDashboardStats()
    stats.value = {
      todayAppointments: data.todayAppointments || 0,
      totalDoctors: data.totalDoctors || 0,
      totalUsers: data.totalUsers || 0,
      totalConsultations: data.totalConsultations || 0
    }
    recentAppointments.value = data.recentAppointments || []

    await nextTick()
    if (data.appointmentTrend && data.appointmentTrend.length > 0) {
      initChart(data.appointmentTrend)
    }
  } catch (error) {
    console.error('加载数据失败:', error)
  }
})
</script>

<style scoped>
.dashboard-page {
  animation: fadeIn 0.5s ease-out;
}

.page-title {
  font-size: var(--font-size-xxl);
  font-weight: 700;
  color: var(--text-primary);
  margin-bottom: var(--spacing-xl);
  position: relative;
  padding-left: var(--spacing-md);
}

.page-title::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 5px;
  height: 28px;
  background: var(--primary-gradient);
  border-radius: 3px;
}

.stats-cards {
  margin-bottom: var(--spacing-xl);
}

.stat-card {
  cursor: pointer;
  transition: var(--transition-base);
  border-radius: var(--border-radius-xl);
  border: 2px solid transparent;
  overflow: hidden;
  position: relative;
}

.stat-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: var(--primary-gradient);
  opacity: 0;
  transition: var(--transition-base);
}

.stat-card:hover {
  transform: translateY(-6px);
  box-shadow: var(--box-shadow-lg);
  border-color: var(--primary-light);
}

.stat-card:hover::before {
  opacity: 1;
}

.stat-card :deep(.el-card__body) {
  padding: var(--spacing-xl);
}

.stat-content {
  display: flex;
  align-items: center;
  gap: var(--spacing-lg);
}

.stat-content :deep(.el-icon) {
  background: linear-gradient(135deg, rgba(0, 180, 166, 0.1) 0%, rgba(0, 206, 201, 0.05) 100%);
  padding: var(--spacing-md);
  border-radius: var(--border-radius-lg);
  transition: var(--transition-base);
}

.stat-card:hover .stat-content :deep(.el-icon) {
  transform: scale(1.1) rotate(5deg);
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: var(--font-size-huge);
  font-weight: 800;
  background: var(--primary-gradient);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  margin: 0 0 var(--spacing-sm) 0;
  line-height: 1.2;
}

.stat-label {
  font-size: var(--font-size-sm);
  color: var(--text-secondary);
  margin: 0;
  font-weight: 500;
}

.chart-card {
  margin-bottom: var(--spacing-xl);
  border-radius: var(--border-radius-xl);
  border: 1px solid var(--border-light);
}

.chart-card :deep(.el-card__body) {
  padding: var(--spacing-xl);
}

.chart-card h3 {
  margin-bottom: var(--spacing-lg);
  color: var(--text-primary);
  font-size: var(--font-size-lg);
  font-weight: 700;
  position: relative;
  padding-left: var(--spacing-md);
}

.chart-card h3::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 4px;
  height: 20px;
  background: var(--primary-gradient);
  border-radius: 2px;
}

/* 表格样式优化 */
:deep(.el-card) {
  border-radius: var(--border-radius-xl);
  border: 1px solid var(--border-light);
  box-shadow: var(--box-shadow-sm);
  transition: var(--transition-base);
}

:deep(.el-card:hover) {
  box-shadow: var(--box-shadow);
}

:deep(.el-card__body) {
  padding: var(--spacing-xl);
}

:deep(.el-card h3) {
  margin-bottom: var(--spacing-lg);
  color: var(--text-primary);
  font-size: var(--font-size-lg);
  font-weight: 700;
  position: relative;
  padding-left: var(--spacing-md);
}

:deep(.el-card h3::before) {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 4px;
  height: 20px;
  background: var(--primary-gradient);
  border-radius: 2px;
}

:deep(.el-table) {
  border-radius: var(--border-radius);
  overflow: hidden;
}

:deep(.el-table thead) {
  background: var(--bg-light);
}

:deep(.el-table th) {
  background: var(--bg-light);
  font-weight: 600;
  color: var(--text-primary);
}

:deep(.el-table td) {
  color: var(--text-regular);
}

:deep(.el-table__row:hover) {
  background-color: var(--primary-lighter);
}
</style>

