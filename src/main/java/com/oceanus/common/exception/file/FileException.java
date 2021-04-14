package com.oceanus.common.exception.file;

import com.oceanus.common.exception.BaseException;

/**
 * 文件信息异常类
 *
 * @author 张家锋
 */
public class FileException extends BaseException
{
    private static final long serialVersionUID = 1L;

    public FileException(String code, Object[] args)
    {
        super("file", code, args, null);
    }

}
