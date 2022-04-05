import router from "@/router";
import axios from 'axios'

router.beforeEach(((to, from, next) => {
    if (to.matched.some(record => record.meta.requireAuth)) {
        const token = localStorage.getItem("token")

        if (!token) {
            axios.post("token", token)
                .then(resp => {
                    if (resp.data.message === '无权限访问!') {
                        next({
                            path: '/login'
                        })
                    }
                })
        } else {
            next()
        }
    } else {
        next()
    }
}))
