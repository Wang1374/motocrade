package com.laogeli.common.core.exceptions;

/**
 * 验证码错误异常
 *
 * @author yangyu
 * @date 2019-12-31
 */
public class InvalidValidateCodeException extends CommonException {

    private static final long serialVersionUID = -7285211528095468156L;

    public InvalidValidateCodeException() {
    }

    public InvalidValidateCodeException(String msg) {
        super(msg);
    }
}
