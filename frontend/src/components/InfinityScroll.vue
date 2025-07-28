<script setup lang="ts">
import {onMounted, Ref, ref} from 'vue';

const props = defineProps<{
  class?: String,
  hasMoreElements: Boolean,
}>();

const LOAD_MORE_EVENT = 'LoadMoreElements';
const emit = defineEmits<(e: typeof LOAD_MORE_EVENT) => void>();

const targetElement: Ref<HTMLDivElement | null> = ref(null);

onMounted(() => {
  const observer = new IntersectionObserver((entries) => {
    entries.forEach((entry) => {
      if (entry.isIntersecting && props.hasMoreElements) {
        emit(LOAD_MORE_EVENT);
      }
    });
  });

  if (targetElement.value) {
    observer.observe(targetElement.value);
  }
});
</script>

<template>
  <div :class="props.class">
    <slot></slot>
  </div>
  <div ref="targetElement"></div>
</template>