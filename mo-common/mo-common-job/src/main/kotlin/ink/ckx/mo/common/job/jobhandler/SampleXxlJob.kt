package ink.ckx.mo.common.job.jobhandler

import com.xxl.job.core.biz.model.ReturnT
import com.xxl.job.core.handler.IJobHandler
import com.xxl.job.core.handler.annotation.XxlJob
import com.xxl.job.core.log.XxlJobLogger
import com.xxl.job.core.util.ShardingUtil
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.TimeUnit

/**
 * XxlJob开发示例（Bean模式）
 *
 *
 * 开发步骤：
 * 1、任务开发：在Spring Bean实例中，开发Job方法；
 * 2、注解配置：为Job方法添加注解 "@XxlJob(value="自定义jobhandler名称", init = "JobHandler初始化方法", destroy = "JobHandler销毁方法")"，注解value值对应的是调度中心新建任务的JobHandler属性的值。
 * 3、执行日志：需要通过 "XxlJobHelper.log" 打印执行日志；
 * 4、任务结果：默认任务结果为 "成功" 状态，不需要主动设置；如有诉求，比如设置任务结果为失败，可以通过 "XxlJobHelper.handleFail/handleSuccess" 自主设置任务结果；
 *
 * @author xuxueli 2019-12-11 21:52:51
 */
@Component
class SampleXxlJob {

    private val log = KotlinLogging.logger {}

    /**
     * 1、简单任务示例（Bean模式）
     */
    @XxlJob("demoJobHandler")
    fun demoJobHandler(param: String): ReturnT<String> {
        XxlJobLogger.log("XXL-JOB, Hello World.")
        for (i in 0..4) {
            XxlJobLogger.log("beat at:$i")
            TimeUnit.SECONDS.sleep(2)
        }
        return ReturnT.SUCCESS
    }

    /**
     * 2、分片广播任务
     */
    @XxlJob("shardingJobHandler")
    fun shardingJobHandler(param: String): ReturnT<String> {

        // 分片参数
        val shardingVO = ShardingUtil.getShardingVo()
        XxlJobLogger.log("分片参数：当前分片序号 = {}, 总分片数 = {}", shardingVO.index, shardingVO.total)

        // 业务逻辑
        for (i in 0 until shardingVO.total) {
            if (i == shardingVO.index) {
                XxlJobLogger.log("第 {} 片, 命中分片开始处理", i)
            } else {
                XxlJobLogger.log("第 {} 片, 忽略", i)
            }
        }
        return ReturnT.SUCCESS
    }

    /**
     * 3、命令行任务
     */
    @XxlJob("commandJobHandler")
    fun commandJobHandler(param: String): ReturnT<String> {
        var exitValue = -1
        var bufferedReader: BufferedReader? = null
        try {
            // command process
            val process = Runtime.getRuntime().exec(param)
            val bufferedInputStream = BufferedInputStream(process.inputStream)
            bufferedReader = BufferedReader(InputStreamReader(bufferedInputStream))

            // command log
            var line: String
            while (bufferedReader.readLine().also { line = it } != null) {
                XxlJobLogger.log(line)
            }

            // command exit
            process.waitFor()
            exitValue = process.exitValue()
        } catch (e: Exception) {
            XxlJobLogger.log(e)
        } finally {
            bufferedReader?.close()
        }
        return if (exitValue == 0) {
            IJobHandler.SUCCESS
        } else {
            ReturnT(IJobHandler.FAIL.code, "command exit value($exitValue) is failed")
        }
    }

    /**
     * 4、跨平台Http任务
     */
    @XxlJob("httpJobHandler")
    fun httpJobHandler(param: String): ReturnT<String> {

        // request
        var connection: HttpURLConnection? = null
        var bufferedReader: BufferedReader? = null
        return try {
            // connection
            val realUrl = URL(param)
            connection = realUrl.openConnection() as HttpURLConnection

            // connection setting
            connection.requestMethod = "GET"
            connection.doOutput = true
            connection.doInput = true
            connection.useCaches = false
            connection.readTimeout = 5 * 1000
            connection.connectTimeout = 3 * 1000
            connection.setRequestProperty("connection", "Keep-Alive")
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8")
            connection.setRequestProperty("Accept-Charset", "application/json;charset=UTF-8")

            // do connection
            connection.connect()

            //Map<String, List<String>> map = connection.getHeaderFields();

            // valid StatusCode
            val statusCode = connection.responseCode
            if (statusCode != 200) {
                throw RuntimeException("Http Request StatusCode($statusCode) Invalid.")
            }

            // result
            bufferedReader = BufferedReader(InputStreamReader(connection.inputStream, "UTF-8"))
            val result = StringBuilder()
            var line: String
            while (bufferedReader.readLine().also { line = it } != null) {
                result.append(line)
            }
            val responseMsg = result.toString()
            XxlJobLogger.log(responseMsg)
            ReturnT.SUCCESS
        } catch (e: Exception) {
            XxlJobLogger.log(e)
            ReturnT.FAIL
        } finally {
            try {
                bufferedReader?.close()
                connection?.disconnect()
            } catch (e2: Exception) {
                XxlJobLogger.log(e2)
            }
        }
    }

    /**
     * 5、生命周期任务示例：任务初始化与销毁时，支持自定义相关逻辑；
     */
    @XxlJob(value = "demoJobHandler2", init = "init", destroy = "destroy")
    fun demoJobHandler2(param: String): ReturnT<String> {
        XxlJobLogger.log("XXL-JOB, Hello World.")
        return ReturnT.SUCCESS
    }

    fun init() {
        log.info { "init" }
    }

    fun destroy() {
        log.info { "destroy" }
    }
}