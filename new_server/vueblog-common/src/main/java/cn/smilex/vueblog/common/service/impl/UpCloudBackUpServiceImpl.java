package cn.smilex.vueblog.common.service.impl;

import cn.smilex.vueblog.common.entity.common.HashMapBuilder;
import cn.smilex.vueblog.common.entity.other.BackUp;
import cn.smilex.vueblog.common.service.BackUpService;
import com.upyun.RestManager;
import com.upyun.UpException;
import com.upyun.UpYunUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author smilex
 * @date 2022/12/17/14:41
 */
@SuppressWarnings("FieldMayBeFinal")
@Service
public class UpCloudBackUpServiceImpl implements BackUpService {

    private static final Object LOCK = new Object();
    private static volatile RestManager REST_MANAGER = null;

    public static RestManager getRestManager(BackUp backUp) {
        if (REST_MANAGER == null) {
            synchronized (LOCK) {
                if (REST_MANAGER == null) {
                    REST_MANAGER = new RestManager(
                            backUp.getBucketName(),
                            backUp.getOperator(),
                            backUp.getOperatorPassWord()
                    );
                }
            }
        }

        return REST_MANAGER;
    }

    public static void resetRestManager() {
        synchronized (LOCK) {
            REST_MANAGER = null;
        }
    }

    /**
     * 远程上传文件
     *
     * @param backUp      备份信息对象
     * @param filePath    远程文件路径
     * @param fileContent 文件内容
     */
    @Override
    public void remoteUploadFile(BackUp backUp, String filePath, String fileContent) throws UpException, IOException {

        byte[] fileContentByteArray = fileContent.getBytes(StandardCharsets.UTF_8);

        getRestManager(backUp).writeFile(
                filePath,
                fileContentByteArray,
                new HashMapBuilder<String, String>(1)
                        .put(RestManager.PARAMS.CONTENT_MD5.getValue(), UpYunUtils.md5(fileContentByteArray))
                        .build()
        );
    }
}
