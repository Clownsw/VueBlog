package cn.smilex.vueblog.common.service;

import cn.smilex.vueblog.common.entity.common.SelectPageFooter;
import cn.smilex.vueblog.common.entity.other.AboutMe;
import cn.smilex.vueblog.common.entity.other.Other;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author smilex
 * @date 2022/11/12/11:45
 * @since 1.0
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public interface OtherService extends IService<Other> {

    /**
     * 查询页面底部脚本
     *
     * @return 页面底部脚本
     */
    SelectPageFooter selectPageFooter();

    /**
     * 获取关于我信息
     *
     * @return 关于我信息
     */
    AboutMe aboutMe();

    /**
     * 更新关于我
     *
     * @param aboutMe 关于我
     * @return 是否成功
     */
    boolean updateMe(AboutMe aboutMe);
}
