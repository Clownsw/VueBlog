import request from '@/utils/request'

export default {
  statistics() {
    return request({
      url: '/statistics',
      method: 'GET'
    })
  },
  statisticsBlog() {
    return request({
        url: '/statistics/blog',
      method: 'GET'
    })
  }
}
