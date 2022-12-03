package cn.smilex.vueblog.common.service;

import cn.smilex.vueblog.common.entity.other.System;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author smilex
 * @date 2022/11/12/11:45
 * @since 1.0
 */
@SuppressWarnings("unused")
public interface SystemService extends IService<System> {

    /**
     * 查询系统信息
     *
     * @return 系统信息
     */
    System selectSystemInfo();
}
