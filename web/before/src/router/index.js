import {createRouter, createWebHistory} from 'vue-router'
import Blogs from '../views/Blogs.vue'
import BlogDetail from '../views/BlogDetail.vue'

const routes = [
    {
        path: '/',
        name: 'Index',
        redirect: {
            name: 'Blogs'
        }
    },
    {
        path: '/blogs',
        name: 'Blogs',
        component: Blogs,
    },
    {
        path: '/blog/:blogId(\\d+)',
        name: 'BlogDetail',
        component: BlogDetail
    },
]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})

export default router
