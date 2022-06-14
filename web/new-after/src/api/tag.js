import request from '@/utils/request'

export default {
  getTagList() {
    return request({
      url: 'tags',
      method: 'GET'
    })
  },
  saveTag(tag) {
    return request({
      url: 'tag/add',
      method: 'POST',
      data: tag
    })
  },
  updateTagById(tag) {
    return request({
      url: 'tag/update',
      method: 'POST',
      data: tag
    })
  },
  deleteTagById(id) {
    return request({
      url: 'tag/delete/' + id,
      method: 'GET'
    })
  }
}
