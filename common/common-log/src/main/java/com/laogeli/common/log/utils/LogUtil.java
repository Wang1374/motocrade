package com.laogeli.common.log.utils;

import cn.hutool.core.util.URLUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.laogeli.common.core.constant.CommonConstant;
import com.laogeli.common.core.model.Log;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 日志工具类
 *
 * @author wang
 * @date 2019-12-31
 */
public class LogUtil {

    public static Log getLog(Object[] args) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        Log sysLog = new Log();
        sysLog.setCreator(Objects.requireNonNull(getUsername()));
        sysLog.setType(CommonConstant.STATUS_NORMAL);
        sysLog.setIp(ServletUtil.getClientIP(request));
        sysLog.setRequestUri(URLUtil.getPath(request.getRequestURI()));
        sysLog.setMethod(request.getMethod());
        sysLog.setUserAgent(request.getHeader("user-agent"));
        if (request.getMethod().equals("POST") || request.getMethod().equals("PUT")) {
            if ("/v1/attachment/upload".equals(URLUtil.getPath(request.getRequestURI()))) {
                String jsonStr = JSON.toJSONString(args[1]);
                sysLog.setParams(jsonStr);
            } else {
                String jsonStr = JSON.toJSONString(args[0]);
                sysLog.setParams(jsonStr);
            }
        } else {
            sysLog.setParams(HttpUtil.toParams(request.getParameterMap()));
        }
        sysLog.setServiceId(getClientId());
        return sysLog;
    }

    /**
     * 获取客户端
     *
     * @return clientId
     */
    private static String getClientId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof OAuth2Authentication) {
            OAuth2Authentication auth2Authentication = (OAuth2Authentication) authentication;
            return auth2Authentication.getOAuth2Request().getClientId();
        }
        return null;
    }

    /**
     * 获取用户名称
     *
     * @return username
     */
    private static String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        return authentication.getName();
    }
}
