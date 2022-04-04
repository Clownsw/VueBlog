import {createRouter, createWebHistory} from 'vue-router'
import Login from '../views/Login.vue'
import Blogs from '../views/Blogs.vue'
import BlogEdit from '../views/BlogEdit.vue'
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
        path: '/login',
        name: 'login',
        component: Login
    },
    {
        path: '/blog/:blogId(\\d+)',
        name: 'BlogDetail',
        component: BlogDetail
    },
    {
        path: '/blog/add',
        name: 'BlogAdd',
        component: BlogEdit,
        meta: {
            requireAuth: true
        }
    },
    {
        path: '/blog/:blogId/edit',
        name: 'BlogEdit',
        component: BlogEdit,
        meta: {
            requireAuth: true
        }
    },
]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})

export default router
