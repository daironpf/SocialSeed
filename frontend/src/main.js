import { createApp } from 'vue'
import { createRouter, createWebHistory } from 'vue-router'

import App from './App.vue'
import NewSocialUser from "@/components/NewSocialUser.vue";
import Home from "@/components/Home.vue";

const router = createRouter({
    history: createWebHistory(),
    routes: [
        { path: '/', component: NewSocialUser },
        { path: '/home', component: Home, name: 'home'}
    ]
})


/* import the fontawesome core */
import { library } from '@fortawesome/fontawesome-svg-core';

/* import font awesome icon component */
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';

/* import specific icons */
import {
    faHome,
    faUser,
    faBell,
    faEnvelope,
    faCog
} from "@fortawesome/free-solid-svg-icons";

/* add icons to the library */
library.add(faHome, faUser, faBell, faEnvelope, faCog);

createApp(App)
    .component('fa', FontAwesomeIcon)
    .use(router)
    .mount('#app');