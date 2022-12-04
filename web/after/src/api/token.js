import request from '../utils/request'

export default {
  signToken(token) {
    return request({
      url: '/admin/token',
      method: 'POST',
      data: token
    })
  }
}
