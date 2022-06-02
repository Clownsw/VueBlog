import Vue from 'vue'
import VueRouter from 'vue-router'
import Blogs from '../views/Blogs.vue'
import BlogDetail from '../views/BlogDetail.vue'
import BlogFriend from "@/views/BlogFriend";
import BlogMe from "@/views/BlogMe";

Vue.use(VueRouter)

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
        path: '/blogs/sort/:id(\\d+)',
        name: 'BlogsId',
        component: Blogs,
    },
    {
        path: '/blog/:blogId(\\d+)',
        name: 'BlogDetail',
        component: BlogDetail
    },
    {
        path: '/friend',
        name: 'Friend',
        component: BlogFriend
    },
    {
        path: '/me',
        name: "BlogMe",
        component: BlogMe
    }
]

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
})

export default router
