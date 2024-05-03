import { createApp } from 'vue'

import router from "@/router/index.js";
import { i18n } from '@/libs/app-i18n/index.js';
import { FontAwesomeIcon } from '@/libs/app-icons/index.js';
import App from './App.vue'

/* constant values */
const api_url = "http://54.183.246.185:8081/api/v0.0.1/";

const app = createApp(App);
app.use(router);
app.use(i18n);
app.component('fa', FontAwesomeIcon);
app.provide('apiUrl',api_url);
app.mount('#app');

