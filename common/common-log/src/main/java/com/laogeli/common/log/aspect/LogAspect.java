package com.laogeli.common.log.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.laogeli.common.core.utils.SpringContextHolder;
import com.laogeli.common.core.utils.SysUtil;
import com.laogeli.common.log.annotation.Log;
import com.laogeli.common.log.event.LogEvent;
import com.laogeli.common.log.utils.LogUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.*;

/**
 * 日志切面，异步记录日志
 *
 * @author wang
 * @date 2019-12-31
 */
@Aspect //标注增强处理类（切面类）
public class LogAspect {

    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    //定义增强，pointcut连接点使用@annotation（xxx）进行定义
    @Around("@annotation(log)") //log 与 下面参数名log对应
    public Object around(ProceedingJoinPoint point, Log log) throws Throwable {
        // 获取操作的参数
        Object[] args = point.getArgs();
        String strClassName = point.getTarget().getClass().getName();
        String strMethodName = point.getSignature().getName();
        logger.debug("[类名]:{},[方法]:{}", strClassName, strMethodName);
        com.laogeli.common.core.model.Log logVo = LogUtil.getLog(args);
        logVo.setTitle(log.value());
        logVo.setType(log.type());
        if (log.type() == 1) {
            JSONObject json = (JSONObject) JSONObject.toJSON(args[0]);
            logVo.setOrderNumberLog(json.getString("orderNumber"));
        }else if(log.type()==2){
            logVo.setOrderNumberLog(args[5].toString());
        }else if(log.type()==3){
            String json =  JSONObject.toJSONString(args[0]);
            JSONArray json1 = JSONObject.parseArray(json);
            JSONObject jsonObject = json1.getJSONObject(0);
            logVo.setOrderNumberLog(jsonObject.getString("mcId"));
        }
        // 发送异步日志事件
        Long startTime = System.currentTimeMillis();
        Object obj = point.proceed();
        Long endTime = System.currentTimeMillis();
        logVo.setTime(String.valueOf(endTime - startTime));
        logVo.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
        SpringContextHolder.publishEvent(new LogEvent(logVo));
        return obj;
    }
}
