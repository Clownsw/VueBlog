package cn.smilex.vueblog.common.service.impl;

import cn.smilex.vueblog.common.dao.SortDao;
import cn.smilex.vueblog.common.entity.Sort;
import cn.smilex.vueblog.common.service.SortService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author smilex
 * @date 2022/11/12/11:49
 * @since 1.0
 */
@SuppressWarnings("unused")
@Service
public class SortServiceImpl extends ServiceImpl<SortDao, Sort> implements SortService {
}
