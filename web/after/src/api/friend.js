import request from '@/utils/request'

export default {
  getFriendList(currentPage) {
    return request({
      url: "friend/list?currentPage=" + currentPage,
      method: 'GET'
    })
  },
  saveFriend(friend) {
    return request({
      url: 'friend/add',
      method: 'POST',
      data: friend
    })
  },
  updateFriendById(friend) {
    return request({
      url: 'friend/update',
      method: 'POST',
      data: friend
    })
  },
  batchDeleteByIds(ids) {
    return request({
      url: 'friend/batchDelete',
      method: 'POST',
      data: ids
    })
  }
}
