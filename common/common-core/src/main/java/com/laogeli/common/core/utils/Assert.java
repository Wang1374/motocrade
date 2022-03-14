package com.laogeli.common.core.utils;

/**
 * @author wang
 * @date 2019-12-31
 */
public class Assert {

    /**
     * 非空校验
     *
     * @param object  object
     * @param message message
     * @author wang
     * @date 2019-12-31
     */
    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }
}
