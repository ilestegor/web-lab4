import '@/style/global-style.css'
import { createApp } from 'vue'
import App from './App.vue'
import components from "@/globalRegistration"
import router from "@/router/router";
import VueAwesomePaginate from "vue-awesome-paginate";
import Notifications from '@kyvg/vue3-notification'
const app = createApp(App);
app.use(router)
app.use(VueAwesomePaginate)
app.use(Notifications)

components.forEach(component => app.component(component.name, component))

app.mount('#app')
