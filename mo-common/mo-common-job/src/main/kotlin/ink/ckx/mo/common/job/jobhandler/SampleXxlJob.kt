package ink.ckx.mo.common.job.jobhandler

import com.xxl.job.core.context.XxlJobHelper
import com.xxl.job.core.handler.annotation.XxlJob
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * XxlJob开发示例（Bean模式）
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

    /**
     * 1、简单任务示例（Bean模式）
     */
    @XxlJob("demoJobHandler")
    fun demoJobHandler() {
        XxlJobHelper.log("XXL-JOB, Hello World.")

        for (i in 0..4) {
            XxlJobHelper.log("beat at:$i")
            TimeUnit.SECONDS.sleep(2)
        }
        // default success
    }


    /**
     * 2、分片广播任务
     */
    @XxlJob("shardingJobHandler")
    fun shardingJobHandler() {
        // 分片参数

        val shardIndex = XxlJobHelper.getShardIndex()
        val shardTotal = XxlJobHelper.getShardTotal()

        XxlJobHelper.log("分片参数：当前分片序号 = {}, 总分片数 = {}", shardIndex, shardTotal)

        // 业务逻辑
        for (i in 0 until shardTotal) {
            if (i == shardIndex) {
                XxlJobHelper.log("第 {} 片, 命中分片开始处理", i)
            } else {
                XxlJobHelper.log("第 {} 片, 忽略", i)
            }
        }
    }


    /**
     * 3、命令行任务
     */
    @XxlJob("commandJobHandler")
    fun commandJobHandler() {
        val command = XxlJobHelper.getJobParam()
        var exitValue = -1

        var bufferedReader: BufferedReader? = null
        try {
            // command process
            val processBuilder = ProcessBuilder()
            processBuilder.command(command)
            processBuilder.redirectErrorStream(true)

            val process = processBuilder.start()

            //Process process = Runtime.getRuntime().exec(command);
            val bufferedInputStream = BufferedInputStream(process.inputStream)
            bufferedReader = BufferedReader(InputStreamReader(bufferedInputStream))

            // command log
            var line: String?
            while ((bufferedReader.readLine().also { line = it }) != null) {
                XxlJobHelper.log(line)
            }

            // command exit
            process.waitFor()
            exitValue = process.exitValue()
        } catch (e: Exception) {
            XxlJobHelper.log(e)
        } finally {
            bufferedReader?.close()
        }

        if (exitValue == 0) {
            // default success
        } else {
            XxlJobHelper.handleFail("command exit value($exitValue) is failed")
        }
    }


    /**
     * 4、跨平台Http任务
     * 参数示例：
     * "url: http://www.baidu.com\n" +
     * "method: get\n" +
     * "data: content\n";
     */
    @XxlJob("httpJobHandler")
    fun httpJobHandler() {
        // param parse

        val param = XxlJobHelper.getJobParam()
        if (param == null || param.trim { it <= ' ' }.length == 0) {
            XxlJobHelper.log("param[$param] invalid.")

            XxlJobHelper.handleFail()
            return
        }

        val httpParams = param.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var url: String? = null
        var method: String? = null
        var data: String? = null
        for (httpParam in httpParams) {
            if (httpParam.startsWith("url:")) {
                url = httpParam.substring(httpParam.indexOf("url:") + 4).trim { it <= ' ' }
            }
            if (httpParam.startsWith("method:")) {
                method = httpParam.substring(httpParam.indexOf("method:") + 7).trim { it <= ' ' }
                    .uppercase(Locale.getDefault())
            }
            if (httpParam.startsWith("data:")) {
                data = httpParam.substring(httpParam.indexOf("data:") + 5).trim { it <= ' ' }
            }
        }

        // param valid
        if (url == null || url.trim { it <= ' ' }.length == 0) {
            XxlJobHelper.log("url[$url] invalid.")

            XxlJobHelper.handleFail()
            return
        }
        if (method == null || !mutableListOf("GET", "POST").contains(method)) {
            XxlJobHelper.log("method[$method] invalid.")

            XxlJobHelper.handleFail()
            return
        }
        val isPostMethod = method == "POST"

        // request
        var connection: HttpURLConnection? = null
        var bufferedReader: BufferedReader? = null
        try {
            // connection
            val realUrl = URL(url)
            connection = realUrl.openConnection() as HttpURLConnection

            // connection setting
            connection.requestMethod = method
            connection!!.doOutput = isPostMethod
            connection.doInput = true
            connection.useCaches = false
            connection.readTimeout = 5 * 1000
            connection.connectTimeout = 3 * 1000
            connection.setRequestProperty("connection", "Keep-Alive")
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8")
            connection.setRequestProperty("Accept-Charset", "application/json;charset=UTF-8")

            // do connection
            connection.connect()

            // data
            if (isPostMethod && data != null && data.trim { it <= ' ' }.length > 0) {
                val dataOutputStream = DataOutputStream(connection.outputStream)
                dataOutputStream.write(data.toByteArray(charset("UTF-8")))
                dataOutputStream.flush()
                dataOutputStream.close()
            }

            // valid StatusCode
            val statusCode = connection.responseCode
            if (statusCode != 200) {
                throw RuntimeException("Http Request StatusCode($statusCode) Invalid.")
            }

            // result
            bufferedReader = BufferedReader(InputStreamReader(connection.inputStream, "UTF-8"))
            val result = StringBuilder()
            var line: String?
            while ((bufferedReader.readLine().also { line = it }) != null) {
                result.append(line)
            }
            val responseMsg = result.toString()

            XxlJobHelper.log(responseMsg)

            return
        } catch (e: Exception) {
            XxlJobHelper.log(e)

            XxlJobHelper.handleFail()
            return
        } finally {
            try {
                bufferedReader?.close()
                connection?.disconnect()
            } catch (e2: Exception) {
                XxlJobHelper.log(e2)
            }
        }
    }

    /**
     * 5、生命周期任务示例：任务初始化与销毁时，支持自定义相关逻辑；
     */
    @XxlJob(value = "demoJobHandler2", init = "init", destroy = "destroy")
    fun demoJobHandler2() {
        XxlJobHelper.log("XXL-JOB, Hello World.")
    }

    fun init() {
        logger.info("init")
    }

    fun destroy() {
        logger.info("destroy")
    }


    companion object {
        private val logger: Logger = LoggerFactory.getLogger(SampleXxlJob::class.java)
    }
}