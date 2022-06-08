import axios from 'axios'

export default {
    getPageFooter() {
        return axios.get('footer')
    },
    getSystemInfo() {
        return axios.get("system/info")
    }
}
