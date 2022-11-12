package cn.smilex.vueblog.common.service.impl;

import cn.smilex.vueblog.common.dao.BlogDao;
import cn.smilex.vueblog.common.entity.Blog;
import cn.smilex.vueblog.common.service.BlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author smilex
 * @date 2022/11/12/11:45
 * @since 1.0
 */
@SuppressWarnings("all")
@Service
public class BlogServiceImpl extends ServiceImpl<BlogDao, Blog> implements BlogService {
}
