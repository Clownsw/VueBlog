import request from '@/utils/request'

export function login(username, password) {
  return request({
    url: '/admin/login',
    method: 'POST',
    data: {
      username,
      password
    }
  })
}

export function getInfo(token) {
  return request({
    url: '/user/info',
    method: 'POST'
  })
}
