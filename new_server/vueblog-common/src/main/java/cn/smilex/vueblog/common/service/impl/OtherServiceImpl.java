package cn.smilex.vueblog.common.service.impl;

import cn.smilex.vueblog.common.config.CommonConfig;
import cn.smilex.vueblog.common.config.ResultCode;
import cn.smilex.vueblog.common.dao.OtherDao;
import cn.smilex.vueblog.common.entity.blog.SearchBlog;
import cn.smilex.vueblog.common.entity.common.HashMapBuilder;
import cn.smilex.vueblog.common.entity.common.Result;
import cn.smilex.vueblog.common.entity.common.SelectPageFooter;
import cn.smilex.vueblog.common.entity.other.AboutMe;
import cn.smilex.vueblog.common.entity.other.BackUp;
import cn.smilex.vueblog.common.entity.other.Footer;
import cn.smilex.vueblog.common.entity.other.Other;
import cn.smilex.vueblog.common.service.*;
import cn.smilex.vueblog.common.util.CommonUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.meilisearch.sdk.Index;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author smilex
 * @date 2022/11/12/11:49
 * @since 1.0
 */
@SuppressWarnings({"unused", "FieldCanBeLocal"})
@Service
public class OtherServiceImpl extends ServiceImpl<OtherDao, Other> implements OtherService {

    private BlogService blogService;
    private SortService sortService;
    private TagService tagService;
    private FriendService friendService;
    private Index index;
    private UpCloudBackUpServiceImpl upCloudBackUpService;

    @Autowired
    public void setBlogService(BlogService blogService) {
        this.blogService = blogService;
    }

    @Autowired
    public void setSortService(SortService sortService) {
        this.sortService = sortService;
    }

    @Autowired
    public void setTagService(TagService tagService) {
        this.tagService = tagService;
    }

    @Autowired
    public void setFriendService(FriendService friendService) {
        this.friendService = friendService;
    }

    @Autowired
    public void setIndex(Index index) {
        this.index = index;
    }

    @Autowired
    public void setUpCloudBackUpService(UpCloudBackUpServiceImpl upCloudBackUpService) {
        this.upCloudBackUpService = upCloudBackUpService;
    }

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

    /**
     * 查询备份信息
     *
     * @return 备份信息
     */
    @Override
    public BackUp selectBackUpInfo() throws JsonProcessingException {
        Other other = getOne(
                new LambdaQueryWrapper<Other>()
                        .eq(Other::getOrder, 3)
        );

        return CommonConfig.OBJECT_MAPPER.readValue(
                other.getContent(),
                new TypeReference<>() {
                }
        );
    }

    /**
     * 更新备份信息
     *
     * @param backUp 备份信息
     * @return 是否成功
     */
    @Override
    public boolean updateBackUp(BackUp backUp) throws JsonProcessingException {
        UpCloudBackUpServiceImpl.resetRestManager();

        return this.update(
                new LambdaUpdateWrapper<Other>()
                        .set(Other::getContent, CommonConfig.OBJECT_MAPPER.writeValueAsString(backUp))
                        .eq(Other::getOrder, 3)
        );
    }

    /**
     * 备份数据库
     */
    @Override
    public Result<?> backUpBuy() throws JsonProcessingException {
        BackUp backUp = selectBackUpInfo();
        try {
            String sql = CommonUtil.dumpSql(backUp.getUserName(), backUp.getPassWord(), "vueblog");
            if (StringUtils.isBlank(sql)) {
                return Result.fromResultCode(ResultCode.ERROR_DUMP_ERROR_NOT_GET_DUMP_SQL);
            }

            upCloudBackUpService.remoteUploadFile(
                    backUp,
                    "/sql_dump/dump_" + LocalDateTime.now() + ".sql",
                    sql
            );
            return Result.success();

        } catch (Exception e) {
            log.error("", e);
        }

        return Result.error();
    }

    /**
     * 更新页面底部脚本
     *
     * @param footer 页面底部脚本
     * @return 是否成功
     */
    @Override
    public boolean updateFooter(Footer footer) {
        return this.update(
                new LambdaUpdateWrapper<Other>()
                        .set(Other::getContent, footer.getContent())
                        .eq(Other::getOrder, 1)
        );
    }

    /**
     * 综合统计
     *
     * @return 统计数据
     */
    @Override
    public Map<String, Object> statistics() {
        return new HashMapBuilder<String, Object>(4)
                .put("blogTotal", blogService.count())
                .put("sortTotal", sortService.count())
                .put("tagTotal", tagService.count())
                .put("friendTotal", friendService.count())
                .getMap();
    }

    /**
     * 刷新搜索数据
     */
    @Override
    public boolean flushSearchData() {
        try {
            index.deleteAllDocuments();
            List<SearchBlog> searchBlogList = blogService.list()
                    .stream()
                    .map(SearchBlog::fromBlog)
                    .collect(Collectors.toList());

            index.addDocuments(
                    CommonConfig.OBJECT_MAPPER.writeValueAsString(searchBlogList),
                    CommonConfig.SEARCH_DOCUMENT_PRIMARY_KEY
            );
            return true;
        } catch (Exception e) {
            log.error("", e);
        }

        return false;
    }
}
