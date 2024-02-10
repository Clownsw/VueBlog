package vip.smilex.vueblog.common.service;

import vip.smilex.vueblog.common.entity.common.Result;
import vip.smilex.vueblog.common.entity.common.SelectPageFooter;
import vip.smilex.vueblog.common.entity.other.AboutMe;
import vip.smilex.vueblog.common.entity.other.BackUp;
import vip.smilex.vueblog.common.entity.other.Footer;
import vip.smilex.vueblog.common.entity.other.Other;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterxml.jackson.core.JsonProcessingException;
import vip.smilex.vueblog.common.entity.common.Result;
import vip.smilex.vueblog.common.entity.common.SelectPageFooter;
import vip.smilex.vueblog.common.entity.other.AboutMe;
import vip.smilex.vueblog.common.entity.other.BackUp;
import vip.smilex.vueblog.common.entity.other.Footer;
import vip.smilex.vueblog.common.entity.other.Other;

import java.util.Map;

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

    /**
     * 查询备份信息
     *
     * @return 备份信息
     */
    BackUp selectBackUpInfo() throws JsonProcessingException;

    /**
     * 更新备份信息
     *
     * @param backUp 备份信息
     * @return 是否成功
     */
    boolean updateBackUp(BackUp backUp) throws JsonProcessingException;

    /**
     * 备份数据库
     */
    Result<?> backUpBuy() throws JsonProcessingException;

    /**
     * 更新页面底部脚本
     *
     * @param footer 页面底部脚本
     * @return 是否成功
     */
    boolean updateFooter(Footer footer);

    /**
     * 综合统计
     *
     * @return 统计数据
     */
    Map<String, Object> statistics();

    /**
     * 刷新搜索数据
     */
    boolean flushSearchData();
}
