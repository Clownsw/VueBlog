import router from "@/router";
import store from './store'
import sortApi from "@/api/sort";
import systemApi from "@/api/system";
import otherApi from "@/api/other";


router.beforeEach(((to, from, next) => {

    let r = async () => {
        if (!sessionStorage.getItem('sortList')) {
            await sortApi.getSortList().then(resp => {
                store.commit('SET_SORT_LIST', resp.data.data)
            })
        }

        if (!sessionStorage.getItem('pageFooter')) {
            await otherApi.getPageFooter().then(resp => {
                store.commit('SET_PAGE_FOOTER', resp.data.data.content)
            })
        }

        if (!sessionStorage.getItem('systemInfo')) {
            await systemApi.getSystemInfo().then(resp => {
                store.commit('SET_SYSTEM_INFO', resp.data.data)
            })
        }
    }


    r().then(() => {
        next()
    })

}))
