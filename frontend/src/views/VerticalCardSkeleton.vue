<script setup>
</script>

<template>
  <svg
      class="rounded-lg shadow-md w-64 h-auto p-3 pt-4 m-1 border border-gray-200"
      role="img"
      width="160"
      height="160"
      aria-labelledby="loading-aria"
      viewBox="0 0 160 160"
      preserveAspectRatio="none"
  >
    <rect
        x="0"
        y="0"
        width="100%"
        height="100%"
        clip-path="url(#clip-path)"
        style='fill: url("#fill");'
    ></rect>
    <defs>
      <clipPath id="clip-path">
        <circle cx="80" cy="31" r="25" />
        <rect x="0" y="95" rx="3" ry="3" width="115" height="6" />
        <rect x="0" y="114" rx="3" ry="3" width="160" height="6" />
        <rect x="0" y="135" rx="4" ry="4" width="70" height="20" />
        <rect x="0" y="76" rx="3" ry="3" width="160" height="6" />
        <rect x="85" y="135" rx="4" ry="4" width="70" height="20" />
      </clipPath>
      <linearGradient id="fill">
        <stop
            offset="0.599964"
            stop-color="#f3f3f3"
            stop-opacity="1"
        >
          <animate
              attributeName="offset"
              values="-2; -2; 1"
              keyTimes="0; 0.25; 1"
              dur="2s"
              repeatCount="indefinite"
          ></animate>
        </stop>
        <stop
            offset="1.59996"
            stop-color="#ecebeb"
            stop-opacity="1"
        >
          <animate
              attributeName="offset"
              values="-1; -1; 2"
              keyTimes="0; 0.25; 1"
              dur="2s"
              repeatCount="indefinite"
          ></animate>
        </stop>
        <stop
            offset="2.59996"
            stop-color="#f3f3f3"
            stop-opacity="1"
        >
          <animate
              attributeName="offset"
              values="0; 0; 3"
              keyTimes="0; 0.25; 1"
              dur="2s"
              repeatCount="indefinite"
          ></animate>
        </stop>
      </linearGradient>
    </defs>
  </svg>
</template>

<style scoped>

</style>