<script setup>
import {onMounted, ref} from 'vue';

const props = defineProps({
  class: String,
  hasMoreElements: Boolean,
})

const LOAD_MORE_EVENT = 'LoadMoreElements';
const emit = defineEmits([LOAD_MORE_EVENT])

const targetElement = ref(null);

onMounted(() => {
  const observer = new IntersectionObserver((entries) => {
    entries.forEach((entry) => {
      if (entry.isIntersecting && props.hasMoreElements) {
        emit(LOAD_MORE_EVENT);
      }
    });
  });

  observer.observe(targetElement.value);
});
</script>

<template>
  <div :class="props.class">
    <slot></slot>
  </div>
  <div ref="targetElement"></div>
</template>