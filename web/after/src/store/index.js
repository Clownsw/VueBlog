import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        token: '',
    },
    getters: {
        getToken: state => {
            return state.token
        }
    },
    mutations: {
        SET_TOKEN: (state, payload) => {
            state.token = payload
            localStorage.setItem("token", payload)
        }
    },
    actions: {},
    modules: {}
})
