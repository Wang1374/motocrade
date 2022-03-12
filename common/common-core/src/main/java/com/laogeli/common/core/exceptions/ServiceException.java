package com.laogeli.common.core.exceptions;

/**
 * 服务异常
 *
 * @author yangyu
 * @date 2021-01-08
 */
public class ServiceException extends CommonException {

    private static final long serialVersionUID = -7285211528095468156L;

    public ServiceException() {
    }

    public ServiceException(String msg) {
        super(msg);
    }
}