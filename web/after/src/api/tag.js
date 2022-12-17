import request from '@/utils/request'

export default {
  getTagList() {
    return request({
      url: '/tag/list',
      method: 'GET'
    })
  },
  getTagIdByName(name) {
    return request({
      url: 'tag/' + name,
      method: 'GET'
    })
  },
  saveTag(tag) {
    return request({
      url: 'tag/add/' + tag,
      method: 'GET'
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
      url: 'tag/exists/' + name,
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
