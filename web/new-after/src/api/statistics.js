import request from '@/utils/request'

export default {
  statistics() {
    return request({
      url: '/statistics',
      method: 'GET'
    })
  }
}
