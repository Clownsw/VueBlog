package cn.smilex.vueblog.common.service.impl;

import cn.smilex.vueblog.common.dao.OtherDao;
import cn.smilex.vueblog.common.entity.Other;
import cn.smilex.vueblog.common.entity.SelectPageFooter;
import cn.smilex.vueblog.common.service.OtherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author smilex
 * @date 2022/11/12/11:49
 * @since 1.0
 */
@SuppressWarnings("unused")
@Service
public class OtherServiceImpl extends ServiceImpl<OtherDao, Other> implements OtherService {

    /**
     * 查询页面底部脚本
     *
     * @return 页面底部脚本
     */
    @Override
    public SelectPageFooter selectPageFooter() {
        return SelectPageFooter.fromOther(
                getOne(
                        new QueryWrapper<Other>()
                                .select("content")
                                .eq("`order`", 1)

                )
        );
    }
}