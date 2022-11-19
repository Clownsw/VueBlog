import axios from 'axios'

export default {
    getFriendList() {
        return axios.get('/friend/list')
    },
}
