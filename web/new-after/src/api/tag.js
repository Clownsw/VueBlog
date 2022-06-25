import request from '@/utils/request'

export default {
  getTagList() {
    return request({
      url: 'tags',
      method: 'GET'
    })
  },
  getTagIdByName(name) {
    return request({
      url: 'tag/id/' + name,
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
  },
  existsTagByName(name) {
    return request({
      url: 'tag/exist/' + name,
      method: 'GET'
    })
  },
  getIdsByNames(names) {
    return request({
      url: 'tag/ids',
      method: 'POST',
      data: names
    })
  }
}
