package com.laogeli.common.log.annotation;

import java.lang.annotation.*;

/**
 * 日志注解
 *
 * @author yangyu
 * @date 2019-12-31
 */
@Target(ElementType.METHOD) //作用于方法
@Retention(RetentionPolicy.RUNTIME) //运行时有效
@Documented
public @interface Log {
    /**
     * 描述
     *
     * @return {String}
     */
    String value();

    /**
     * 日志类型 默认：0 ， 订单管理： 1
     *
     * @return {int}
     */
    int type();
}
