package cn.smilex.vueblog.common.util

import cn.smilex.vueblog.common.config.CommonConfig
import cn.smilex.vueblog.common.config.ResultCode
import cn.smilex.vueblog.common.entity.blog.SearchBlog
import cn.smilex.vueblog.common.entity.common.Result
import cn.smilex.vueblog.common.entity.common.Triplet
import cn.smilex.vueblog.common.exception.VueBlogException
import cn.smilex.vueblog.common.function.TryRunExceptionHandler
import cn.smilex.vueblog.common.function.TryRunTask
import cn.smilex.vueblog.common.function.TryRunTaskNotResult
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonNode
import com.linecorp.armeria.common.*
import com.linecorp.armeria.internal.server.annotation.AnnotatedService
import com.linecorp.armeria.server.HttpService
import com.meilisearch.sdk.Index
import com.meilisearch.sdk.exceptions.MeilisearchException
import org.apache.commons.io.IOUtils
import org.apache.commons.lang3.StringUtils
import java.io.IOException
import java.lang.reflect.Method
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.concurrent.Future
import java.util.function.Function
import java.util.function.Predicate
import java.util.function.Supplier
import java.util.stream.Collectors

/**
 * TODO
 * @author smilex
 * @date 2023/12/4 21:47:36
 */
class CommonUtils {
    companion object {

        /**
         * 创建任务并提交到公共线程池
         *
         * @param runnable 任务
         * @return 结果
         */
        @JvmStatic
        fun createTask(runnable: Runnable): Future<*>? {
            return CommonConfig.COMMON_THREAD_POOL.submit(runnable)
        }

        @JvmStatic
        fun isInForArray(clazz: Class<*>, array: Array<Class<*>>): Boolean {
            for (aClass in array) {
                if (aClass == clazz) {
                    return true
                }
            }
            return false
        }

        /**
         * 根据每页数量查询起始查询位置
         *
         * @param currentPage 当前页
         * @param pageSize    每页大小
         * @return 起始查询位置
         */
        @JvmStatic
        fun calcLimit(currentPage: Long, pageSize: Long): Long {
            return (currentPage - 1L) * pageSize
        }

        /**
         * 根据总数量和每页数量计算总页数
         *
         * @param totalCount 总数量
         * @param pageSize   每页数量
         * @return 总页数
         */
        @JvmStatic
        fun calcPageCount(totalCount: Long, pageSize: Long): Long {
            var pageCount: Long = 0
            if (totalCount > 0) {
                pageCount = if (totalCount <= pageSize) {
                    1
                } else if (totalCount % pageSize == 0L) {
                    totalCount / pageSize
                } else {
                    totalCount / pageSize + 1
                }
            }
            return pageCount
        }

        /**
         * object集合检查存在null
         *
         * @param objectList object集合
         * @return 检查存在null
         */
        @JvmStatic
        fun objectListCheckHaveNull(vararg objectList: Any?): Boolean {
            for (`object` in objectList) {
                if (`object` == null) {
                    return true
                }
                if (`object` is String) {
                    if (StringUtils.isBlank(`object` as String?)) {
                        return true
                    }
                }
                if (`object` is Optional<*>) {
                    if (!`object`.isPresent) {
                        return true
                    }
                }
            }
            return false
        }

        /**
         * try catch 运行 task, 如果发生异常 则调用exceptionHandler进行异常处理
         *
         * @param tryRunTaskNotResult                   任务
         * @param exceptionHandler 异常处理
         */
        @JvmStatic
        fun tryRun(tryRunTaskNotResult: TryRunTaskNotResult, exceptionHandler: TryRunExceptionHandler) {
            try {
                tryRunTaskNotResult.run()
            } catch (e: Throwable) {
                exceptionHandler.handler(e)
            }
        }

        /**
         * try catch 运行 task, 如果发生异常 则调用exceptionHandler进行异常处理
         *
         * @param task                   任务
         * @param tryRunExceptionHandler 异常处理
         * @param <T>                    unknown type1
         * @param <E>                    unknown type2
         * @return 结果
        </E></T> */
        @JvmStatic
        fun <T> tryRun(task: TryRunTask<T>, exceptionHandler: Optional<TryRunExceptionHandler>): Result<*> {
            return try {
                Result.success(task.run())
            } catch (e: Throwable) {
                if (exceptionHandler.isPresent) {
                    exceptionHandler.get()
                        .handler(e)
                }
                Result.error()
            }
        }

        /**
         * try catch 运行 task, 如果发生异常 则调用exceptionHandler进行异常处理并调用 defaultValue返回默认值
         *
         * @param task                   任务
         * @param tryRunExceptionHandler 异常处理
         * @param <T>                    unknown type1
         * @param <E>                    unknown type2
         * @return 结果
        </E></T> */
        @JvmStatic
        fun <T, E> tryRun(task: TryRunTask<T>, defaultValue: Supplier<T>, exceptionHandler: TryRunExceptionHandler): T {
            try {
                return task.run()
            } catch (e: Throwable) {
                exceptionHandler.handler(e)
            }
            return defaultValue.get()
        }

        /**
         * 检查请求对象中的token有效性
         *
         * @param request 请求对象
         * @return 有效性
         */
        @JvmStatic
        fun checkTokenAndGetData(request: HttpRequest): Map<String?, Any?>? {
            return checkTokenAndGetData(
                request.headers()["authorization"]
            )
        }

        /**
         * 检查token有效性
         *
         * @param token token
         * @return 有效性
         */
        @JvmStatic
        fun checkTokenAndGetData(token: String?): Map<String?, Any?>? {
            if (StringUtils.isBlank(token)) {
                throw VueBlogException(ResultCode.ERROR_CURRENT_LOGIN_USER_INFO_ERROR_NOT_FOUND_TOKEN)
            }
            val signResult = JwtUtils.signJWTToken(token)
            if (!signResult.left!!) {
                throw VueBlogException(ResultCode.ERROR_CURRENT_LOGIN_USER_INFO_ERROR_NOT_FOUND_TOKEN)
            }
            return signResult.right
        }

        /**
         * 计算a集合和b集合中被删除、新添加、未变动集合
         *
         * @param a   a集合
         * @param b   b集合
         * @param <T> unknown type
         * @return 集合
        </T> */
        @JvmStatic
        fun <T> getDelAndAddAndDefaultList(a: List<T>, b: List<T>): Triplet<List<T>, List<T>, List<T>>? {
            val _del: MutableList<T> = ArrayList()
            val _add: MutableList<T> = ArrayList()
            val _default: MutableList<T> = ArrayList()

            // del
            for (v in a) {
                if (!b.contains(v)) {
                    _del.add(v)
                }
            }

            // add
            for (v in b) {
                if (!a.contains(v)) {
                    _add.add(v)
                }
            }

            // default
            for (v in a) {
                if (!_del.contains(v) && !_add.contains(v) && !_default.contains(v)) {
                    _default.add(v)
                }
            }
            for (v in b) {
                if (!_del.contains(v) && !_add.contains(v) && !_default.contains(v)) {
                    _default.add(v)
                }
            }
            return Triplet(_del, _add, _default)
        }

        /**
         * 生成一个允许跨域的响应对象
         *
         * @return 响应对象
         */
        @JvmStatic
        fun createCrossOriginHttpResponse(): HttpResponse? {
            return HttpResponse.of(
                ResponseHeaders.of(HttpStatus.OK)
                    .toBuilder()
                    .add("Access-Control-Allow-Origin", "*")
                    .add("Access-Control-Allow-Credentials", "true")
                    .add("Access-Control-Allow-Methods", "*")
                    .add("Access-Control-Allow-Headers", "*")
                    .add("Access-Control-Expose-Headers", "*")
                    .build()
            )
        }

        fun interface CollectionToStrMap<T> {
            fun map(t: T): String?
        }

        /**
         * 集合转字符串
         *
         * @param collection 集合
         * @param handler    处理方法
         * @param split      分隔符
         * @param <T>        unknown type
         * @return 字符串
        </T> */
        @JvmStatic
        fun <T> collectionToStr(collection: Collection<T>, handler: CollectionToStrMap<T>, split: String?): String? {
            val sb = StringBuilder()
            for (t in collection) {
                sb.append(handler.map(t))
                    .append(split)
            }
            return if (collection.size == 0) CommonConfig.EMPTY_STRING else sb.substring(0, sb.length - 1)
        }

        /**
         * 集合转字符串(最后一个不添加分割)
         *
         * @param collection 集合
         * @param handler    处理方法
         * @param split      分隔符
         * @param <T>        unknown type
         * @return 字符串
        </T> */
        @JvmStatic
        fun <T> collectionToStrNotLast(
            collection: Collection<T>,
            handler: CollectionToStrMap<T>,
            split: String?
        ): String? {
            val len = collection.size
            val sb = StringBuilder()
            var i = 0
            for (t in collection) {
                sb.append(handler.map(t))
                if (i + 1 == len) {
                    continue
                }
                sb.append(split)
                i++
            }
            return if (collection.size == 0) CommonConfig.EMPTY_STRING else sb.substring(0, sb.length - 1)
        }

        /**
         * JsonNode转集合
         *
         * @param collection 集合
         * @param jsonNode   JsonNode
         * @param handler    处理方法
         * @param <R>        unknown type
         * @return 集合
        </R> */
        @JvmStatic
        fun <R> jsonNodeToCollection(
            collection: MutableCollection<R>,
            jsonNode: JsonNode,
            handler: Function<JsonNode, R>
        ): Collection<R> {
            for (node in jsonNode) {
                collection.add(handler.apply(node))
            }
            return collection
        }

        /**
         * obj是否在集合中存在
         *
         * @param collection 集合
         * @param handler    处理方法
         * @param <T>        unknown type
         * @return 是否存在
        </T> */
        @JvmStatic
        fun <T> objInCollectionExists(collection: List<T>, handler: Predicate<T>): Boolean {
            for (t in collection) {
                if (handler.test(t)) {
                    return true
                }
            }
            return false
        }

        /**
         * 搜索客户端添加或更新
         *
         * @param blogIndex  博文索引
         * @param document   内容
         * @param searchBlog 实体
         * @throws MeilisearchException    unknown exception
         * @throws JsonProcessingException unknown exception
         */
        @JvmStatic
        fun searchClientAddOrUpdate(blogIndex: Index, document: String?, searchBlog: SearchBlog) {
            val root = CommonConfig.OBJECT_MAPPER.readTree(blogIndex.documents)
            if (root.size() == 0) {
                blogIndex.addDocuments(document, CommonConfig.SEARCH_DOCUMENT_PRIMARY_KEY)
                return
            }
            val searchBlogList = jsonNodeToCollection(
                ArrayList(),
                root
            ) { node: JsonNode ->
                try {
                    return@jsonNodeToCollection CommonConfig.OBJECT_MAPPER.readValue(
                        node.toString(),
                        object : TypeReference<SearchBlog?>() {}
                    )
                } catch (e: JsonProcessingException) {
                    return@jsonNodeToCollection null
                }
            }.stream()
                .filter { obj: SearchBlog? -> Objects.nonNull(obj) }
                .collect(Collectors.toList())

            if (objInCollectionExists(
                    searchBlogList
                ) { searchBlogDocument: SearchBlog? -> searchBlog.id == searchBlogDocument?.id }
            ) {
                blogIndex.updateDocuments(document, CommonConfig.SEARCH_DOCUMENT_PRIMARY_KEY)
                return
            }

            blogIndex.addDocuments(document, CommonConfig.SEARCH_DOCUMENT_PRIMARY_KEY)
        }

        /**
         * 空任务 Lambda Interface
         */
        fun interface EmptyTask {
            @Throws(Exception::class)
            fun handler()
        }

        /**
         * 捕获异常转运行时异常, 避免直接抛出异常
         *
         * @param emptyTask 任务
         */
        @JvmStatic
        fun exceptionToRunTimeException(emptyTask: EmptyTask) {
            try {
                emptyTask.handler()
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }

        /**
         * 判断一个Optional内的值是否为空
         *
         * @param value 值
         * @param <T>   unknwon type
         * @return Optional值是否为空
        </T> */
        @JvmStatic
        fun <T> isEmpty(value: Optional<T>): Boolean {
            return !value.isPresent
        }

        /**
         * 判断Optional数组内是否包含空数据
         *
         * @param values Optional 数组
         * @return 结果
         */
        @JvmStatic
        fun isEmpty(vararg values: Optional<Any?>): Boolean {
            for (value in values) {
                if (!value.isPresent) {
                    return true
                }
            }
            return false
        }

        /**
         * 备份数据库
         *
         * @param userName 数据库用户名
         * @param passWord 数据库密码
         * @param dataBase 数据库名称
         * @return 数据库sql
         * @throws IOException unknown exception
         */
        @JvmStatic
        fun dumpSql(userName: String?, passWord: String?, dataBase: String): String? {
            val exec = Runtime.getRuntime()
                .exec(
                    arrayOf(
                        "mysqldump", String.format("-u%s", userName), String.format("-p%s", passWord),
                        dataBase
                    )
                )
            return IOUtils.toString(exec.inputStream, StandardCharsets.UTF_8)
        }

        /**
         * 构建一个JSON响应对象根据ResultCode
         *
         * @param resultCode resultCode
         * @return 响应对象
         */
        @JvmStatic
        fun buildJsonHttpResponseByResultCode(resultCode: ResultCode): HttpResponse {
            return HttpResponse.ofJson(
                HttpStatus.OK,
                MediaType.JSON_UTF_8,
                Result.fromResultCode(resultCode)
            )
        }

        /**
         * 获取HttpService的Method对象
         *
         * @param httpService HttpService
         * @return Method对象
         * @throws NoSuchFieldException   unknown exception
         * @throws IllegalAccessException unknown exception
         */
        @JvmStatic
        fun getHttpServiceMethodField(httpService: HttpService?): Method? {
            if (httpService is AnnotatedService) {
                val methodField = AnnotatedService::class.java.getDeclaredField("method")
                methodField.isAccessible = true
                return methodField[httpService] as Method
            }
            return null
        }

        /**
         * 解析QueryString到Map
         *
         * @param queryString queryString
         * @return Map
         */
        @JvmStatic
        fun parseQueryStringToMap(queryString: Optional<String?>): Map<String?, Any?>? {
            try {
                return CommonConfig.OBJECT_MAPPER.readValue(
                    queryString.get(),
                    object : TypeReference<HashMap<String?, Any?>?>() {}
                )
            } catch (ignore: Exception) {
            }
            return emptyMap<String?, Any>()
        }

        /**
         * 遍历JsonNode
         *
         * @param jsonNode JsonNode
         * @param map      map
         * @param <T>
         * @return
        </T> */
        @JvmStatic
        fun <T> mapJsonNode(jsonNode: JsonNode, mapHandler: Function<JsonNode?, T>): List<T>? {
            val valueList: MutableList<T> = ArrayList(jsonNode.size())
            for (node in jsonNode) {
                var value: T
                if (mapHandler.apply(node).also { value = it } != null) {
                    valueList.add(value)
                }
            }
            return valueList
        }
    }
}