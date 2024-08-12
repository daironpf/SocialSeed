import {createRouter, createWebHistory} from "vue-router";
import NotFoundView from "@/components/NotFoundView.vue";
import PostDetailsView from "@/posts/PostDetailsView.vue";
import LoginView from "@/components/LoginView.vue";
import {loadLocaleMessages, setI18nLanguage, SUPPORT_LOCALES, i18n, DEFAULT_LOCALE} from "@/core/libs/app-i18n/index.js";
import { getCurrentUser } from "@/core/services/local-storage.js";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: "/",
            name: "feed",
            meta: {showHeader: true},
            component: () => import("@/feed/FeedPage.vue")
            // component: FeedView
        },
        {
            path: "/login",
            name: "login",
            meta: {showHeader: false},            
            component: LoginView
        },
        {
            path: "/su-profile/:id",
            name: "su-profile",
            meta: {showHeader: true},
            component: () => import("@/userProfile/SocialUserProfileView.vue")
        },
        {
            path: "/post/:id",
            name: "post-details",
            meta: {showHeader: true},
            component: PostDetailsView
        },
        {
            path: "/list/friends-recommendations",
            name: "list-of-friends-recommendations",
            meta: {showHeader: true},
            component: () => import("@/recommendations/ListOfFriendsRecommendations.vue")
        },
        {
            path: "/list/follow-recommendations",
            name: "list-of-follow-recommendations",
            meta: {showHeader: true},
            component: () => import("@/recommendations/ListOfFollowRecommendations.vue")            
        },
        {
            path: "/:pathMatch(.*)*",
            name: "notFound",
            meta: {showHeader: true},
            component: NotFoundView
        }
    ]
})

router.beforeEach(async (to, from, next) => {
    const locale = getCurrentUser()?.language ?? DEFAULT_LOCALE

    // load locale messages
    if (!i18n.global.availableLocales.includes(locale)) {
        await loadLocaleMessages(i18n, locale)
    }

    // set i18n language
    setI18nLanguage(i18n, locale)

    return next()
})

export default router;