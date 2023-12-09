import {createRouter, createWebHistory} from "vue-router"
import Registration from "@/view/Registration.vue";
import MainPage from "@/view/MainPage.vue";
import registration from "@/view/Registration.vue";
import store from "@/store/mainStore";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: "/",
            name: 'startPage',
            component: Registration
        },
        {
            path: "/auth",
            name: 'authPage',
            component: Registration
        },
        {
            path:"/main",
            name: "mainPage",
            component: MainPage,
            meta: {
                reqiresAuth: true
            }
        }
    ]
})
router.beforeEach((to, from, next) => {
    if (to.matched.some(record => record.meta.reqiresAuth)){
        if (store.getters['auth/getUserStatus']){
            next()
            return
        }
        next("/auth")
    } else {
        next()
    }
})
export default router;