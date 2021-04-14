package com.oceanus.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具类
 * @Author 张家峰
 */
public class DateUtil {
    public static String getDateFormat(String date, String mat) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = format.parse(date);
            SimpleDateFormat format1 = new SimpleDateFormat(mat);
            return format1.format(date1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getCurrentDateFormat() {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return format.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取当前系统时间到毫秒级
     * @return
     */
    public static String getCurrentDateMFormat() {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSS");
            return format.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
