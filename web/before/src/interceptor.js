import router from "@/router";
import store from './store'
import sortApi from "@/api/sort";
import systemApi from "@/api/system";


router.beforeEach(((to, from, next) => {

    if (!store.getters.sortList) {
        sortApi.getSortList().then(resp => {
            store.commit('SET_SORT_LIST', resp.data.data)
        })
    }

    if (!store.getters.getPageFooter) {
        systemApi.getPageFooter().then(resp => {
            store.commit('SET_PAGE_FOOTER', resp.data.data.content)
        })
    }

    if (!store.getters.getSystemInfo) {
        systemApi.getSystemInfo().then(resp => {
            store.commit('SET_SYSTEM_INFO', resp.data.data)
            next()
        })
    } else {
        next()
    }
}))
