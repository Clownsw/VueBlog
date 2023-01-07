import request from '@/utils/request'

export default {
  getBlogById(id) {
    return request({
      url: 'blog/' + id,
      method: 'GET'
    })
  },
  getBlogList(currentPage, pageSize, queryObj) {
    return request({
      url: `blog/list?currentPage=${currentPage}&pageSize=${pageSize}`,
      method: 'POST',
      data: queryObj
    })
  },
  saveBlog(blog) {
    return request({
      url: 'blog/edit',
      method: 'POST',
      data: blog
    })
  },
  updateBlogById(blog) {
    return request({
      url: 'blog/edit',
      method: 'POST',
      data: blog
    })
  },
  batchDeleteBlogByIds(ids) {
    return request({
      url: 'blog/batchDelete',
      method: 'POST',
      data: ids
    })
  },
  getBlogKeyById(id) {
    return request({
      url: '/blog/key/' + id,
      method: 'GET'
    })
  }
}
