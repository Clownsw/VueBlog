package cn.smilex.vueblog.common.service.impl;

import cn.smilex.vueblog.common.dao.SystemDao;
import cn.smilex.vueblog.common.entity.System;
import cn.smilex.vueblog.common.service.SystemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author smilex
 * @date 2022/11/12/11:50
 * @since 1.0
 */
@SuppressWarnings("unused")
@Service
public class SystemServiceImpl extends ServiceImpl<SystemDao, System> implements SystemService {
}
