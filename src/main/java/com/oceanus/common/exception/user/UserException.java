package com.oceanus.common.exception.user;

import com.oceanus.common.exception.BaseException;

/**
 * 用户信息异常类
 *
 * @author 张家锋
 */
public class UserException extends BaseException
{
    private static final long serialVersionUID = 1L;

    public UserException(String code, Object[] args)
    {
        super("user", code, args, null);
    }
}
