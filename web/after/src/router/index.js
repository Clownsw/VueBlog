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
export const constantRouterMap = [{
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },
  {
    path: '/',
    component: Layout,
    redirect: '/home',
    hidden: true,
  },
  {
    path: '/home',
    component: Layout,
    redirect: '/home/index',
    name: 'Home',
    meta: {
      title: '首页'
    },
    children: [{
      path: 'index',
      name: 'HomeIndex',
      component: () => import('@/views/home/index'),
      meta: {
        title: '控制台'
      }
    }]
  },
  {
    path: '/blog',
    component: Layout,
    redirect: '/blog/list',
    name: 'Blog',
    meta: {
      title: '博文管理'
    },
    children: [{
        path: 'list',
        name: 'BlogList',
        component: () => import('@/views/blog/index'),
        meta: {
          title: '博文列表'
        }
      },
      {
        path: 'edit',
        name: 'BlogAdd',
        component: () => import('@/views/blog/edit'),
        meta: {
          title: '博文添加'
        }
      },
      {
        path: 'edit/:id(\\d+)',
        name: 'BlogEdit',
        hidden: true,
        component: () => import('@/views/blog/edit'),
        meta: {
          title: '博文编辑'
        }
      }
    ]
  },
  {
    path: '/other',
    component: Layout,
    redirect: '/other/me/edit',
    name: 'Me',
    meta: {
      title: '其他管理'
    },
    children: [{
        path: 'me/edit',
        name: 'OtherMeEdit',
        component: () => import('@/views/other/me'),
        meta: {
          title: '关于我'
        }
      },
      {
        path: 'sort/edit',
        name: 'OtherSortEdit',
        component: () => import('@/views/sort/index'),
        meta: {
          title: '分类管理'
        }
      },
      {
        path: 'tag/edit',
        name: 'OtherTagEdit',
        component: () => import('@/views/tag/index'),
        meta: {
          title: '标签管理'
        }
      },
      {
        path: 'friend/edit',
        name: 'OtherFriendEdit',
        component: () => import('@/views/friend/index'),
        meta: {
          title: '友链管理'
        }
      },
      {
        path: 'search/index',
        name: 'SearchIndex',
        component: () => import('@/views/search/index'),
        meta: {
          title: '搜索管理'
        }
      },
      {
        path: 'music/index',
        name: 'MusicIndex',
        component: () => import('@/views/music/index'),
        meta: {
          title: '歌单管理'
        }
      }
    ]
  },
  {
    path: '/backup',
    component: Layout,
    redirect: '/backup/edit',
    name: 'Backup',
    meta: {
      title: '备份管理'
    },
    children: [{
        path: 'edit',
        name: 'BackUpEdit',
        component: () => import('@/views/backup/index'),
        meta: {
          title: '备份设置'
        }
      },
      {
        path: 'real',
        name: 'BackUpReal',
        component: () => import('@/views/backup/backup'),
        meta: {
          title: '立即备份'
        }
      }
    ]
  },
  {
    path: '/user',
    component: Layout,
    redirect: '/user/edit',
    name: 'User',
    meta: {
      title: '用户管理'
    },
    children: [{
      path: 'edit',
      name: 'UserEdit',
      component: () => import('@/views/user/index'),
      meta: {
        title: '用户设置'
      }
    }]
  },
  {
    path: '/system',
    component: Layout,
    redirect: '/system/edit',
    name: 'System',
    meta: {
      title: '系统管理'
    },
    children: [{
      path: 'edit',
      name: 'SystemEdit',
      component: () => import('@/views/system/index'),
      meta: {
        title: '系统设置'
      }
    }]
  },
  {
    path: '*',
    redirect: '/404',
    hidden: true
  }
]

export default new Router({
  // mode: 'history', //后端支持可开
  scrollBehavior: () => ({
    y: 0
  }),
  routes: constantRouterMap
})
