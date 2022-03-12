package com.laogeli.auth.config;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.WxMaConfig;
import cn.binarywang.wx.miniapp.config.WxMaInMemoryConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(WxMaService.class)
public class WxMaConfiguration {

    /**
     * 小程序 appId
     */
    @Value("${wx.appId}")
    private String appId;

    /**
     * 小程序 appSecret
     */
    @Value("${wx.appSecret}")
    private String appSecret;

    @Bean
    @ConditionalOnMissingBean
    public WxMaConfig wxMaConfig() {
        WxMaInMemoryConfig config = new WxMaInMemoryConfig();
        config.setAppid(appId);
        config.setSecret(appSecret);
        return config;
    }

    @Bean
    @ConditionalOnMissingBean
    public WxMaService wxMaService(WxMaConfig maConfig) {
        WxMaService service = new WxMaServiceImpl();
        service.setWxMaConfig(maConfig);
        return service;
    }
}
