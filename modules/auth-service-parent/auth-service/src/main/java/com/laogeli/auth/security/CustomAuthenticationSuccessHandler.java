package com.laogeli.auth.security;

import com.laogeli.auth.model.CustomUserDetails;
import com.laogeli.common.core.constant.CommonConstant;
import com.laogeli.common.core.constant.ServiceConstant;
import com.laogeli.common.core.model.Log;
import com.laogeli.common.core.utils.SysUtil;
import com.laogeli.common.security.utils.SecurityUtil;
import com.laogeli.user.api.feign.UserServiceClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import java.security.Principal;

/**
 * 监听登录成功事件，记录登录信息
 *
 * @author wang
 * @date 2019-12-31
 */
@Slf4j
@AllArgsConstructor
@Component
public class CustomAuthenticationSuccessHandler {

    private final UserServiceClient userServiceClient;

    @EventListener({AuthenticationSuccessEvent.class, InteractiveAuthenticationSuccessEvent.class})
    public void processAuthenticationSuccessEvent(AbstractAuthenticationEvent event) {
        // 注意：登录包括oauth2客户端、用户名密码登录都会触发AuthenticationSuccessEvent，这里只记录用户名密码登录的日志
        if (event.getSource() instanceof OAuth2Authentication)
            return;
        if (event.getAuthentication().getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) event.getAuthentication().getPrincipal();
            String username = userDetails.getUsername();
            log.info("CustomAuthenticationSuccessHandler->登录成功, username: {}", username);
            // 记录日志
            Log logInfo = new Log();
            logInfo.setCommonValue(username, SysUtil.getSysCode());
            logInfo.setTitle("用户登录");
            logInfo.setType(CommonConstant.STATUS_NORMAL);
            logInfo.setMethod(HttpMethod.POST.name());
            logInfo.setTime(String.valueOf(System.currentTimeMillis() - userDetails.getStart()));
            logInfo.setRequestUri(userDetails.getLoginType().getUri());
            // 获取ip、浏览器信息
            this.initLogInfo(logInfo, event.getSource());
            logInfo.setServiceId(ServiceConstant.AUTH_SERVICE);
            saveLoginSuccessLog(logInfo);
        }
    }

    /**
     * 参考源码：
     * org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter
     * org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails
     *
     * @param logInfo logInfo
     * @param source  source
     */
    private void initLogInfo(Log logInfo, Object source) {
        if (source instanceof UsernamePasswordAuthenticationToken) {
            Object currentAuthentication = SecurityUtil.getCurrentAuthentication();
            if (currentAuthentication instanceof UsernamePasswordAuthenticationToken) {
                Object currentDetails = ((UsernamePasswordAuthenticationToken) currentAuthentication).getDetails();
                if (currentDetails instanceof WebAuthenticationDetails) {
                    WebAuthenticationDetails webAuthenticationDetails = (WebAuthenticationDetails) currentDetails;
                    logInfo.setIp(webAuthenticationDetails.getRemoteAddress());
                }
            }
        }
    }

    /**
     * 获取用户名
     *
     * @param authentication authentication
     * @return String
     */
    public String getUsername(Authentication authentication) {
        String username = "";
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else if (principal instanceof Principal) {
            username = ((Principal) principal).getName();
        }
        return username;
    }

    /**
     * 异步记录登录日志
     *
     * @author tangyi
     * @date 2019/05/30 23:30
     */
    @Async
    public void saveLoginSuccessLog(Log logInfo) {
        try {
            userServiceClient.saveLog(logInfo);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
