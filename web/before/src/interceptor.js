import router from "@/router";
import store from './store'
import axios from 'axios'

router.beforeEach(((to, from, next) => {
    if (store.getters.getSystemInfo === null) {
        let f = async () => {
            await axios.get("system/info")
                .then(resp => {
                    store.commit('SET_SYSTEM_INFO', resp.data.data)
                })
        }
        f().then(() => {
            next()
        })
    } else {
        next()
    }
}))
