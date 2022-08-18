import request from '@/utils/request'

export default {
  getUserInfo() {
    return request({
      url: 'user/info',
      method: 'POST'
    })
  },
  updateUserById(user) {
    return request({
      url: 'user/update',
      method: 'POST',
      data: user
    })
  }
}
