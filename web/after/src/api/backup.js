import request from '@/utils/request'

export default {
  getBackUp() {
    return request({
      url: '/backup/info',
      method: 'GET'
    })
  },
  updateBackUp(backup) {
    return request({
      url: '/backup/update',
      method: 'POST',
      data: backup
    })
  },
  buyBackUp() {
    return request({
        url: '/backup/buy',
        method: 'GET'
    })
  }
}
