package vip.smilex.vueblog.common.annotation

import com.linecorp.armeria.server.annotation.AdditionalHeader

/**
 * @author smilex
 * @date 2023/12/4 21:45:31
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@AdditionalHeader(name = "Access-Control-Allow-Origin", value = ["*"])
@AdditionalHeader(name = "Access-Control-Allow-Credentials", value = ["true"])
@AdditionalHeader(name = "Access-Control-Allow-Methods", value = ["*"])
@AdditionalHeader(name = "Access-Control-Allow-Headers", value = ["*"])
@AdditionalHeader(name = "Access-Control-Expose-Headers", value = ["*"])
annotation class CrossOrigin()
