<script setup lang="ts">
import { ref } from 'vue'
import { Picture } from '@element-plus/icons-vue'

interface Props {
  src: string
  width?: string | number
  height?: string | number
  fit?: 'fill' | 'contain' | 'cover' | 'none' | 'scale-down'
  preview?: boolean
  previewSrcList?: string[]
}

const props = withDefaults(defineProps<Props>(), {
  width: 60,
  height: 60,
  fit: 'cover',
  preview: true
})

const previewList = ref<string[]>([])

const handlePreview = () => {
  if (props.preview) {
    previewList.value = props.previewSrcList || [props.src]
  }
}
</script>

<template>
  <el-image
    :src="src"
    :style="{ width: typeof width === 'number' ? `${width}px` : width, height: typeof height === 'number' ? `${height}px` : height }"
    :fit="fit"
    :preview-src-list="preview ? (previewSrcList || [src]) : []"
    lazy
    loading="lazy"
  >
    <template #error>
      <div class="image-error">
        <el-icon><Picture /></el-icon>
        <span>加载失败</span>
      </div>
    </template>
  </el-image>
</template>

<style scoped>
.image-error {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
  background-color: #f5f7fa;
  color: #909399;
  font-size: 12px;
}
</style>

