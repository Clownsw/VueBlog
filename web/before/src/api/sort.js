import axios from 'axios'

export default {
    getSortList() {
        return axios.get('sort/list')
    }
}
