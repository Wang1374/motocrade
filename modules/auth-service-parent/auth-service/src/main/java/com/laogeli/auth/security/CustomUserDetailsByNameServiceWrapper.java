package com.laogeli.auth.security;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;

/**
 * @author wang
 * @date 2021-01-18
 */
public class CustomUserDetailsByNameServiceWrapper<T extends Authentication> implements AuthenticationUserDetailsService<T>, InitializingBean {
    private UserDetailsService userDetailsService = null;

    public CustomUserDetailsByNameServiceWrapper() {
    }

    public CustomUserDetailsByNameServiceWrapper(UserDetailsService userDetailsService) {
        Assert.notNull(userDetailsService, "userDetailsService cannot be null.");
        this.userDetailsService = userDetailsService;
    }

    public void afterPropertiesSet() {
        Assert.notNull(this.userDetailsService, "UserDetailsService must be set");
    }

    public UserDetails loadUserDetails(T authentication) throws UsernameNotFoundException {
        return this.userDetailsService.loadUserByUsername(authentication.getName());
    }

    public void setUserDetailsService(UserDetailsService aUserDetailsService) {
        this.userDetailsService = aUserDetailsService;
    }
}

