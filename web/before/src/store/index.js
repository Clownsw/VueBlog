import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        sortList: JSON.parse(sessionStorage.getItem('sortList')),
        systemInfo: JSON.parse(sessionStorage.getItem('systemInfo')),
        pageFooter: JSON.parse(sessionStorage.getItem('pageFooter'))
    },
    getters: {
        getSortList: state => {
            return state.sortList
        },
        getSystemInfo: state => {
            return state.systemInfo
        },
        getPageFooter: state => {
            return state.pageFooter
        }
    },
    mutations: {
        SET_SORT_LIST: (state, payload) => {
            state.sortList = payload
            sessionStorage.setItem('sortList', JSON.stringify(payload))
        },
        SET_SYSTEM_INFO: (state, payload) => {
            state.systemInfo = payload
            sessionStorage.setItem('systemInfo', JSON.stringify(payload))
        },
        SET_PAGE_FOOTER: (state, payload) => {
            state.pageFooter = payload
            sessionStorage.setItem('pageFooter', JSON.stringify(payload))
        },
        REMOVE_INFO: (state) => {
            state.token = ''
            state.userInfo = {}
            localStorage.setItem('token', '')
            sessionStorage.setItem('userInfo', JSON.stringify({}))
        }
    },
    actions: {},
    modules: {}
})
