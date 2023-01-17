import request from '@/utils/request'

export default {
  search(keyWord) {
    return request({
      url: `/music/search?keyWord=${keyWord}`,
      method: 'GET'
    })
  },
  list(currentPage, pageSize) {
    return request({
      url: `/music/list?currentPage=${currentPage}&pageSize=${pageSize}`,
      method: 'GET'
    })
  },
  add(music) {
    return request({
      url: `/music/add`,
      method: 'POST',
      data: music
    })
  },
  delete(id) {
    return request({
      url: `/music/delete?id=${id}`,
      method:'GET'
    })
  },
  playListImport(id) {
    return request({
      url: `/music/playListImport?playListId=${id}`,
      method: 'GET'
    })
  }
}
