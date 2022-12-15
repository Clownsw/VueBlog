import request from '@/utils/request'

export default {
  getMe() {
    return request({
      url: '/other/me',
      method: 'GET'
    })
  },
  updateMe(me) {
    return request({
      url: '/other/me/update',
      method: 'POST',
      data: me
    })
  }
}
