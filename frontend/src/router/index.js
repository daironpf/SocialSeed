import {createRouter, createWebHistory} from "vue-router";
import FeedView from "@/views/FeedView.vue";
import NotFoundView from "@/views/NotFoundView.vue";
import SocialUserProfileView from "@/views/SocialUserProfileView.vue";
import PostDetailsView from "@/views/PostDetailsView.vue";
import LoginView from "@/views/LoginView.vue";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: "/",
            name: "feed",
            meta: { showHeader: true },
            component: FeedView
        },
        {
            path: "/login",
            name: "login",
            meta: { showHeader: false },
            component: LoginView
        },
        {
            path: "/su-profile/:id",
            name: "su-profile",
            meta: { showHeader: true },
            component: SocialUserProfileView
        },
        {
            path: "/post/:id",
            name: "post-details",
            meta: { showHeader: true },
            component: PostDetailsView
        },
        {
            path: "/:pathMatch(.*)*",
            name: "notFound",
            meta: { showHeader: true },
            component: NotFoundView
        }
    ]
})

export default router;