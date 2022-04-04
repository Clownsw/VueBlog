import axios from 'axios'
import store from './store'
import { ElMessage } from 'element-plus'

axios.defaults.baseURL='http://localhost:8888/'

// 前置拦截
axios.interceptors.request.use(config => {
    return config
})

// 后置拦截
axios.interceptors.response.use(response => {
    return response;
}, error => {

    let msg = undefined;
    let data = error.response;

    msg = error.response.data.message;

    if (data.status === 404) {
        ElMessage.error(msg)
    } else if (data.status === 403) {
        ElMessage.error(msg)
    }
    store.commit('REMOVE_INFO');
    return Promise.reject(error);
})