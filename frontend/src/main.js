import { createApp, provide } from 'vue'
import App from './App.vue'
import router from "@/router/index.js";

/* import the fontawesome core */
import { library } from '@fortawesome/fontawesome-svg-core';

/* import font awesome icon component */
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';

/* import specific icons */
import {
    faBan,
    faHome,
    faUser,
    faBell,
    faEnvelope,
    faCog,
    faImage,
    faClock,
    faThumbsUp,
    faComment,
    faSync,
    faPencil,
    faEarth,
    faSortDown,
    faUserGroup,
    faLock,
    faUsers,
    faUserFriends,
    faUsersBetweenLines,
    faUsersLine,
    faUserPlus,
    faPersonCirclePlus,
    faPenToSquare
} from "@fortawesome/free-solid-svg-icons";

/* add icons to the library */
library.add(faBan, faHome, faUser, faBell, faEnvelope, faCog, faImage, faClock, faThumbsUp, faComment, faSync, faPencil, faEarth, faSortDown, faUserGroup, faLock, faUsers, faUserFriends, faUsersBetweenLines, faUsersLine, faUserPlus, faPersonCirclePlus, faPenToSquare);

/* constant values */
const api_url = "http://127.0.0.1:8081/api/v0.0.1/";

const app = createApp(App);
app.use(router);
app.component('fa', FontAwesomeIcon);
app.provide('apiUrl',api_url);
app.mount('#app');

