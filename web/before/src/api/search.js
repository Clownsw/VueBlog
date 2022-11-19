import axios from 'axios'

export default {
    searchBlog(q) {
        return axios.get(`/search?q=${q}`)
    }
}