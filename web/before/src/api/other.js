import axios from 'axios'

export default {
    getPageFooter() {
        return axios.get('/other/footer')
    },
}
