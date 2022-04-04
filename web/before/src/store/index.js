import {createStore} from 'vuex'

export default createStore({
    state: {
        token: localStorage.getItem('token'),
        userInfo: JSON.parse(sessionStorage.getItem('userInfo'))
    },
    getters: {
        getUserInfo: state => {
            return state.userInfo
        },
        getToken: state => {
            return state.token
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
