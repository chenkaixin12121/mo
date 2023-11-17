package ink.ckx.mo.pay.util;

import cn.hutool.core.util.StrUtil;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;

import java.io.InputStream;

public class XmlUtil {

    /**
     * xml 字符串转换为对象
     */
    public static Object xml2Object(String inputXml, Class<?> type) {
        if (StrUtil.isBlank(inputXml)) {
            return null;
        }
        XStream xstream = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
        xstream.alias("xml", type);
        return xstream.fromXML(inputXml);
    }

    /**
     * 从 inputStream 中读取对象
     */
    public static Object xml2Object(InputStream inputStream, Class<?> type) {
        if (null == inputStream) {
            return null;
        }
        XStream xstream = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
        xstream.alias("xml", type);
        return xstream.fromXML(inputStream, type);
    }

    /**
     * 对象转换为 xml 字符串
     */
    public static String object2Xml(Object ro, Class<?> types) {
        if (null == ro) {
            return null;
        }
        XStream xstream = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
        xstream.alias("xml", types);
        return xstream.toXML(ro);
    }
}
