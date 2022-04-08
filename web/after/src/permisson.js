import router from "@/router";
import axios from 'axios'
import store from './store'

router.beforeEach(((to, from, next) => {
    if (to.meta.title) {
        document.title = to.meta.title
    }

    if (to.matched.some(record => record.meta.requireAuth)) {

        const token = localStorage.getItem("token")

        if (!token) {
            axios.post("token", token)
                .then(resp => {
                    if (resp.data.message === '无权限访问!') {
                        sessionStorage.clear()
                        next({
                            path: '/login'
                        })
                    }
                })
        } else {
            // 同步获取用户信息
            let f = async () => {
                await axios.post('user/info', {}, {
                    headers: {
                        'authorization': token
                    }
                }).then(resp => {
                    store.commit('SET_USER', resp.data.data)
                })
                    .catch(_ => {
                        localStorage.clear()
                        sessionStorage.clear()
                        next({
                            path: '/login'
                        })
                    })
            }

            f().then(() => {
                next()
            })
        }
    } else {
        next()
    }
}))
