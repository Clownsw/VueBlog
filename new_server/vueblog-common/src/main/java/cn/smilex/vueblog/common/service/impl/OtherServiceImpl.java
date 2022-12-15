package cn.smilex.vueblog.common.service.impl;

import cn.smilex.vueblog.common.dao.OtherDao;
import cn.smilex.vueblog.common.entity.common.SelectPageFooter;
import cn.smilex.vueblog.common.entity.other.AboutMe;
import cn.smilex.vueblog.common.entity.other.Other;
import cn.smilex.vueblog.common.service.OtherService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
                        new LambdaQueryWrapper<Other>()
                                .select(Other::getContent)
                                .eq(Other::getOrder, 1)

                )
        );
    }

    /**
     * 获取关于我信息
     *
     * @return 关于我信息
     */
    @Override
    public AboutMe aboutMe() {
        Other other = this.getOne(
                new LambdaQueryWrapper<Other>()
                        .select(Other::getId, Other::getTitle, Other::getContent)
                        .eq(Other::getOrder, 0)
        );
        return AboutMe.fromOther(other);
    }

    /**
     * 更新关于我
     *
     * @param aboutMe 关于我
     * @return 是否成功
     */
    @Override
    public boolean updateMe(AboutMe aboutMe) {
        return this.getBaseMapper()
                .updateMe(aboutMe);
    }
}
