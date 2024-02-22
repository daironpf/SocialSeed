import { createApp } from 'vue'
import App from './App.vue'


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

createApp(App).component('fa', FontAwesomeIcon).mount('#app');