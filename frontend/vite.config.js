import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  //change port for production
  preview: {
    port: 8080,
  },
  // for dev
  server: {
    port: 8080,
  },
  // plugins
  plugins: [
    vue(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  // define
  define: {
    __APP_VERSION__: JSON.stringify('v0.0.1')
  },
  // base:'https://daironpf.github.io/SocialSeed/',
  base:'/SocialSeed/',
})