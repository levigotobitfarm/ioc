package org.levi.learn.util;

/**
 * 字符串工具类
 */
public class StringUtil {

    /**
     * 首字母转成小写
     * @param oldStr
     * @return
     */
    public static String lowerFirst(String oldStr) {

        char[] chars = oldStr.toCharArray();

        chars[0] += 32;

        return String.valueOf(chars);

    }

}
