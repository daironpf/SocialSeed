/*
* Copyright 2011-2023 the original author or authors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      https://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

/*
* Description: A tooltip component is a small pop-up window that provides
*   additional information to the user when they hover over a specific element,
*   such as a button, link, or icon
* @author Constantin
* https://www.youtube.com/@cdruc
* source code link: https://www.youtube.com/watch?v=kfjVorSv_rI
*/

<script setup lang="ts">
import { ref, Ref } from "vue";
import { arrow, computePosition, flip, offset, shift } from "@floating-ui/dom";

const props = defineProps<{
  content: string;
  placement?: string;
}>();

const referenceRef = ref<HTMLElement | null>(null);
const floatingRef = ref<HTMLElement | null>(null);
const arrowRef = ref<HTMLElement | null>(null);
const isHidden = ref(true);

async function calculatePosition() {
  if (!referenceRef.value || !floatingRef.value) return;

  const { x, y, middlewareData, placement } = await computePosition(
      referenceRef.value,
      floatingRef.value,
      {
        placement: props.placement || "bottom",
        middleware: [
          offset(8),
          flip(),
          shift({ padding: 5 }),
          arrow({ element: arrowRef.value }),
        ],
      }
  );

  Object.assign(floatingRef.value.style, {
    left: `${x}px`,
    top: `${y}px`,
  });

  const { x: arrowX, y: arrowY } = middlewareData.arrow || {};

  const opposedSide: { [key: string]: string } = {
    left: "right",
    right: "left",
    bottom: "top",
    top: "bottom",
  }[placement.split("-")[0]];

  Object.assign(arrowRef.value.style, {
    left: arrowX ? `${arrowX}px` : "",
    top: arrowY ? `${arrowY}px` : "",
    bottom: "",
    right: "",
    [opposedSide]: "-4px"
  });
}

function hide() {
  isHidden.value = true;
}

function show() {
  isHidden.value = false;
  calculatePosition();
}
</script>

<template>
  <div class="inline-block">
    <div
        @mouseenter="show"
        @mouseleave="hide"
        @focus="show"
        @blur="hide"
        ref="referenceRef"
        class="inline-block"
    >
      <slot/>
    </div>
    <div
        ref="floatingRef"
        :class="[
        'absolute top-0 left-0 z-50 bg-gray-700 text-sm text-white px-3 py-1.5 rounded-md cursor-default',
        isHidden && 'hidden'
      ]"
    >
      {{ props.content }}
      <div
          class="absolute bg-gray-700 h-[8px] w-[8px] rotate-45"
          ref="arrowRef"
      ></div>
    </div>
  </div>
</template>