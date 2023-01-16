package cn.smilex.vueblog.common.service.impl;

import cn.smilex.vueblog.common.config.CommonConfig;
import cn.smilex.vueblog.common.config.ResultCode;
import cn.smilex.vueblog.common.entity.common.VueBlogConfig;
import cn.smilex.vueblog.common.entity.music.MusicSearchResult;
import cn.smilex.vueblog.common.exception.VueBlogException;
import cn.smilex.vueblog.common.service.MusicService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author smilex
 */
@SuppressWarnings({"unused", "unchecked"})
@Service
public class MusicServiceImpl implements MusicService {

    private VueBlogConfig vueBlogConfig;

    @Autowired
    public void setVueBlogConfig(VueBlogConfig vueBlogConfig) {
        this.vueBlogConfig = vueBlogConfig;
    }

    private void checkServer() {
        // Easy check music server url
        if (StringUtils.isBlank(vueBlogConfig.getMusicServer())) {
            throw new VueBlogException(ResultCode.ERROR_SYSTEM_CONFIG_ERROR);
        }
    }

    /**
     * 搜索音乐
     *
     * @param musicName 音乐名称
     * @return 结果
     */
    @Override
    public List<MusicSearchResult> searchMusic(String musicName) {
        checkServer();

        return (List<MusicSearchResult>) CommonConfig.EMPTY_LIST;
    }
}
