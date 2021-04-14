package com.oceanus.common.utils;

/**
 * 处理并记录日志文件
 *
 * @author 张家锋
 */
public class LogUtils
{
    public static String getBlock(Object msg)
    {
        if (msg == null)
        {
            msg = "";
        }
        return "[" + msg.toString() + "]";
    }
}
