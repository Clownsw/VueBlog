import request from '@/utils/request'

export default {
  getSystem() {
    return request({
      url: 'system/info',
      method: 'GET'
    })
  },
  getFooter() {
    return request({
      url: 'footer',
      method: 'GET'
    })
  },
  updateSystem(system) {
    return request({
      url: 'system/update',
      method: 'POST',
      data: system
    })
  }
}
