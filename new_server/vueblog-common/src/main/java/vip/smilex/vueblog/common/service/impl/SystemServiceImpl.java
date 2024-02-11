package vip.smilex.vueblog.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.smilex.vueblog.common.dao.SystemDao;
import vip.smilex.vueblog.common.entity.other.Footer;
import vip.smilex.vueblog.common.entity.other.System;
import vip.smilex.vueblog.common.entity.other.SystemUpdateRequest;
import vip.smilex.vueblog.common.service.OtherService;
import vip.smilex.vueblog.common.service.SystemService;

/**
 * @author smilex
 * @date 2022/11/12/11:50
 * @since 1.0
 */
@SuppressWarnings("unused")
@Service
public class SystemServiceImpl extends ServiceImpl<SystemDao, System> implements SystemService {
    private OtherService otherService;

    @Autowired
    public void setOtherService(OtherService otherService) {
        this.otherService = otherService;
    }

    /**
     * 查询系统信息
     *
     * @return 系统信息
     */
    @Override
    public System selectSystemInfo() {
        return getOne(
                new LambdaQueryWrapper<System>()
                        .eq(System::getId, 1)
        );
    }

    /**
     * 更新系统设置
     *
     * @param systemUpdateRequest 系统设置更新请求对象
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateSystem(SystemUpdateRequest systemUpdateRequest) {
        System system = systemUpdateRequest.getSystem();
        Footer footer = systemUpdateRequest.getFooter();

        this.updateById(system);
        otherService.updateFooter(footer);
    }
}
