<template>
  <el-card class="appointment-card" shadow="hover">
    <div class="card-header">
      <span class="appointment-no">预约单号：{{ appointment.appointmentNo }}</span>
      <el-tag :type="statusType">{{ appointment.status }}</el-tag>
    </div>

    <el-divider />

    <div class="card-body">
      <div class="info-row">
        <span class="label">医生：</span>
        <span class="value">{{ appointment.doctorName }} | {{ appointment.departmentName }}</span>
      </div>
      <div class="info-row">
        <span class="label">时间：</span>
        <span class="value">{{ appointment.appointmentDate }} {{ appointment.timeSlot }}</span>
      </div>
      <div class="info-row" v-if="appointment.remark">
        <span class="label">备注：</span>
        <span class="value">{{ appointment.remark }}</span>
      </div>
      <div class="info-row" v-if="appointment.cancelReason">
        <span class="label">取消原因：</span>
        <span class="value text-danger">{{ appointment.cancelReason }}</span>
      </div>
    </div>

    <div class="card-footer">
      <el-button
        v-if="appointment.status === '待确认'"
        type="danger"
        size="small"
        @click="$emit('cancel', appointment)"
      >
        取消预约
      </el-button>
      <el-button
        v-if="appointment.status === '已完成'"
        type="primary"
        size="small"
        @click="$emit('viewRecord', appointment)"
      >
        查看病历
      </el-button>
      <el-button
        v-if="appointment.status === '已完成' && !appointment.hasReview"
        type="success"
        size="small"
        @click="$emit('review', appointment)"
      >
        评价医生
      </el-button>
    </div>
  </el-card>
</template>

<script setup>
import { computed, defineProps, defineEmits } from 'vue'

const props = defineProps({
  appointment: {
    type: Object,
    required: true
  }
})

defineEmits(['cancel', 'viewRecord', 'review'])

const statusType = computed(() => {
  const statusMap = {
    '待确认': 'warning',
    '已确认': 'primary',
    '就诊中': 'info',
    '已完成': 'success',
    '已取消': 'danger'
  }
  return statusMap[props.appointment.status] || 'info'
})
</script>

<style scoped>
.appointment-card {
  margin-bottom: 16px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.appointment-no {
  font-size: 14px;
  color: var(--text-primary);
  font-weight: 500;
}

.card-body {
  padding: 12px 0;
}

.info-row {
  display: flex;
  margin-bottom: 8px;
  font-size: 14px;
}

.info-row:last-child {
  margin-bottom: 0;
}

.label {
  color: var(--text-secondary);
  min-width: 80px;
}

.value {
  color: var(--text-primary);
  flex: 1;
}

.card-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}
</style>




