package cn.smilex.vueblog.common.service;

import cn.smilex.vueblog.common.entity.Sort;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author smilex
 * @date 2022/11/12/11:45
 * @since 1.0
 */
@SuppressWarnings("unused")
public interface SortService extends IService<Sort> {

    /**
     * 查询分类集合
     *
     * @return 分类集合
     */
    List<Sort> selectSortList();
}
