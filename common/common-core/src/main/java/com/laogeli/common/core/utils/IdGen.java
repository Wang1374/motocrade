package com.laogeli.common.core.utils;

import java.util.Random;
import java.util.UUID;

/**
 * id生成工具类
 *
 * @author wang
 * @date 2019-12-31
 */
public class IdGen {

    /**
     * 封装JDK自带的UUID, 中间无-分割.
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 基于snowflake算法生成ID
     *
     * @return String
     * @author wang
     * @date 2019-12-31
     */
    public static String snowflakeId() {
        return Long.toString(SpringContextHolder.getApplicationContext().getBean(SnowflakeIdWorker.class).nextId());
    }

    //生成随机数
    public static String getCard(){
        Random rand=new Random();//生成随机数
        String cardNnumer="";
        for(int a=0;a<6;a++){
            cardNnumer+=rand.nextInt(10);//生成6位数字
        }
        return cardNnumer;
    }
}