package com.laogeli.gateway.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author yangyu
 * @date 2019-12-31
 */
@ConfigurationProperties("gateway.token-transfer")
public class GatewayTokenTransferProperties {

    /**
     * 默认开启
     */
    private boolean enabled = true;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
