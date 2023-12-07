package ink.ckx.mo.common.web.util

import jakarta.servlet.http.HttpServletRequest
import java.net.InetAddress
import java.net.UnknownHostException

/**
 * @author chenkaixin
 * @description
 * @since 2023/12/07
 */
object IpUtil {

    private const val UNKNOWN = "unknown"
    private const val HEADER_FORWARDED = "x-forwarded-for"
    private val CLIENT_IP_LIST =
        listOf("Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR")
    private const val LOCAL_IP = "127.0.0.1"
    private const val LOCAL_HOST = "localhost"

    /**
     * 获取 IP 地址
     *
     * @param request
     * @return
     */
    fun getIpAddr(request: HttpServletRequest): String {
        var ip = request.getHeader(HEADER_FORWARDED)
        for (clientId in CLIENT_IP_LIST) {
            if (ip.isBlank() || UNKNOWN.equals(ip, ignoreCase = true)) {
                ip = request.getHeader(clientId)
            } else {
                ip = request.remoteAddr
                break
            }
        }

        // 本机访问
        if (LOCAL_IP.equals(ip, ignoreCase = true) || LOCAL_HOST.equals(
                ip,
                ignoreCase = true
            ) || "0:0:0:0:0:0:0:1".equals(ip, ignoreCase = true)
        ) {
            // 根据网卡取本机配置的 IP
            try {
                val localHost = InetAddress.getLocalHost()
                ip = localHost.hostAddress
            } catch (e: UnknownHostException) {
                e.printStackTrace()
            }
        }

        // 对于通过多个代理的情况，第一个 IP 为客户端真实 IP,多个 IP 按照','分割
        if (ip != null && ip.length > 15) {
            if (ip.indexOf(",") > 15) {
                ip = ip.substring(0, ip.indexOf(","))
            }
        }
        return ip
    }
}