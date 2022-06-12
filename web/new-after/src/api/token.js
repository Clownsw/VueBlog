import request from '../utils/request'

export default {
  signToken(token) {
    return request({
      url: '/token',
      method: 'POST',
      data: token
    })
  }
}
