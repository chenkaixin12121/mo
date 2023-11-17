package ink.ckx.mo.pay.util;

import cn.hutool.crypto.digest.DigestUtil;

import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

public class Sign {

    /**
     * 微信支付签名算法sign
     */
    public static String createSign(String characterEncoding, SortedMap<Object, Object> parameters, String key) {

        StringBuilder sb = new StringBuilder();
        Set<Map.Entry<Object, Object>> es = parameters.entrySet();
        for (Map.Entry<Object, Object> e : es) {
            String k = (String) e.getKey();
            Object v = e.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k).append("=").append(v).append("&");
            }
        }
        sb.append("key=").append(key);
        return DigestUtil.md5Hex(sb.toString(), characterEncoding);
    }
}
