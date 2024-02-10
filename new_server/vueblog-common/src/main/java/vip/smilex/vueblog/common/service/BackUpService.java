package vip.smilex.vueblog.common.service;

import vip.smilex.vueblog.common.entity.other.BackUp;
import com.upyun.UpException;
import vip.smilex.vueblog.common.entity.other.BackUp;

import java.io.IOException;

/**
 * @author smilex
 * @date 2022/12/17/14:41
 */
public interface BackUpService {

    /**
     * 远程上传文件
     *
     * @param backUp      备份信息对象
     * @param filePath    远程文件路径
     * @param fileContent 文件内容
     */
    void remoteUploadFile(BackUp backUp, String filePath, String fileContent) throws UpException, IOException;
}
