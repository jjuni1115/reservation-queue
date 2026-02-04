import { createRouter, createWebHistory } from 'vue-router'
import EntryView from '@/views/EntryView.vue'
import WaitView from '@/views/WaitView.vue'
import EnterView from '@/views/EnterView.vue'



const router = createRouter({
    history: createWebHistory(),
    routes: [
        { path: '/', component: EntryView },
        { path: '/wait', component: WaitView },
        { path: '/enter', component: EnterView }
    ]
})

export default router