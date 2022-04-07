import axios from 'axios'
import {Message} from "element-ui";

axios.defaults.baseURL = 'http://localhost:9999/'

// 后置拦截
axios.interceptors.response.use(response => {
    return response;
}, error => {

    let msg = error.response.data.message;

    Message.error(msg)

    return Promise.reject(error);
})
