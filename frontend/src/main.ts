import { createApp } from 'vue'
import router from "@/core/router";
import { i18n } from '@/core/libs/app-i18n';
import { FontAwesomeIcon } from '@/core/libs/app-icons';
import App from './App.vue'
import './style.css'

/* constant values */
const api_url = "http://127.0.0.1:8081/api/v0.0.1/";
// const api_url = "http://54.183.246.185:8081/api/v0.0.1/";
const img_url = "https://raw.githubusercontent.com/daironpf/SocialSeed/2e789e7d6f129a7aec71f15953d04809a6011b3f/frontend/public";

const app = createApp(App);
app.use(router);
app.use(i18n);
app.component('fa', FontAwesomeIcon);
app.provide('apiUrl',api_url);
app.provide('imgUrl',img_url);
app.mount('#app');