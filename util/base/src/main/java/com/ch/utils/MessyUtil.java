package com.ch.utils;

/**
 * 杂乱的工具类
 *
 * @author Ch
 * @since 1.0
 */
public class MessyUtil {

    public static String getStringVal(Object val) {
        String r;
        if (val != null) {
            if (val instanceof String) {
                r = (String) val;
            } else {
                r = val.toString();
            }
        } else {
            r = "";
        }
        return r;
    }

    /**
     *  <p>判断类型是否是String</p>
     * @param value
     * @return
     */
    public static boolean isNumber(String value){
        try {
            Double.parseDouble(value);
            return false;
        } catch (Exception e1) {
            return true;
        }
    }

    /**
     * <p>截取字符串方法</p>
     * @param url 链接
     * @param split "\\."
     * @return number 截取长度
     */
    public static String getUrl(String url ,String split ,int number){
        String pref = url.split(split)[0];
        if(pref.length() > number){
            pref = pref.substring(0,number);
        }
        return pref;
    }
}
