package com.laogeli.common.core.exceptions;

/**
 * 公共异常
 *
 * @author yangyu
 * @date 2019-12-31
 */
public class CommonException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CommonException() {

    }

    public CommonException(String msg) {
        super(msg);
    }

    public CommonException(Throwable throwable) {
        super(throwable);
    }

    public CommonException(Throwable throwable, String msg) {
        super(throwable);
    }
}
