package com.laogeli.common.core.config;

import com.laogeli.common.core.constant.CommonConstant;
import com.laogeli.common.core.properties.SysProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 系统启动时的一些处理
 *
 * @author wang
 * @date 2019-12-31
 */
@Slf4j
@AllArgsConstructor
@Component
public class AppStartupRunner implements CommandLineRunner {

    private final SysProperties sysProperties;

    @Override
    public void run(String... args) throws Exception {
        log.info("start command line...");
        log.info("set system properties...");
        // 设置系统属性
        if (StringUtils.isNotBlank(sysProperties.getCacheExpire()))
            System.setProperty(CommonConstant.CACHE_EXPIRE, sysProperties.getCacheExpire());
        log.info("end command line...");
    }
}
