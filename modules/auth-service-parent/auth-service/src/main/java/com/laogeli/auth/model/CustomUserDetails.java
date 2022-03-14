package com.laogeli.auth.model;

import com.laogeli.common.core.enums.LoginType;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * 用户信息
 *
 * @author wang
 * @date 2019-12-31
 */
@Data
public class CustomUserDetails extends User {

    private static final long serialVersionUID = 1L;

    /**
     * 开始授权时间
     */
    private long start;

    /**
     * 登录类型
     */
    private LoginType loginType;

    /**
     * 构造方法
     *
     * @param username    username
     * @param password    password
     * @param enabled     enabled
     * @param authorities authorities
     * @param start       start
     * @param loginType   loginType
     */
    public CustomUserDetails(String username, String password, boolean enabled, Collection<? extends GrantedAuthority> authorities, long start, LoginType loginType) {
        super(username, password, enabled, true, true, true, authorities);
        this.start = start;
        this.loginType = loginType;
    }
}
