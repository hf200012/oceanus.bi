package com.oceanus.common.util;


import com.google.common.base.Strings;

import java.util.regex.Pattern;

/**
 * 正则表达式工具类
 * @Author 张家峰
 */
public class RegExpUtil {

    /**
     * 判断数据是否是数字
     * @param source
     * @return
     */
    public static boolean isDigit(String source){
        if(!Strings.isNullOrEmpty(source)) {
            return Pattern.matches("^[0-9]*$", source);
        }
        return false;
    }

    /**
     * 判断数据是否是数字,并限定长度,最长多少位
     * @param source
     * @param length
     * @return
     */
    public static boolean isDigit(String source,int length){
        if(!Strings.isNullOrEmpty(source)) {
            String regex = "^\\d{0," + length + "}$";
            return Pattern.matches(regex, source);
        }
        return false;
    }

    /**
     * 判断数据是否是数字,可以是正数，也可以是负数
     * @param source
     * @return
     */
    public static boolean isNegativeDigit(String source,int length){
        if(!Strings.isNullOrEmpty(source)) {
            if(isOutLimitLength(source,length)) {
                String regex = "^[\\+\\-]?[\\d]+(\\.[\\d]+)?$";
                return Pattern.matches(regex, source);
            }
            return false;
        }
        return false;
    }

    public static boolean isDate(String source){
        if(!Strings.isNullOrEmpty(source)){
            String regex = "\\d{4}[-.]\\d{1,2}[-.]\\d{1,2}(\\s\\d{2}:\\d{2}(:\\d{2})?)?";
            return Pattern.matches(regex,source);
        }
        return false;
    }

    /**
     * 只能输入有最多四位小数的正实数
     * @param source
     */
    public static boolean isDecimals(String source){
        if(!Strings.isNullOrEmpty(source)) {
            String regex = "^[0-9]+(\\.[0-9]{0,4})?$";
            return Pattern.matches(regex, source);
        }
        return false;
    }

    /**
     * 只能输入由数字和26个英文字母组成的字符串,不包含中文
     * @param source
     */
    public static boolean isStringNoContainCN(String source){
        if(!Strings.isNullOrEmpty(source)) {
            String regex = "^\\w+$";
            return Pattern.matches(regex, source);
        }
        return false;
    }

    /**
     * 只能输入由数字和26个英文字母及下划线组成的字符串,不包含中文,限定长度
     * @param source
     * @param len
     */
    public static boolean isStringNoContainCN(String source,int len){
        if(!Strings.isNullOrEmpty(source)) {
            String regex = "^[a-zA-Z0-9]\\w{0," + len + "}$";
            return Pattern.matches(regex, source);
        }
        return false;
    }

    /**
     * 判断是否是IP地址
     * @param source
     * @return
     */
    public static boolean isIp(String source){
        if(!Strings.isNullOrEmpty(source)) {
            String regex = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
            return Pattern.matches(regex, source);
        }
        return false;
    }


    /**
     * 判断输入的字符串是否超出长度限制
     * @param source
     * @param length
     * @return
     */
    public static boolean isOutLimitLength(String source,int length){
        if(!Strings.isNullOrEmpty(source)) {
            if(length(source) > length){
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * 判断字符串是否为空，包括null和空字符串
     * @param source
     * @return
     */
    public static boolean isEmpty(String source){
        if(!Strings.isNullOrEmpty(source)) {
            return true;
        }
        return false;
    }

    /**
     * 计算字符串长度，包括中文，一个中文字符占两个字节
     * @param value
     * @return
     */
    public static int length(String value) {
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        for (int i = 0; i < value.length(); i++) {
            String temp = value.substring(i, i + 1);
            if (temp.matches(chinese)) {
                valueLength += 2;
            } else {
                valueLength += 1;
            }
        }
        return valueLength;
    }
}
