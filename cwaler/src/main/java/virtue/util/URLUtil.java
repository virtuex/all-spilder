package virtue.util;

import com.sun.org.apache.bcel.internal.classfile.ConstantValue;
import virtue.constant.ConValues;

import java.io.UnsupportedEncodingException;

import static virtue.constant.ConValues.SEARCH_ADDRESS_SUFFIX;

public class URLUtil {
    /**
     * url字符转码
     */
    public static String getURLEncode(String urlValue){
        String urlEncode= null;

        try {
            urlEncode = java.net.URLEncoder.encode(urlValue, "gb2312");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return urlEncode;
    }

    /**
     * 拼接完整的查询地址
     */
    public static String getFullSearchAddress(String target){
        return ConValues.SEARCH_ADDRESS_PREFIX+getURLEncode(target)+ SEARCH_ADDRESS_SUFFIX;
    }
}
