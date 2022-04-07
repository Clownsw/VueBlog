import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from "@/views/Home";
import Login from "@/views/Login";
import Index from "@/views/Index"
import Word from "@/views/sys/Word";
import User from "@/views/sys/User";
import Role from "@/views/sys/Role";
import Menu from "@/views/sys/Menu"

Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        name: 'Home',
        component: Home,
        meta: {
            requireAuth: true,
        },
        redirect: "/sys/index",
        children: [
            {
                path: '/sys/index',
                name: 'Index',
                component: Index,
                meta: {
                    title: '后台首页',
                    requireAuth: true
                }
            },
            {
                path: '/sys/users',
                name: 'SysUser',
                component: User,
                meta: {
                    title: '用户管理',
                    requireAuth: true
                }
            },
            {
                path: '/sys/words',
                name: 'SysWord',
                component: Word,
                meta: {
                    title: '文章管理',
                    requireAuth: true
                }
            },
            {
                path: '/sys/roles',
                name: 'SysRole',
                component: Role,
                meta: {
                    title: '角色管理',
                    requireAuth: true
                }
            },
            {
                path: '/sys/menus',
                name: 'SysMenu',
                component: Menu,
                meta: {
                    title: '菜单管理',
                    requireAuth: true
                }
            },
        ]
    },
    {
        path: '/login',
        name: 'Login',
        component: Login,
        meta: {
            title: '登录',
            requireAuth: false
        }
    },
]

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
})

export default router
