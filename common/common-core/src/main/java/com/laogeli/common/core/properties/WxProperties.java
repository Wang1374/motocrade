package com.laogeli.common.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author beifang
 * @Date 2021-08-24 16:32
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "wx")
public class WxProperties {

    /**
     * 小程序 appId
     */
    private String appId;

    /**
     * 小程序 appSecret
     */
    private String appSecret;

    /**
     * 授权类型
     */
    private String grantType;

}
