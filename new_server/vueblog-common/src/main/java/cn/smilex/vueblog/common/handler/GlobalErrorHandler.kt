package cn.smilex.vueblog.common.handler

import cn.smilex.vueblog.common.config.CommonConfig
import cn.smilex.vueblog.common.config.ResultCode
import cn.smilex.vueblog.common.util.CommonUtils
import com.linecorp.armeria.common.HttpResponse
import com.linecorp.armeria.server.HttpStatusException
import com.linecorp.armeria.server.ServerErrorHandler
import com.linecorp.armeria.server.ServiceRequestContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

/**
 * 全局异常处理
 *
 * @author smilex
 * @date 2023/12/3 16:25:12
 */
@Component
class GlobalErrorHandler : ServerErrorHandler {
    companion object {
        @JvmStatic
        val LOGGER: Logger = LoggerFactory.getLogger(GlobalErrorHandler::class.java)
    }

    override fun onServiceException(ctx: ServiceRequestContext, cause: Throwable): HttpResponse {
        LOGGER.error(CommonConfig.EMPTY_STRING, cause)

        val defaultError = CommonUtils.buildJsonHttpResponseByResultCode(ResultCode.UNKNOWN_ERROR)

        return when (cause) {
            is HttpStatusException -> {
                return when (cause.httpStatus().code()) {
                    403 -> CommonUtils.buildJsonHttpResponseByResultCode(ResultCode.FORBIDDEN)
                    404 -> CommonUtils.buildJsonHttpResponseByResultCode(ResultCode.NOT_FOUND)
                    405 -> CommonUtils.buildJsonHttpResponseByResultCode(ResultCode.METHOD_NOT_ALLOWED)
                    else -> defaultError
                }
            }
            is NumberFormatException -> CommonUtils.buildJsonHttpResponseByResultCode(ResultCode.ERROR_REQUEST_PARAM_ERROR)
            else -> defaultError
        }
    }
}