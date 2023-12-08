package cn.smilex.vueblog.common.util

import com.fasterxml.jackson.databind.JsonNode

/**
 * @author smilex
 * @date 2023/12/8 13:13:11
 */
class CollectionUtils {
    companion object {
        @JvmStatic
        fun <T> isEmpty(collection: Collection<T>?): Boolean {
            return collection === null || collection.isEmpty()
        }

        @JvmStatic
        fun isEmpty(jsonNode: JsonNode?): Boolean {
            return jsonNode == null || jsonNode.isEmpty
        }
    }
}