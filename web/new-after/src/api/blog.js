import request from '@/utils/request'

export default {
  getBlogById(id) {
    return request({
      url: 'blog/' + id,
      method: 'GET'
    })
  },
  getBlogList(currentPage) {
    return request({
      url: "blogs?currentPage=" + currentPage,
      method: 'GET',
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
      url: 'blog/deletes',
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
