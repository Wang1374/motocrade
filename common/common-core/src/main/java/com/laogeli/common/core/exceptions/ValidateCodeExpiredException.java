package com.laogeli.common.core.exceptions;

/**
 * 验证码过期异常
 *
 * @author wang
 * @date 2019-12-31
 */
public class ValidateCodeExpiredException extends CommonException {

    private static final long serialVersionUID = -7285211528095468156L;

    public ValidateCodeExpiredException() {
    }

    public ValidateCodeExpiredException(String msg) {
        super(msg);
    }
}
