import axios from 'axios'
import {Message} from "element-ui";

axios.defaults.baseURL='http://localhost:9999/'

// 后置拦截
axios.interceptors.response.use(response => {
    return response;
}, error => {

    let msg = undefined;
    let data = error.response;

    msg = error.response.data.message;

    if (data.status === 404 || data.status === 403) {
        Message.error(msg)
    }
    // store.commit('REMOVE_INFO');
    return Promise.reject(error);
})
