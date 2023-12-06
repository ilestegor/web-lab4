import {createRouter, createWebHistory} from "vue-router"
import Registration from "@/view/Registration.vue";
import MainPage from "@/view/MainPage.vue";

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
            component: MainPage
        }
    ]

})
export default router;