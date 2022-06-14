import Vue from 'vue'
import Router from 'vue-router'
/* Layout */
import Layout from '../views/layout/Layout'

// in development-env not use lazy-loading, because lazy-loading too many pages will cause webpack hot update too slow. so only in production use lazy-loading;
// detail: https://panjiachen.github.io/vue-element-admin-site/#/lazy-loading

Vue.use(Router)

/**
 * hidden: true                   if `hidden:true` will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu, whatever its child routes length
 *                                if not set alwaysShow, only more than one route under the children
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noredirect           if `redirect:noredirect` will no redirct in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    title: 'title'               the name show in submenu and breadcrumb (recommend set)
    icon: 'svg-name'             the icon show in the sidebar,
  }
 **/
export const constantRouterMap = [
  { path: '/login', component: () => import('@/views/login/index'), hidden: true },
  { path: '/404', component: () => import('@/views/404'), hidden: true },

  {
    path: '/',
    component: Layout,
    redirect: '/home',
    name: 'Dashboard',
    hidden: true,
    children: [{
      path: 'dashboard',
      component: () => import('@/views/dashboard/index')
    }]
  },
  {
    path: '/home',
    component: Layout,
    redirect: '/home/index',
    name: 'Home',
    meta: { title: '首页' },
    children: [
      {
        path: 'index',
        name: 'HomeIndex',
        component: () => import('@/views/home/index'),
        meta: { title: '首页' }
      }
    ]
  },
  {
    path: '/blog',
    component: Layout,
    redirect: '/blog/list',
    name: 'Blog',
    meta: { title: '博文管理' },
    children: [
      {
        path: 'list',
        name: 'BlogList',
        component: () => import('@/views/blog/list'),
        meta: { title: '博文列表' }
      },
      {
        path: 'form',
        name: 'BlogForm',
        component: () => import('@/views/blog/form'),
        meta: { title: '博文编辑' }
      }
    ]
  },
  {
    path: '/other',
    component: Layout,
    redirect: '/other/me/edit',
    name: 'Me',
    meta: { title: '其他管理' },
    children: [
      {
        path: 'me/edit',
        name: 'OtherMeEdit',
        component: () => import('@/views/other/me'),
        meta: { title: '编辑我' }
      },
      {
        path: 'sort/edit',
        name: 'OtherSortEdit',
        component: () => import('@/views/other/sort'),
        meta: { title: '编辑分类' }
      },
      {
        path: 'tag/edit',
        name: 'OtherTagEdit',
        component: () => import('@/views/tag/index'),
        meta: { title: '标签管理' }
      },
      {
        path: 'friend/edit',
        name: 'OtherFriendEdit',
        component: () => import('@/views/other/friend'),
        meta: { title: '友链管理' }
      }
    ]
  },
  {
    path: '/user',
    component: Layout,
    redirect: '/user/edit',
    name: 'User',
    meta: { title: '用户管理' },
    children: [
      {
        path: 'edit',
        name: 'UserEdit',
        component: () => import('@/views/user/index'),
        meta: { title: '用户编辑' }
      }
    ]
  },
  {
    path: '/system',
    component: Layout,
    redirect: '/system/edit',
    name: 'System',
    meta: { title: '系统管理' },
    children: [
      {
        path: 'edit',
        name: 'SystemEdit',
        component: () => import('@/views/system/index'),
        meta: { title: '系统编辑' }
      }
    ]
  },
  { path: '*', redirect: '/404', hidden: true }
]

export default new Router({
  // mode: 'history', //后端支持可开
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRouterMap
})
