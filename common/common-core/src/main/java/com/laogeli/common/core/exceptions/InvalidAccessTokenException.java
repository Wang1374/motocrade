package com.laogeli.common.core.exceptions;

/**
 * token非法异常
 *
 * @author yangyu
 * @date 2019-12-31
 */
public class InvalidAccessTokenException extends CommonException {

    private static final long serialVersionUID = -7285211528095468156L;

    public InvalidAccessTokenException() {
    }

    public InvalidAccessTokenException(String msg) {
        super(msg);
    }
}
