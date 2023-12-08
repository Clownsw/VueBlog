package cn.smilex.vueblog.common.entity.music

import cn.smilex.vueblog.common.config.CommonConfig
import cn.smilex.vueblog.common.util.CommonUtils.Companion.collectionToStrNotLast
import cn.smilex.vueblog.common.util.CommonUtils.Companion.mapJsonNode
import cn.smilex.vueblog.common.util.CommonUtils.Companion.tryRun
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author smilex
 * @date 2023/12/8 14:04:20
 */
data class MusicSearchResult(
    @JsonSerialize(using = ToStringSerializer::class)
    var id: Int?,
    var name: String?,
    var artist: String?,
    var cover: String?
) {
    companion object {
        val LOG: Logger = LoggerFactory.getLogger(MusicSearchResult::javaClass.javaClass)

        @JvmStatic
        fun parse(song: JsonNode): MusicSearchResult {
            return MusicSearchResult(
                song["id"].asInt(),
                song["name"].asText(),
                collectionToStrNotLast(
                    tryRun(
                        {
                            val ar = song["ar"]
                            mapJsonNode(
                                ar
                            ) { v: JsonNode -> v["name"].asText() }
                        }, { emptyList() }
                    ) { e: Throwable -> LOG.error("", e) },
                    { v: String? -> v },
                    ", "
                ),
                tryRun(
                    {
                        val al = song["al"]
                        al["picUrl"].asText()
                    },
                    { CommonConfig.EMPTY_STRING }
                ) { e: Throwable -> LOG.error("", e) }
            )
        }
    }
}