import { createApp, provide } from 'vue'
import App from './App.vue'
import router from "@/router/index.js";

import { FontAwesomeIcon } from '@/libs/app-icons.js';

/* constant values */
const api_url = "http://54.183.246.185:8081/api/v0.0.1/";

const app = createApp(App);
app.use(router);
app.component('fa', FontAwesomeIcon);
app.provide('apiUrl',api_url);
app.mount('#app');

