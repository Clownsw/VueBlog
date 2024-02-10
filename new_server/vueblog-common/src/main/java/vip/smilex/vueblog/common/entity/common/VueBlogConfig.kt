package vip.smilex.vueblog.common.entity.common

import lombok.Data

/**
 * @author smilex
 * @date 2023/12/8 13:49:57
 */
@Data
data class VueBlogConfig(
    var dataSourceJdbcUrl: String,
    var dataSourceUserName: String,
    var datasourcePassWord: String,
    var jwtKey: String,
    var jwtDayNumber: Int,
    var vueBlogBeforeWebPageSize: Long,
    var vueBlogFriendDefaultHref: String,
    var vueBlogFriendDefaultAvatar: String,
    var vueBlogAfterFriendPageSize: Long,
    var searchServer: String,
    var searchKey: String,
    var searchAttributesToHighlight: Array<String>,
    var searchLimit: Int,
    var musicServer: String,
    var isProc: Boolean = false
)