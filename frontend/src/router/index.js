import {createRouter, createWebHistory} from "vue-router";
import FeedView from "@/views/FeedView.vue";
import NotFoundView from "@/views/NotFoundView.vue";
import SocialUserProfileView from "@/views/SocialUserProfileView.vue";
import PostDetailsView from "@/views/PostDetailsView.vue";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: "/",
            name: "feed",
            component: FeedView
        },
        {
            path: "/su-profile/:id",
            name: "su-profile",
            component: SocialUserProfileView
        },
        {
            path: "/post/:id",
            name: "post-details",
            component: PostDetailsView
        },
        {
            path: "/:pathMatch(.*)*",
            name: "notFound",
            component: NotFoundView
        }
    ]
})

export default router;