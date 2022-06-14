import request from '@/utils/request'

export default {
  getSortList() {
    return request({
      url: 'sort/list',
      method: 'GET'
    })
  },
  saveSort(sort) {
    return request({
      url: 'sort/add',
      method: 'POST',
      data: sort
    })
  },
  updateSortById(sort) {
    return request({
      url: 'sort/update',
      method: 'POST',
      data: sort
    })
  },
  deleteSortById(id) {
    return request({
      url: 'sort/delete/' + id,
      method: 'GET'
    })
  }
}
