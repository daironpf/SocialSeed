import {createRouter, createWebHistory} from "vue-router";
import FeedView from "@/views/FeedView.vue";
import NotFoundView from "@/views/NotFoundView.vue";
import SocialUserProfileView from "@/views/SocialUser/Profile/SocialUserProfileView.vue";
import PostDetailsView from "@/views/PostDetailsView.vue";
import LoginView from "@/views/LoginView.vue";
import ListOfFriendsRecommendations from "@/views/Lists/ListOfFriendsRecommendations.vue";
import ListOfFollowRecommendations from "@/views/Lists/ListOfFollowRecommendations.vue";
import {loadLocaleMessages, setI18nLanguage, SUPPORT_LOCALES, i18n, DEFAULT_LOCALE} from "@/libs/app-i18n";
import { getCurrentUser } from "@/services/local-storage.js";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: "/",
            name: "feed",
            meta: {showHeader: true},
            component: FeedView
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
            component: SocialUserProfileView
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
            component: ListOfFriendsRecommendations
        },
        {
            path: "/list/follow-recommendations",
            name: "list-of-follow-recommendations",
            meta: {showHeader: true},
            component: ListOfFollowRecommendations
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