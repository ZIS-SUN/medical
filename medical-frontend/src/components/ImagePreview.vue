<template>
  <div class="image-preview">
    <el-image
      v-for="(url, index) in images"
      :key="index"
      :src="url"
      :preview-src-list="images"
      :initial-index="index"
      fit="cover"
      class="preview-image"
    />
  </div>
</template>

<script setup>
import { defineProps } from 'vue'

defineProps({
  images: {
    type: Array,
    default: () => []
  }
})
</script>

<style scoped>
.image-preview {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.preview-image {
  width: 100px;
  height: 100px;
  border-radius: var(--border-radius);
  cursor: pointer;
}
</style>




