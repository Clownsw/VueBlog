package cn.smilex.vueblog.common.entity.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author smilex
 * @date 2022/11/13/12:21
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VueBlogConfig {
    private String dataSourceJdbcUrl;
    private String dataSourceUserName;
    private String datasourcePassWord;
    private String jwtKey;
    private Integer jwtDayNumber;
    private Long vueBlogBeforeWebPageSize;
    private String vueBlogFriendDefaultHref;
    private String vueBlogFriendDefaultAvatar;
    private Long vueBlogAfterFriendPageSize;
    private String searchServer;
    private String searchKey;
    private String[] searchAttributesToHighlight;
    private Integer searchLimit;
    private String musicServer;
    private Boolean isProc;
}
