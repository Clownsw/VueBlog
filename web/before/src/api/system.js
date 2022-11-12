import axios from 'axios'

export default {
    getSystemInfo() {
        return axios.get("system/info")
    }
}
