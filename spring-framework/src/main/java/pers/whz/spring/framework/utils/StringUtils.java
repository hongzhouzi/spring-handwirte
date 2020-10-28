package pers.whz.spring.framework.utils;

/**
 * @author hongzhou.wei
 * @date 2020/10/28
 */
public class StringUtils {

    public static boolean isBlank(String s) {
        return s == null || "".equals(s);
    }

    public static boolean isNotBlank(String s) {
        return s != null && !"".equals(s);
    }

}
