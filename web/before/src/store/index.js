import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        token: localStorage.getItem('token'),
        userInfo: JSON.parse(sessionStorage.getItem('userInfo')),
        systemInfo: JSON.parse(sessionStorage.getItem('systemInfo'))
    },
    getters: {
        getUserInfo: state => {
            return state.userInfo
        },
        getToken: state => {
            return state.token
        },
        getSystemInfo: state => {
            return state.systemInfo
        }
    },
    mutations: {
        SET_TOKEN: (state, token) => {
            state.token = token
            localStorage.setItem('token', token)
        },
        SET_USER_INFO: (state, userInfo) => {
            state.userInfo = userInfo
            sessionStorage.setItem('userInfo', JSON.stringify(userInfo))
        },
        SET_SYSTEM_INFO: (state, payload) => {
            state.systemInfo = payload
            sessionStorage.setItem('systemInfo', JSON.stringify(payload))
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
