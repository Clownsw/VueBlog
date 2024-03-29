import {
  getToken
} from '@/utils/auth'; // 验权
import {
  Message
} from 'element-ui';
import NProgress from 'nprogress'; // Progress 进度条
import 'nprogress/nprogress.css'; // Progress 进度条样式
import router from './router';
import store from './store';

const systemName = 'VueBlogAdmin - '
const whiteList = ['/login'] // 不重定向白名单

router.beforeEach((to, from, next) => {
  if (to.meta.title) {
    document.title = systemName + to.meta.title
  }

  NProgress.start()
  const token = getToken()
  if (getToken()) {
    store.dispatch('signToken', token).then(resp => {
      const isTrue = resp.code === 200
      const currentPageIsLogin = to.path === '/login'

      if (isTrue) {
        if (currentPageIsLogin) {
          next({
            path: '/'
          })
          NProgress.done() // if current page is dashboard will not trigger	afterEach hook, so manually handle it
        } else {
          store.commit('SET_TOKEN', token)

          if (!store.getters.userInfo) {
            store.dispatch('GetInfo').then(res => { // 拉取用户信息
              next()
            }).catch((err) => {
              store.dispatch('FedLogOut').then(() => {
                Message.error(err || 'Verification failed, please login again')
                next({
                  path: '/'
                })
              })
            })
          } else {
            next()
          }
        }
      } else {
        if (currentPageIsLogin) {
          next()
        } else {
          next('/login')
        }
      }
    }).catch(error => {
      console.log(error)
    })

  } else {
    if (whiteList.indexOf(to.path) !== -1) {
      next()
    } else {
      next('/login')
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  NProgress.done() // 结束Progress
})
