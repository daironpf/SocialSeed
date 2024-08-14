import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";
import PostDetailsView from "@/posts/PostDetailsView.vue";
import { loadLocaleMessages, setI18nLanguage, i18n, DEFAULT_LOCALE } from "@/core/libs/app-i18n";
import { getCurrentUser } from "@/core/services/local-storage";

//Active Load
import LoginView from "@/auth/LoginView.vue";
import NotFoundView from "@/components/NotFoundView.vue";

//Lazy Load
const feed = () => import(/* webpackChunkName: "FeedPage" */ "@/feed/FeedPage.vue");
const profile = () => import(/* webpackChunkName: "SocialUserProfileView" */ "@/features/UserProfile/views/UserProfilePage.vue");
const listOfFriendsRecommendations = () => import(/* webpackChunkName: "ListOfFriendsRecommendations" */ "@/recommendations/ListOfFriendsRecommendations.vue")
const listOfFollowRecommendations = () => import(/* webpackChunkName: "ListOfFollowRecommendations" */ "@/recommendations/ListOfFollowRecommendations.vue")

// Define the routes with appropriate types
const routes: Array<RouteRecordRaw> = [
    {
        path: "/login",
        name: "login",
        meta: { showHeader: false },
        component: LoginView
    },
    {
        path: "/",
        name: "feed",
        meta: { showHeader: true },
        component: feed
    },
    {
        path: "/su-profile/:id",
        name: "su-profile",
        meta: { showHeader: true },
        component: profile
    },
    // {
    //     path: '/settings',
    //     name: 'Settings',
    //     component: SettingsView,
    //     children: [
    //         {
    //             path: 'account',
    //             name: 'AccountSettings',
    //             component: AccountView
    //         },
    //         {
    //             path: 'privacy',
    //             name: 'PrivacySettings',
    //             component: PrivacyView
    //         }
    //     ]
    // },
    {
        path: "/post/:id",
        name: "post-details",
        meta: { showHeader: true },
        component: PostDetailsView
    },
    {
        path: "/list/friends-recommendations",
        name: "list-of-friends-recommendations",
        meta: { showHeader: true },
        component: listOfFriendsRecommendations
    },
    {
        path: "/list/follow-recommendations",
        name: "list-of-follow-recommendations",
        meta: { showHeader: true },
        component: listOfFollowRecommendations
    },
    {
        path: "/:pathMatch(.*)*",
        name: "notFound",
        meta: { showHeader: true },
        component: NotFoundView
    }
];

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes
});

router.beforeEach(async (to, from, next) => {
    const locale = getCurrentUser()?.language ?? DEFAULT_LOCALE;

    // load locale messages
    if (!i18n.global.availableLocales.includes(locale)) {
        await loadLocaleMessages(i18n, locale);
    }

    // set i18n language
    setI18nLanguage(i18n, locale);

    next();
});

export default router;
