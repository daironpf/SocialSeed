import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";
import PostDetailsView from "@/features/Posts/PostDetailsView.vue";
import { loadLocaleMessages, setI18nLanguage, i18n, DEFAULT_LOCALE } from "@/core/libs/app-i18n";
import { getCurrentUser } from "@/core/services/local-storage";

//Active Load
import LoginView from "@/features/Auth/LoginView.vue";
import NotFoundView from "@/components/NotFoundView.vue";

//Lazy Load
const feed = () => import(/* webpackChunkName: "FeedPage" */ "@/features/Feed/FeedPage.vue");
const SocialUserProfile = () => import(/* webpackChunkName: "SocialUserProfilePage" */ "@/features/UserProfile/views/SocialUserProfilePage.vue");

const SocialUserDetailsView = () => import(/* webpackChunkName: "SocialUserProfilePage" */ "@/features/UserProfile/views/SocialUserProfileDetailsView.vue");
const SocialUserFriendsView = () => import(/* webpackChunkName: "SocialUserProfileFriends" */ "@/features/UserProfile/views/SocialUserProfileFriendsView.vue");
const SocialUserFollowsView = () => import(/* webpackChunkName: "SocialUserProfileFollows" */ "@/features/UserProfile/views/SocialUserProfileFollowsView.vue");
const SocialUserFollowersView = () => import(/* webpackChunkName: "SocialUserProfileFollowers" */ "@/features/UserProfile/views/SocialUserProfileFollowersView.vue");


const listOfFriendsRecommendations = () => import(/* webpackChunkName: "ListOfFriendsRecommendations" */ "@/features/Recommendations/ListOfFriendsRecommendations.vue")
const listOfFollowRecommendations = () => import(/* webpackChunkName: "ListOfFollowRecommendations" */ "@/features/Recommendations/ListOfFollowRecommendations.vue")


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
        name: "SocialUserProfile",
        props: true,
        meta: { showHeader: true },
        component: SocialUserProfile,
        children: [
            {
                path: '',
                name: 'SocialUserDetails',
                component: SocialUserDetailsView
            },
            {
                path: 'friends',
                name: 'SocialUserFriends',
                component: SocialUserFriendsView
            },
            {
                path: 'follows',
                name: 'SocialUserFollows',
                component: SocialUserFollowsView
            },
            {
                path: 'followers',
                name: 'SocialUserFollowers',
                component: SocialUserFollowersView
            }
        ]
    },
    // {
    //     path: '/Settings',
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
        path: "/list/friends-Recommendations",
        name: "list-of-friends-recommendations",
        meta: { showHeader: true },
        component: listOfFriendsRecommendations
    },
    {
        path: "/list/follow-Recommendations",
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
