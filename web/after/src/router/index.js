import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from "@/views/Home";
import Login from "@/views/Login";
import Index from "@/views/Index"
import Word from "@/views/sys/Word";
import EditWord from "@/views/sys/EditWord";
import EditMe from "@/views/sys/EditMe";
import User from "@/views/sys/User";
import Friend from "@/views/sys/Friend";
import Tag from "@/views/sys/Tag"
import Sort from "@/views/sys/Sort";

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
                path: '/sys/tags',
                name: 'SysTag',
                component: Tag,
                meta: {
                    title: '标签管理',
                    requireAuth: true
                }
            },
            {
                path: '/sys/sorts',
                name: 'SysSort',
                component: Sort,
                meta: {
                    title: '分类管理',
                    requireAuth: true
                }
            },
            {
                path: '/sys/friend',
                name: 'SysFriend',
                component: Friend,
                meta: {
                    title: '友链管理',
                    requireAuth: true
                }
            },
            {
                path: '/sys/me/edit',
                name: 'SysMe',
                component: EditMe,
                meta: {
                    title: '关于我',
                    requireAuth: true
                }
            },
            {
                path: '/sys/word/edit/:id(\\d+)',
                name: 'SysEditWord',
                component: EditWord,
                meta: {
                    title: '编辑文章',
                    requireAuth: true
                }
            }
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
