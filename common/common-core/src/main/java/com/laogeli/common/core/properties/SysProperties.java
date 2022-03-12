package com.laogeli.common.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author yangyu
 * @date 2019-12-31
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "sys")
public class SysProperties {

    /**
     * fastDfs服务器的HTTP地址
     */
    private String fdfsHttpHost;

    /**
     * 上传地址
     */
    private String uploadUrl;

    /**
     * 默认头像
     */
    private String defaultAvatar;

    /**
     * 管理员账号
     */
    private String adminUser;

    /**
     * 密码加密解密的key
     */
    private String key;

    /**
     * 缓存超时时间
     */
    private String cacheExpire;
}
