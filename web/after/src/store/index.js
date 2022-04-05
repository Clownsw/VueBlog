import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        user: JSON.parse(sessionStorage.getItem("user")),
        token: '',
    },
    getters: {
        getToken: state => {
            return state.token
        },
        getUser: state => {
            return state.user
        }
    },
    mutations: {
        SET_TOKEN: (state, payload) => {
            state.token = payload
            localStorage.setItem("token", payload)
        },
        SET_USER: (state, payload) => {
            state.user = payload
            sessionStorage.setItem("user", JSON.stringify(payload))
        }
    },
    actions: {},
    modules: {}
})
