import request from '@/utils/request'

export default {
  getMe() {
    return request({
      url: 'me',
      method: 'GET'
    })
  },
  updateMe(me) {
    return request({
      url: 'me/update',
      method: 'POST',
      data: me
    })
  }
}
