package cn.smilex.vueblog.common.service.impl;

import cn.smilex.vueblog.common.dao.SortDao;
import cn.smilex.vueblog.common.entity.sort.Sort;
import cn.smilex.vueblog.common.service.SortService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author smilex
 * @date 2022/11/12/11:49
 * @since 1.0
 */
@SuppressWarnings("unused")
@Service
public class SortServiceImpl extends ServiceImpl<SortDao, Sort> implements SortService {

    /**
     * 查询分类集合
     *
     * @return 分类集合
     */
    @Override
    public List<Sort> selectSortList() {
        return this.list(
                new LambdaQueryWrapper<Sort>()
                        .orderByDesc(Sort::getOrder)
        );
    }
}
