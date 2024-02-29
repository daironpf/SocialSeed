import { createApp } from 'vue'
import App from './App.vue'
import router from "@/router/index.js";

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

const app = createApp(App);
app.use(router)
app.component('fa', FontAwesomeIcon)

app.mount('#app')

// createApp(App)
//     .component('fa', FontAwesomeIcon)
//     .use(router)
//     .mount('#app');