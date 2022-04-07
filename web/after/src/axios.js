import axios from 'axios'
import {Message} from "element-ui";

axios.defaults.baseURL = 'http://localhost:9999/'

// 后置拦截
axios.interceptors.response.use(response => {
    return response;
}, error => {

    let data = error.response;
    let msg = error.response.data.message;

    Message.error(msg)

    // store.commit('REMOVE_INFO');
    return Promise.reject(error);
})
