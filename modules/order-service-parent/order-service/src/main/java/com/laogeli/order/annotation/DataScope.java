package com.laogeli.order.annotation;

import java.lang.annotation.*;

/**
 * 数据权限过滤注解
 *
 * @author wang
 */
@Target(ElementType.METHOD) // @Target用来表示注解作用范围，超过这个作用范围，编译的时候就会报错。 METHOD 用于描述方法
@Retention(RetentionPolicy.RUNTIME) // @Retention定义了该Annotation被保留的时间长短 注解不仅被保存到class文件中，jvm加载class文件之后，仍然存在；
@Documented // Documented注解表明这个注释是由 javadoc记录的，在默认情况下也有类似的记录工具。 如果一个类型声明被注释了文档化，它的注释成为公共API的一部分
public @interface DataScope {

    /**
     * 用户表的别名
     */
    String userAlias() default "";

}
