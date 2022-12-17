import request from '@/utils/request'

export default {
  getBackUp() {
    return request({
      url: '/other/backUp/info',
      method: 'GET'
    })
  },
  updateBackUp(backup) {
    return request({
      url: '/other/backUp/update',
      method: 'POST',
      data: backup
    })
  },
  buyBackUp() {
    return request({
        url: '/other/backUp/buy',
        method: 'GET'
    })
  }
}
