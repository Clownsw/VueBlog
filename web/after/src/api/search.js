import request from '@/utils/request'

export default {
  flushSearchData() {
    return request({
      url: '/other/flushSearchData',
      method: 'GET'
    })
  }
}
